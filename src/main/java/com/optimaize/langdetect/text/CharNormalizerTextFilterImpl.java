package com.optimaize.langdetect.text;

import com.optimaize.langdetect.cybozu.util.CharNormalizer;

/**
 * Runs through the {@link CharNormalizer}.
 *
 * @author Fabian Kessler
 * @deprecated can't be used because it would be a big loss to not inline this code.
 */
public class CharNormalizerTextFilterImpl implements TextFilter {

    @Override
    public String filter(CharSequence text) {
        StringBuilder ret = new StringBuilder();
        char pre = 0;
        for (int i=0; i<text.length(); i++) {
            char c = CharNormalizer.normalize(text.charAt(i));
            if (c != ' ' || pre != ' ') {
                ret.append(c);
            }
            pre = c;
        }
        return ret.toString();
    }

}
