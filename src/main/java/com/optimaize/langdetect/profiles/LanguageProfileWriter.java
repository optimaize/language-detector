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

package com.optimaize.langdetect.profiles;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Writes a {@link LanguageProfile} to an output stream or file.
 *
 * <p>All file operations are done with UTF-8.</p>
 *
 * @author François ROLAND
 * @author Fabian Kessler
 */
public class LanguageProfileWriter {

    /**
     * Writes a {@link LanguageProfile} to an OutputStream in UTF-8.
     *
     * @throws java.io.IOException
     */
    public void write(@NotNull LanguageProfile languageProfile, @NotNull OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("utf-8")))) {
            writer.write("{\"freq\":{");
            boolean first = true;
            for (Map.Entry<String, Integer> entry : languageProfile.iterateGrams()) {
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
            for (int i=1; i<=10; i++) {
                long nWord = languageProfile.getNumGramOccurrences(i);
                if (nWord ==0) break;
                if (!first) {
                    writer.write(',');
                }
                writer.write(Long.toString(nWord));
                first = false;
            }
            writer.write("],\"name\":\"");
            writer.write(languageProfile.getLocale().toString());
            writer.write("\"}");
            writer.flush();
        }
    }

    /**
     * Writes a {@link LanguageProfile} to a folder using the language name as the file name.
     *
     * @param fullPath Must be an existing writable directory path.
     * @throws java.io.IOException if such a file name exists already.
     */
    public void writeToDirectory(@NotNull LanguageProfile languageProfile, @NotNull File fullPath) throws IOException {
        if (!fullPath.exists()) {
            throw new IOException("Path does not exist: "+fullPath);
        }
        if (!fullPath.canWrite()) {
            throw new IOException("Path not writable: "+fullPath);
        }
        File file = new File(fullPath.getAbsolutePath()+"/"+languageProfile.getLocale());
        if (file.exists()) {
            throw new IOException("File exists already, refusing to overwrite: "+file);
        }
        try (FileOutputStream output = new FileOutputStream(file)) {
            write(languageProfile, output);
        }
    }

}
