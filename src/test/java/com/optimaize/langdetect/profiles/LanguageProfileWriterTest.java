/*
 * Copyright 2011 Francois ROLAND
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

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author François ROLAND
 * @author Fabian Kessler
 */
public class LanguageProfileWriterTest {

    private static final File PROFILE_DIR = new File(new File(new File(new File("src"), "main"), "resources"), "languages");

    @Test
    public void writeEnProfile() throws IOException {
        checkProfileCopy("en");
    }

    @Test
    public void writeFrProfile() throws IOException {
        checkProfileCopy("fr");
    }

    @Test
    public void writeNlProfile() throws IOException {
        checkProfileCopy("nl");
    }

    protected void checkProfileCopy(String language) throws IOException {
        File originalFile = new File(PROFILE_DIR, language);
        final LanguageProfile originalProfile = new LanguageProfileReader().read(originalFile);
        File newFile = File.createTempFile("profile-copy-", null);
        try (FileOutputStream output = new FileOutputStream(newFile)) {
            new LanguageProfileWriter().write(originalProfile, output);
            LanguageProfile newProfile = new LanguageProfileReader().read(newFile);
            assertEquals(newProfile.getLocale(), originalProfile.getLocale());
            assertEquals(newProfile.getNumGrams(), originalProfile.getNumGrams());
            assertEquals(newProfile.getGramLengths(), originalProfile.getGramLengths());
            assertEquals(newProfile, originalProfile);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            newFile.delete();
        }
    }

}
