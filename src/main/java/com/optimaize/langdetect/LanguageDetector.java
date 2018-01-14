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

package com.optimaize.langdetect;

import com.optimaize.langdetect.i18n.LdLocale;

import java.util.List;
import java.util.Optional;

/**
 * Guesses the language of an input string or text.
 *
 * <p>See website for details.</p>
 *
 * <p>This detector cannot handle well:
 * Short input text, can work or give wrong results.
 * Text written in multiple languages. It likely returns the language for the most prominent text. It's not made for that.
 * Text written in languages for which the detector has no profile loaded. It may just return other similar languages.
 * </p>
 *
 * @author Fabian Kessler
 */
public interface LanguageDetector {

    /**
     * Returns the best detected language if the algorithm is very confident.
     *
     * <p>Note: you may want to use getProbabilities() instead. This here is very strict, and sometimes returns
     * absent even though the first choice in getProbabilities() is correct.</p>
     *
     * @param text You probably want a {@link com.optimaize.langdetect.text.TextObject}.
     * @return The language if confident, absent if unknown or not confident enough.
     */
    Optional<LdLocale> detect(CharSequence text);

    /**
     * Returns all languages with at least some likeliness.
     *
     * <p>There is a configurable cutoff applied for languages with very low probability.</p>
     *
     * <p>The way the algorithm currently works, it can be that, for example, this method returns a 0.99 for
     * Danish and less than 0.01 for Norwegian, and still they have almost the same chance. It would be nice if
     * this could be improved in future versions.</p>
     *
     * @param text You probably want a {@link com.optimaize.langdetect.text.TextObject}.
     * @return Sorted from better to worse. May be empty.
     *         It's empty if the program failed to detect any language, or if the input text did not
     *         contain any usable text (just noise).
     */
    List<DetectedLanguage> getProbabilities(CharSequence text);

}
