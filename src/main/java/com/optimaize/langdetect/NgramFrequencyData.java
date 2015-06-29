package com.optimaize.langdetect;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.profiles.LanguageProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Contains frequency information for n-grams coming from multiple {@link LanguageProfile}s.
 *
 * <p>For each n-gram string it knows the locales (languages) in which it occurs, and how frequent it
 * occurs in those languages in relation to other n-grams of the same length in those same languages.</p>
 *
 * <p>Immutable by definition (can't make Arrays unmodifiable).</p>
 *
 * @author Fabian Kessler
 */
public final class NgramFrequencyData {

    /**
     * Key   = ngram
     * Value = array with probabilities per loaded language, in the same order as {@code langlist}.
     */
    @NotNull
    private final Map<String, double[]> wordLangProbMap;

    /**
     * All the loaded languages, in exactly the same order as the data is in the double[] in wordLangProbMap.
     * Example: if wordLangProbMap has an entry for the n-gram "foo" then for each locale in this langlist here
     * it has a value there. Languages that don't know the n-gram have the value 0d.
     */
    @NotNull
    private final List<LdLocale> langlist;


    /**
     * @param gramLengths for example [1,2,3]
     * @throws java.lang.IllegalArgumentException if languageProfiles or gramLengths is empty, or if one of the
     *         languageProfiles does not have the grams of the required sizes.
     */
    @NotNull
    public static NgramFrequencyData create(@NotNull Collection<LanguageProfile> languageProfiles, @NotNull Collection<Integer> gramLengths) throws IllegalArgumentException {
        if (languageProfiles.isEmpty()) throw new IllegalArgumentException("No languageProfiles provided!");
        if (gramLengths.isEmpty()) throw new IllegalArgumentException("No gramLengths provided!");

        Map<String, double[]> wordLangProbMap = new HashMap<>();
        List<LdLocale> langlist = new ArrayList<>();
        int langsize = languageProfiles.size();

        int index = -1;
        for (LanguageProfile profile : languageProfiles) {
            index++;

            langlist.add( profile.getLocale() );

            for (Integer gramLength : gramLengths) {
                if (!profile.getGramLengths().contains(gramLength)) {
                    throw new IllegalArgumentException("The language profile for "+profile.getLocale()+" does not contain "+gramLength+"-grams!");
                }
                for (Map.Entry<String, Integer> ngramEntry : profile.iterateGrams(gramLength)) {
                    String ngram      = ngramEntry.getKey();
                    Integer frequency = ngramEntry.getValue();
                    if (!wordLangProbMap.containsKey(ngram)) {
                        wordLangProbMap.put(ngram, new double[langsize]);
                    }
                    double prob = frequency.doubleValue() / profile.getNumGramOccurrences(ngram.length());
                    wordLangProbMap.get(ngram)[index] = prob;
                }
            }
        }

        return new NgramFrequencyData(wordLangProbMap, langlist);
    }

    private NgramFrequencyData(@NotNull Map<String, double[]> wordLangProbMap,
                               @NotNull List<LdLocale> langlist) {
        //not making immutable copies because I create them here (optimization).
        this.wordLangProbMap = Collections.unmodifiableMap(wordLangProbMap);
        this.langlist = Collections.unmodifiableList(langlist);
    }


    @NotNull
    public List<LdLocale> getLanguageList() {
        return langlist;
    }
    @NotNull
    public LdLocale getLanguage(int pos) {
        return langlist.get(pos);
    }

    /**
     * Don't modify this data structure! (Can't make array immutable...)
     * @return null if no language profile knows that ngram.
     *         entries are 0 for languages that don't know that ngram at all.
     *         The array is in the order of the {@link #getLanguageList()} language list, and has exactly that size.
     *         impl note: this way the caller can handle it more efficient than returning an empty array.
     */
    @Nullable
    public double[] getProbabilities(String ngram) {
        return wordLangProbMap.get(ngram);
    }
}
