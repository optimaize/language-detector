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

package com.optimaize.langdetect.frma;

import com.optimaize.langdetect.cybozu.util.LangProfile;
import com.optimaize.langdetect.cybozu.util.Util;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

/**
 * Generate a language profile from any given text file.
 *
 * TODO this is copy/paste from the other class with the same name. Check if code can be re-used. Rename to something meaningful.
 * 
 * @author François ROLAND
 */
public class GenProfile {

    private static final TextObjectFactory textObjectFactory = CommonTextObjectFactories.forIndexing();


    /**
     * Loads a text file and generate a language profile from its content. The input text file is supposed to be encoded in UTF-8.
     * @param lang target language name.
     * @param textFile input text file.
     * @return Language profile instance
     */
    public static LangProfile generate(String lang, File textFile) {
        LangProfile profile = new LangProfile(lang);

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(textFile));
            if (textFile.getName().endsWith(".gz")) is = new GZIPInputStream(is);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                TextObject textObject = textObjectFactory.forText(" "+line+" ");
                Util.addCharSequence(profile, textObject);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't open training database file '" + textFile.getName() + "'", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return profile;
    }
}
