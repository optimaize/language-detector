package com.optimaize.langdetect.text;

import com.optimaize.langdetect.cybozu.util.CharNormalizer;
import com.google.common.annotations.Beta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

/**
 * A convenient text object implementing CharSequence and Appendable.
 *
 * This is an ideal object to use for learning text to create {@link com.optimaize.langdetect.profiles.LanguageProfile}s,
 * as well as to pass it in to {@link com.optimaize.langdetect.LanguageDetector#detect}.
 *
 * To get one, use a TextObjectFactory (through a TextObjectFactoryBuilder).
 *
 * Example use:
 * //create the factory once:
 * TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder()
 *     .withTextFilter(UrlTextFilter.getInstance())
 *     .build();
 * //then create as many text objects as you like:
 * TextObject inputText = textObjectFactory.create().append("deutsche Text").append(" ").append("blah blah");
 *
 * All append() methods go through the {@code textFilter}.
 *
 * Equals/hashCode are not implemented as of now on purpose. You may want to call toString() and compare that.
 *
 * @author Fabian Kessler
 */
@Beta
public class TextObject implements CharSequence, Appendable {

    @NotNull
    private final TextFilter textFilter;

    @NotNull
    private final StringBuilder stringBuilder;

    private final int maxTextLength;


    /**
     * @param maxTextLength 0 for no limit
     */
    public TextObject(@NotNull TextFilter textFilter, int maxTextLength) {
        this.textFilter = textFilter;
        this.maxTextLength = maxTextLength;
        this.stringBuilder = new StringBuilder();
    }


    /**
     * Append the target text for language detection.
     * This method read the text from specified input reader.
     * If the total size of target text exceeds the limit size,
     * the rest is ignored.
     *
     * @param reader the input reader (BufferedReader as usual)
     * @throws java.io.IOException Can't read the reader.
     */
    public TextObject append(Reader reader) throws IOException {
        char[] buf = new char[1024];
        while (reader.ready() && (maxTextLength==0 || stringBuilder.length()<maxTextLength)) {
            int length = reader.read(buf);
            append(String.valueOf(buf, 0, length));
        }
        return this;
    }

    /**
     * Append the target text for language detection.
     * If the total size of target text exceeds the limit size ,
     * the rest is cut down.
     *
     * @param text the target text to append
     */
    @Override
    public TextObject append(CharSequence text) {
        if (maxTextLength>0 && stringBuilder.length()>=maxTextLength) return this;

        text = textFilter.filter(text);

        //unfortunately this code can't be put into a TextFilter because:
        //1) the limit could not be detected early, a lot of work would be done to waste time and memory
        //2) the last character of the existing string builder could not be seen. if it is a space, we don't want
        //   to add yet another space.
        char pre = stringBuilder.length()==0 ? 0 : stringBuilder.charAt(stringBuilder.length()-1);
        for (int i=0; i<text.length() && (maxTextLength==0 || stringBuilder.length()<maxTextLength); i++) {
            char c = CharNormalizer.normalize(text.charAt(i));
            if (c != ' ' || pre != ' ') {
                stringBuilder.append(c);
            }
            pre = c;
        }

        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return append(csq.subSequence(start, end));
    }

    @Override
    public Appendable append(char c) throws IOException {
        return append(Character.toString(c));
    }



    @Override
    public int length() {
        return stringBuilder.length();
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    @Override @NotNull
    public String toString() {
        return stringBuilder.toString(); //only correct impl, see interface CharSequence!
    }

}
