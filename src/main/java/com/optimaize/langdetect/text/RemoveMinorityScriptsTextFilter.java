/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimaize.langdetect.text;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Removes text written in scripts that are not the dominant script of the text.
 *
 * TODO this does not do special handling for Japanese (3 scripts) and Korean (2 scripts), they should be
 * counted together and kept.
 *
 * @author Fabian Kessler
 */
public class RemoveMinorityScriptsTextFilter implements TextFilter {

    private final double threshold;

    /**
     * If a script has less than this fraction of content compared to the most used one, its text is removed.
     *
     * Example: Latin 10%, Cyrillic 80%, Common 10% (punctuation n'stuff). Now 10 is put in relation to 80.
     *
     * @param threshold 0-1, suggested value is 0.3. If smaller then removed, equal remains.
     */
    public static RemoveMinorityScriptsTextFilter forThreshold(double threshold) {
        return new RemoveMinorityScriptsTextFilter(threshold);
    }

    private RemoveMinorityScriptsTextFilter(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String filter(CharSequence text) {
        Map<Character.UnicodeScript, Long> counts = countByScript(text);
        if (counts.size()<=1) {
            //nothing to do
            return text.toString();
        } else {
            long most = findMost(counts);
            Set<Character.UnicodeScript> toRemove = EnumSet.noneOf(Character.UnicodeScript.class);
            for (Map.Entry<Character.UnicodeScript, Long> entry : counts.entrySet()) {
                if (entry.getValue()==most) continue;
                double ratio = entry.getValue().doubleValue() / most;
                if (ratio <= threshold) {
                    toRemove.add(entry.getKey());
                }
            }
            if (toRemove.isEmpty()) {
                return text.toString();
            } else {
                return remove(text, toRemove);
            }
        }
    }

    private String remove(CharSequence text, Set<Character.UnicodeScript> toRemove) {
        StringBuilder remaining = new StringBuilder();
        Character.UnicodeScript last = null;
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            Character.UnicodeScript unicodeScript = Character.UnicodeScript.of(c);
            if (unicodeScript == Character.UnicodeScript.INHERITED) {
                if (toRemove.contains(last)) {
                    //remove, don't update 'last'
                    continue;
                }
            }
            last = unicodeScript;
            if (toRemove.contains(unicodeScript)) {
                continue; //remove it
            }
            //if we get here then we keep it.
            remaining.append(c);
        }
        return remaining.toString();
    }

    private long findMost(Map<Character.UnicodeScript, Long> counts) {
        long max = 0L;
        for (Long aLong : counts.values()) {
            if (aLong > max) max = aLong;
        }
        return max;
    }

    private Map<Character.UnicodeScript, Long> countByScript(CharSequence text) {
        Map<Character.UnicodeScript, Long> counter = new EnumMap<>(Character.UnicodeScript.class);
        Character.UnicodeScript last = null;
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            Character.UnicodeScript unicodeScript = Character.UnicodeScript.of(c);
            switch (unicodeScript) {
                case INHERITED:
                    //counts as what the last was.
                    if (last!=null) { //really shouldn't be null
                        increment(counter, last);
                    }
                    break;
                case COMMON:
                case UNKNOWN:
                    //don't count it
                    break;
                default:
                    increment(counter, unicodeScript);
                    last = unicodeScript;
            }
        }
        return counter;
    }
    private void increment(Map<Character.UnicodeScript, Long> counter, Character.UnicodeScript unicodeScript) {
        Long number = counter.get(unicodeScript);
        if (number==null) {
            counter.put(unicodeScript, 1L);
        } else {
            counter.put(unicodeScript, number+1);
        }
    }

}
