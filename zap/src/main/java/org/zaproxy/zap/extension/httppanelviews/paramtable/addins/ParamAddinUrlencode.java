/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
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
package org.zaproxy.zap.extension.httppanelviews.paramtable.addins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ParamAddinUrlencode implements ParamAddinInterface {
	
	private static final String URL_ENCODE = "URLEncode";

	@Override
	public String convertData(String data) throws UnsupportedEncodingException {
		return URLEncoder.encode(data, "UTF-8");
	}

	@Override
	public String getName() {
		return URL_ENCODE;
	}

	@Override
	public String toString() {
		return URL_ENCODE;
	}

}
