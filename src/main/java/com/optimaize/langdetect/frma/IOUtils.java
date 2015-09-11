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

package com.optimaize.langdetect.frma;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utils to manage IO streams.
 * @author François ROLAND
 */
@Deprecated
public class IOUtils {
	/**
	 * Private constructor to prevent instantiation.
	 */
	private IOUtils() {}

	/**
	 * Closes a stream without returning any exception.
	 * 
	 * @param stream the stream to close. Can be <code>null</code>.
     * @deprecated use java7 closeable
	 */
	public static void closeQuietly(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException ioe) {
				// ignore exception at this point.
			}
		}
	}
}
