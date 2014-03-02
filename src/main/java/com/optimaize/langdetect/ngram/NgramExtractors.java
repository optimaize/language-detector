package com.optimaize.langdetect.ngram;

/**
 * Provides easy access to commonly used NgramExtractor configs.
 *
 * @author Fabian Kessler
 */
public class NgramExtractors {

    private static final NgramExtractor STANDARD = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(StandardNgramFilter.getInstance())
            .textPadding(' ');

    private static final NgramExtractor BACKWARDS = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(BackwardsCompatibleNgramFilter.getInstance())
            .textPadding(' ');


    /**
     * The new standard n-gram algorithm.
     */
    public static NgramExtractor standard() {
        return STANDARD;
    }

    /**
     * The old way of doing n-grams.
     */
    public static NgramExtractor backwards() {
        return BACKWARDS;
    }

}
