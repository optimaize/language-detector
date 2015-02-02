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

package com.optimaize.langdetect;

import com.optimaize.langdetect.i18n.LdLocale;
import org.jetbrains.annotations.NotNull;

/**
 * Holds information about a detected language: the locale (language) and the probability.
 *
 * <p>Comparable: the "better" one comes before the worse.
 * First order by probability descending (1 to 0).
 * Then order by language ascending (a to z).</p>
 *
 * <p>This class is immutable.</p>
 *
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 */
public class DetectedLanguage implements Comparable<DetectedLanguage> {

    @NotNull
    private final LdLocale locale;
    private final double probability;

    /**
     * @param locale
     * @param probability 0-1
     */
    public DetectedLanguage(@NotNull LdLocale locale, double probability) {
        if (probability<0d) throw new IllegalArgumentException("Probability must be >= 0 but was "+probability);
        if (probability>1d) throw new IllegalArgumentException("Probability must be <= 1 but was "+probability);
        this.locale = locale;
        this.probability = probability;
    }

    @NotNull
    public LdLocale getLocale() {
        return locale;
    }

    /**
     * @return 0-1, the higher the better.
     */
    public double getProbability() {
        return probability;
    }

    public String toString() {
        return "DetectedLanguage["+ locale + ":" + probability+"]";
    }

    /**
     * See class header.
     */
    @Override
    public int compareTo(DetectedLanguage o) {
        int compare = Double.compare(o.probability, this.probability);
        if (compare!=0) return compare;
        return this.locale.toString().compareTo(o.locale.toString());
    }
}
