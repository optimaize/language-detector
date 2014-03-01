package com.optimaize.langdetect.profiles;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author François ROLAND
 * @author Fabian Kessler
 */
public class LanguageProfileReaderTest {

    private static final File PROFILE_DIR = new File(new File(new File(new File("src"), "main"), "resources"), "languages");


    @Test
    public void readEnFile() throws IOException {
        checkProfileFile("en", 3, 2301);
    }

    @Test
    public void readBnFile() throws IOException {
        checkProfileFile("bn", 3, 2846);
    }

    @Test
    public void readFrFile() throws IOException {
        checkProfileFile("fr", 3, 2232);
    }

    @Test
    public void readNlFile() throws IOException {
        checkProfileFile("nl", 3, 2163);
    }

    private static void checkProfileFile(String language, int nWordSize, int freqSize) throws IOException {
        File profileFile = new File(PROFILE_DIR, language);
        final LanguageProfile languageProfile = new LanguageProfileReader().read(profileFile);
        assertThat(languageProfile, is(notNullValue()));
        assertThat(languageProfile.getLanguage(), is(equalTo(language)));
        assertEquals(languageProfile.getGramLengths().size(), nWordSize);
        assertEquals(languageProfile.getGramLengths(), ImmutableList.of(1, 2, 3));
        assertEquals(languageProfile.getNumGrams(), freqSize);
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
    public void readAll() throws IOException {
        List<LanguageProfile> read = new LanguageProfileReader().readAll();
        assertEquals(read.size(), 47); //adjust this number when adding more languages
    }



    @Test
    public void loadProfilesFromClasspath() throws IOException {
        List<LanguageProfile> result = new LanguageProfileReader().read(this.getClass().getClassLoader(), "languages", ImmutableList.of("en", "fr", "nl", "de"));
        assertEquals(result.size(), 4);
    }

    @Test
    public void loadProfilesFromFile() throws IOException {
        List<LanguageProfile> result = new LanguageProfileReader().readAll(new File(new File(new File(new File("src"), "main"), "resources"), "languages"));
        assertEquals(result.size(), 47);
    }

}
