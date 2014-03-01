package com.optimaize.langdetect.profiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Writes a {@link LanguageProfile} to an output stream or file.
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
    public void write(LanguageProfile languageProfile, OutputStream outputStream) throws IOException {
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
            writer.write(languageProfile.getLanguage());
            writer.write("\"}");
            writer.flush();
        }
    }

}
