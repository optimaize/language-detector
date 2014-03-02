package com.optimaize.langdetect.ngram;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Fabian Kessler
 */
public class BackwardsCompatibleNgramFilterTest {

    @Test
    public void oneGram() throws Exception {
        assertTrue(new BackwardsCompatibleNgramFilter().use("a"));
        assertTrue(new BackwardsCompatibleNgramFilter().use("A"));

        assertFalse(new BackwardsCompatibleNgramFilter().use(" "));
    }

    @Test
    public void twoGram() throws Exception {
        assertTrue(new BackwardsCompatibleNgramFilter().use("ab"));
        assertTrue(new BackwardsCompatibleNgramFilter().use("Ab"));
        assertTrue(new BackwardsCompatibleNgramFilter().use("a "));
        assertTrue(new BackwardsCompatibleNgramFilter().use("a"));

        assertFalse(new BackwardsCompatibleNgramFilter().use("AB"));
    }

    @Test
    public void threeGram() throws Exception {
        assertTrue(new BackwardsCompatibleNgramFilter().use("abc"));
        assertTrue(new BackwardsCompatibleNgramFilter().use("Abc"));
        assertTrue(new BackwardsCompatibleNgramFilter().use("ab "));
        assertTrue(new BackwardsCompatibleNgramFilter().use(" ab"));

        assertFalse(new BackwardsCompatibleNgramFilter().use("a c"));
        assertFalse(new BackwardsCompatibleNgramFilter().use("ABC"));
    }
    
}
