package com.optimaize.langdetect.ngram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class StandardNgramFilterTest {

    private static final NgramFilter filter = StandardNgramFilter.getInstance();

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
        assertTrue(filter.use("AB"));
        assertTrue(filter.use("a "));
        assertTrue(filter.use("a"));
    }

    @Test
    public void threeGram() throws Exception {
        assertTrue(filter.use("abc"));
        assertTrue(filter.use("Abc"));
        assertTrue(filter.use("ABC"));
        assertTrue(filter.use("ab "));
        assertTrue(filter.use(" ab"));

        assertFalse(filter.use("a c"));
    }

}
