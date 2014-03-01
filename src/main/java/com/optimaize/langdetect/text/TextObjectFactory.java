package com.optimaize.langdetect.text;

/**
 * Factory for {@link TextObject}s.
 *
 * @author Fabian Kessler
 */
public class TextObjectFactory {

    private final TextFilter textFilter;

    public TextObjectFactory(TextFilter textFilter) {
        this.textFilter = textFilter;
    }

    public TextObject create() {
        return new TextObject(textFilter);
    }

}
