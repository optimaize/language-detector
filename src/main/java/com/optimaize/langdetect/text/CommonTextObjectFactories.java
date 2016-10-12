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

/**
 * Contains some standard {@link com.optimaize.langdetect.text.TextObjectFactory}s ready to use for
 * common use cases.
 *
 * @author Fabian Kessler
 */
public class CommonTextObjectFactories {

    public static TextObjectFactory forDetectingOnLargeText() {
        return new TextObjectFactoryBuilder()
                .maxTextLength(10000)
                .withTextFilter(UrlTextFilter.getInstance())
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .build();
    }

    public static TextObjectFactory forDetectingShortCleanText() {
        return new TextObjectFactoryBuilder()
                .build();
    }

    public static TextObjectFactory forIndexing() {
        return new TextObjectFactoryBuilder()
                .withTextFilter(UrlTextFilter.getInstance())
                .withTextFilter(RemoveMinorityScriptsTextFilter.forThreshold(0.3))
                .build();
    }

    public static TextObjectFactory forIndexingCleanText() {
        return new TextObjectFactoryBuilder()
                .build();
    }

    public static TextObjectFactory forInstagramPosts() {
        return new TextObjectFactoryBuilder()
                .withTextFilter(UrlTextFilter.getInstance())
                .build();
    }

}
