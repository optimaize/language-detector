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
            assertEquals(newProfile.getLanguage(), originalProfile.getLanguage());
            assertEquals(newProfile.getNumGrams(), originalProfile.getNumGrams());
            assertEquals(newProfile.getGramLengths(), originalProfile.getGramLengths());
            assertEquals(newProfile, originalProfile);
        } finally {
            //noinspection ResultOfMethodCallIgnored
            newFile.delete();
        }
    }

}
