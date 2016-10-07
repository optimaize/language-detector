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

package com.optimaize.langdetect.profiles;

import com.optimaize.langdetect.cybozu.util.LangProfile;
import com.optimaize.langdetect.i18n.LdLocale;

import java.util.Map;

/**
 * Converts an old {@link LangProfile} to a new {@link LanguageProfile}.
 *
 * @author Fabian Kessler
 */
public class OldLangProfileConverter {

    public static LanguageProfile convert(LangProfile langProfile) {
        LdLocale locale;
        try {
            locale = LdLocale.fromString(langProfile.getName());
        } catch (Exception e) {
            throw new RuntimeException("Profile file name logic was changed in v0.5, please update your custom profiles!", e);
        }
        LanguageProfileBuilder builder = new LanguageProfileBuilder(locale);
        for (Map.Entry<String, Integer> entry : langProfile.getFreq().entrySet()) {
            builder.addGram(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

}
