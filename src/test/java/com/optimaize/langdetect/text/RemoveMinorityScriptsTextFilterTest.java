package com.optimaize.langdetect.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class RemoveMinorityScriptsTextFilterTest {

    @Test
    public void testWithCyrillicAndHani() throws Exception {
        RemoveMinorityScriptsTextFilter filter = RemoveMinorityScriptsTextFilter.forThreshold(0.35);
        String result = filter.filter("Hu Jintao (in Chinese 胡錦濤) and Leo Tolstoy (in Russian Лев Николаевич Толстой) are two well known people.");
        assertEquals("Hu Jintao (in Chinese ) and Leo Tolstoy (in Russian   ) are two well known people.", result);
    }

    @Test
    public void testJustLatin() throws Exception {
        RemoveMinorityScriptsTextFilter filter = RemoveMinorityScriptsTextFilter.forThreshold(0.01);
        String text = "Hu Jintao is a well known person.";
        String result = filter.filter(text);
        assertEquals(text, result);
    }
}
