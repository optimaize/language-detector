package com.optimaize.langdetect.ngram;

import com.cybozu.labs.langdetect.util.NGram;
import com.google.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for extracting n-grams out of a text.
 *
 * @author Fabian Kessler
 * @author Nakatani Shuyo
 */
public class NgramExtractor {

    public interface Filter {
        /**
         * Allows to skip some n-grams.
         *
         * This is currently used to filter n-grams in to-analyze text when the n-gram is unknown to the loaded
         * language profiles.
         *
         * @return true to use this n-gram, false to skip it.
         */
        boolean use(String gram);
    }

    /**
     * This was the method found in the {@link com.cybozu.labs.langdetect.Detector} class, it was used to extract
     * grams from the to-analyze text.
     *
     * NOTE: although it adds the first ngram with space, it does not add the last n-gram with space. example: "foo" gives " fo" but not "oo "!.
     * It is not clear yet whether this is desired (and why) or a bug.
     *
     * TODO replace this algorithm with a simpler, faster one that uses less memory: only by position shifting. also, the returned list size
     * can be computed before making it (based on text length and number of n-grams).
     *
     * @author Nakatani Shuyo
     */
    @NotNull
    @Deprecated
    public static List<String> extractNGrams(@NotNull CharSequence text, @Nullable Filter filter) {
        List<String> list = new ArrayList<>();
        NGram ngram = new NGram();
        for(int i=0;i<text.length();++i) {
            ngram.addChar(text.charAt(i));
            for(int n=1;n<=NGram.N_GRAM;++n){
                String w = ngram.get(n);
                if (w!=null) { //TODO this null check is ugly
                    if (filter==null || filter.use(w)) {
                        list.add(w);
                    }
                }
            }
        }
        return list;
    }


    /**
     * Creates the n-grams for a given text in the order they occur.
     *
     * <p>Example: extractSortedGrams("Foo bar", 2) => [Fo,oo,o , b,ba,ar]</p>
     *
     * <p>To have border grams, modify your input text. Example: instead of "foo" pass in " foo " or "$foo$".</p>
     *
     * @param  text
     * @param  gramLength 1-n
     * @return The grams, empty if the input was empty or if none for that gramLength fits.
     *         Impl notes: this returns Array instead of List because
     *         a) it's 10% faster to create
     *         b) it produces a lot less garbage, which is good for gc and low latency
     *         c) no one will need to touch this data anyway other than iterating
     */
    @NotNull
    public static String[] extractGrams(@NotNull CharSequence text, int gramLength) {
        int len = text.length();
        int numGrams = len - (gramLength -1);
        if (numGrams <= 0) {
            return new String[]{};
        }
        String[] grams = new String[numGrams];
        for (int pos=0; pos<numGrams; pos++) {
            grams[pos] = text.subSequence(pos, pos+gramLength).toString();
        }
        return grams;
    }

}
