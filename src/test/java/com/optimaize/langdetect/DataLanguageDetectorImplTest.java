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

package com.optimaize.langdetect;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileBuilder;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.profiles.LanguageProfileWriter;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Uses all built-in language profiles and tests some simple clean phrases as well as longer texts  against them
 * with expected outcome.
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

    @Test(dataProvider = "shortCleanTexts")
    public void shortTextAlgo(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(shortDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        //the detect() method doesn't have enough confidence for all these short texts.
    }

    @Test(dataProvider = "shortCleanTexts")
    public void longTextAlgoWorkingOnShortText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        //the detect() method doesn't have enough confidence for all these short texts.
    }

    @Test(dataProvider = "longerWikipediaTexts")
    public void longTextAlgoWorkingOnLongText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        assertEquals(longDetector.detect(text).get().getLanguage(), expectedLanguage);
    }

    @DataProvider
    protected Object[][] shortCleanTexts() {
        return new Object[][] {
                {"en", shortCleanText("This is some English text.")},
                {"fr", shortCleanText("Ceci est un texte français.")},
                {"nl", shortCleanText("Dit is een Nederlandse tekst.")},
                {"de", shortCleanText("Dies ist eine deutsche Text")},
                {"km", shortCleanText("សព្វវចនាធិប្បាយសេរីសម្រាប់អ្នកទាំងអស់គ្នា។" + "នៅក្នុងវិគីភីឌាភាសាខ្មែរឥឡូវនេះមាន ១១៩៨រូបភាព សមាជិក១៥៣៣៣នាក់ និងមាន៤៥៨៣អត្ថបទ។")},
                {"bg", shortCleanText("Европа не трябва да стартира нов конкурентен маратон и изход с приватизация")},
                {"wa", shortCleanText("Çouchal c' est on tecse pår e walon.")},
                {"tig", shortCleanText("ከሰፍሐት ፈን ወዓዳት እሊ እት አክትበት በዲር ለህለ ርዝቅ ህግያነ ክሉ አዳም ጀላብ እግል ልርከቡ፡ እትሊ ዓሙድ እሊ ምነ ብዞሕ ጽበጥለ አክትበት ዲብ ለሐሬ ቀድም እኩም ህለ።")}
        };
    }
    private CharSequence shortCleanText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingShortCleanText().forText( text );
    }

    @DataProvider
    protected Object[][] longerWikipediaTexts() {
        return new Object[][] {
                {"de", largeText(readText("/texts/de-wikipedia-Deutschland.txt"))},
                {"fr", largeText(readText("/texts/fr-wikipedia-France.txt"))},
                {"it", largeText(readText("/texts/it-wikipedia-Italia.txt"))},
                {"tig", largeText(readText("/texts/tig-EritreaHaddas-Tigre.txt"))},
        };
    }

    private CharSequence readText(String path) {
        try (InputStream inputStream = DataLanguageDetectorImplTest.class.getResourceAsStream(path)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CharSequence largeText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingOnLargeText().forText( text );
    }




}
