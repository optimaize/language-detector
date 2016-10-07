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
