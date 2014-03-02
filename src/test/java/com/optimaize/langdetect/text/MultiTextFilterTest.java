package com.optimaize.langdetect.text;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Fabian Kessler
 */
public class MultiTextFilterTest {

    @Test
    public void empty() throws Exception {
        assertEquals(new MultiTextFilter(Collections.<TextFilter>emptyList()).filter("foo"), "foo");
    }

    @Test
    public void doubleFilter() throws Exception {
        assertEquals(new MultiTextFilter(ImmutableList.of(
                new TextFilter() {
                    @Override
                    public String filter(CharSequence text) {
                        return text.toString().replace("a", "A");
                    }
                }, new TextFilter() {
                    @Override
                    public String filter(CharSequence text) {
                        return text.toString().replace("A", "B");
                    }
                }
        )).filter("nananaa"), "nBnBnBB");
    }
}
