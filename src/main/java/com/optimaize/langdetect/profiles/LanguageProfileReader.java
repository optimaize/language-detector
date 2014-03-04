package com.optimaize.langdetect.profiles;

import be.frma.langguess.LangProfileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reads {@link LanguageProfile}s.
 *
 * @author Fabian Kessler
 */
public class LanguageProfileReader {

    private static final LangProfileReader internalReader = new LangProfileReader();
    private static final String PROFILES_DIR = "languages";


    /**
     * Reads a {@link LanguageProfile} from a File in UTF-8.
     */
    public LanguageProfile read(File profileFile) throws IOException {
        return OldLangProfileConverter.convert(internalReader.read(profileFile));
    }

    /**
     * Reads a {@link LanguageProfile} from an InputStream in UTF-8.
     */
    public LanguageProfile read(InputStream inputStream) throws IOException {
        return OldLangProfileConverter.convert(internalReader.read(inputStream));
    }


    /**
     * Load profiles from the classpath in a specific directory.
     *
     * <p>This is usually used to load built-in profiles, shipped with the jar.</p>
     *
     * @param classLoader the ClassLoader to load the profiles from. Use {@code MyClass.class.getClassLoader()}
     * @param profileDirectory profile directory path inside the classpath. The default profiles are in "languages".
     * @param languages for example ["en", "fr", "de"].
     */
    public List<LanguageProfile> read(ClassLoader classLoader, String profileDirectory, Collection<String> languages) throws IOException {
        List<LanguageProfile> loaded = new ArrayList<>(languages.size());
        for (String language : languages) {
            String fullpath = profileDirectory + '/' + language; //WITHOUT slash before the profileDirectory when using the classloader!
            try (InputStream in = classLoader.getResourceAsStream(fullpath)) {
                if (in == null) {
                    throw new IOException("No language file available for language "+language+"!");
                }
                loaded.add( read(in) );
            }
        }
        return loaded;
    }

    /**
     * Same as {@link #read(ClassLoader, String, java.util.Collection)} using the class loader of this class.
     */
    public List<LanguageProfile> read(String profileDirectory, Collection<String> languages) throws IOException {
        return read(LanguageProfileReader.class.getClassLoader(), profileDirectory, languages);
    }

    /**
     * Same as {@link #read(ClassLoader, String, java.util.Collection)} using the class loader of this class,
     * and the default profiles directory of this library.
     */
    public List<LanguageProfile> read(Collection<String> languages) throws IOException {
        return read(LanguageProfileReader.class.getClassLoader(), PROFILES_DIR, languages);
    }

    /**
     * Reads all built-in language profiles from the "languages" folder (shipped with the jar).
     */
    public List<LanguageProfile> readAll() throws IOException {
        List<String> strings = BuiltInLanguages.getLanguages();
        return read(strings);
    }

    /**
     * Loads all profiles from the specified directory.
     *
     * Do not use this method for files distributed within a jar.
     *
     * @param path profile directory path
     * @return empty if there is no language file in it.
     */
    public List<LanguageProfile> readAll(File path) throws IOException {
        if (!path.exists()) {
            throw new IOException("No such folder: "+path);
        }
        if (!path.canRead()) {
            throw new IOException("Folder not readable: "+path);
        }
        File[] listFiles = path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return looksLikeLanguageProfileFile(pathname);
            }
        });
        if (listFiles == null) {
            throw new IOException("Failed reading from folder: " + path);
        }

        List<LanguageProfile> profiles = new ArrayList<>(listFiles.length);
        for (File file: listFiles) {
            if (!looksLikeLanguageProfileFile(file)) {
                continue;
            }
            profiles.add(read(file));
        }
        return profiles;
    }

    private boolean looksLikeLanguageProfileFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        return looksLikeLanguageProfileName(file.getName());
    }
    private boolean looksLikeLanguageProfileName(String fileName) {
        if (fileName.startsWith(".")) {
            return false;
        }
        if (!fileName.matches("^[a-z]{2,3}$") && !fileName.matches("^[a-z]{2}\\-[a-z]{2}$")) {
            return false;
        }
        return true;
    }

}
