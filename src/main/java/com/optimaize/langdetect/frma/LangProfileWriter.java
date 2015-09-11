/*
 * Copyright 2011 Francois ROLAND
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

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Writes a {@link LangProfile} to an output stream (file).
 *
 * @author François ROLAND
 * @author Fabian Kessler
 */
public class LangProfileWriter {

    /**
     * Writes a {@link LangProfile} to an OutputStream in UTF-8.
     *
     * @throws IOException
     */
	public void write(LangProfile langProfile, OutputStream outputStream) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("utf-8")))) {
            writer.write("{\"freq\":{");
            boolean first = true;
            for (Map.Entry<String, Integer> entry : langProfile.getFreq().entrySet()) {
                if (!first) {
                    writer.write(',');
                }
                writer.write('"');
                writer.write(entry.getKey());
                writer.write("\":");
                writer.write(entry.getValue().toString());
                first = false;
            }
            writer.write("},\"n_words\":[");
            first = true;
            for (int nWord : langProfile.getNWords()) {
                if (!first) {
                    writer.write(',');
                }
                writer.write(Integer.toString(nWord));
                first = false;
            }
            writer.write("],\"name\":\"");
            writer.write(langProfile.getName());
            writer.write("\"}");
            writer.flush();
        }
	}
}
