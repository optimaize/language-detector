package com.optimaize.langdetect.text;

/**
 * Factory for {@link TextObject}s.
 *
 * @author Fabian Kessler
 */
public class TextObjectFactory {

    private final TextFilter textFilter;
    private final int maxTextLength;

    /**
     * @param maxTextLength 0 for none
     */
    public TextObjectFactory(TextFilter textFilter, int maxTextLength) {
        this.textFilter = textFilter;
        this.maxTextLength = maxTextLength;
    }

    public TextObject create() {
        return new TextObject(textFilter, maxTextLength);
    }

    public TextObject forText(CharSequence text) {
        return create().append(text);
    }

}
