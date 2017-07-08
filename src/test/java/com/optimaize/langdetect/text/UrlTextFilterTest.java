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

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UrlTextFilterTest {

    @DataProvider(name = "exampleUrls")
    public static Object[][] data() {
        return new Object[][]{ //
                {"http://foo.com/blah_blah", true}, //
                {"http://foo.com:8080/blah_blah", true}, //
                {"http://foo.com/blah_blah/", true}, //
                {"http://foo-bar.com/blah_blah/", true}, //
                {"http://foo.com/blah_blah_(wikipedia)", true}, //
                {"http://foo.com/blah_blah_(wikipedia)_(again)", true}, //
                {"http://www.example.com/wpstyle/?p=364", true}, //
                {"https://www.example.com/foo/?bar=baz&inga=42&quux", true}, //
                {"http://userid:password@example.com:8080", true}, //
                {"http://userid:password@example.com:8080/", true}, //
                {"http://userid@example.com", true}, //
                {"http://userid@example.com/", true}, //
                {"http://userid@example.com:8080", true}, //
                {"http://userid@example.com:8080/", true}, //
                {"http://userid:password@example.com", true}, //
                {"http://foo.com/blah_(wikipedia)#cite-1", true}, //
                {"http://foo.com/blah_(wikipedia)_blah#cite-1", true}, //
                {"http://foo.com/(something)?after=parens", true}, //
                {"http://code.google.com/events/#&product=browser", true}, //
                {"https://foo.com/blah_blah", true}, //
                {"ftp://foo.com/blah_blah", true}, //
                {"http://.", true}, //
                {"http://?", true}, //
                {"http://#", true}, //
                {"http://##", true}, //
                {"//", false}, //
                {"//a", false}, //
                {"///a", false}, //
                {"///", false}, //
                {"h://test", true}, //
                {"http://-error-.invalid/", true} //
        };
    }

    @Test(dataProvider = "exampleUrls")
    public void checkUrl(String testUrl, Boolean acceptUrl) {
        String testTextPrefix = "just a short test ";
        String testTextSuffix = " here";

        String expectedResult = testTextPrefix + (acceptUrl ? " " : testUrl) + testTextSuffix;

        UrlTextFilter filter = UrlTextFilter.getInstance();
        String result = filter.filter(testTextPrefix + testUrl + testTextSuffix);

        assertEquals(result, expectedResult);
    }
}
