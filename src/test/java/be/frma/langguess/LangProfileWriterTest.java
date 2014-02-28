/*
 * Copyright 2011 Francois ROLAND
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

package be.frma.langguess;

import com.cybozu.labs.langdetect.util.LangProfile;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class LangProfileWriterTest {
	private static final File PROFILE_DIR = new File(new File(new File(new File("src"), "main"), "resources"), "languages");

	@Test
	public void writeEnProfile() throws IOException {
		checkProfileCopy("en");
	}

	@Test
	public void writeFrProfile() throws IOException {
		checkProfileCopy("fr");
	}

	@Test
	public void writeNlProfile() throws IOException {
		checkProfileCopy("nl");
	}

	protected void checkProfileCopy(String language) throws IOException {
		File originalFile = new File(PROFILE_DIR, language);
		final LangProfile originalProfile = new LangProfileReader().readProfile(originalFile);
		File newFile = File.createTempFile("profile-copy-", null);
		try (FileOutputStream output = new FileOutputStream(newFile)) {
			new LangProfileWriter().writeProfile(originalProfile, output);
			LangProfile newProfile = new LangProfileReader().readProfile(newFile);
			assertThat(newProfile.getFreq().size(), is(equalTo(originalProfile.getFreq().size())));
			assertThat(newProfile.getFreq(), is(equalTo(originalProfile.getFreq())));
			assertThat(newProfile.getNWords(), is(equalTo(originalProfile.getNWords())));
			assertThat(newProfile.getName(), is(equalTo(originalProfile.getName())));
		} finally {
            //noinspection ResultOfMethodCallIgnored
            newFile.delete();
		}
	}

}
