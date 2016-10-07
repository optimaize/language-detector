package com.optimaize.langdetect.ngram;

/**
 * Filters out some undesired n-grams.
 *
 * Implementations must be immutable.
 *
 * @author Fabian Kessler
 */
public interface NgramFilter {

    boolean use(String ngram);

}
