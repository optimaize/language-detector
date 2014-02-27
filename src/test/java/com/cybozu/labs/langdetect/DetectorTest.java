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

package com.cybozu.labs.langdetect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Unit test for {@link Detector} and {@link DetectorFactory}.
 * @author Nakatani Shuyo
 *
 */
public class DetectorTest {

    private static final String TRAINING_EN = "a a a b b c c d e";
    private static final String TRAINING_FR = "a b b c c c d d d";
    private static final String TRAINING_JA = "\u3042 \u3042 \u3042 \u3044 \u3046 \u3048 \u3048";

    @Before
    public void setUp() throws Exception {
        DetectorFactory.clear();
        
        LangProfile profile_en = new LangProfile("en");
        for (String w : TRAINING_EN.split(" "))
            profile_en.add(w);
        DetectorFactory.addProfile(profile_en, 0, 3);

        LangProfile profile_fr = new LangProfile("fr");
        for (String w : TRAINING_FR.split(" "))
            profile_fr.add(w);
        DetectorFactory.addProfile(profile_fr, 1, 3);

        LangProfile profile_ja = new LangProfile("ja");
        for (String w : TRAINING_JA.split(" "))
            profile_ja.add(w);
        DetectorFactory.addProfile(profile_ja, 2, 3);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testDetector1() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("a");
        assertEquals(detect.detect(), "en");
    }

    @Test
    public final void testDetector2() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("b d");
        assertEquals(detect.detect(), "fr");
    }

    @Test
    public final void testDetector3() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("d e");
        assertEquals(detect.detect(), "en");
    }

    @Test
    public final void testDetector4() throws LangDetectException {
        Detector detect = DetectorFactory.create();
        detect.append("\u3042\u3042\u3042\u3042a");
        assertEquals(detect.detect(), "ja");
    }
}
