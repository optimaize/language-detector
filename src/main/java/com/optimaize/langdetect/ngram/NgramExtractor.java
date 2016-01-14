package com.optimaize.langdetect.ngram;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Class for extracting n-grams out of a text.
 *
 * @author Fabian Kessler
 */
public class NgramExtractor {

    @NotNull
    private final List<Integer> gramLengths = new ArrayList<>(4);
    @Nullable
    private final NgramFilter filter;
    @Nullable
    private final Character textPadding;

    public static NgramExtractor gramLength(int gramLength) {
        return new NgramExtractor(ImmutableList.of(gramLength), null, null);
    }
    public static NgramExtractor gramLengths(Integer... gramLength) {
        return new NgramExtractor(Arrays.asList(gramLength), null, null);
    }

    public NgramExtractor filter(NgramFilter filter) {
        return new NgramExtractor(this.gramLengths, filter, this.textPadding);
    }

    /**
     * To ensure having border grams, this character is added to the left and right of the text.
     *
     * <p>Example: when textPadding is a space ' ' then a text input "foo" becomes " foo ", ensuring that n-grams like " f"
     * are created.</p>
     *
     * <p>If the text already has such a character in that position (eg starts with), it is not added there.</p>
     *
     * @param textPadding for example a space ' '.
     */
    public NgramExtractor textPadding(char textPadding) {
        return new NgramExtractor(this.gramLengths, this.filter, textPadding);
    }

    private NgramExtractor(@NotNull List<Integer> gramLengths, @Nullable NgramFilter filter, @Nullable Character textPadding) {
        if (gramLengths.isEmpty()) throw new IllegalArgumentException();
        this.gramLengths.addAll(gramLengths);
        this.filter = filter;
        this.textPadding = textPadding;
    }

    public List<Integer> getGramLengths() {
        return Collections.unmodifiableList(gramLengths);
    }

    /**
     * Creates the n-grams for a given text in the order they occur.
     *
     * <p>Example: extractSortedGrams("Foo bar", 2) => [Fo,oo,o , b,ba,ar]</p>
     *
     * @param  text
     * @return The grams, empty if the input was empty or if none for that gramLength fits.
     */
    @NotNull
    public List<String> extractGrams(@NotNull CharSequence text) {
        text = applyPadding(text);
        int len = text.length();

        //the actual size will be totalNumGrams or less (filter)
        int totalNumGrams = 0;
        for (Integer gramLength : gramLengths) {
            int num = len - (gramLength - 1);
            if (num >= 1) { //yes can be negative
                totalNumGrams += num;
            }
        }
        if (totalNumGrams <= 0) {
            return Collections.emptyList();
        }
        List<String> grams = new ArrayList<>(totalNumGrams);
        String textAsString = text.toString();
        for (Integer gramLength : gramLengths) {
            int numGrams = len - (gramLength -1);
            if (numGrams >= 1) { //yes can be negative
                for (int pos=0; pos<numGrams; pos++) {
                    String gram = textAsString.substring(pos, pos + gramLength);
                    if (filter==null || filter.use(gram)) {
                        grams.add(gram);
                    }
                }
            }
        }
        return grams;
    }

    /**
     * @return Key = ngram, value = count
     *         The order is as the n-grams appeared first in the string.
     *
     */
    @NotNull
    public Map<String,Integer> extractCountedGrams(@NotNull CharSequence text) {
        text = applyPadding(text);
        int len = text.length();

        int initialCapacity = 0;
        for (Integer gramLength : gramLengths) {
            initialCapacity += guessNumDistinctiveGrams(len, gramLength);
        }

        Map<String,Integer> grams = new LinkedHashMap<>(initialCapacity);
        for (Integer gramLength : gramLengths) {
            _extractCounted(text, gramLength, len, grams);
        }
        return grams;
    }


    private void _extractCounted(CharSequence text, int gramLength, int len, Map<String, Integer> grams) {
        int endPos = len - (gramLength -1);
        for (int pos=0; pos<endPos; pos++) {
            String gram = text.subSequence(pos, pos + gramLength).toString();
            if (filter==null || filter.use(gram)) {
                Integer counter = grams.get(gram);
                if (counter==null) {
                    grams.put(gram, 1);
                } else {
                    grams.put(gram, counter+1);
                }
            }
        }
    }

    /**
     * This is trying to be smart.
     * It also depends on script (alphabet less than ideographic).
     * So I'm not sure how good it really is. Just trying to prevent array copies... and for Latin it seems to work fine.
     */
    private static int guessNumDistinctiveGrams(int textLength, int gramLength) {
        switch (gramLength) {
            case 1:
                return Math.min(80, textLength);
            case 2:
                if (textLength < 40) return textLength;
                if (textLength < 100) return (int)(textLength*0.8);
                if (textLength < 1000) return (int)(textLength * 0.6);
                return (int)(textLength * 0.5);
            case 3:
                if (textLength < 40) return textLength;
                if (textLength < 100) return (int)(textLength*0.9);
                if (textLength < 1000) return (int)(textLength * 0.8);
                return (int)(textLength * 0.6);
            case 4:
            case 5:
            default:
                if (textLength < 100) return textLength;
                if (textLength < 1000) return (int)(textLength * 0.95);
                return (int)(textLength * 0.9);
        }
    }

    private CharSequence applyPadding(CharSequence text) {
        if (textPadding==null) return text;
        if (text.length()==0) return text;
        if (text.charAt(0)==textPadding && text.charAt(text.length()-1)==textPadding) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        if (text.charAt(0) != textPadding) {
            sb.append(textPadding);
        }
        sb.append(text);
        if (text.charAt(text.length()-1) != textPadding) {
            sb.append(textPadding);
        }
        return sb;
    }

}
