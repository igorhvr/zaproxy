/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Copyright 2010 psiinon@gmail.com
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

package org.zaproxy.zap.extension.pscan.scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Source;

import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.pscan.PassiveScanThread;
import org.zaproxy.zap.extension.pscan.PassiveScanner;
import org.zaproxy.zap.extension.pscan.PluginPassiveScanner;

public class RegexAutoTagScanner extends PluginPassiveScanner implements PassiveScanner {

    // protected static final int PATTERN_SCAN = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE;
    protected static final int PATTERN_SCAN = Pattern.CASE_INSENSITIVE;

	public enum TYPE {ALERT, TAG, TECH};
	
	private String name = null;
	private String requestUrlRegex = null;
	private String requestHeaderRegex = null;
	private String responseHeaderRegex = null;
	private String responseBodyRegex = null;

	private Pattern requestUrlPattern = null;
	private Pattern requestHeaderPattern = null;
	private Pattern responseHeaderPattern = null;
	private Pattern responseBodyPattern = null;

	private PassiveScanThread parent = null;
	
	public Pattern getRequestUrlPattern() {
		return requestUrlPattern;
	}

	public Pattern getRequestHeaderPattern() {
		return requestHeaderPattern;
	}

	public Pattern getResponseHeaderPattern() {
		return responseHeaderPattern;
	}

	public Pattern getResponseBodyPattern() {
		return responseBodyPattern;
	}
	private TYPE type = null;
	private String config = null;
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getConf() {
		return config;
	}

	public void setConf(String config) {
		this.config = config;
	}

	public RegexAutoTagScanner(String name, TYPE type, String config) {
		super();
		this.name = name;
		this.type = type;
		this.config = config;
	}

	public RegexAutoTagScanner(String name, TYPE type, String config,
			String requestUrlregex, String requestHeaderRegex,
			String responseHeaderRegex, String responseBodyRegex,
			boolean enabled) {
		super();
		this.name = name;
		this.setRequestUrlRegex(requestUrlregex);
		this.setRequestHeaderRegex(requestHeaderRegex);
		this.setResponseHeaderRegex(responseHeaderRegex);
		this.setResponseBodyRegex(responseBodyRegex);
		this.type = type;
		this.config = config;
	}

	public String getRequestUrlRegex() {
		return requestUrlRegex;
	}
	public void setRequestUrlRegex(String requestUrlregex) {
		this.requestUrlRegex = requestUrlregex;
		if (requestUrlregex == null || requestUrlregex.length() == 0) {
			this.requestUrlPattern = null;
		} else {
			this.requestUrlPattern = Pattern.compile(requestUrlregex, PATTERN_SCAN);
		}
	}
	public String getRequestHeaderRegex() {
		return requestHeaderRegex;
	}
	public void setRequestHeaderRegex(String requestHeaderRegex) {
		this.requestHeaderRegex = requestHeaderRegex;
		if (requestHeaderRegex == null || requestHeaderRegex.length() == 0) {
			this.requestHeaderPattern = null;
		} else {
			this.requestHeaderPattern = Pattern.compile(requestHeaderRegex, PATTERN_SCAN);
		}
	}
	public String getResponseHeaderRegex() {
		return responseHeaderRegex;
	}
	public void setResponseHeaderRegex(String responseHeaderRegex) {
		this.responseHeaderRegex = responseHeaderRegex;
		if (responseHeaderRegex == null || responseHeaderRegex.length() == 0) {
			this.responseHeaderPattern = null;
		} else {
			this.responseHeaderPattern = Pattern.compile(responseHeaderRegex, PATTERN_SCAN);
		}
	}
	public String getResponseBodyRegex() {
		return responseBodyRegex;
	}
	public void setResponseBodyRegex(String responseBodyRegex) {
		this.responseBodyRegex = responseBodyRegex;
		if (responseBodyRegex == null || responseBodyRegex.length() == 0) {
			this.responseBodyPattern = null;
		} else {
			this.responseBodyPattern = Pattern.compile(responseBodyRegex, PATTERN_SCAN);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void scanHttpRequestSend(HttpMessage msg, int id) {
		if (! this.isEnabled()) {
			return;
		}
		if (getRequestHeaderPattern() != null) {
			Matcher m = getRequestHeaderPattern().matcher(
					msg.getRequestHeader().toString());
			if (m.find()) {
				// Scanner matches, so do what it wants...
				parent.addTag(id, this.getConf());
				return;
			}
		}
		if (getRequestUrlPattern() != null) {
			Matcher m = getRequestUrlPattern().matcher(
					msg.getRequestHeader().getURI().toString());
			if (m.find()) {
				// Scanner matches, so do what it wants...
				parent.addTag(id, this.getConf());
				return;
			}
		}
	}

	public Alert getAlert(HttpMessage msg) {
		return null;
	}

	@Override
	public void scanHttpResponseReceive(HttpMessage msg, int id, Source source) {
		if (! this.isEnabled()) {
			return;
		}
		if (getResponseHeaderPattern() != null) {
			Matcher m = getResponseHeaderPattern().matcher(
					msg.getResponseHeader().toString());
			if (m.find()) {
				// Scanner matches, so do what it wants...
				parent.addTag(id, this.getConf());
				return;
			}
		}
		if (getResponseBodyPattern() != null) {
			Matcher m = getResponseBodyPattern().matcher(
					msg.getResponseBody().toString());
			if (m.find()) {
				// Scanner matches, so do what it wants...
				parent.addTag(id, this.getConf());
				return;
			}
		}
	}

	@Override
	public void setParent(PassiveScanThread parent) {
		this.parent = parent;
	}

}
