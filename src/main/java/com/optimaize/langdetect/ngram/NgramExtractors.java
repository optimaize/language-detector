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

/**
 * Provides easy access to commonly used NgramExtractor configs.
 *
 * @author Fabian Kessler
 */
public class NgramExtractors {

    private static final NgramExtractor STANDARD = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(StandardNgramFilter.getInstance())
            .textPadding(' ');

    private static final NgramExtractor BACKWARDS = NgramExtractor
            .gramLengths(1, 2, 3)
            .filter(BackwardsCompatibleNgramFilter.getInstance())
            .textPadding(' ');


    /**
     * The new standard n-gram algorithm.
     */
    public static NgramExtractor standard() {
        return STANDARD;
    }

    /**
     * The old way of doing n-grams.
     */
    public static NgramExtractor backwards() {
        return BACKWARDS;
    }

}
