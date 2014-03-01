package com.optimaize.langdetect.profiles;

import com.optimaize.langdetect.text.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Fabian Kessler
 */
public class LanguageProfileBuilderTest {

    @Test
    public void german() throws Exception {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder()
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .withTextFilter(UrlTextFilter.getInstance())
                .build();

        TextObject inputText = textObjectFactory.create()
                .append("deutsche Text")
                .append(" ")
                .append("http://www.github.com/");

        LanguageProfile languageProfile = new LanguageProfileBuilder("de")
                .addText(inputText)
                .build();

        assertEquals(1, languageProfile.getFrequency("sch"));
        assertEquals(0, languageProfile.getFrequency("www"));
    }

}
