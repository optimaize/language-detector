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

package com.cybozu.labs.langdetect;

import java.util.ArrayList;

/**
 * {@link Language} is to store the detected language.
 * {@link Detector#getProbabilities()} returns an {@link ArrayList} of {@link Language}s.
 *  
 * @see Detector#getProbabilities()
 * @author Nakatani Shuyo
 *
 */
public class Language {
    public String lang;
    public double prob;
    public Language(String lang, double prob) {
        this.lang = lang;
        this.prob = prob;
    }
    public String toString() {
        if (lang==null) return "";
        return lang + ":" + prob;
    }
}
