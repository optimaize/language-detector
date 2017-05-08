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

package com.optimaize.langdetect.ngram;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class OldNgramExtractorTest {

    @Test
    public void testExtractNGrams() {
        List<String> ngrams = OldNgramExtractor.extractNGrams("Foo bar", null);
        assertTrue(ngrams.contains("Foo"));
        assertTrue(ngrams.contains("F"));
        assertTrue(ngrams.contains(" Fo"));  //algorithm makes prefix-grams
        assertFalse(ngrams.contains("ar ")); //algorithm does not make suffix-grams
        assertEquals(ngrams.size(), 18); //adapt when making changes to the extractor...
    }

    @Test
    public void testExtractNGrams2() {
        List<String> ngrams = OldNgramExtractor.extractNGrams("Hallo DAA.", null);
        System.out.println(ngrams);
    }



    @Test
    public void stressTestAlgo1() {
        String text = "Foo bar hello world and so on nana nunu dada dudu asdf asdf akewf köjvnawer aisdfj awejfr iajdsöfj ewi adjsköfjwei ajsdökfj ief asd";
        Stopwatch stopwatch = (new Stopwatch()).start();
        for (int i=0; i<100000; i++) {
            OldNgramExtractor.extractNGrams(text, null); //2.745s
        }
        System.out.println(stopwatch);
    }

}
