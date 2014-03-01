package com.optimaize.langdetect;

import be.frma.langguess.LangProfileReader;
import com.cybozu.labs.langdetect.util.LangProfile;
import com.google.common.collect.ImmutableList;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.OldLangProfileConverter;
import com.optimaize.langdetect.text.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Fabian Kessler
 */
public class LanguageDetectorImplTest {

    @Test
    public void german() throws IOException {
        LanguageDetector languageDetector = makeNewDetector();
        List<DetectedLanguage> result = languageDetector.getProbabilities("Dies ist eine deutsche Text");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void germanShort() throws IOException {
        LanguageDetector languageDetector = makeNewDetector();
        List<DetectedLanguage> result = languageDetector.getProbabilities("deutsche Text");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void germanShortWithUrl() throws IOException {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder()
                .maxTextLength(10000)
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .withTextFilter(UrlTextFilter.getInstance())
                .build();
        TextObject inputText = textObjectFactory.create().append("deutsche Text").append(" ").append("http://www.github.com/");

        LanguageDetector languageDetector = makeNewDetector();
        List<DetectedLanguage> result = languageDetector.getProbabilities(inputText);
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    private LanguageDetector makeNewDetector() throws IOException {
        LanguageDetectorBuilder builder = new LanguageDetectorBuilder();
        builder.skipUnknownNgrams(false);
        builder.shortTextAlgorithm(50);
        builder.borderFactor(2.0);

        LangProfileReader langProfileReader = new LangProfileReader();
        for (String language : ImmutableList.of("en", "fr", "nl", "de")) {
            LangProfile langProfile = langProfileReader.read(LanguageDetectorImplTest.class.getResourceAsStream("/languages/" + language));
            LanguageProfile languageProfile = OldLangProfileConverter.convert(langProfile);
            builder.withProfile(languageProfile);
        }

        return builder.build();
    }

}
