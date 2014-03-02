package com.optimaize.langdetect.text;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Groups multiple {@link com.optimaize.langdetect.text.TextFilter}s as one and runs them in the given order.
 *
 * @author Fabian Kessler
 */
public class MultiTextFilter implements TextFilter {

    @Nullable
    private final List<TextFilter> filters;

    /**
     * @param filters may be empty by definition
     */
    public MultiTextFilter(@NotNull List<TextFilter> filters) {
        if (filters.isEmpty()) {
            this.filters = null;
        } else {
            this.filters = ImmutableList.copyOf(filters);
        }
    }

    @Override
    public String filter(CharSequence text) {
        if (filters==null) {
            return text.toString();
        } else {
            String modified = text.toString();
            for (TextFilter filter : filters) {
                modified = filter.filter(modified);
            }
            return modified;
        }
    }
}
