/*
 * Copyright 2011 Nakatani Shuyo
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

package com.optimaize.langdetect.cybozu.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Nakatani Shuyo
 */
public class NGramTest {

    /**
     * Test method for constants
     */
    @Test
    public final void testConstants() {
        assertThat(NGram.N_GRAM, is(3));
        assertEquals(NGram.N_GRAM, 3);
    }


    /**
     * Test method for {@link NGram#get(int)} and {@link NGram#addChar(char)}
     */
    @Test
    public final void testNGram() {
        NGram ngram = new NGram();
        assertEquals(ngram.get(0), null);
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), null);
        assertEquals(ngram.get(3), null);
        assertEquals(ngram.get(4), null);
        ngram.addChar(' ');
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), null);
        assertEquals(ngram.get(3), null);
        ngram.addChar('A');
        assertEquals(ngram.get(1), "A");
        assertEquals(ngram.get(2), " A");
        assertEquals(ngram.get(3), null);
        ngram.addChar('\u06cc');
        assertEquals(ngram.get(1), "\u064a");
        assertEquals(ngram.get(2), "A\u064a");
        assertEquals(ngram.get(3), " A\u064a");
        ngram.addChar('\u1ea0');
        assertEquals(ngram.get(1), "\u1ec3");
        assertEquals(ngram.get(2), "\u064a\u1ec3");
        assertEquals(ngram.get(3), "A\u064a\u1ec3");
        ngram.addChar('\u3044');
        assertEquals(ngram.get(1), "\u3042");
        assertEquals(ngram.get(2), "\u1ec3\u3042");
        assertEquals(ngram.get(3), "\u064a\u1ec3\u3042");

        ngram.addChar('\u30a4');
        assertEquals(ngram.get(1), "\u30a2");
        assertEquals(ngram.get(2), "\u3042\u30a2");
        assertEquals(ngram.get(3), "\u1ec3\u3042\u30a2");
        ngram.addChar('\u3106');
        assertEquals(ngram.get(1), "\u3105");
        assertEquals(ngram.get(2), "\u30a2\u3105");
        assertEquals(ngram.get(3), "\u3042\u30a2\u3105");
        ngram.addChar('\uac01');
        assertEquals(ngram.get(1), "\uac00");
        assertEquals(ngram.get(2), "\u3105\uac00");
        assertEquals(ngram.get(3), "\u30a2\u3105\uac00");
        ngram.addChar('\u2010');
        assertEquals(ngram.get(1), null);
        assertEquals(ngram.get(2), "\uac00 ");
        assertEquals(ngram.get(3), "\u3105\uac00 ");

        ngram.addChar('a');
        assertEquals(ngram.get(1), "a");
        assertEquals(ngram.get(2), " a");
        assertEquals(ngram.get(3), null);
    }
   
}