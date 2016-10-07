/*
 * Copyright 2011 Nicole Torres
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

import com.google.common.collect.ImmutableList;
import com.optimaize.langdetect.i18n.LdLocale;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicole Torres
 */
public class BuiltInLanguages {

    private static final List<LdLocale> languages;
    private static final List<String> shortTextLanguages;

    static {
        List<LdLocale> names = new ArrayList<>();

        //sorted alphabetically
        names.add(LdLocale.fromString("af"));
        names.add(LdLocale.fromString("an"));
        names.add(LdLocale.fromString("ar"));
        names.add(LdLocale.fromString("ast"));
        names.add(LdLocale.fromString("be"));
        names.add(LdLocale.fromString("bg"));
        names.add(LdLocale.fromString("bn"));
        names.add(LdLocale.fromString("br"));
        names.add(LdLocale.fromString("ca"));
        names.add(LdLocale.fromString("cs"));
        names.add(LdLocale.fromString("cy"));
        names.add(LdLocale.fromString("da"));
        names.add(LdLocale.fromString("de"));
        names.add(LdLocale.fromString("el"));
        names.add(LdLocale.fromString("en"));
        names.add(LdLocale.fromString("es"));
        names.add(LdLocale.fromString("et"));
        names.add(LdLocale.fromString("eu"));
        names.add(LdLocale.fromString("fa"));
        names.add(LdLocale.fromString("fi"));
        names.add(LdLocale.fromString("fr"));
        names.add(LdLocale.fromString("ga"));
        names.add(LdLocale.fromString("gl"));
        names.add(LdLocale.fromString("gu"));
        names.add(LdLocale.fromString("he"));
        names.add(LdLocale.fromString("hi"));
        names.add(LdLocale.fromString("hr"));
        names.add(LdLocale.fromString("ht"));
        names.add(LdLocale.fromString("hu"));
        names.add(LdLocale.fromString("id"));
        names.add(LdLocale.fromString("is"));
        names.add(LdLocale.fromString("it"));
        names.add(LdLocale.fromString("ja"));
        names.add(LdLocale.fromString("km"));
        names.add(LdLocale.fromString("kn"));
        names.add(LdLocale.fromString("ko"));
        names.add(LdLocale.fromString("lt"));
        names.add(LdLocale.fromString("lv"));
        names.add(LdLocale.fromString("mk"));
        names.add(LdLocale.fromString("ml"));
        names.add(LdLocale.fromString("mr"));
        names.add(LdLocale.fromString("ms"));
        names.add(LdLocale.fromString("mt"));
        names.add(LdLocale.fromString("ne"));
        names.add(LdLocale.fromString("nl"));
        names.add(LdLocale.fromString("no"));
        names.add(LdLocale.fromString("oc"));
        names.add(LdLocale.fromString("pa"));
        names.add(LdLocale.fromString("pl"));
        names.add(LdLocale.fromString("pt"));
        names.add(LdLocale.fromString("ro"));
        names.add(LdLocale.fromString("ru"));
        names.add(LdLocale.fromString("sk"));
        names.add(LdLocale.fromString("sl"));
        names.add(LdLocale.fromString("so"));
        names.add(LdLocale.fromString("sq"));
        names.add(LdLocale.fromString("sr"));
        names.add(LdLocale.fromString("sv"));
        names.add(LdLocale.fromString("sw"));
        names.add(LdLocale.fromString("ta"));
        names.add(LdLocale.fromString("te"));
        names.add(LdLocale.fromString("th"));
        names.add(LdLocale.fromString("tl"));
        names.add(LdLocale.fromString("tr"));
        names.add(LdLocale.fromString("uk"));
        names.add(LdLocale.fromString("ur"));
        names.add(LdLocale.fromString("vi"));
        names.add(LdLocale.fromString("wa"));
        names.add(LdLocale.fromString("yi"));
        names.add(LdLocale.fromString("zh-CN"));
        names.add(LdLocale.fromString("zh-TW"));

        languages = ImmutableList.copyOf(names);
    }

    static {
        List<String> texts = new ArrayList<>();
        texts.add("cs");
        texts.add("da");
        texts.add("de");
        texts.add("en");
        texts.add("es");
        texts.add("fi");
        texts.add("fr");
        texts.add("id");
        texts.add("it");
        texts.add("nl");
        texts.add("no");
        texts.add("pl");
        texts.add("pt");
        texts.add("ro");
        texts.add("sv");
        texts.add("tr");
        texts.add("vi");
        shortTextLanguages = ImmutableList.copyOf(texts);
    }

    /**
     * Returns the languages for which the library provides full profiles.
     * Full provides are generated from regular text, usually Wikipedia abstracts.
     * @return immutable
     */
    public static List<LdLocale> getLanguages() {
        return languages;
    }

    /**
     * Returns the languages for which the library provides profiles created from short text.
     * Twitter was used as source by @shuyo.
     * Much less languages have short text profiles as of now.
     * @return immutable
     */
    public static List<String> getShortTextLanguages() {
        return shortTextLanguages;
    }
}
