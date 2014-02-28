package com.optimaize.langdetect;

import com.cybozu.labs.langdetect.util.NGram;
import com.google.common.annotations.Beta;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * ...
 *
 * @author Fabian Kessler
 */
@Beta
public class TextObject implements CharSequence {

    @NotNull
    private StringBuilder stringBuilder;

    private final int maxTextLength = 10000;

    private static final Pattern URL_REGEX = Pattern.compile("https?://[-_.?&~;+=/#0-9A-Za-z]+");
    private static final Pattern MAIL_REGEX = Pattern.compile("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");


    public TextObject() {
        this.stringBuilder = new StringBuilder();
    }
    public TextObject(String s) {
        this.stringBuilder = new StringBuilder(s);
    }




    /**
     * Append the target text for language detection.
     * This method read the text from specified input reader.
     * If the total size of target text exceeds the limit size ,
     * the rest is cut down.
     *
     * @param reader the input reader (BufferedReader as usual)
     * @throws java.io.IOException Can't read the reader.
     */
    public void append(Reader reader) throws IOException {
        char[] buf = new char[maxTextLength/2];
        while (stringBuilder.length() < maxTextLength && reader.ready()) {
            int length = reader.read(buf);
            append(String.valueOf(buf, 0, length));
        }
    }

    /**
     * Append the target text for language detection.
     * If the total size of target text exceeds the limit size ,
     * the rest is cut down.
     *
     * @param text the target text to append
     */
    public void append(String text) {
        text = URL_REGEX.matcher(text).replaceAll(" ");
        text = MAIL_REGEX.matcher(text).replaceAll(" ");
        char pre = 0;
        for (int i = 0; i < text.length() && i < maxTextLength; ++i) {
            char c = NGram.normalize(text.charAt(i));
            if (c != ' ' || pre != ' ') stringBuilder.append(c);
            pre = c;
        }
    }

    /**
     * Cleaning text to detect
     * (eliminate URL, e-mail address and Latin sentence if it is not written in Latin alphabet)
     */
    private void cleaningText() {
        int latinCount = 0, nonLatinCount = 0;
        for (int i = 0; i < stringBuilder.length(); ++i) {
            char c = stringBuilder.charAt(i);
            if (c <= 'z' && c >= 'A') {
                ++latinCount;
            } else if (c >= '\u0300' && Character.UnicodeBlock.of(c) != Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) {
                ++nonLatinCount;
            }
        }
        if (latinCount * 2 < nonLatinCount) {
            StringBuilder textWithoutLatin = new StringBuilder();
            for (int i = 0; i < stringBuilder.length(); ++i) {
                char c = stringBuilder.charAt(i);
                if (c > 'z' || c < 'A') {
                    textWithoutLatin.append(c);
                }
            }
            stringBuilder = textWithoutLatin;
        }
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

}
