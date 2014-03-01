package com.optimaize.langdetect;

import com.cybozu.labs.langdetect.ErrorCode;
import com.cybozu.labs.langdetect.LangDetectException;
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


    private boolean verbose = false;
    private double alpha = ALPHA_DEFAULT;
    private boolean skipUnknownNgrams = true;
    private int shortTextAlgorithm = 0;

    @Nullable
    private Map<String, Double> langWeightingMap;

    @NotNull
    private Set<LanguageProfile> languageProfiles = new HashSet<>();
    @NotNull
    private Set<String> langsAdded = new HashSet<>();


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
    public LanguageDetector build() throws IllegalStateException, LangDetectException {
        if (languageProfiles.isEmpty()) throw new IllegalStateException();

        Map<String, double[]> wordLangProbMap = new HashMap<>();
        List<String> langlist = new ArrayList<>();
        int langsize = languageProfiles.size(); //that's how the orig code did it. dunno what that's for.

        int index = -1;
        for (LanguageProfile profile : languageProfiles) {
            index++;

            String lang = profile.getLanguage();
            if (langlist.contains(lang)) {
                throw new LangDetectException(ErrorCode.DuplicateLangError, "duplicate the same language profile");
            }
            langlist.add(lang);

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
                verbose, alpha, skipUnknownNgrams, shortTextAlgorithm,
                langWeightingMap
        );
    }

}
