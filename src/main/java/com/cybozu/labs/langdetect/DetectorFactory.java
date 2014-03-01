/*
 * Copyright 2011 Nakatani Shuyo
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

package com.cybozu.labs.langdetect;

import be.frma.langguess.LangProfileReader;
import com.cybozu.labs.langdetect.util.LangProfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Language Detector Factory Class
 * 
 * This class manages an initialization and constructions of {@link Detector}. 
 * 
 * Before using language detection library, 
 * load profiles with {@link DetectorFactory#loadProfile(String)} method
 * and set initialization parameters.
 * 
 * When the language detection,
 * construct Detector instance via {@link DetectorFactory#create()}.
 * See also {@link Detector}'s sample code.
 * 
 * <ul>
 * <li>4x faster improvement based on Elmer Garduno's code. Thanks!</li>
 * </ul>
 * 
 * @see Detector
 * @author Nakatani Shuyo
 * @author Francois ROLAND
 * @author Fabian Kessler
 */
@Deprecated
public class DetectorFactory {

    //TODO refactor, this is messy.

    public Map<String, double[]> wordLangProbMap;
    public List<String> langlist;
    public Long seed = null;

    private DetectorFactory() {
        wordLangProbMap = new HashMap<>();
        langlist = new ArrayList<>();
    }
    static private DetectorFactory instance_ = new DetectorFactory();

    /**
     * Load profiles from specified directory.
     * This method must be called once before language detection.
     *  
     * @param profileDirectory profile directory path
     * @throws LangDetectException  Can't open profiles(error code = {@link ErrorCode#FileLoadError})
     *                              or profile's format is wrong (error code = {@link ErrorCode#FormatError})
     */
    public static void loadProfile(String profileDirectory) throws LangDetectException {
        loadProfile(new File(profileDirectory));
    }

    /**
     * Load profiles from specified directory.
     * This method must be called once before language detection.
     *  
     * @param profileDirectory profile directory path
     * @throws LangDetectException  Can't open profiles(error code = {@link ErrorCode#FileLoadError})
     *                              or profile's format is wrong (error code = {@link ErrorCode#FormatError})
     */
    public static void loadProfile(File profileDirectory) throws LangDetectException {
        File[] listFiles = profileDirectory.listFiles();
        if (listFiles == null)
            throw new LangDetectException(ErrorCode.NeedLoadProfileError, "Not found profile: " + profileDirectory);

        LangProfileReader langProfileReader = new LangProfileReader();
        int langsize = listFiles.length, index = 0;
        for (File file: listFiles) {
            if (file.getName().startsWith(".") || !file.isFile()) { //TODO this is a bit limited... should look for valid file name patterns. also, langsize is not reduced.
                continue;
            }
            try {
                LangProfile profile = langProfileReader.read(file);
                addProfile(profile, index, langsize);
                ++index;
            } catch (IOException e) {
                throw new LangDetectException(ErrorCode.FileLoadError, "can't open '" + file.getName() + "'");
            }
        }
    }
    
    /**
     * Load profiles from the classpath in a specific directory.
     *
     * @param classLoader the ClassLoader to load the profiles from.
     * @param profileDirectory profile directory path inside the classpath.
     * @throws LangDetectException  Can't open profiles(error code = {@link ErrorCode#FileLoadError})
     *                              or profile's format is wrong (error code = {@link ErrorCode#FormatError})
     */
    public static void loadProfile(ClassLoader classLoader, String profileDirectory, String... languages) throws LangDetectException {
        LangProfileReader langProfileReader = new LangProfileReader();
        int index = 0;
		for (String language : languages) {
			String languageFileName = profileDirectory + '/' + language;
			try (InputStream in = classLoader.getResourceAsStream(languageFileName)) {
				if (in == null) {
					continue;
				}
				assert in.available() > 0;
                LangProfile profile = langProfileReader.read(in);
                addProfile(profile, index, languages.length);
                ++index;
            } catch (IOException e) {
                throw new LangDetectException(ErrorCode.FileLoadError, "can't open '" + languageFileName + "'");
			}
		}
    }

	/**
     * @param profile
     * @param langsize 
     * @param index 
     * @throws LangDetectException 
     */
    static /* package scope */ void addProfile(LangProfile profile, int index, int langsize) throws LangDetectException {
        String lang = profile.getName();
        if (instance_.langlist.contains(lang)) {
            throw new LangDetectException(ErrorCode.DuplicateLangError, "duplicate the same language profile");
        }
        instance_.langlist.add(lang);
        for (String word: profile.getFreq().keySet()) {
            if (!instance_.wordLangProbMap.containsKey(word)) {
                instance_.wordLangProbMap.put(word, new double[langsize]);
            }
            double prob = profile.getFreq().get(word).doubleValue() / profile.getNWords()[word.length()-1];
            instance_.wordLangProbMap.get(word)[index] = prob;
        }
    }

    /**
     * for only Unit Test
     */
    static /* package scope */ void clear() {
        instance_.langlist.clear();
        instance_.wordLangProbMap.clear();
    }

    /**
     * Construct Detector instance
     * 
     * @return Detector instance
     * @throws LangDetectException 
     */
    static public Detector create() throws LangDetectException {
        return createDetector();
    }

    /**
     * Construct Detector instance with smoothing parameter 
     * 
     * @param alpha smoothing parameter (default value = 0.5)
     * @return Detector instance
     * @throws LangDetectException 
     */
    public static Detector create(double alpha) throws LangDetectException {
        Detector detector = createDetector();
        detector.setAlpha(alpha);
        return detector;
    }

    static private Detector createDetector() throws LangDetectException {
        if (instance_.langlist.size()==0)
            throw new LangDetectException(ErrorCode.NeedLoadProfileError, "need to load profiles");
        return new Detector(instance_);
    }
    
    public static void setSeed(long seed) {
        instance_.seed = seed;
    }
}
