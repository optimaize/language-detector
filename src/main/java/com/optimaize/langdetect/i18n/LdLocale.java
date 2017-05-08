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

package com.optimaize.langdetect.i18n;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A language-detector implementation of a Locale, similar to the java.util.Locale.
 *
 * <p>It represents a IETF BCP 47 tag, but does not implement all the features. Features can be added as needed.</p>
 *
 * <p>It is constructed through the {@link #fromString} factory method. The {@link #toString()} method
 * produces a parseable and persistable string.</p>
 *
 * <p>The class is immutable.</p>
 *
 * <p>The java.util.Locale cannot be used because it has issues for historical reasons, notably the
 * script code conversion for Hebrew, Yiddish and Indonesian, and more. If one needs a Locale,
 * it is simple to create one based on this object.<br/>
 * The ICU ULocale cannot be used because a) it has issues too (for our use case) and b) we're not
 * using ICU in here [yet].</p>
 *
 * <p>This class does not perform any modifications on the input. The input is used as is, and the getters
 * return it in exactly the same way. No standardization, canonicalization, cleaning.</p>
 *
 * <p>The input is validated syntactically, but not for code existence. For example the script code must
 * be a valid ISO 15924 like "Latn" or "Cyrl", in correct case. But whether the code exists or not is not checked.
 * These code standards are not fixed, simply because regional entities like Countries can change for political
 * reasons, and languages are living entities. Therefore certain codes may exist at some point in time only
 * (be introduced late, or be deprecated or removed, or even be re-assigned another meaning).
 * It is not up to us to decide whether Kosovo is a country in 2015 or not.
 * If one needs to only work with a certain range of acceptable codes, he can validate the codes through other
 * classes that have knowledge about the codes.
 * </p>
 *
 * <p>Language: as for BCP 47, the iso 639-1 code must be used if there is one. For example "fr" for French.
 * If not, the ISO 639-3 should be used. It is highly discouraged to use 639-2.
 * Right now this class enforces a 2 or 3 char code, but this may be relaxed in the future.</p>
 *
 * <p>Script: Only ISO 15924, no discussion.</p>
 *
 * <p>Region: same as for BCP 47. That means ISO 3166-1 alpha-2 and "UN M.49".
 * I can imagine relaxing it in the future to also allow 3166-2 codes.
 * In most cases the "region" is a "country".</p>
 *
 * @author fabian kessler
 */
public final class LdLocale {

    @NotNull
    private final String language;
    @NotNull
    private final Optional<String> script;
    @NotNull
    private final Optional<String> region;

    private LdLocale(@NotNull String language, @NotNull Optional<String> script, @NotNull Optional<String> region) {
        this.language = language;
        this.script = script;
        this.region = region;
    }

    /**
     * @param string The output of the toString() method.
     * @return either a new or possibly a cached (immutable) instance.
     */
    @NotNull
    public static LdLocale fromString(@NotNull String string) {
        if (string==null || string.isEmpty()) throw new IllegalArgumentException("At least a language is required!");

        String language = null;
        Optional<String> script = null;
        Optional<String> region = null;

        List<String> strings = splitToList('-', string);
        for (int i=0; i<strings.size(); i++) {
            String chunk = strings.get(i);
            if (i==0) {
                language = assignLang(chunk);
            } else {
                if (script == null && region == null && looksLikeScriptCode(chunk)) {
                    script = Optional.of(chunk);
                } else if (region==null && (looksLikeGeoCode3166_1(chunk) || looksLikeGeoCodeNumeric(chunk))) {
                    region = Optional.of(chunk);
                } else {
                    throw new IllegalArgumentException("Unknown part: >>>"+chunk+"<<<!");
                }
            }
        }
        assert language != null;
        if (script==null) script = Optional.absent();
        if (region==null) region = Optional.absent();
        return new LdLocale(language, script, region);
    }

    private static List<String> splitToList(char separator, String string) {
        Preconditions.checkNotNull(string);
        Iterable<String> iterator = Splitter.on(separator).split(string);
        ArrayList<String> result = new ArrayList<String>();
        for (String s: iterator) {
            result.add(s);
        }
        return Collections.unmodifiableList(result);
    }

    private static boolean looksLikeScriptCode(String string) {
        return string.length() == 4 && string.matches("[A-Z][a-z]{3}");
    }

    private static boolean looksLikeGeoCode3166_1(String string) {
        return string.length()==2 && string.matches("[A-Z]{2}");
    }
    private static boolean looksLikeGeoCodeNumeric(String string) {
        return string.length()==3 && string.matches("[0-9]{3}");
    }

    private static String assignLang(String s) {
        if (!s.matches("[a-z]{2,3}")) throw new IllegalArgumentException("Invalid language code syntax: >>>"+s+"<<<!");
        return s;
    }

    /**
     * The output of this can be fed to the fromString() method.
     * @return for example "de" or "de-Latn" or "de-CH" or "de-Latn-CH", see class header.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(language);

        if (script.isPresent()) {
            sb.append('-');
            sb.append(script.get());
        }

        if (region.isPresent()) {
            sb.append('-');
            sb.append(region.get());
        }

        return sb.toString();
    }


    /**
     * @return ISO 639-1 or 639-3 language code, eg "fr" or "gsw", see class header.
     */
    @NotNull
    public String getLanguage() {
        return language;
    }

    /**
     * @return ISO 15924 script code, eg "Latn", see class header.
     */
    @NotNull
    public Optional<String> getScript() {
        return script;
    }

    /**
     * @return ISO 3166-1 or UN M.49 code, eg "DE" or 150, see class header.
     */
    @NotNull
    public Optional<String> getRegion() {
        return region;
    }



    @Override //generated-code
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LdLocale ldLocale = (LdLocale) o;

        if (!language.equals(ldLocale.language)) return false;
        if (!region.equals(ldLocale.region)) return false;
        if (!script.equals(ldLocale.script)) return false;

        return true;
    }

    @Override //generated-code
    public int hashCode() {
        int result = language.hashCode();
        result = 31 * result + script.hashCode();
        result = 31 * result + region.hashCode();
        return result;
    }
}
