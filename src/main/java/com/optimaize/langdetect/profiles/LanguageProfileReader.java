package com.optimaize.langdetect.profiles;

import be.frma.langguess.LangProfileReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads {@link LanguageProfile}s.
 *
 * @author Fabian Kessler
 */
public class LanguageProfileReader {

    private static final LangProfileReader internalReader = new LangProfileReader();


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

}
