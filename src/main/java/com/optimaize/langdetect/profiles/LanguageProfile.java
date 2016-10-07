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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * A language profile knows the locale (language), and contains the n-grams and some statistics.
 *
 * <p>It is built from a training text that should be fairly large and clean.</p>
 *
 * <p>It contains the n-grams from the training text in the desired gram sizes (eg 2 and 3-grams),
 * with possible text filters applied for cleaning. Also, rarely occurring n-grams may have been cut to
 * reduce the noise and index size. Use a {@link LanguageProfileBuilder}.</p>
 *
 * <p>The profile may be created at runtime on-the-fly, or it may be loaded from a previously generated
 * text file (see OldLangProfileConverter).</p>
 *
 * @author Fabian Kessler
 */
public interface LanguageProfile {

    @NotNull
    LdLocale getLocale();

    /**
     * Tells what the n in n-grams are used here.
     * Example: [1,2,3]
     * @return Sorted from smaller to larger.
     */
    @NotNull
    List<Integer> getGramLengths();

    /**
     * @param gram for example "a" or "foo".
     * @return 0-n, also zero if this profile does not use n-grams of that length (for example if no 4-grams are made).
     */
    int getFrequency(String gram);

    /**
     * Tells how many different n-grams there are for a certain n-gram size.
     * For example the English language has about 57 different 1-grams, whereas Chinese in Hani has thousands.
     * @param gramLength 1-n
     * @return 0-n, returns zero if no such n-grams were made (for example if no 4-grams were made),
     *              or if all the training text did not contain such long words.
     */
    int getNumGrams(int gramLength);

    /**
     * Tells how many n-grams there are for all n-gram sizes combined.
     * @return 0-n (0 only on an empty profile...)
     */
    int getNumGrams();

    /**
     * Tells how often all n-grams of a certain length occurred, combined.
     * This returns a much larger number than {@link #getNumGrams}.
     * @param gramLength 1-n
     * @return 0-n, returns zero if no such n-grams were made (for example if no 4-grams were made),
     *              or if all the training text did not contain such long words.
     */
    long getNumGramOccurrences(int gramLength);

    /**
     * Tells how often the n-gram with the lowest amount of occurrences used in this profile occurred.
     *
     * Most likely there were n-grams with less (unless the returned number is 1), but they were eliminated
     * in order to keep the profile reasonably small.
     *
     * This is the opposite of getMaxGramCount().
     *
     * @param gramLength 1-n
     * @return 0-n, returns zero if no such n-grams were made or existed.
     */
    long getMinGramCount(int gramLength);
    /**
     * Tells how often the n-gram with the highest amount of occurrences used in this profile occurred.
     *
     * This is the opposite of getMinGramCount().
     *
     * @param gramLength 1-n
     * @return 0-n, returns zero if no such n-grams were made or existed.
     */
    long getMaxGramCount(int gramLength);

    /**
     * Iterates all ngram strings with frequency.
     */
    @NotNull
    Iterable<Map.Entry<String,Integer>> iterateGrams();

    /**
     * Iterates all gramLength-gram strings with frequency.
     */
    @NotNull
    Iterable<Map.Entry<String,Integer>> iterateGrams(int gramLength);

}
