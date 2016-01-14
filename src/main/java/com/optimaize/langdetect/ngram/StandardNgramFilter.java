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

    private StandardNgramFilter() {}

    @Override
    public boolean use(String ngram) {
        switch (ngram.length()) {
            case 1:
                return ngram.charAt(0) != ' ';
            case 2:
                return true;
            case 3:
                return ngram.charAt(1) != ' ';
            case 4:
                //one of the middle chars is a space
                return ngram.charAt(1) !=' ' && ngram.charAt(2) !=' ';
            default:
                //would need the same check: no space in the middle, border is fine.
                throw new UnsupportedOperationException("Unsupported n-gram length: "+ngram.length());
        }
    }
}
