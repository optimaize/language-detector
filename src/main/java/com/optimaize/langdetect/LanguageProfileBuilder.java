package com.optimaize.langdetect;

import java.util.HashMap;
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
    private final Map<Integer, Map<String,Integer>> ngrams = new HashMap<>();


    public LanguageProfileBuilder(String language) {
        this.language = language;
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
        return new LanguageProfileImpl(language, ngrams);
    }
}
