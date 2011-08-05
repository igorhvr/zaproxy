package org.zaproxy.zap.extension.httppanel.model;

import java.util.List;
import java.util.regex.Pattern;

import org.parosproxy.paros.view.HttpPanel;
import org.zaproxy.zap.extension.httppanel.HttpPanelSplitUi;
import org.zaproxy.zap.extension.search.SearchMatch;

public class HttpDataModelResSplit {

	private HttpPanel httpPanel;
	private HttpPanelSplitUi splitUi;
		
	public HttpDataModelResSplit(HttpPanel httpPanel, HttpPanelSplitUi splitUi) {
		this.httpPanel = httpPanel;
		this.splitUi = splitUi;
	}
		
	public void loadData() {
		splitUi.setMessage(httpPanel.getHttpMessage(), false);
	}

	public void saveData() {
		splitUi.getMessage(httpPanel.getHttpMessage(), false);
	}
	
	public void highlightHeader(SearchMatch sm) {
		splitUi.highlightHeader(sm);
	}

	public void highlightBody(SearchMatch sm) {
		splitUi.highlightBody(sm);
	}

	public void search(Pattern p, List<SearchMatch> matches) {
		splitUi.search(p, matches);
	}

	public void clearData() {
		splitUi.setMessage(null, false);
	}
}