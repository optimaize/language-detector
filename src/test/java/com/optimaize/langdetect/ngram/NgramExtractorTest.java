package com.optimaize.langdetect.ngram;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class NgramExtractorTest {

    @Test
    public void testExtractNGrams() {
        List<String> ngrams = NgramExtractor.extractNGrams("Foo bar", null);
        assertTrue(ngrams.contains("Foo"));
        assertTrue(ngrams.contains("F"));
        assertTrue(ngrams.contains(" Fo"));  //algorithm makes prefix-grams
        assertFalse(ngrams.contains("ar ")); //algorithm does not make suffix-grams
        assertEquals(ngrams.size(), 18); //adapt when making changes to the extractor...
    }

    @Test
    public void extractSortedGrams_1() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 1);
        assertEquals(ngrams.length, text.length());
        assertEquals(Arrays.asList(ngrams), Arrays.asList("F","o","o"," ","b","a","r"));
    }

    @Test
    public void extractSortedGrams_2() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 2);
        assertEquals(ngrams.length, text.length() - 1);
        assertEquals(Arrays.asList(ngrams), Arrays.asList("Fo","oo","o "," b","ba","ar"));
    }

    @Test
    public void extractSortedGrams_3() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 3);
        assertEquals(ngrams.length, text.length()-2);
    }

    @Test
    public void extractSortedGrams_6() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 6);
        assertEquals(ngrams.length, text.length()-5);
    }

    @Test
    public void extractSortedGrams_7() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 7);
        assertEquals(ngrams.length, text.length()-6);
    }

    @Test
    public void extractSortedGrams_8() {
        String text = "Foo bar";
        String[] ngrams = NgramExtractor.extractGrams(text, 8);
        assertTrue(ngrams.length==0);
    }




    @Test
    public void stressTestAlgo1() {
        String text = "Foo bar hello world and so on nana nunu dada dudu asdf asdf akewf köjvnawer aisdfj awejfr iajdsöfj ewi adjsköfjwei ajsdökfj ief asd";
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i=0; i<100000; i++) {
            NgramExtractor.extractNGrams(text, null); //2.745s
        }
        System.out.println(stopwatch);
    }

    @Test
    public void stressTestAlgo2() {
        String text = "Foo bar hello world and so on nana nunu dada dudu asdf asdf akewf köjvnawer aisdfj awejfr iajdsöfj ewi adjsköfjwei ajsdökfj ief asd";
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i=0; i<100000; i++) {
            NgramExtractor.extractGrams(text, 1);
            NgramExtractor.extractGrams(text, 2);
            NgramExtractor.extractGrams(text, 3);
        }
        System.out.println(stopwatch); //876.6ms
    }

}
