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

package be.frma.langguess;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import com.cybozu.labs.langdetect.ErrorCode;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.util.LangProfile;
import com.cybozu.labs.langdetect.util.NGram;

/**
 * Generate a language profile from any given text file.
 * 
 * @author François ROLAND
 * 
 */
public class GenProfile {

    /**
     * Loads a text file and generate a language profile from its content. The input text file is supposed to be encoded in UTF-8.
     * @param lang target language name.
     * @param textFile input text file.
     * @return Language profile instance
     * @throws LangDetectException 
     */
    public static LangProfile generate(String lang, File textFile) throws LangDetectException {

        LangProfile profile = new LangProfile(lang);

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(textFile));
            if (textFile.getName().endsWith(".gz")) is = new GZIPInputStream(is);

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                NGram ngram = new NGram();
            	for (char c : line.toCharArray()) {
					ngram.addChar((char) c);
					for (int i = 1; i <= NGram.N_GRAM; i++) {
						profile.add(ngram.get(i));
					}
            	}
			}
        } catch (IOException e) {
            throw new LangDetectException(ErrorCode.CantOpenTrainData, "Can't open training database file '" + textFile.getName() + "'");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return profile;
    }
}
