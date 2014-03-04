package com.optimaize.langdetect.profiles;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicole Torres
 */
public class BuiltInLanguages {

    private static final List<String> languages;
    private static final List<String> shortTextLanguages;

    static {
        List<String> names = new ArrayList<>();
        names.add("af");
        names.add("an");
        names.add("ar");
        names.add("ast");
        names.add("be");
        names.add("bg");
        names.add("bn");
        names.add("br");
        names.add("ca");
        names.add("cs");
        names.add("cy");
        names.add("da");
        names.add("de");
        names.add("el");
        names.add("en");
        names.add("es");
        names.add("et");
        names.add("eu");
        names.add("fa");
        names.add("fi");
        names.add("fr");
        names.add("ga");
        names.add("gl");
        names.add("gu");
        names.add("he");
        names.add("hi");
        names.add("hr");
        names.add("ht");
        names.add("hu");
        names.add("id");
        names.add("is");
        names.add("it");
        names.add("ja");
        names.add("kn");
        names.add("ko");
        names.add("lt");
        names.add("lv");
        names.add("mk");
        names.add("ml");
        names.add("mr");
        names.add("ms");
        names.add("mt");
        names.add("ne");
        names.add("nl");
        names.add("no");
        names.add("oc");
        names.add("pa");
        names.add("pl");
        names.add("pt");
        names.add("ro");
        names.add("ru");
        names.add("sk");
        names.add("sl");
        names.add("so");
        names.add("sq");
        names.add("sr");
        names.add("sv");
        names.add("sw");
        names.add("ta");
        names.add("te");
        names.add("th");
        names.add("tl");
        names.add("tr");
        names.add("uk");
        names.add("ur");
        names.add("vi");
        names.add("yi");
        names.add("zh-cn");
        names.add("zh-tw");
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
     * @return immutable
     */
    public static List<String> getLanguages() {
        return languages;
    }
    /**
     * @return immutable
     */
    public static List<String> getShortTextLanguages() {
        return shortTextLanguages;
    }
}
