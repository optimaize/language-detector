package com.optimaize.langdetect;

import com.cybozu.labs.langdetect.ErrorCode;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.util.Util;
import com.google.common.base.Optional;
import com.optimaize.langdetect.ngram.NgramExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *
 *
 * <p>This class is immutable and thus thread-safe.</p>
 *
 * @author Fabian Kessler
 * @author Nakatani Shuyo
 */
public final class LanguageDetectorImpl implements LanguageDetector {

    private static final double ALPHA_WIDTH = 0.05;

    private static final int ITERATION_LIMIT = 1000;
    private static final double PROB_THRESHOLD = 0.1;
    private static final double CONV_THRESHOLD = 0.99999;
    private static final int BASE_FREQ = 10000;

    private static final int N_TRIAL = 7;


    /**
     * Key=ngram
     * Value = array with probabilities per loaded language, in the same order as {@code langlist}.
     */
    @NotNull
    private final Map<String, double[]> wordLangProbMap;
    /**
     * Used for computation when {@code wordLangProbMap} does not have an entry.
     */
    private final double[] dummyWordLangProbMap;
    /**
     * All the loaded languages, order is important.
     */
    @NotNull
    private final List<String> langlist;

    private final boolean verbose;
    private final double alpha;
    private final boolean skipUnknownNgrams;
    private final int shortTextAlgorithm;

    /**
     * User-defined language priorities, in the same order as {@code langlist}.
     */
    @Nullable
    private final double[] priorMap;



    /**
     * Use the {@link LanguageDetectorBuilder}.
     */
    LanguageDetectorImpl(@NotNull Map<String, double[]> wordLangProbMap,
                         @NotNull List<String> langlist,
                         boolean verbose, double alpha, boolean skipUnknownNgrams, int shortTextAlgorithm,
                         @Nullable Map<String, Double> langWeightingMap) throws LangDetectException {
        if (alpha<0d || alpha >1d) throw new IllegalArgumentException(""+alpha);
        if (langWeightingMap!=null && langWeightingMap.isEmpty()) langWeightingMap = null;
        if (langlist.isEmpty()) throw new IllegalArgumentException();

        //not making a copy because these 2 come fresh from the builder.
        this.wordLangProbMap = wordLangProbMap;
        this.dummyWordLangProbMap = new double[langlist.size()];
        this.langlist = langlist;

        this.verbose = verbose;
        this.alpha = alpha;
        this.skipUnknownNgrams = skipUnknownNgrams;
        this.shortTextAlgorithm = shortTextAlgorithm;
        this.priorMap = (langWeightingMap==null) ? null : Util.makeInternalPrioMap(langWeightingMap, langlist);
    }


    @Override
    public Optional<String> detect(CharSequence text) throws LangDetectException {
        List<DetectedLanguage> probabilities = getProbabilities(text);
        if (probabilities.isEmpty()) {
            return Optional.absent();
        } else {
            DetectedLanguage best = probabilities.get(0);
            if (best.getProbability() >= 0.9999d) {
                return Optional.of(best.getLanguage());
            } else {
                return Optional.absent();
            }
        }
    }

    @Override
    public List<DetectedLanguage> getProbabilities(CharSequence text) throws LangDetectException {
        double[] langprob = detectBlock(text);
        return sortProbability(langprob);
    }


    private double[] detectBlock(CharSequence text) throws LangDetectException {
        List<String> ngrams = extractNGrams(text, skipUnknownNgrams);
        if (ngrams.size()==0) {
            throw new LangDetectException(ErrorCode.CantDetectError, "no features in text");
        } else if (text.length() <= shortTextAlgorithm) {
            return detectBlockShortText(ngrams);
        } else {
            return detectBlockLongText(ngrams);
        }
    }

    /**
     */
    private double[] detectBlockShortText(List<String> ngrams) {
        double[] langprob = new double[langlist.size()];

        double[] prob = initProbability();
        double alpha = this.alpha; //TODO i don't understand what this does. my change might break stuff.

        for (String ngram : ngrams) {
            updateLangProb(prob, ngram, alpha);
        }
        Util.normalizeProb(prob);
        for (int j=0; j<langprob.length; ++j) {
            langprob[j] += prob[j];
        }
        if (verbose) System.out.println("==> " + sortProbability(prob));

        return langprob;
    }

