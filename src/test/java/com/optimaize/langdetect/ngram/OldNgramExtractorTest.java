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
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i=0; i<100000; i++) {
            OldNgramExtractor.extractNGrams(text, null); //2.745s
        }
        System.out.println(stopwatch);
    }

}
