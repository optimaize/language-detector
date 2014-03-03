package com.optimaize.langdetect;

import com.optimaize.langdetect.profiles.LanguageProfile;

import java.util.*;

/**
 * Contains frequency information for ngram strings and the languages.
 *
 * <p>Immutable in theory.</p>
 *
 * @author Fabian Kessler
 */
public class NgramFrequencyData {

    /**
     * Key=ngram
     * Value = array with probabilities per loaded language, in the same order as {@code langlist}.
     */
    private final Map<String, double[]> wordLangProbMap;

    /**
     * All the loaded languages, order is important.
     */
    private final List<String> langlist;


    public static NgramFrequencyData create(Collection<LanguageProfile> languageProfiles) {
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

        return new NgramFrequencyData(wordLangProbMap, langlist);
    }

    private NgramFrequencyData(Map<String, double[]> wordLangProbMap,
                               List<String> langlist) {
        //not making immutable copies because I create them here (optimization).
        this.wordLangProbMap = Collections.unmodifiableMap(wordLangProbMap);
        this.langlist = Collections.unmodifiableList(langlist);
    }


    public List<String> getLanglist() {
        return langlist;
    }

    /**
     * Don't modify this data structure! (Can't make array immutable...)
     * @return null if no language profile knows that ngram.
     *         entries are 0 for languages that don't know that ngram at all.
     *         The array is in the order of the {@link #getLanglist()} language list, and has exactly that size.
     *         impl note: this way the caller can handle it more efficient than returning an empty array.
     */
    public double[] getProbabilities(String ngram) {
        return wordLangProbMap.get(ngram);
    }
}
