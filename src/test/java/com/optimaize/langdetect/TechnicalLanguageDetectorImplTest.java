/*
 * Copyright 2011 François ROLAND
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

import com.cybozu.labs.langdetect.LangDetectException;
import com.optimaize.langdetect.profiles.LanguageProfileBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * These are the tests of the old detector from Shoyu. Running them against the new detector from Fabian.
 *
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 */
public class TechnicalLanguageDetectorImplTest {

    private static final String TRAINING_EN = "a a a b b c c d e";
    private static final String TRAINING_FR = "a b b c c c d d d";
    private static final String TRAINING_JA = "\u3042 \u3042 \u3042 \u3044 \u3046 \u3048 \u3048";


    private LanguageDetector makeDetector() {
        //building exactly like the old detector behaved.
        LanguageDetectorBuilder detectorBuilder = new LanguageDetectorBuilder()
                .borderFactor(1.0)
                .shortTextAlgorithm(0)
                .skipUnknownNgrams(true);

        LanguageProfileBuilder profileBuilder = new LanguageProfileBuilder("en");
        add(detectorBuilder, profileBuilder, TRAINING_EN);

        profileBuilder = new LanguageProfileBuilder("fr");
        add(detectorBuilder, profileBuilder, TRAINING_FR);

        profileBuilder = new LanguageProfileBuilder("ja");
        add(detectorBuilder, profileBuilder, TRAINING_JA);

        return detectorBuilder.build();
    }
    private void add(LanguageDetectorBuilder detectorBuilder, LanguageProfileBuilder profileBuilder, String trainingEn) {
        for (String w : trainingEn.split(" ")) {
            profileBuilder.addGram(w);
        }
        detectorBuilder.withProfile(profileBuilder.build());
    }


    @Test
    public final void testDetector1() {
        LanguageDetector languageDetector = makeDetector();
        assertEquals(languageDetector.detect("a").get(), "en");
    }

    @Test
    public final void testDetector2() {
        LanguageDetector languageDetector = makeDetector();
        assertEquals(languageDetector.detect("b d").get(), "fr");
    }

    @Test
    public final void testDetector3() {
        LanguageDetector languageDetector = makeDetector();
        assertEquals(languageDetector.detect("d e").get(), "en");
    }

    @Test
    public final void testDetector4() throws LangDetectException {
        LanguageDetector languageDetector = makeDetector();
        assertEquals(languageDetector.detect("\u3042\u3042\u3042\u3042a").get(), "ja");
    }
}
