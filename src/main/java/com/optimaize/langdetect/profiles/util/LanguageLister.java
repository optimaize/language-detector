package com.optimaize.langdetect.profiles.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This is just a utility to update the code with the existing languages.
 *
 * @author Nicole Torres
 */
class LanguageLister {

    public static void main(String[] args) throws IOException {
        List<String> languages = readFilesFromClassPathFolder("languages/.");
        for (String lang : languages) {
            System.out.println("names.add(\""+lang+"\");");
        }
        System.out.println("--------------------------------");
        List<String> shortText = readFilesFromClassPathFolder("languages.shorttext/.");
        for (String text : shortText) {
            System.out.println("texts.add(\""+text+"\");");
        }
    }

    private static List<String> readFilesFromClassPathFolder(String resourceNameFolder) throws IOException {
        List<String> files = new ArrayList<>();
        ClassLoader loader = LanguageLister.class.getClassLoader();
        try (InputStream in = loader.getResourceAsStream(resourceNameFolder)) {
            BufferedReader rdr = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = rdr.readLine()) != null) {
                files.add(line);
            }
            rdr.close();
        }
        return files;
    }

}
