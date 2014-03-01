package com.optimaize.langdetect.profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * <p>This class is immutable.</p>
 *
 * @author Fabian Kessler
 */
public final class LanguageProfileImpl implements LanguageProfile {

    @NotNull
    private final String language;
    @NotNull
    private final Map<Integer, Map<String,Integer>> ngrams;
    /**
     * Key = gram length (1-3 or so).
     * Value = number of all occurrences of these grams combined.
     */
    @NotNull
    private final Map<Integer, Long> numOccurrences;


    /**
     * Use the builder.
     */
    LanguageProfileImpl(@NotNull String language, @NotNull Map<Integer, Map<String, Integer>> ngrams) {
        this.language = language;
        this.ngrams = ImmutableMap.copyOf(ngrams);
        this.numOccurrences = computeNumOccurrences(ngrams);
    }

    private static Map<Integer, Long> computeNumOccurrences(Map<Integer, Map<String, Integer>> ngrams) {
        Map<Integer, Long> map = new HashMap<>(6);
        for (Map.Entry<Integer, Map<String, Integer>> entry : ngrams.entrySet()) {
            long count = 0;
            for (Integer integer : entry.getValue().values()) {
                count += integer;
            }
            map.put(entry.getKey(), count);
        }
        return map;
    }

    @NotNull
    @Override
    public String getLanguage() {
        return language;
    }

    @NotNull @Override
    public List<Integer> getGramLengths() {
        List<Integer> lengths = new ArrayList<>(ngrams.keySet());
        Collections.sort(lengths);
        return lengths;
    }

    @Override
    public int getFrequency(String gram) {
        Map<String, Integer> map = ngrams.get(gram.length());
        if (map==null) return 0;
        Integer freq = map.get(gram);
        if (freq==null) return 0;
        return freq;
    }

    @Override
    public int getNumGrams(int gramLength) {
        if (gramLength<1) throw new IllegalArgumentException(""+gramLength);
        Map<String, Integer> map = ngrams.get(gramLength);
        if (map==null) return 0;
        return map.size();
    }

    @Override
    public int getNumGrams() {
        int ret = 0;
        for (Map<String, Integer> stringIntegerMap : ngrams.values()) {
            ret += stringIntegerMap.size();
        }
        return ret;
    }

    @Override
    public long getNumGramOccurrences(int gramLength) {
        Long aLong = numOccurrences.get(gramLength);
        if (aLong==null) return 0;
        return aLong;
    }

    @NotNull
    @Override
    public Iterable<Map.Entry<String,Integer>> iterateGrams() {
        Iterable[] arr = new Iterable[ngrams.size()];
        int i=0;
        for (Map<String, Integer> stringIntegerMap : ngrams.values()) {
            arr[i] = stringIntegerMap.entrySet();
            i++;
        }
        //noinspection unchecked
        return Iterables.concat(arr);
    }
}
