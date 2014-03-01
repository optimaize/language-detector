package com.optimaize.langdetect.text;

import java.util.regex.Pattern;

/**
 * Removes URLs and email addresses from the text.
 *
 * @author Fabian Kessler
 */
public class UrlTextFilter implements TextFilter {

    private static final Pattern URL_REGEX = Pattern.compile("https?://[-_.?&~;+=/#0-9A-Za-z]+");
    private static final Pattern MAIL_REGEX = Pattern.compile("[-_.0-9A-Za-z]+@[-_0-9A-Za-z]+[-_.0-9A-Za-z]+");

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
