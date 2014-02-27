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

package com.cybozu.labs.langdetect.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * {@link LangProfile} is a Language Profile Class.
 * Users don't use this class directly.
 * 
 * @author Nakatani Shuyo
 */
public class LangProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int MINIMUM_FREQ = 2;
    private static final int LESS_FREQ_RATIO = 100000;
    private String name = null;
    private HashMap<String, Integer> freq = new HashMap<String, Integer>();
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
    public void add(String gram) {
        if (getName() == null || gram == null) return;   // Illegal
        int len = gram.length();
        if (len < 1 || len > NGram.N_GRAM) return;  // Illegal
        ++getNWords()[len - 1];
        if (getFreq().containsKey(gram)) {
            getFreq().put(gram, getFreq().get(gram) + 1);
        } else {
            getFreq().put(gram, 1);
        }
    }

    /**
     * Eliminate below less frequency n-grams and noise Latin alphabets
     */
    public void omitLessFreq() {
        if (getName() == null) return;   // Illegal
        int threshold = getNWords()[0] / LESS_FREQ_RATIO;
        if (threshold < MINIMUM_FREQ) threshold = MINIMUM_FREQ;
        
        Set<String> keys = getFreq().keySet();
        int roman = 0;
        for(Iterator<String> i = keys.iterator(); i.hasNext(); ){
            String key = i.next();
            int count = getFreq().get(key);
            if (count <= threshold) {
                getNWords()[key.length()-1] -= count; 
                i.remove();
            } else {
                if (key.matches("^[A-Za-z]$")) {
                    roman += count;
                }
            }
        }

        // roman check
        if (roman < getNWords()[0] / 3) {
            Set<String> keys2 = getFreq().keySet();
            for(Iterator<String> i = keys2.iterator(); i.hasNext(); ){
                String key = i.next();
                if (key.matches(".*[A-Za-z].*")) {
                    getNWords()[key.length()-1] -= getFreq().get(key); 
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

	public HashMap<String, Integer> getFreq() {
		return freq;
	}

	public void setFreq(HashMap<String, Integer> freq) {
		this.freq = freq;
	}

	public int[] getNWords() {
		return nWords;
	}

	public void setNWords(int[] nWords) {
		this.nWords = nWords;
	}
}
