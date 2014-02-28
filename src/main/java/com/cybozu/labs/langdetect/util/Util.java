package com.cybozu.labs.langdetect.util;

import com.cybozu.labs.langdetect.ErrorCode;
import com.cybozu.labs.langdetect.LangDetectException;
import com.optimaize.langdetect.ngram.NgramExtractor;
import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * A place for sharing code.
 *
 * @author Fabian Kessler
 */
public class Util {

    public static void addCharSequence(LangProfile langProfile, CharSequence charSequence) {
        for (String s : NgramExtractor.extractNGrams(charSequence, null)) {
            langProfile.add(s);
        }
    }



    /**
     * unicode encoding (for verbose mode)
     */
    public static String unicodeEncode(String s) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch >= '\u0080') {
                String st = Integer.toHexString(0x10000 + (int) ch);
                while (st.length() < 4) st = "0" + st;
                buf.append("\\u").append(st.subSequence(1, 5));
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }


    /**
     * normalize probabilities and check convergence by the maximum probability
     * @return maximum of probabilities
     */
    public static double normalizeProb(double[] prob) {
        double maxp = 0, sump = 0;
        for(int i=0;i<prob.length;++i) sump += prob[i];
        for(int i=0;i<prob.length;++i) {
            double p = prob[i] / sump;
            if (maxp < p) maxp = p;
            prob[i] = p;
        }
        return maxp;
    }


    public static String wordProbToString(double[] prob, List<String> langlist) {
        Formatter formatter = new Formatter();
        for(int j=0;j<prob.length;++j) {
            double p = prob[j];
            if (p>=0.00001) {
                formatter.format(" %s:%.5f", langlist.get(j), p);
            }
        }
        return formatter.toString();
    }


    /**
     * @throws com.cybozu.labs.langdetect.LangDetectException
     */
    public static double[] makeInternalPrioMap(@NotNull Map<String, Double> langWeightingMap,
                                                @NotNull List<String> langlist) throws LangDetectException {
        assert !langWeightingMap.isEmpty();
        double[] priorMap = new double[langlist.size()];
        double sump = 0;
        for (int i=0;i<priorMap.length;++i) {
            String lang = langlist.get(i);
            if (langWeightingMap.containsKey(lang)) {
                double p = langWeightingMap.get(lang);
                if (p<0) throw new LangDetectException(ErrorCode.InitParamError, "Prior probability must be non-negative.");
                priorMap[i] = p;
                sump += p;
            }
        }
        if (sump<=0) throw new LangDetectException(ErrorCode.InitParamError, "More one of prior probability must be non-zero.");
        for (int i=0;i<priorMap.length;++i) priorMap[i] /= sump;
        return priorMap;
    }

}
