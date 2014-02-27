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

package com.cybozu.labs.langdetect.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Nakatani Shuyo
 *
 */
public class LangProfileTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.LangProfile#LangProfile()}.
     */
    @Test
    public final void testLangProfile() {
        LangProfile profile = new LangProfile();
        assertEquals(profile.getName(), null);
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.LangProfile#LangProfile(java.lang.String)}.
     */
    @Test
    public final void testLangProfileStringInt() {
        LangProfile profile = new LangProfile("en");
        assertEquals(profile.getName(), "en");
    }

    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.LangProfile#add(java.lang.String)}.
     */
    @Test
    public final void testAdd() {
        LangProfile profile = new LangProfile("en");
        profile.add("a");
        assertEquals((int)profile.getFreq().get("a"), 1);
        profile.add("a");
        assertEquals((int)profile.getFreq().get("a"), 2);
        profile.omitLessFreq();
    }

    
    /**
     * Illegal call test for {@link LangProfile#add(String)}
     */
    @Test
    public final void testAddIllegally1() {
        LangProfile profile = new LangProfile(); // Illegal ( available for only JSONIC ) but ignore  
        profile.add("a"); // ignore
        assertEquals(profile.getFreq().get("a"), null); // ignored
    }

    /**
     * Illegal call test for {@link LangProfile#add(String)}
     */
    @Test
    public final void testAddIllegally2() {
        LangProfile profile = new LangProfile("en");
        profile.add("a");
        profile.add("");  // Illegal (string's length of parameter must be between 1 and 3) but ignore
        profile.add("abcd");  // as well
        assertEquals((int)profile.getFreq().get("a"), 1);
        assertEquals(profile.getFreq().get(""), null);     // ignored
        assertEquals(profile.getFreq().get("abcd"), null); // ignored
        
    }
    
    /**
     * Test method for {@link com.cybozu.labs.langdetect.util.LangProfile#omitLessFreq()}.
     */
    @Test
    public final void testOmitLessFreq() {
        LangProfile profile = new LangProfile("en");
        String[] grams = "a b c \u3042 \u3044 \u3046 \u3048 \u304a \u304b \u304c \u304d \u304e \u304f".split(" ");
        for (int i=0;i<5;++i) for (String g : grams) {
            profile.add(g);
        }
        profile.add("\u3050");

        assertEquals((int)profile.getFreq().get("a"), 5);
        assertEquals((int)profile.getFreq().get("\u3042"), 5);
        assertEquals((int)profile.getFreq().get("\u3050"), 1);
        profile.omitLessFreq();
        assertEquals(profile.getFreq().get("a"), null); // omitted
        assertEquals((int)profile.getFreq().get("\u3042"), 5);
        assertEquals(profile.getFreq().get("\u3050"), null); // omitted
    }

    /**
     * Illegal call test for {@link com.cybozu.labs.langdetect.util.LangProfile#omitLessFreq()}.
     */
    @Test
    public final void testOmitLessFreqIllegally() {
        LangProfile profile = new LangProfile();
        profile.omitLessFreq();  // ignore
    }

}
