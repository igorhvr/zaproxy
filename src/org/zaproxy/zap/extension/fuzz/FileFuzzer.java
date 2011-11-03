/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2011 The Zed Attack Proxy dev team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.extension.fuzz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileFuzzer {

	private long length = -1;
	private List<String> fuzzStrs = new ArrayList<String>();
	
	private static Log log = LogFactory.getLog(FuzzDialog.class);

	protected FileFuzzer(File file) {
		try {
			length = 0;
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			String line;

			while ((line = in.readLine()) != null) {
				if (line != null && line.trim().length() > 0 && ! line.startsWith("#")) {
					fuzzStrs.add(line);	// TODO trim??
					length++;
				}
			}

			in.close();
			
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public Iterator<String> getIterator() {
		return fuzzStrs.iterator();
	}
	
	public long getLength() {
		return length;
	}
	
}
