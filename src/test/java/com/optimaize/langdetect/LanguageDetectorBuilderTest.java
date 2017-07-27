package com.optimaize.langdetect;

import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017-07-27.
 */
public class LanguageDetectorBuilderTest {
    private static List<LanguageProfile> languageProfiles;

    @BeforeClass
    public static void setUp() throws Exception {
        languageProfiles = new LanguageProfileReader().readAllBuiltIn();
    }

    @Test
    public void languagePrioritiesEmptyShouldNotThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
    @Test
    public void languagePrioritiesShouldNotThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        priorityMap.put(LdLocale.fromString("en"),1.0);
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
    @Test(expected = IllegalArgumentException.class)
    public void languagePrioritiesNegativeShouldThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        priorityMap.put(LdLocale.fromString("en"),-1.0);
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
    @Test(expected = IllegalArgumentException.class)
    public void languagePrioritiesOnlyUnknownLocalesShouldThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        priorityMap.put(LdLocale.fromString("xx"),1.0);
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
    @Test(expected = IllegalArgumentException.class)
    public void languagePrioritiesAllZerosShouldThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        priorityMap.put(LdLocale.fromString("en"),0.0);
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
    @Test(expected = IllegalArgumentException.class)
    public void languagePrioritiesNANShouldThrow()  {
        Map<LdLocale, Double> priorityMap = new HashMap<LdLocale, Double>();
        priorityMap.put(LdLocale.fromString("en"),Double.NaN);
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .languagePriorities(priorityMap)
                .build();
    }
}