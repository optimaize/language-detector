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

import com.google.common.collect.ImmutableList;
import com.optimaize.langdetect.i18n.LdLocale;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 * @author François ROLAND
 */
public class LanguageProfileReaderTest {

    private static final File PROFILE_DIR = new File(new File(new File(new File("src"), "main"), "resources"), "languages");


    /*
    * In case someone creates new language profiles then these numbers need to be adjusted.
    */

    @Test
    public void readEnFile() throws IOException {
        checkProfileFile("en", 3, 2301, 26164, 3774627);
    }

    @Test
    public void readBnFile() throws IOException {
        checkProfileFile("bn", 3, 2846, 198, 22964);
    }

    @Test
    public void readFrFile() throws IOException {
        checkProfileFile("fr", 3, 2232, 6653, 1120211);
    }

    @Test
    public void readNlFile() throws IOException {
        checkProfileFile("nl", 3, 2163, 5640, 1373884);
    }

    private static void checkProfileFile(String language, int nWordSize, int freqSize, long minFreq, long maxFreq) throws IOException {
        File profileFile = new File(PROFILE_DIR, language);
        final LanguageProfile languageProfile = new LanguageProfileReader().read(profileFile);
        assertThat(languageProfile, is(notNullValue()));
        assertThat(languageProfile.getLocale().getLanguage(), is(equalTo(language)));
        assertEquals(languageProfile.getGramLengths().size(), nWordSize);
        assertEquals(languageProfile.getGramLengths(), ImmutableList.of(1, 2, 3));
        assertEquals(languageProfile.getNumGrams(), freqSize);

        assertTrue(languageProfile.getMinGramCount(nWordSize) < languageProfile.getMaxGramCount(nWordSize));
        assertEquals(languageProfile.getMinGramCount(nWordSize), minFreq);
        assertEquals(languageProfile.getMaxGramCount(nWordSize), maxFreq);
    }


    @Test
    public void readFromDir() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().read(ImmutableList.of("de", "fr"));
        assertEquals(read.size(), 2);
    }

    @Test
    public void readFromDirWithClassloader() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().read(
                LanguageProfileReaderTest.class.getClassLoader(),
                "languages",
                ImmutableList.of("de", "fr")
        );
        assertEquals(read.size(), 2);
    }


    @Test
    public void read() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().read(ImmutableList.of("de", "fr"));
        assertEquals(read.size(), 2);
    }

    @Test
    public void read_folder() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().read("languages", ImmutableList.of("de", "fr"));
        assertEquals(read.size(), 2);
    }

    @Test
    public void read_classpathAndFolder() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().read(LanguageProfileReaderTest.class.getClassLoader(), "languages", ImmutableList.of("de", "fr"));
        assertEquals(read.size(), 2);
    }

    @Test
    public void readAllBuiltIn() throws IOException {
        verify_readAllBuiltIn(new LanguageProfileReader().readAllBuiltIn());
    }
    private void verify_readAllBuiltIn(List<LanguageProfile> profiles) {
        assertEquals(profiles.size(), 71); //adjust this number when adding more languages
        Set<LdLocale> allLangs = new HashSet<>();
        for (LanguageProfile profile : profiles) {
            assertFalse("Duplicate language: "+profile.getLocale(), allLangs.contains(profile.getLocale()));
            allLangs.add(profile.getLocale());
        }
        assertTrue(allLangs.contains(LdLocale.fromString("de")));
        assertTrue(allLangs.contains(LdLocale.fromString("zh-CN")));
        assertTrue(allLangs.contains(LdLocale.fromString("zh-TW")));
    }


    @Test
    public void loadProfilesFromClasspath() throws IOException {
        List<LanguageProfile> result = new LanguageProfileReader().read(this.getClass().getClassLoader(), "languages", ImmutableList.of("en", "fr", "nl", "de"));
        assertEquals(result.size(), 4);
    }

    @Test
    public void loadProfilesFromFile() throws IOException {
        List<LanguageProfile> result = new LanguageProfileReader().readAll(new File(new File(new File(new File("src"), "main"), "resources"), "languages"));
        assertEquals(result.size(), 71); //adjust this number when adding more languages
    }

}
