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

package com.optimaize.langdetect.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class TextObjectTest {

    @Test
    public void simpleText() throws Exception {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder().withTextFilter(UrlTextFilter.getInstance()).build();
        TextObject inputText = textObjectFactory.create().append("Dies ist").append(" ").append("deutscher Text.");
        assertEquals(inputText.toString(), "Dies ist deutscher Text ");
    }

    @Test
    public void filteredContent() throws Exception {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder().withTextFilter(UrlTextFilter.getInstance()).build();
        TextObject inputText = textObjectFactory.create().append("deutscher Text").append(" ").append("http://www.github.com/");
        assertEquals(inputText.toString(), "deutscher Text ");
    }
}
