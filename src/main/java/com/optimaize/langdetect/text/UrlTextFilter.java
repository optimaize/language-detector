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

import java.util.regex.Pattern;

/**
 * Removes URLs and email addresses from the text.
 *
 * @author Fabian Kessler
 */
public class UrlTextFilter implements TextFilter {

    private static final Pattern URL_REGEX = Pattern.compile("https?://[-_.?&~;+=/#0-9A-Za-z]+");
    private static final Pattern MAIL_REGEX = Pattern.compile("(?<![-+_.0-9A-Za-z])[-+_.0-9A-Za-z]+@[-0-9A-Za-z]+[-.0-9A-Za-z]+");

    private static final UrlTextFilter INSTANCE = new UrlTextFilter();

    public static UrlTextFilter getInstance() {
        return INSTANCE;
    }

    private UrlTextFilter() {
    }

    @Override
    public String filter(CharSequence text) {
        String modified = URL_REGEX.matcher(text).replaceAll(" ");
        return MAIL_REGEX.matcher(modified).replaceAll(" ");
    }

}
