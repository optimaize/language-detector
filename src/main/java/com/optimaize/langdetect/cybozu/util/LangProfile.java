/*
 * Copyright 2011 Nakatani Shuyo
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

/*
 * This file has been modified by François ROLAND.
 */

package com.optimaize.langdetect.cybozu.util;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * {@link LangProfile} is a Language Profile Class.
 * Users don't use this class directly.
 *
 * TODO split into builder and immutable class.
 *
 * TODO currently this only makes n-grams with the space before a word included. no n-gram with the space after the word.
 * Example: "foo" creates " fo" as 3gram, but not "oo ". Either this is a bug, or if intended then needs documentation.
 * 
 * @author Nakatani Shuyo
 * @deprecated replaced by LanguageProfile
 */
@Deprecated
public class LangProfile implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * n-grams that occur less than this often can be removed using omitLessFreq().
     * This number can change, see LESS_FREQ_RATIO.
     */
	private static final int MINIMUM_FREQ = 2;

    /**
     * Explanation by example:
     *
     * If the most frequent n-gram occurs 1 mio times, then
     * 1'000'000 / this (100'000) = 10.
     * 10 is larger than MINIMUM_FREQ (2), thus MINIMUM_FREQ remains at 2.
     * All n-grams that occur less than 2 times can be removed as noise using omitLessFreq().
     *
     * If the most frequent n-gram occurs 5000 times, then
     * 5'000 / this (100'000) = 0.05.
     * 0.05 is smaller than MINIMUM_FREQ (2), thus MINIMUM_FREQ becomes 0.
     * No n-grams are removed because of insignificance when calling omitLessFreq().
     */
    private static final int LESS_FREQ_RATIO = 100000;

    /**
     * The language name (identifier).
     */
    private String name = null;

    /**
     * Key = ngram, value = count.
     * All n-grams are in here (1-gram, 2-gram, 3-gram).
     */
    private Map<String, Integer> freq = new HashMap<>();

    /**
     * Tells how many occurrences of n-grams exist per gram length.
     * When making 1grams, 2grams and 3grams (currently) then this contains 3 entries where
     * element 0 = number occurrences of 1-grams
     * element 1 = number occurrences of 2-grams
     * element 2 = number occurrences of 3-grams
     * Example: if there are 57 1-grams (English language has about that many) and the training text is
     * fairly long, then this number is in the millions.
     */
    private int[] nWords = new int[NGram.N_GRAM];

    /**
     * Constructor for JSONIC 
     */
    public LangProfile() {}

    /**
     * Normal Constructor
     * @param name language name
     */
    public LangProfile(String name) {
        this.setName(name);
    }
    
    /**
     * Add n-gram to profile
     * @param gram
     */
    public void add(@NotNull String gram) {
        if (name == null) throw new IllegalStateException();
        int len = gram.length();
        if (len < 1 || len > NGram.N_GRAM) {
            throw new IllegalArgumentException("ngram length must be 1-3 but was "+len+": >>>"+gram+"<<<!");
        }
        nWords[len - 1]++;
        if (freq.containsKey(gram)) {
            freq.put(gram, freq.get(gram) + 1);
        } else {
            freq.put(gram, 1);
        }
    }

    /**
     * Removes ngrams that occur fewer times than MINIMUM_FREQ to get rid of rare ngrams.
     *
     * Also removes ascii ngrams if the total number of ascii ngrams is less than one third of the total.
     * This is done because non-latin text (such as Chinese) often has some latin noise in between.
     *
     * TODO split the 2 cleaning to separate methods.
     * TODO distinguish ascii/latin, currently it looks for latin only, should include characters with diacritics, eg Vietnamese.
     * TODO current code counts ascii, but removes any latin. is that desired? if so then this needs documentation.
     */
    public void omitLessFreq() {
        if (name == null) throw new IllegalStateException();

        int threshold = nWords[0] / LESS_FREQ_RATIO;
        if (threshold < MINIMUM_FREQ) threshold = MINIMUM_FREQ;
        
        Set<String> keys = freq.keySet();
        int roman = 0;
        for(Iterator<String> i = keys.iterator(); i.hasNext(); ){
            String key = i.next();
            int count = freq.get(key);
            if (count <= threshold) {
                nWords[key.length()-1] -= count;
                i.remove();
            } else {
                if (key.matches("^[A-Za-z]$")) {
                    roman += count;
                }
            }
        }

        // roman check
        if (roman < nWords[0] / 3) {
            Set<String> keys2 = freq.keySet();
            for(Iterator<String> i = keys2.iterator(); i.hasNext(); ){
                String key = i.next();
                if (key.matches(".*[A-Za-z].*")) {
                    nWords[key.length()-1] -= freq.get(key);
                    i.remove();
                }
            }
        }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Integer> getFreq() {
		return freq;
	}

	public void setFreq(Map<String, Integer> freq) {
		this.freq = freq;
	}

	public int[] getNWords() {
		return nWords;
	}

	public void setNWords(int[] nWords) {
		this.nWords = nWords;
	}
}
