package com.optimaize.langdetect;

import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Uses all built-in language profiles and tests some simple clean phrases against them with expected outcome.
 *
 * @author Fabian Kessler
 */
public class DataLanguageDetectorImplTest {

    private final LanguageDetector shortDetector;
    private final LanguageDetector longDetector;

    public DataLanguageDetectorImplTest() throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        shortDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(100)
                .withProfiles(languageProfiles)
                .build();

        longDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(0)
                .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                .build();
    }

    @Test(dataProvider = "texts")
    public void shortTextAlgo(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(shortDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);

        //not all are confident enough for the detect() method.
        //assertEquals(shortDetector.detect(text).get().getLanguage(), expectedLanguage);
    }

    @Test(dataProvider = "texts")
    public void longTextAlgo(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);

        //not all are confident enough for the detect() method.
        //assertEquals(longDetector.detect(text).get().getLanguage(), expectedLanguage);
    }

    @DataProvider
    protected Object[][] texts() {
        return new Object[][] {
                {"en", text("This is some English text.")},
                {"fr", text("Ceci est un texte français.")},
                {"nl", text("Dit is een Nederlandse tekst.")},
                {"de", text("Dies ist eine deutsche Text")},
                {"km", text("សព្វវចនាធិប្បាយសេរីសម្រាប់អ្នកទាំងអស់គ្នា។" +"នៅក្នុងវិគីភីឌាភាសាខ្មែរឥឡូវនេះមាន ១១៩៨រូបភាព សមាជិក១៥៣៣៣នាក់ និងមាន៤៥៨៣អត្ថបទ។")},
                {"bg", text("Европа не трябва да стартира нов конкурентен маратон и изход с приватизация")},
        };
    }


    private CharSequence text(CharSequence text) {
        return CommonTextObjectFactories.forDetectingShortCleanText().forText( text );
    }

}
