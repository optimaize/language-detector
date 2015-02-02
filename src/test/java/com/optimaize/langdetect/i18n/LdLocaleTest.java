package com.optimaize.langdetect.i18n;

import org.junit.Test;

import static org.junit.Assert.*;

public class LdLocaleTest {

    @Test
    public void justLanguage() throws Exception {
        expectJustLanguage("en");
        expectJustLanguage("gsw");
    }
    private void expectJustLanguage(String lang) throws Exception {
        LdLocale locale = LdLocale.fromString(lang);
        assertEquals(locale.toString(), lang);
        assertEquals(locale.getLanguage(), lang);
        assertFalse(locale.getScript().isPresent());
        assertFalse(locale.getRegion().isPresent());
    }

    @Test
    public void languageAndScript() throws Exception {
        expectLanguageAndScript("en", "Latn");
        expectLanguageAndScript("gsw", "Latn");
        expectLanguageAndScript("zh", "Hans");
    }
    private void expectLanguageAndScript(String lang, String script) throws Exception {
        LdLocale locale = LdLocale.fromString(lang+'-'+script);
        assertEquals(locale.toString(), lang+'-'+script);
        assertEquals(locale.getLanguage(), lang);
        assertEquals(locale.getScript().get(), script);
        assertFalse(locale.getRegion().isPresent());
    }

    @Test
    public void languageAndRegion() throws Exception {
        expectLanguageAndRegion("en", "UK");
        expectLanguageAndRegion("zh", "CN");
    }
    private void expectLanguageAndRegion(String lang, String region) throws Exception {
        LdLocale locale = LdLocale.fromString(lang+'-'+region);
        assertEquals(locale.toString(), lang+'-'+region);
        assertEquals(locale.getLanguage(), lang);
        assertFalse(locale.getScript().isPresent());
        assertEquals(locale.getRegion().get(), region);
    }

    @Test
    public void all() throws Exception {
        expectAll("en", "Latn", "UK");
        expectAll("zh", "Hant", "CN");
    }
    private void expectAll(String lang, String script, String region) throws Exception {
        LdLocale locale = LdLocale.fromString(lang+'-'+script+'-'+region);
        assertEquals(locale.toString(), lang+'-'+script+'-'+region);
        assertEquals(locale.getLanguage(), lang);
        assertEquals(locale.getScript().get(), script);
        assertEquals(locale.getRegion().get(), region);
    }



    @Test
    public void equalsYes() throws Exception {
        expectEqualsYes("en");
        expectEqualsYes("en-Latn-UK");
    }
    private void expectEqualsYes(String s) throws Exception {
        LdLocale locale1 = LdLocale.fromString(s);
        LdLocale locale2 = LdLocale.fromString(locale1.toString());
        assertEquals(locale1, locale2);
    }




    @Test
    public void invalid() throws Exception {
        //language required
        expectInvalid("");
        expectInvalid(null);

        //invalid syntax
        expectInvalid("-");
        expectInvalid("--");
        expectInvalid("xx-");
        expectInvalid("-xx");
        expectInvalid("-xx-");
        expectInvalid("de--CH");
        expectInvalid("de--Latn");

        //invalid language: too short or too long
        expectInvalid("x");
        expectInvalid("xxxx");

        //wrong order
        expectInvalid("de-CH-Latn");

        //missing language
        expectInvalid("Latn");
        expectInvalid("CH");
        expectInvalid("CH-Latn");

        //incorrect case
        expectInvalid("JA");
        expectInvalid("ja-jp");
        expectInvalid("ja-jpan");
        expectInvalid("ja-JPAN");

        //incorrect separator
        expectInvalid("de_CH");
        expectInvalid("de CH");
    }
    public void expectInvalid(String s) throws Exception {
        try {
            LdLocale.fromString(s);
            fail("Expected failure for: "+s);
        } catch (IllegalArgumentException e) {
            //ok, expected that
        }
    }

}