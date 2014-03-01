package com.optimaize.langdetect.ngram;

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
        List<String> ngrams = NgramExtractor.extractGrams(text, 1);
        assertEquals(ngrams.size(), text.length());
        assertEquals(ngrams, Arrays.asList("F","o","o"," ","b","a","r"));
    }

    @Test
    public void extractSortedGrams_2() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.extractGrams(text, 2);
        assertEquals(ngrams.size(), text.length()-1);
        assertEquals(ngrams, Arrays.asList("Fo","oo","o "," b","ba","ar"));
    }

    @Test
    public void extractSortedGrams_3() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.extractGrams(text, 3);
        assertEquals(ngrams.size(), text.length()-2);
    }

    @Test
    public void extractSortedGrams_6() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.extractGrams(text, 6);
        assertEquals(ngrams.size(), text.length()-5);
    }

    @Test
    public void extractSortedGrams_7() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.extractGrams(text, 7);
        assertEquals(ngrams.size(), text.length()-6);
    }

    @Test
    public void extractSortedGrams_8() {
        String text = "Foo bar";
        List<String> ngrams = NgramExtractor.extractGrams(text, 8);
        assertTrue(ngrams.isEmpty());
    }

}
