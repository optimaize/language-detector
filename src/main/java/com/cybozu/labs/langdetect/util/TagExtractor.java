/*
 * Copyright 2011 Nakatani Shuyo
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

package com.cybozu.labs.langdetect.util;

import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObjectFactory;

/**
 * {@link TagExtractor} is a class which extracts inner texts of specified tag.
 * Users don't use this class directly.
 * @author Nakatani Shuyo
 */
public class TagExtractor {

    private static final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexing();

    /* package scope */ String target_;
    /* package scope */ int threshold_;
    /* package scope */ StringBuilder buf_;
    /* package scope */ String tag_;
    private int count_;

    public TagExtractor(String tag, int threshold) {
        target_ = tag;
        threshold_ = threshold;
        count_ = 0;
        clear();
    }
    public int count() {
        return count_;
    }
    public void clear() {
        buf_ = new StringBuilder(" ");
        tag_ = null;
    }
    public void setTag(String tag){
        tag_ = tag;
    }
    public void add(String line) {
        if (tag_ != null && tag_.equals(target_) && line != null) {
            buf_.append(line);
        }
    }
    public void closeTag(LangProfile profile) {
        if ((profile != null) && tag_.equals(target_) && (buf_.length() > threshold_) && !isSpace()) {
            Util.addCharSequence(profile, textObjectFactory.forText(buf_));
            ++count_;
        }
        clear();
    }

    private boolean isSpace() {
        return (buf_.length()==1 && buf_.toString().equals(" "));
    }

}
