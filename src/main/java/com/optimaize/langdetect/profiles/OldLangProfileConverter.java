package com.optimaize.langdetect.profiles;

import com.cybozu.labs.langdetect.util.LangProfile;

import java.util.Map;

/**
 * Converts an old {@link LangProfile} to a new {@link LanguageProfile}.
 *
 * @author Fabian Kessler
 */
public class OldLangProfileConverter {

    public static LanguageProfile convert(LangProfile langProfile) {
        LanguageProfileBuilder builder = new LanguageProfileBuilder(langProfile.getName());
        for (Map.Entry<String, Integer> entry : langProfile.getFreq().entrySet()) {
            builder.addGram(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

}
