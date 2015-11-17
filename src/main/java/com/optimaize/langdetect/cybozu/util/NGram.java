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

package com.optimaize.langdetect.cybozu.util;

import org.jetbrains.annotations.Nullable;

/**
 * TODO document.
 *
 * Users don't use this class directly.
 *
 * TODO this class treats a word as "upper case" if the first 2 characters are upper case. That seems like a simplification,
 * would need documentation.
 *
 * @author Nakatani Shuyo
 */
public class NGram {

    /**
     * ngrams are created from 1gram to this amount, currently 2grams and 3grams.
     */
    public static final int N_GRAM = 3;

    private StringBuilder grams_;
    private boolean capitalword_;

    public NGram() {
        grams_ = new StringBuilder(" ");
        capitalword_ = false;
    }

    public void addChar(char ch) {
        ch = CharNormalizer.normalize(ch);
        char lastChar = grams_.charAt(grams_.length() - 1);
        if (lastChar == ' ') {
            grams_ = new StringBuilder(" ");
            capitalword_ = false;
            if (ch==' ') return;
        } else if (grams_.length() >= N_GRAM) {
            grams_.deleteCharAt(0);
        }
        grams_.append(ch);

        if (Character.isUpperCase(ch)){
            if (Character.isUpperCase(lastChar)) capitalword_ = true;
        } else {
            capitalword_ = false;
        }
    }

    /**
     * TODO this method has some weird, undocumented behavior to ignore ngrams with upper case.
     *
     * Get n-Gram
     * @param n length of n-gram
     * @return n-Gram String (null if it is invalid)
     */
    @Nullable
    public String get(int n) {
        if (capitalword_) return null;
        int len = grams_.length(); 
        if (n < 1 || n > N_GRAM || len < n) return null;
        if (n == 1) {
            char ch = grams_.charAt(len - 1);
            if (ch == ' ') return null;
            return Character.toString(ch);
        } else {
            return grams_.substring(len - n, len);
        }
    }

}
