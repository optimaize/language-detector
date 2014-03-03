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

    private double alpha = ALPHA_DEFAULT;
    private int shortTextAlgorithm = 50;
    private double prefixFactor = 1.0d;
    private double suffixFactor = 1.0d;

    private double probabilityThreshold = 0.1;
    private double minimalConfidence = 0.9999d;

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


    public LanguageDetectorBuilder alpha(double alpha) {
        if (alpha<0 || alpha>1) throw new IllegalArgumentException(""+alpha);
        this.alpha = alpha;
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
     * Sets prefixFactor() and suffixFactor() both to the given value.
     * @see #prefixFactor(double)
     */
    public LanguageDetectorBuilder affixFactor(double affixFactor) {
        prefixFactor(affixFactor);
        suffixFactor(affixFactor);
        return this;
    }
    /**
     * To weight n-grams that are on the left border of a word differently from n-grams
     * in the middle of words, assign a value here.
     *
     * Affixes (prefixes and suffixes) often distinguish the specific features of languages.
     * Giving a value greater than 1.0 weights these n-grams higher. A 2.0 weights them double.
     *
     * Defaults to 1.0, which means don't use this feature.
     * @param prefixFactor 0.0 to 10.0, a suggested value is 1.5
     */
    public LanguageDetectorBuilder prefixFactor(double prefixFactor) {
        this.prefixFactor = prefixFactor;
        return this;
    }
    /**
     * Defaults to 1.0, which means don't use this feature.
     * @param suffixFactor 0.0 to 10.0, a suggested value is 2.0
     * @see #prefixFactor(double)
     */
    public LanguageDetectorBuilder suffixFactor(double suffixFactor) {
        this.suffixFactor = suffixFactor;
        return this;
    }

    /**
     * {@link LanguageDetector#getProbabilities} does not return languages with less probability than this.
     * The default currently is 0.1 (the old hardcoded value), but don't rely on it, if you need to be sure
     * then set one.
     */
    public LanguageDetectorBuilder probabilityThreshold(double probabilityThreshold) {
        this.probabilityThreshold = probabilityThreshold;
        return this;
    }

    /**
     * {@link LanguageDetector#detect} returns a language if the best detected language has at least this probability.
     * The default currently is 0.9999d, but don't rely on it, if you need to be sure then set one.
     */
    public LanguageDetectorBuilder minimalConfidence(double minimalConfidence) {
        this.minimalConfidence = minimalConfidence;
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
        return new LanguageDetectorImpl(
                NgramFrequencyData.create(languageProfiles),
                alpha, shortTextAlgorithm,
                prefixFactor, suffixFactor,
                probabilityThreshold, minimalConfidence,
                langWeightingMap,
                ngramExtractor
        );
    }

}
