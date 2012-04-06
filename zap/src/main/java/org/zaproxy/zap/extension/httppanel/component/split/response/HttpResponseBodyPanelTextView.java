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
package org.zaproxy.zap.extension.httppanel.component.split.response;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zaproxy.zap.extension.httppanel.view.models.response.ResponseBodyStringHttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.text.HttpPanelTextArea;
import org.zaproxy.zap.extension.httppanel.view.text.HttpPanelTextView;
import org.zaproxy.zap.extension.search.SearchMatch;

public class HttpResponseBodyPanelTextView extends HttpPanelTextView {

	public HttpResponseBodyPanelTextView(ResponseBodyStringHttpPanelViewModel model) {
		super(model);
	}
	
	@Override
	protected HttpPanelTextArea createHttpPanelTextArea() {
		return new HttpResponseBodyPanelTextArea();
	}
	
	private static class HttpResponseBodyPanelTextArea extends HttpPanelTextArea {

		private static final long serialVersionUID = -3732558325150729623L;

		@Override
		public void search(Pattern p, List<SearchMatch> matches) {
			Matcher m = p.matcher(getText());
			while (m.find()) {
				matches.add(new SearchMatch(SearchMatch.Location.RESPONSE_BODY, m.start(), m.end()));
			}
		}
		
		@Override
		public void highlight(SearchMatch sm) {
			if (!SearchMatch.Location.RESPONSE_BODY.equals(sm.getLocation())) {
				return;
			}
			
			int len = getText().length();
			if (sm.getStart() > len || sm.getEnd() > len) {
				return;
			}
			
			highlight(sm.getStart(), sm.getEnd());
		}
	}
}
