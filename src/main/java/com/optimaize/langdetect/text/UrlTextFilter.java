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

import org.nibor.autolink.*;

import java.util.EnumSet;

/**
 * Removes URLs and email addresses from the text.
 *
 * @author Fabian Kessler
 */
public class UrlTextFilter implements TextFilter {

    private static final LinkExtractor linkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL))
            .build();

    private static final UrlTextFilter INSTANCE = new UrlTextFilter();

    public static UrlTextFilter getInstance() {
        return INSTANCE;
    }

    private UrlTextFilter() {
    }

    @Override
    public String filter(CharSequence originalText) {
        return Autolink.renderLinks(originalText, linkExtractor.extractLinks(originalText), new NullRenderer());
    }

    private class NullRenderer implements LinkRenderer {

        @Override
        public void render(LinkSpan link, CharSequence text, StringBuilder sb) {
            sb.append(" ");
        }
    }

}
