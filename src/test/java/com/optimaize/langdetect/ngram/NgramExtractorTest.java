package com.optimaize.langdetect.ngram;

import org.junit.Test;

import java.util.List;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class NgramExtractorTest {

    @Test
    public void testExtractNGrams() throws Exception {
        List<String> ngrams = NgramExtractor.extractNGrams("Foo bar", null);
        assertTrue(ngrams.contains("Foo"));
        assertTrue(ngrams.contains(" Fo"));
        assertTrue(ngrams.contains("F"));
        assertEquals(ngrams.size(), 18); //adapt when making changes to the extractor...
    }

}
