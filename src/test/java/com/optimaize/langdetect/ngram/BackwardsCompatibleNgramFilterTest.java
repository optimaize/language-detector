package com.optimaize.langdetect.ngram;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Fabian Kessler
 */
public class BackwardsCompatibleNgramFilterTest {

    public static final NgramFilter filter = BackwardsCompatibleNgramFilter.getInstance();

    @Test
    public void oneGram() throws Exception {
        assertTrue(filter.use("a"));
        assertTrue(filter.use("A"));

        assertFalse(filter.use(" "));
    }

    @Test
    public void twoGram() throws Exception {
        assertTrue(filter.use("ab"));
        assertTrue(filter.use("Ab"));
        assertTrue(filter.use("a "));
        assertTrue(filter.use("a"));

        assertFalse(filter.use("AB"));
    }

    @Test
    public void threeGram() throws Exception {
        assertTrue(filter.use("abc"));
        assertTrue(filter.use("Abc"));
        assertTrue(filter.use("ab "));
        assertTrue(filter.use(" ab"));

        assertFalse(filter.use("a c"));
        assertFalse(filter.use("ABC"));
    }
    
}
