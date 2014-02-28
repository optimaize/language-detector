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

import org.jetbrains.annotations.NotNull;

/**
 * Holds information about a detected language: the language and the probability.
 *
 * <p>Comparable: the "better" one comes before the worse.
 * First order by probability descending (1 to 0).
 * Then order by language ascending (a to z).</p>
 *
 * <p>This class is immutable.</p>
 *
 * <p>Renamed: this class was previously known as "Language".</p>
 *
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 */
public class DetectedLanguage implements Comparable<DetectedLanguage> {

    @NotNull
    private String language;
    private double probability;

    /**
     * @param language TODO document and validate syntax
     * @param probability 0-1
     */
    public DetectedLanguage(@NotNull String language, double probability) {
        if (probability<0d) throw new IllegalArgumentException("Probability must be >= 0 but was "+probability);
        if (probability>1d) throw new IllegalArgumentException("Probability must be <= 1 but was "+probability);
        this.language = language;
        this.probability = probability;
    }

    @NotNull
    public String getLanguage() {
        return language;
    }

    /**
     * @return 0-1, the higher the better.
     */
    public double getProbability() {
        return probability;
    }

    public String toString() {
        return "DetectedLanguage["+language + ":" + probability+"]";
    }

    /**
     * See class header.
     */
    @Override
    public int compareTo(DetectedLanguage o) {
        int compare = Double.compare(o.probability, this.probability);
        if (compare!=0) return compare;
        return this.language.compareTo(o.language);
    }
}
