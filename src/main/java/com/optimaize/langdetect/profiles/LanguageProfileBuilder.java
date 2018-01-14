/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimaize.langdetect.profiles;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractor;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private final LdLocale locale;
    private int minimalFrequency = 1;
    private NgramExtractor ngramExtractor;
    private final Map<Integer, Map<String,Integer>> ngrams = new HashMap<>();


    public LanguageProfileBuilder(@NotNull LdLocale locale) {
        this.locale = locale;
    }
    @Deprecated
    public LanguageProfileBuilder(@NotNull String locale) {
        this.locale = LdLocale.fromString(locale);
    }

    /**
     * Copy constructor.
     */
    public LanguageProfileBuilder(@NotNull LanguageProfileBuilder languageProfileBuilder) {
        this.locale = languageProfileBuilder.locale;
        this.minimalFrequency = languageProfileBuilder.minimalFrequency;
        this.ngramExtractor = languageProfileBuilder.ngramExtractor;
        this.ngrams.putAll(languageProfileBuilder.ngrams);
    }

    public LanguageProfileBuilder ngramExtractor(@NotNull NgramExtractor ngramExtractor) {
        this.ngramExtractor = ngramExtractor;
        return this;
    }

    /**
     * @param minimalFrequency 1-n, the default is 1. n-grams that occurred less often in the text are removed.
     *                         This really should be set to something higher.
     *                         Try to play with the number until you get a profile file of satisfying size,
     *                         that produces good language detection results.
     */
    public LanguageProfileBuilder minimalFrequency(int minimalFrequency) {
        if (minimalFrequency < 1) throw new IllegalArgumentException("minimalFrequency must be >= 1, but was: "+minimalFrequency);
        this.minimalFrequency = minimalFrequency;
        return this;
    }

    /**
     * In order to use this you must set the {@link #ngramExtractor} first.
     */
    public LanguageProfileBuilder addText(CharSequence text) {
        if (ngramExtractor==null) {
            throw new IllegalStateException("NgramExtractor has not been set yet!");
        }
        for (Map.Entry<String, Integer> entry : ngramExtractor.extractCountedGrams(text).entrySet()) {
            addGram(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Shortcut for addGram(ngram, 1).
     */
    public LanguageProfileBuilder addGram(String ngram) {
        return addGram(ngram, 1);
    }
    /**
     * If the builder already has this ngram, the given frequency is added to the current count.
     */
    public LanguageProfileBuilder addGram(String ngram, int frequency) {
        Map<String, Integer> map = ngrams.computeIfAbsent(ngram.length(), k -> new HashMap<>());
        Integer total = map.get(ngram);
        if (total==null) total = 0;
        total += frequency;
        map.put(ngram, total);
        return this;
    }


    public LanguageProfile build() {
        if (minimalFrequency >1) {
            removeNgramsWithLessFrequency();
        }
        return new LanguageProfileImpl(locale, ngrams);
    }


    private void removeNgramsWithLessFrequency() {
        for (Map<String, Integer> map : ngrams.values()) {
            map.entrySet().removeIf(next -> next.getValue() < minimalFrequency);
        }
    }

}
