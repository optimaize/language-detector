package com.optimaize.langdetect.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Fabian Kessler
 */
public class TextObjectTest {

    @Test
    public void simpleText() throws Exception {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder().withTextFilter(UrlTextFilter.getInstance()).build();
        TextObject inputText = textObjectFactory.create().append("Dies ist").append(" ").append("deutscher Text.");
        assertEquals(inputText.toString(), "Dies ist deutscher Text ");
    }

    @Test
    public void filteredContent() throws Exception {
        TextObjectFactory textObjectFactory = new TextObjectFactoryBuilder().withTextFilter(UrlTextFilter.getInstance()).build();
        TextObject inputText = textObjectFactory.create().append("deutscher Text").append(" ").append("http://www.github.com/");
        assertEquals(inputText.toString(), "deutscher Text ");
    }
}
