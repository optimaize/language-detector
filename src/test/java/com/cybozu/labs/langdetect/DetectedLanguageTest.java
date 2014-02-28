/*
 * Copyright 2011 Nakatani Shuyo
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

import com.optimaize.langdetect.DetectedLanguage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Nakatani Shuyo
 * @author Fabian Kessler
 */
public class DetectedLanguageTest {

    @Test
    public final void basic() {
        DetectedLanguage lang = new DetectedLanguage("en", 1.0);
        assertEquals(lang.getLanguage(), "en");
        assertEquals(lang.getProbability(), 1.0, 0.0001);
        assertEquals(lang.toString(), "DetectedLanguage[en:1.0]");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void invalidProbability() {
        new DetectedLanguage("en", 1.1);
    }

    @Test
    public final void comparable() {
        List<DetectedLanguage> list = new ArrayList<>();
        list.add(new DetectedLanguage("en", 1.0));
        list.add(new DetectedLanguage("de", 1.0));
        list.add(new DetectedLanguage("fr", 0.9));
        Collections.sort(list);
        assertEquals(list.get(0).getLanguage(), "de"); //alphabetical de before en
        assertEquals(list.get(1).getLanguage(), "en");
        assertEquals(list.get(2).getLanguage(), "fr"); //points 0.9 the last
    }

}
