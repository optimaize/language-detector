package com.optimaize.langdetect.text;

import java.lang.Character.UnicodeScript;
import java.util.HashMap;
import java.util.HashSet;
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

    private static final int INHERITED = UnicodeScript.INHERITED.ordinal();
    private static final int COMMON = UnicodeScript.COMMON.ordinal();
    private static final int UNKNOWN = UnicodeScript.UNKNOWN.ordinal();

    private static final int[] SCRIPT_IDS = new int[65536];
    static {
        for (int c = 0; c < SCRIPT_IDS.length; c++) {
            SCRIPT_IDS[c] = Character.UnicodeScript.of(c).ordinal();
        }
    }
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
        Map<Integer, Long> counts = countByScript(text);
        if (counts.size()<=1) {
            //nothing to do
            return text.toString();
        } else {
            long most = findMost(counts);
            Set<Integer> toRemove = new HashSet<>();
            for (Map.Entry<Integer, Long> entry : counts.entrySet()) {
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

    private String remove(CharSequence text, Set<Integer> toRemove) {
        StringBuilder remaining = new StringBuilder();
        int last = -1;
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            int id = SCRIPT_IDS[c];
            if (id == INHERITED) {
                if (toRemove.contains(last)) {
                    //remove, don't update 'last'
                    continue;
                }
            }
            last = id;
            if (toRemove.contains(id)) {
                continue; //remove it
            }
            //if we get here then we keep it.
            remaining.append(c);
        }
        return remaining.toString();
    }

    private long findMost(Map<Integer, Long> counts) {
        long max = 0L;
        for (Long aLong : counts.values()) {
            if (aLong > max) max = aLong;
        }
        return max;
    }

    private Map<Integer, Long> countByScript(CharSequence text) {
        int last = -1;
        long[] counter = new long[UnicodeScript.values().length];
        for (int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            int id = SCRIPT_IDS[c];
            if (id == INHERITED) {
                //counts as what the last was.
                if (last >= 0) { //really shouldn't be null
                    counter[last]++;
                }
            } else if(id != COMMON && id != UNKNOWN) {
                counter[id]++;
                last = id;
            }
        }

        Map<Integer, Long> result = new HashMap<>();
        for (int i = 0; i < counter.length; i++) {
            long value = counter[i];
            if (value > 0) {
                result.put(i, value);
            }
        }

        return result;
    }
}
