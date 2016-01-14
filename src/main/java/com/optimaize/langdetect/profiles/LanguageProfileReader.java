package com.optimaize.langdetect.profiles;

import com.optimaize.langdetect.frma.LangProfileReader;
import com.optimaize.langdetect.i18n.LdLocale;
import org.jetbrains.annotations.NotNull;

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
     * @param profileFileNames for example ["en", "fr", "de"].
     */
    public List<LanguageProfile> read(ClassLoader classLoader, String profileDirectory, Collection<String> profileFileNames) throws IOException {
        List<LanguageProfile> loaded = new ArrayList<>(profileFileNames.size());
        for (String profileFileName : profileFileNames) {
            String path = makePathForClassLoader(profileDirectory, profileFileName);
            try (InputStream in = classLoader.getResourceAsStream(path)) {
                if (in == null) {
                    throw new IOException("No language file available named "+profileFileName+" at " + path + "!");
                }
                loaded.add( read(in) );
            }
        }
        return loaded;
    }

    private String makePathForClassLoader(String profileDirectory, String fileName) {
        //WITHOUT slash before the profileDirectory when using the classloader!
        return profileDirectory + '/' + fileName;
    }

    /**
     * Same as {@link #read(ClassLoader, String, java.util.Collection)} using the class loader of this class.
     */
    public List<LanguageProfile> read(String profileDirectory, Collection<String> profileFileNames) throws IOException {
        return read(LanguageProfileReader.class.getClassLoader(), profileDirectory, profileFileNames);
    }

    /**
     * Same as {@link #read(ClassLoader, String, java.util.Collection)} using the class loader of this class,
     * and the default profiles directory of this library.
     */
    public List<LanguageProfile> read(Collection<String> profileFileNames) throws IOException {
        return read(LanguageProfileReader.class.getClassLoader(), PROFILES_DIR, profileFileNames);
    }

    @NotNull
    public LanguageProfile readBuiltIn(@NotNull LdLocale locale) throws IOException {
        String filename = makeProfileFileName(locale);
        String path = makePathForClassLoader(PROFILES_DIR, filename);
        try (InputStream in = LanguageProfileReader.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("No language file available named "+filename+" at " + path + "!");
            }
            return read(in);
        }
    }

    @NotNull
    private String makeProfileFileName(@NotNull LdLocale locale) {
        return locale.toString();
    }

    @NotNull
    public List<LanguageProfile> readBuiltIn(@NotNull Collection<LdLocale> languages) throws IOException {
        List<String> profileNames = new ArrayList<>(languages.size());
        for (LdLocale locale : languages) {
            profileNames.add(makeProfileFileName(locale));
        }
        return read(LanguageProfileReader.class.getClassLoader(), PROFILES_DIR, profileNames);
    }

    /**
     * @deprecated renamed to readAllBuiltIn()
     */
    public List<LanguageProfile> readAll() throws IOException {
        return readAllBuiltIn();
    }

    /**
     * Reads all built-in language profiles from the "languages" folder (shipped with the jar).
     */
    public List<LanguageProfile> readAllBuiltIn() throws IOException {
        final List<LdLocale> languages = BuiltInLanguages.getLanguages();
        List<LanguageProfile> loaded = new ArrayList<>(languages.size());
        for (LdLocale locale : languages) {
            loaded.add(readBuiltIn(locale));
        }
        return loaded;
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
            if (looksLikeLanguageProfileFile(file)) {
                profiles.add(read(file));
            }
        }
        return profiles;
    }

    private boolean looksLikeLanguageProfileFile(File file) {
        return file.isFile() ? looksLikeLanguageProfileName(file.getName()) : false;
    }

    private boolean looksLikeLanguageProfileName(String fileName) {
        if (fileName.contains(".")) {
            return false;
        }
        try {
            LdLocale.fromString(fileName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
