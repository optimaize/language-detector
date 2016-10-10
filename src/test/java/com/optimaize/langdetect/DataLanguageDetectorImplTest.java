package com.optimaize.langdetect;

import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;

//import org.junit.BeforeClass;
//import org.junit.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Uses all built-in language profiles and tests some simple clean phrases as well as longer texts  against them
 * with expected outcome.
 *
 * @author Fabian Kessler
 */
public class DataLanguageDetectorImplTest {

    private static LanguageDetector shortDetector;
    private static LanguageDetector longDetector;

    @BeforeTest
    //public DataLanguageDetectorImplTest() throws IOException {
    public void init() throws IOException {
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

    @Test(dataProvider = "onLargeTexts")
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
    protected Object[][] onLargeTexts() {
        return new Object[][] {
                {"en", largeText("This is some English text.")},
                {"fr", largeText("Ceci est un texte français.")},
                {"nl", largeText("Dit is een Nederlandse tekst.")},
                {"de", largeText("Dies ist eine deutsche Text")},
                {"km", largeText("សព្វវចនាធិប្បាយសេរីសម្រាប់អ្នកទាំងអស់គ្នា។" + "នៅក្នុងវិគីភីឌាភាសាខ្មែរឥឡូវនេះមាន ១១៩៨រូបភាព សមាជិក១៥៣៣៣នាក់ និងមាន៤៥៨៣អត្ថបទ។")},
                {"bg", largeText("Европа не трябва да стартира нов конкурентен маратон и изход с приватизация")},
                {"it", largeText("Persone nate a padova")},
                {"it", largeText("attori canada")},
                {"de", largeText("Was ist die hauptstadt von kanada")},
                {"pl", largeText("I Kanadyjczycy")},
                {"en", largeText("actors from Canada")},
                {"id", largeText("Eropa tidak harus memulai maraton dan output kompetitif baru privatisasi.")},
                {"ja", largeText("ヨーロッパは新たな競争マラソンと出力民営化を起動してはいけません")},
                //{"zh", largeText("欧洲不能推出一个新的竞争马拉松和输出私有化")},   //simplified-chinese
                //{"zh", largeText(readTextChinese("/texts/zh-wikipedia-Chinese.txt"))},   //simplified-chinese
                {"ko", largeText("유럽은 새로운 경쟁 마라톤 및 출력 민영화를 시작하지 않아야합니다")},

        };
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
                {"it", shortCleanText("Persone nate a padova")},
                {"it", shortCleanText("attori canada")},
                {"de", shortCleanText("Was ist die hauptstadt von kanada")},
                {"pl", shortCleanText("I Kanadyjczycy")},
                {"en", shortCleanText("actors from Canada")},
                {"id", shortCleanText("Eropa tidak harus memulai maraton dan output kompetitif baru privatisasi.")},
                {"ja", shortCleanText("ヨーロッパは新たな競争マラソンと出力民営化を起動してはいけません")},
                {"zh", shortCleanText("欧洲不能推出一个新的竞争马拉松和输出私有化")},   //simplified-chinese
                //{"zh", shortCleanText("歐洲不能推出一個新的競爭馬拉松和輸出私有化")}, //traditional-chinese
                {"ko", shortCleanText("유럽은 새로운 경쟁 마라톤 및 출력 민영화를 시작하지 않아야합니다")},
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

    private CharSequence readTextChinese(String path) {
        try (InputStream inputStream = DataLanguageDetectorImplTest.class.getResourceAsStream(path)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("Big5")))) {
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
