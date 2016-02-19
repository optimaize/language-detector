package com.optimaize.langdetect;

import com.optimaize.langdetect.frma.LangProfileReader;
import com.optimaize.langdetect.cybozu.util.LangProfile;
import com.google.common.collect.ImmutableList;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.OldLangProfileConverter;
import com.optimaize.langdetect.text.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import static org.testng.Assert.*;


/**
 * Basic tests for the LanguageDetectorImpl.
 *
 * @author Fabian Kessler
 */
public class LanguageDetectorImplTest {

    @Test(dataProvider = "confident")
    public void confident(String expectedLanguage, CharSequence text) throws Exception {
        LanguageDetector languageDetector = makeNewDetector();
        List<DetectedLanguage> result = languageDetector.getProbabilities(text);
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), expectedLanguage);
        assertTrue(best.getProbability() >= 0.9999d);
    }
    @DataProvider
    protected Object[][] confident() {
        return new Object[][] {
                {"de", "Dies ist eine deutsche Text"},
                {"de", "deutsche Text"},
                {"de", CommonTextObjectFactories.forDetectingOnLargeText().create().append("deutsche Text").append(" ").append("http://www.github.com/")},
        };
    }


    private LanguageDetector makeNewDetector() throws IOException {
        LanguageDetectorBuilder builder = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .shortTextAlgorithm(50)
            .prefixFactor(1.5)
            .suffixFactor(2.0);

        LangProfileReader langProfileReader = new LangProfileReader();
        for (String language : ImmutableList.of("en", "fr", "nl", "de")) {
            LangProfile langProfile = langProfileReader.read(LanguageDetectorImplTest.class.getResourceAsStream("/languages/" + language));
            LanguageProfile languageProfile = OldLangProfileConverter.convert(langProfile);
            builder.withProfile(languageProfile);
        }

        return builder.build();
    }

}
