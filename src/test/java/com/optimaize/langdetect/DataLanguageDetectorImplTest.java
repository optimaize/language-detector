package com.optimaize.langdetect;

import com.optimaize.langdetect.ngram.NgramExtractor;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Fabian Kessler
 */
public class DataLanguageDetectorImplTest {

    @Test
    public void shortTextAlgo() throws IOException {
        LanguageDetector detector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(100)
                .withProfiles(new LanguageProfileReader().readAll())
                .build();
        runTests(detector);
    }

    @Test
    public void longTextAlgo() throws IOException {
        LanguageDetector detector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(0)
                .withProfiles(new LanguageProfileReader().readAll())
                .build();
        runTests(detector);
    }

    /**
     * Note: this works stable with getProbabilities(), the best language comes first.
     * However, with detect(), it would not always work for Dutch.
     * The short text algorithm returns a stable 0.99something, but not high enough for the current standards.
     * The long text algorithm returns either a 0.9999something (often), or a much lower 0.7something (sometimes).
     */
    private void runTests(LanguageDetector detector) {
        assertEquals(detector.getProbabilities(text("This is some English text.")).get(0).getLanguage(), "en");
        assertEquals(detector.getProbabilities(text("Ceci est un texte français.")).get(0).getLanguage(), "fr");
        assertEquals(detector.getProbabilities(text("Dit is een Nederlandse tekst.")).get(0).getLanguage(), "nl");
        assertEquals(detector.getProbabilities(text("Dies ist eine deutsche Text")).get(0).getLanguage(), "de");
    }

    private CharSequence text(CharSequence text) {
        return CommonTextObjectFactories.forDetectingShortCleanText().forText( text );
    }

}
