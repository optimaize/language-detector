package com.optimaize.langdetect;

import com.cybozu.labs.langdetect.LangDetectException;
import com.google.common.base.Optional;

import java.util.List;

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
     * @param text You probably want a {@link com.optimaize.langdetect.text.TextObject}.
     * @return The language if confident, absent if not.
     */
    Optional<String> detect(CharSequence text) throws LangDetectException;

    /**
     * There may be some PROB_THRESHOLD applied to cut unlikely results.
     *
     * @param text You probably want a {@link com.optimaize.langdetect.text.TextObject}.
     * @return Sorted from better to worse. May be empty.
     */
    List<DetectedLanguage> getProbabilities(CharSequence text) throws LangDetectException;

}
