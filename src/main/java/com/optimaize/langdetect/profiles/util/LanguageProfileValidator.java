package com.optimaize.langdetect.profiles.util;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileBuilder;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs k-fold cross-validation.
 * See https://en.wikipedia.org/wiki/Cross-validation_(statistics)#k-fold_cross-validation
 *
 * This is meant to be run as a maintenance program, or for debugging. It's not used in production by this library.
 * Use the unit test.
 *
 * @author Robert Erdin
 */
public class LanguageProfileValidator {

    private final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexingCleanText();

    private int k = 10;
    private boolean breakWords = false;

    /**
     * All loaded language profiles.
     */
    private final List<LanguageProfile> languageProfiles = new ArrayList<>();
    private LanguageProfileBuilder languageProfileBuilder;
    private TextObject inputSample;


    /**
     * Set the k parameter to select into how many parts to partition the original sample. Default is 10.
     * @param k Minimum: 3
     */
    public LanguageProfileValidator setK(int k) {
        if( k <= 2 ) {
            throw new IllegalArgumentException("k hast to be at least 3 but was: "+k);
        }
        this.k = k;
        return this;
    }

    /**
     * Adds all {@link LanguageProfile}s that are available when calling {@link LanguageProfileReader#readAllBuiltIn()}.
     */
    public LanguageProfileValidator loadAllBuiltInLanguageProfiles() throws IOException {
        this.languageProfiles.addAll(new LanguageProfileReader().readAllBuiltIn());
        return this;
    }

    /**
     * Load the given {@link LanguageProfile}.
     */
    public LanguageProfileValidator loadLanguageProfile(LanguageProfile languageProfile) {
        this.languageProfiles.add(languageProfile);
        return this;
    }

    /**
     * Load the given {@link LanguageProfile}s.
     */
    public LanguageProfileValidator loadLanguageProfiles(Collection<LanguageProfile> languageProfiles) {
        this.languageProfiles.addAll(languageProfiles);
        return this;
    }

    /**
     * Sets the {@link LanguageProfileBuilder} which will be used to create the {@link LanguageProfile} during the validation.
     */
    public LanguageProfileValidator setLanguageProfileBuilder(LanguageProfileBuilder languageProfileBuilder) {
        this.languageProfileBuilder = languageProfileBuilder;
        return this;
    }

    /**
     * The sample to be used in the validation.
     */
    public LanguageProfileValidator loadInputSample(TextObject inputSample) {
        this.inputSample = inputSample;
        return this;
    }

    /**
     * Use for languages that don't use whitespaces to denominate word boundaries.
     * Default is false.
     * @param breakWords set true is you want to break sample into truly equal sizes.
     */
    public LanguageProfileValidator setBreakWords(boolean breakWords) {
        this.breakWords = breakWords;
        return this;
    }

    /**
     * Remove potential LanguageProfiles, e.g. in combination with {@link #loadAllBuiltInLanguageProfiles()}.
     * @param isoString the ISO string of the LanguageProfile to be removed.
     */
    public LanguageProfileValidator removeLanguageProfile(final String isoString) {
        Iterables.removeIf(this.languageProfiles, new Predicate<LanguageProfile>() {
            @Override
            public boolean apply(LanguageProfile languageProfile) {
                return languageProfile.getLocale().getLanguage().equals(isoString);
            }
        });
        return this;
    }

    /**
     * Run the n-fold validation.
     * @return the average probability over all runs.
     */
    public double validate() {
        // remove a potential duplicate LanguageProfile
        this.removeLanguageProfile(this.languageProfileBuilder.build().getLocale().getLanguage());

        List<TextObject> partitionedInput = partition();
        List<Double> probabilities = new ArrayList<>(this.k);

        System.out.println("------------------- Running " + this.k + "-fold cross-validation -------------------");

        for (int i = 0; i < this.k; i++) {
            System.out.println(" ----------------- Run " + (i + 1) + " -------------------");
            LanguageProfileBuilder lpb = new LanguageProfileBuilder(this.languageProfileBuilder);
            TextObject testSample = partitionedInput.get(i);

            List<TextObject> trainingSamples = new ArrayList<>(partitionedInput);
            trainingSamples.remove(i);
            for (TextObject token : trainingSamples) {
                lpb.addText(token);
            }
            final LanguageProfile languageProfile = lpb.build();

            this.languageProfiles.add(languageProfile);

            final LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(this.languageProfiles)
                    .build();

            // remove the newly created LanguageProfile for the next round
            this.languageProfiles.remove(this.languageProfiles.size() - 1);

            List<DetectedLanguage> detectedLanguages = languageDetector.getProbabilities(testSample);

            try{
                DetectedLanguage kResult = Iterables.find(detectedLanguages, new Predicate<DetectedLanguage>() {
                    public boolean apply(DetectedLanguage language) {
                        return language.getLocale().getLanguage().equals(languageProfile.getLocale().getLanguage());
                    }
                });

                probabilities.add(kResult.getProbability());
                System.out.println("Probability: " + kResult.getProbability());

            }catch (NoSuchElementException e){
                System.out.println("No match. Probability: 0");
                probabilities.add(0D);
            }
        }

        double sum = 0D;
        for (Double token : probabilities) {
            sum += token;
        }
        double avg = sum / this.k;

        System.out.println("The average probability over all runs is: " + avg);

        return avg;
    }

    private List<TextObject> partition() {
        List<TextObject> result = new ArrayList<>(this.k);
        if (!breakWords) {
            int maxLength = this.inputSample.length() / (this.k - 1);
            Pattern p = Pattern.compile("\\G\\s*(.{1," + maxLength + "})(?=\\s|$)", Pattern.DOTALL);
            Matcher m = p.matcher(this.inputSample);
            while (m.find())
                result.add(textObjectFactory.create().append(m.group(1)));
        } else {
            Splitter splitter = Splitter.fixedLength(this.k);
            for (String token : splitter.split(this.inputSample.toString())) {
                result.add(textObjectFactory.create().append(token));
            }
        }
        return result;
    }
}
