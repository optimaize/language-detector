package com.optimaize.langdetect.ngram;

import com.cybozu.labs.langdetect.util.NGram;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for extracting n-grams out of a text.
 *
 * @author Fabian Kessler
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
     */
    @NotNull
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

}
