package com.optimaize.langdetect;

import com.optimaize.langdetect.ngram.NgramExtractor;
import com.optimaize.langdetect.profiles.LanguageProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Builder for {@link LanguageDetector}.
 *
 * <p>This class does no internal synchronization.</p>
 *
 * @author Fabian Kessler
 */
public class LanguageDetectorBuilder {

    private static final double ALPHA_DEFAULT = 0.5;

    @NotNull
    private final NgramExtractor ngramExtractor;

    private boolean verbose = false;
    private double alpha = ALPHA_DEFAULT;
    private boolean skipUnknownNgrams = true;
    private int shortTextAlgorithm = 0;
    private double borderFactor = 1.0d;

    @Nullable
    private Map<String, Double> langWeightingMap;

    @NotNull
    private Set<LanguageProfile> languageProfiles = new HashSet<>();
    @NotNull
    private Set<String> langsAdded = new HashSet<>();

    public static LanguageDetectorBuilder create(@NotNull NgramExtractor ngramExtractor) {
        return new LanguageDetectorBuilder(ngramExtractor);
    }

    private LanguageDetectorBuilder(@NotNull NgramExtractor ngramExtractor) {
        this.ngramExtractor = ngramExtractor;
    }


    public LanguageDetectorBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public LanguageDetectorBuilder alpha(double alpha) {
        if (alpha<0 || alpha>1) throw new IllegalArgumentException(""+alpha);
        this.alpha = alpha;
        return this;
    }

    /**
     * If not specified, the default is {@code true}. This is the old behavior.
     */
    public LanguageDetectorBuilder skipUnknownNgrams(boolean skipUnknownNgrams) {
        this.skipUnknownNgrams = skipUnknownNgrams;
        return this;
    }

    /**
     * Defaults to 0, which means don't use this feature. That's the old behavior.
     */
    public LanguageDetectorBuilder shortTextAlgorithm(int shortTextAlgorithm) {
        this.shortTextAlgorithm = shortTextAlgorithm;
        return this;
    }

    /**
     * To weight n-grams that are on the left or right border of a word differently from n-grams
     * in the middle of words, assign a value here.
     *
     * Affixes (prefixes and suffixes) often distinguish the specific features of languages.
     * Giving a value greater than 1.0 weights these n-grams higher. A 2.0 weights them double.
     *
     * TODO split into prefixFactor and suffixFactor.
     *
     * Defaults to 1.0, which means don't use this feature. That's the old behavior.
     * @param borderFactor 0.0 to 10.0, a suggested value is 2.0
     */
    public LanguageDetectorBuilder borderFactor(double borderFactor) {
        this.borderFactor = borderFactor;
        return this;
    }

    /**
     * TODO document exactly. Also explain how it influences the results.
     * Maybe check for unsupported languages at some point, or not, but document whether it does throw or ignore.
     * String key = language code, Double value = priority (probably 0-1).
     */
    public LanguageDetectorBuilder languagePriorities(@Nullable Map<String, Double> langWeightingMap) {
        this.langWeightingMap = langWeightingMap;
        return this;
    }

    /**
     * @throws IllegalStateException if a profile for the same language was added already (must be a userland bug).
     */
    public LanguageDetectorBuilder withProfile(LanguageProfile languageProfile) throws IllegalStateException {
        if (langsAdded.contains(languageProfile.getLanguage())) {
            throw new IllegalStateException("A language profile for language "+languageProfile.getLanguage()+" was added already!");
        }
        for (Integer gramLength : ngramExtractor.getGramLengths()) {
            if (!languageProfile.getGramLengths().contains(gramLength)) {
                throw new IllegalArgumentException("The NgramExtractor is set to handle "+gramLength+"-grams but the given language profile for "+languageProfile.getLanguage()+" does not support this!");
            }
        }
        langsAdded.add(languageProfile.getLanguage());
        languageProfiles.add(languageProfile);
        return this;
    }
    /**
     * @throws IllegalStateException if a profile for the same language was added already (must be a userland bug).
     */
    public LanguageDetectorBuilder withProfiles(Iterable<LanguageProfile> languageProfiles) throws IllegalStateException {
        for (LanguageProfile languageProfile : languageProfiles) {
            withProfile(languageProfile);
        }
        return this;
    }


    /**
     * @throws IllegalStateException if no LanguageProfile was {@link #withProfile added}.
     */
    public LanguageDetector build() throws IllegalStateException {
        if (languageProfiles.isEmpty()) throw new IllegalStateException();

        Map<String, double[]> wordLangProbMap = new HashMap<>();
        List<String> langlist = new ArrayList<>();
        int langsize = languageProfiles.size(); //that's how the orig code did it. dunno what that's for.

        int index = -1;
        for (LanguageProfile profile : languageProfiles) {
            index++;

            langlist.add( profile.getLanguage() );

            for (Map.Entry<String, Integer> ngramEntry : profile.iterateGrams()) {
                String ngram      = ngramEntry.getKey();
                Integer frequency = ngramEntry.getValue();
                if (!wordLangProbMap.containsKey(ngram)) {
                    wordLangProbMap.put(ngram, new double[langsize]);
                }
                double prob = frequency.doubleValue() / profile.getNumGramOccurrences(ngram.length());
                wordLangProbMap.get(ngram)[index] = prob;
            }
        }

        return new LanguageDetectorImpl(
                wordLangProbMap, langlist,
                verbose, alpha, skipUnknownNgrams, shortTextAlgorithm, borderFactor,
                langWeightingMap,
                ngramExtractor
        );
    }

}
