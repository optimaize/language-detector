/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
