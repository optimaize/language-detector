package com.optimaize.langdetect.profiles;

import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Builder for {@link LanguageProfile}.
 *
 * <p>This class does no internal synchronization.</p>
 *
 * @author Fabian Kessler
 */
public class LanguageProfileBuilder {

    private final String language;
    private int minimalFrequency = 1;
    private Optional<Double> filterMinorityScriptContent = Optional.absent();
    private final Map<Integer, Map<String,Integer>> ngrams = new HashMap<>();


    public LanguageProfileBuilder(String language) {
        this.language = language;
    }

    public LanguageProfileBuilder minimalFrequency(int minimalFrequency) {
        if (minimalFrequency<1) throw new IllegalArgumentException(""+minimalFrequency);
        this.minimalFrequency = minimalFrequency;
        return this;
    }

    /**
     * If n-grams of minority scripts should be removed in the end.
     *
     * The default is to not remove anything.
     *
     * Example: The text is mostly Cyrillic, but contains 10% Latin. If you pass in any number >= 0.1 then
     *          Latin is removed.
     */
    public LanguageProfileBuilder filterMinorityScriptContent(Optional<Double> threshold) {
        if (threshold.isPresent()) {
            if (threshold.get()<0d || threshold.get()>1d) throw new IllegalArgumentException("Invalid: "+threshold.get());
        }
        this.filterMinorityScriptContent = threshold;
        return this;
    }

    /**
     * If the builder already has this ngram, the given frequency is added to the current count.
     */
    public void add(String ngram, int frequency) {
        Map<String, Integer> map = ngrams.get(ngram.length());
        if (map==null) {
            map = new HashMap<>();
            ngrams.put(ngram.length(), map);
        }
        Integer total = map.get(ngram);
        if (total==null) total = 0;
        total += frequency;
        map.put(ngram, total);
    }

    public LanguageProfile build() {
        //note: must remove minority script content BEFORE removing marginal n-grams.
        if (filterMinorityScriptContent.isPresent()) {
            removeMinorityScriptContent(filterMinorityScriptContent.get());
        }
        if (minimalFrequency >1) {
            removeNgramsWithLessFrequency();
        }
        return new LanguageProfileImpl(language, ngrams);
    }

    private void removeMinorityScriptContent(double threshold) {
        //TODO this is the logic of the old code, rewritten.
        //instead, we should profile by script, not just for ascii or latin.
        //and then remove the minority script content and only keep the primary.
        //special rules apply only for Japanese (Hira+Katakana+Kanji) and Korean (Hang+Kani).

        Map<String, Integer> oneGrams = ngrams.get(1);
        int numAscii = 0;
        int numOther = 0;
        for (Map.Entry<String, Integer> entry : oneGrams.entrySet()) {
            char c = entry.getKey().charAt(0);
            if ((c>='A' && c<='Z') || (c>='a' && c<='z')) {
                numAscii += entry.getValue();
            } else {
                numOther += entry.getValue();
            }
        }

        double asciiPercent = numAscii / (numAscii+numOther);
        if (asciiPercent <= threshold) {
            //remove ascii, all of it.
            for (Map<String, Integer> map : ngrams.values()) {
                Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Integer> next = iterator.next();
                    if (next.getKey().matches(".*[A-Za-z].*")) { //if it contains ascii, we remove it.
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void removeNgramsWithLessFrequency() {
        for (Map<String, Integer> map : ngrams.values()) {
            Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> next = iterator.next();
                if (next.getValue() < minimalFrequency) {
                    iterator.remove();
                }
            }
        }
    }

}