    /**
     * This is the original algorithm used for all text length.
     * It is inappropriate for short text.
     */
    private double[] detectBlockLongText(List<String> ngrams) {
        double[] langprob = new double[langlist.size()];
        Random rand = new Random();
        for (int t = 0; t < N_TRIAL; ++t) {
            double[] prob = initProbability();
            double alpha = this.alpha + (rand.nextGaussian() * ALPHA_WIDTH);

            for (int i=0; i<ITERATION_LIMIT; i++) {
                int r = rand.nextInt(ngrams.size());
                updateLangProb(prob, ngrams.get(r), alpha);
                if (i % 5 == 0) {
                    if (Util.normalizeProb(prob) > CONV_THRESHOLD) break; //this looks like an optimization to return quickly when sure. TODO document what's the plan.
                    if (verbose) System.out.println("> " + sortProbability(prob));
                }
            }
            for(int j=0;j<langprob.length;++j) langprob[j] += prob[j] / N_TRIAL;
            if (verbose) System.out.println("==> " + sortProbability(prob));
        }
        return langprob;
    }

    /**
     * Initialize the map of language probabilities.
     * If there is the specified prior map, use it as initial map.
     * @return initialized map of language probabilities
     */
    private double[] initProbability() {
        double[] prob = new double[langlist.size()];
        if (priorMap != null) {
            //TODO analyze and optimize this code, looks like double copy.
            System.arraycopy(priorMap, 0, prob, 0, prob.length);
            for(int i=0;i<prob.length;++i) prob[i] = priorMap[i];
        } else {
            for(int i=0;i<prob.length;++i) prob[i] = 1.0 / langlist.size();
        }
        return prob;
    }

    /**
     * Extract n-grams from target text, except some!
     * @return n-grams list
     */
    private List<String> extractNGrams(CharSequence text, final boolean skipUnknownNgrams) {
        NgramExtractor.Filter filter = null;
        if (skipUnknownNgrams) {
            filter = new NgramExtractor.Filter() {
                @Override
                public boolean use(String gram) {
                    //the n-gram is skipped if the LanguageProfiles don't know about it.
                    //that may be problematic, because it does not downgrade the confidence.
                    return wordLangProbMap.containsKey(gram);
                }
            };
        }
        return NgramExtractor.extractNGrams(text, filter);
    }

    /**
     * update language probabilities with N-gram string(N=1,2,3)
     */
    private boolean updateLangProb(@NotNull double[] prob, @NotNull String ngram, double alpha) {
        double[] langProbMap = wordLangProbMap.get(ngram);
        if (langProbMap==null) {
            if (skipUnknownNgrams) {
                //this is not nice, an unknown n-gram must lower confidence and total points.
                //it is how the original program worked.
                return false;
            }
            langProbMap = dummyWordLangProbMap;
        }

        if (verbose) System.out.println(ngram + "(" + Util.unicodeEncode(ngram) + "):" + Util.wordProbToString(langProbMap, langlist));

        double weight = alpha / BASE_FREQ;
        for (int i=0; i<prob.length; ++i) {
            prob[i] *= (weight + langProbMap[i]);
        }
        return true;
    }


    /**
     * @return language candidates order by probabilities descending
     */
    @NotNull
    private List<DetectedLanguage> sortProbability(double[] prob) {
        List<DetectedLanguage> list = new ArrayList<>();
        for (int j=0;j<prob.length;++j) {
            double p = prob[j];
            if (p > PROB_THRESHOLD) {
                for (int i = 0; i <= list.size(); ++i) {
                    if (i == list.size() || list.get(i).getProbability() < p) {
                        list.add(i, new DetectedLanguage(langlist.get(j), p));
                        break;
                    }
                }
            }
        }
        return list;
    }

}
