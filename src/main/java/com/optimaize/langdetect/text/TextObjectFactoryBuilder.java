package com.optimaize.langdetect.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link com.optimaize.langdetect.text.TextObjectFactory}.
 *
 * @author Fabian Kessler
 */
public class TextObjectFactoryBuilder {

    private int maxTextLength = 0;
    private final List<TextFilter> textFilters = new ArrayList<>();

    /**
     * @param maxTextLength 0 for no limit (that's the default).
     */
    public TextObjectFactoryBuilder maxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
        return this;
    }


    /**
     * Adds the given TextFilter to be run on {@link TextObject#append} methods.
     *
     * <p>Note that the order of filters. may be important. They are executed in the same order as they
     * are passed in here.</p>
     */
    public TextObjectFactoryBuilder withTextFilter(TextFilter textFilter) {
        textFilters.add(textFilter);
        return this;
    }

    public TextObjectFactory build() {
        return new TextObjectFactory(
                new MultiTextFilter(textFilters),
                maxTextLength
        );
    }

}
