package com.optimaize.langdetect.ngram;

/**
 * Filters what is generally not desired.
 *
 * @author Fabian Kessler
 */
public class StandardNgramFilter implements NgramFilter {

    private static final StandardNgramFilter INSTANCE = new StandardNgramFilter();

    public static NgramFilter getInstance() {
        return INSTANCE;
    }

    private StandardNgramFilter() {
    }

    @Override
    public boolean use(String ngram) {
        switch (ngram.length()) {
            case 1:
                if (ngram.charAt(0)==' ') {
                    return false;
                }
                return true;
            case 2:
                return true;
            case 3:
                if (ngram.charAt(1)==' ') {
                    //middle char is a space
                    return false;
                }
                return true;
            default:
                //would need the same check: no space in the middle, border is fine.
                throw new UnsupportedOperationException("Unsupported n-gram length: "+ngram.length());
        }
    }

}
