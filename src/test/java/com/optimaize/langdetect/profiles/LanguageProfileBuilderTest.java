package com.optimaize.langdetect.profiles;

import com.optimaize.langdetect.ngram.NgramExtractor;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.text.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class LanguageProfileBuilderTest {

    @Test
    public void german() throws Exception {
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexing();

        TextObject inputText = textObjectFactory.create()
                .append("deutsche Text")
                .append(" ")
                .append("http://www.github.com/");

        LanguageProfile languageProfile = new LanguageProfileBuilder("de")
                .ngramExtractor(NgramExtractors.standard())
                .addText(inputText)
                .build();

        assertEquals(1, languageProfile.getFrequency("sch"));
        assertEquals(0, languageProfile.getFrequency("www"));
    }

    @Test
    public void profile_equals() throws Exception {
        LanguageProfile languageProfile1 = new LanguageProfileBuilder("de")
                .addGram("foo", 1)
                .build();

        LanguageProfile languageProfile2 = new LanguageProfileBuilder("de")
                .addGram("foo", 1)
                .build();

        LanguageProfile languageProfile3 = new LanguageProfileBuilder("de")
                .addGram("bar", 1)
                .build();

        assertEquals(languageProfile1, languageProfile2);
        assertNotEquals(languageProfile1, languageProfile3);
    }

    @Test
    public void profile_toString() throws Exception {
        LanguageProfile languageProfile = new LanguageProfileBuilder("de")
                .addGram("foo", 1)
                .build();
        assertTrue(languageProfile.toString().contains("de"));
        assertTrue(languageProfile.toString().contains("1"));
    }

}
