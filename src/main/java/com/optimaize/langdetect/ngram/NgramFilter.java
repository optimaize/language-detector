package com.optimaize.langdetect.ngram;

/**
 * Filters out some undesired n-grams.
 *
 * @author Fabian Kessler
 */
public interface NgramFilter {

    boolean use(String ngram);

}
