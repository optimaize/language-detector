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
