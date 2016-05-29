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
 * @author Robert Erdin
 *         https://en.wikipedia.org/wiki/Cross-validation_(statistics)#k-fold_cross-validation
 */
public class LanguageProfileValidator {

    private final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexingCleanText();

    private int k = 10;

    private boolean breakWords = false;

    private final List<LanguageProfile> languageProfiles = new ArrayList<>();
    private LanguageProfileBuilder languageProfileBuilder;
    private TextObject inputSample;


    /**
     * Set the k parameter to select into how many parts to partition the original sample. Default is 10.
     * Minimum: 3
     *
     * @param k
     * @return
     */
    public LanguageProfileValidator setK(int k) {
        if( k <= 2 )
            System.err.println("k hast to be at least 3");
        this.k = k;
        return this;
    }

    /**
     * Adds all {@link LanguageProfile}s that are available when calling {@link LanguageProfileReader#readAllBuiltIn()}.
     * @return
     * @throws IOException
     */
    public LanguageProfileValidator loadAllBuiltInLanguageProfiles() throws IOException {
        this.languageProfiles.addAll(new LanguageProfileReader().readAllBuiltIn());
        return this;
    }

    /**
     * Load a custom {@link LanguageProfile}
     * @param languageProfile
     * @return
     */
    public LanguageProfileValidator loadLanguageProfile(LanguageProfile languageProfile) {
        this.languageProfiles.add(languageProfile);
        return this;
    }

    /**
     * Load custom {@link LanguageProfile}s.
     * @param languageProfiles
     * @return
     */
    public LanguageProfileValidator loadLanguageProfiles(Collection<LanguageProfile> languageProfiles) {
        this.languageProfiles.addAll(languageProfiles);
        return this;
    }

    /**
     * Load the {@link LanguageProfileBuilder} which should be used to create the {@link LanguageProfile} during the validation.
     * @param languageProfileBuilder
     * @return
     */
    public LanguageProfileValidator loadLanguageProfileBuilder(LanguageProfileBuilder languageProfileBuilder) {
        this.languageProfileBuilder = languageProfileBuilder;
        return this;
    }

    /**
     * The sample to be used in the validation.
     * @param inputSample
     * @return
     */
    public LanguageProfileValidator loadInputSample(TextObject inputSample) {
        this.inputSample = inputSample;
        return this;
    }

    /**
     * Use for languages that don't use whitespaces to denominate word boundaries.
     *
     * @param breakWords set true is you want to break sample into truly equal sizes.
     * @return
     */
    public LanguageProfileValidator breakWords(boolean breakWords) {
        this.breakWords = breakWords;
        return this;
    }

    /**
     * Remove potential LanguageProfiles, e.g. in combination with {@link #loadAllBuiltInLanguageProfiles()}.
     * @param isoString the ISO string of the LanguageProfile to be removed.
     * @return
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
            this.languageProfileBuilder.clearText();
            TextObject testSample = partitionedInput.get(i);

            List<TextObject> trainingSamples = new ArrayList<>(partitionedInput);
            trainingSamples.remove(i);
            for (TextObject token : trainingSamples) {
                this.languageProfileBuilder.addText(token);
            }
            final LanguageProfile languageProfile = this.languageProfileBuilder.build();

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

        Double sum = 0D;
        for (Double token : probabilities) {
            sum += token;
        }
        Double avg = sum / this.k;

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
            for (String token : Splitter
                    .fixedLength(this.k)
                    .split(this.inputSample.toString())) {
                result.add(textObjectFactory.create().append(token));
            }
        }
        return result;
    }
}
