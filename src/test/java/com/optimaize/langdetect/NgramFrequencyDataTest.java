package com.optimaize.langdetect;

import com.google.common.collect.ImmutableSet;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Some rudimentary tests for NgramFrequencyData.
 *
 * @author Fabian Kessler
 */
public class NgramFrequencyDataTest {

    private static NgramFrequencyData allThreeGrams;

    @BeforeClass
    public static void init() throws IOException {
        allThreeGrams = forAll(3);
    }
    private static NgramFrequencyData forAll(int gramSize) throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        return NgramFrequencyData.create(languageProfiles, ImmutableSet.of(gramSize));
    }


    @Test
    public void size() throws Exception {
        //update the number when adding built-in languages
        assertEquals(allThreeGrams.getLanguageList().size(), 71);
    }

    @Test
    public void constantOrder() throws Exception {
        //expect constant order:
        int pos=0;
        for (LdLocale locale : allThreeGrams.getLanguageList()) {
            assertEquals(allThreeGrams.getLanguage(pos), locale);
            pos++;
        }
    }

    @Test
    public void expectGram() throws Exception {
        //this must exist in many languages
        double[] probabilities = allThreeGrams.getProbabilities("dam");
        assert probabilities != null;
        assertTrue(probabilities.length >= 5 && probabilities.length <= allThreeGrams.getLanguageList().size());
    }

    @Test
    public void forbidGramOfWrongSize() throws Exception {
        //we said 3-grams, not 2 grams
        assertEquals(allThreeGrams.getProbabilities("da"), null);
    }

}