package com.optimaize.langdetect.profiles;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * A language profile is built from a training text that should be fairly large and clean.
 *
 * @author Fabian Kessler
 */
public interface LanguageProfile {

    /**
     * The language code, which can be a 2-letter ISO 639-1 or a 3-letter ISO 639-3 code.
     * If there is a 2-letter available ("fr" for French) it is used, if not then the 3-letter (gsw for Swiss german).
     */
    @NotNull
    String getLanguage();

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
     * This returns a much larger number than {@link #getNumGrams}.
     * @param gramLength 1-n
     * @return 0-n, returns zero if no such n-grams were made (for example if no 4-grams were made),
     *              or if all the training text did not contain such long words.
     */
    long getNumGramOccurrences(int gramLength);

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
