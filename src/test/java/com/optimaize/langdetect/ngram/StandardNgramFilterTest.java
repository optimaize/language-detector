package com.optimaize.langdetect.ngram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class StandardNgramFilterTest {

    @Test
    public void oneGram() throws Exception {
        assertTrue(new StandardNgramFilter().use("a"));
        assertTrue(new StandardNgramFilter().use("A"));

        assertFalse(new StandardNgramFilter().use(" "));
    }

    @Test
    public void twoGram() throws Exception {
        assertTrue(new StandardNgramFilter().use("ab"));
        assertTrue(new StandardNgramFilter().use("Ab"));
        assertTrue(new StandardNgramFilter().use("AB"));
        assertTrue(new StandardNgramFilter().use("a "));
        assertTrue(new StandardNgramFilter().use("a"));
    }

    @Test
    public void threeGram() throws Exception {
        assertTrue(new StandardNgramFilter().use("abc"));
        assertTrue(new StandardNgramFilter().use("Abc"));
        assertTrue(new StandardNgramFilter().use("ABC"));
        assertTrue(new StandardNgramFilter().use("ab "));
        assertTrue(new StandardNgramFilter().use(" ab"));

        assertFalse(new StandardNgramFilter().use("a c"));
    }

}
