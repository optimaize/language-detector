package com.optimaize.langdetect;

import com.optimaize.langdetect.frma.LangProfileReader;
import com.optimaize.langdetect.cybozu.util.LangProfile;
import com.google.common.collect.ImmutableList;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.OldLangProfileConverter;
import com.optimaize.langdetect.text.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Fabian Kessler
 */
public class LanguageDetectorImplTest {

    private LanguageDetector languageDetector;

    @Before
    public void setUp() throws IOException {
        LanguageDetectorBuilder builder = LanguageDetectorBuilder.create(NgramExtractors.standard());
        builder.shortTextAlgorithm(50)
                .prefixFactor(1.5)
                .suffixFactor(2.0);

        LangProfileReader langProfileReader = new LangProfileReader();
        for (String language : ImmutableList.of("en", "fr", "nl", "de")) {
            LangProfile langProfile = langProfileReader.read(LanguageDetectorImplTest.class.getResourceAsStream("/languages/" + language));
            LanguageProfile languageProfile = OldLangProfileConverter.convert(langProfile);
            builder.withProfile(languageProfile);
        }
        languageDetector = builder.build();
    }

    @Test
    public void german() throws IOException {
        List<DetectedLanguage> result = languageDetector.getProbabilities("Dies ist eine deutsche Text");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void french() throws IOException {
        List<DetectedLanguage> result = languageDetector.getProbabilities("Ceci est un texte en français");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), "fr");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void dutch() throws IOException {
        List<DetectedLanguage> result = languageDetector.getProbabilities("Dit is wat tekst");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), "nl");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void germanShort() throws IOException {
        List<DetectedLanguage> result = languageDetector.getProbabilities("deutsche Text");
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

    @Test
    public void germanShortWithUrl() throws IOException {
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        TextObject inputText = textObjectFactory.create().append("deutsche Text").append(" ").append("http://www.github.com/");

        List<DetectedLanguage> result = languageDetector.getProbabilities(inputText);
        DetectedLanguage best = result.get(0);
        assertEquals(best.getLocale().getLanguage(), "de");
        assertTrue(best.getProbability() >= 0.9999d);
    }

}
