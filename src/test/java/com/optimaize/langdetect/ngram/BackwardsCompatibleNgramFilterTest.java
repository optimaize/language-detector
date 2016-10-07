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
