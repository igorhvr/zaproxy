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
package org.zaproxy.zap.extension.httppanel.component.split.request;

import java.awt.BorderLayout;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;

import org.apache.commons.configuration.FileConfiguration;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.httppanel.component.HttpPanelComponentInterface;
import org.zaproxy.zap.extension.httppanel.component.HttpPanelComponentViewsManager;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelDefaultViewSelector;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelView;
import org.zaproxy.zap.extension.httppanel.view.models.request.RequestBodyStringHttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.models.request.RequestHeaderStringHttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.text.HttpPanelTextView;
import org.zaproxy.zap.extension.search.SearchMatch;
import org.zaproxy.zap.extension.search.SearchableHttpPanelComponent;

public class RequestSplitComponent implements HttpPanelComponentInterface, SearchableHttpPanelComponent {

	public static final String NAME = "RequestSplit";
	
	public enum ViewComponent {HEADER, BODY}; 
	
	private static final String DIVIDER_LOCATION_CONFIG_KEY = "dividerLocation";
	
	private static final String BUTTON_TOOL_TIP = Constant.messages.getString("http.panel.component.split.tooltip");
	private static final String HEADER_LABEL = Constant.messages.getString("http.panel.component.split.header");
	private static final String BODY_LABEL = Constant.messages.getString("http.panel.component.split.body");
	
	protected JToggleButton buttonShowView;
	
	protected JPanel panelOptions;
	protected JPanel panelMain;
	protected JSplitPane splitMain;

	protected HttpMessage httpMessage;
	protected boolean isEditable;
	
	protected HttpPanelComponentViewsManager headerViews;
	protected HttpPanelComponentViewsManager bodyViews;
	
	private String configurationKey;

	public RequestSplitComponent() {
		configurationKey = "";
		
		headerViews = new HttpPanelComponentViewsManager("split.header", HEADER_LABEL);
		bodyViews = new HttpPanelComponentViewsManager("split.body", BODY_LABEL);
		
		initUi();
	}

	protected void initUi() {
		buttonShowView = new JToggleButton(new ImageIcon(getClass().getResource("/resource/icon/view_split.png")));
		buttonShowView.setToolTipText(BUTTON_TOOL_TIP);
		
		panelOptions = new JPanel();
		
		panelOptions.add(headerViews.getComboBox());
		panelOptions.add(bodyViews.getComboBox());
		
		headerViews.addView(createHttpPanelHeaderTextView());
		
		splitMain = new JSplitPane();
		splitMain.setDividerSize(3);
		splitMain.setResizeWeight(0.5d);
		splitMain.setContinuousLayout(false);
		splitMain.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		splitMain.setTopComponent(headerViews.getViewsPanel());
		splitMain.setBottomComponent(bodyViews.getViewsPanel());
		
		initViews();

		panelMain = new JPanel(new BorderLayout());
		panelMain.add(splitMain, BorderLayout.CENTER);
		
		setSelected(false);
	}

	@Override
	public JToggleButton getButton() {
		return buttonShowView;
	}

	@Override
	public JPanel getOptionsPanel() {
		return panelOptions;
	}

	@Override
	public JPanel getMainPanel() {
		return panelMain;
	}
	
	@Override
	public void setSelected(boolean selected) {
		buttonShowView.setSelected(selected);

		headerViews.setSelected(selected);
	}
	
	protected HttpPanelTextView createHttpPanelHeaderTextView() {
		return new HttpRequestHeaderPanelTextView(new RequestHeaderStringHttpPanelViewModel());
	}
	
	protected void initViews() {
		bodyViews.addView(new HttpRequestBodyPanelTextView(new RequestBodyStringHttpPanelViewModel()));
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void setHttpMessage(HttpMessage httpMessage) {
		this.httpMessage = httpMessage;
		
		headerViews.setHttpMessage(httpMessage);
		bodyViews.setHttpMessage(httpMessage);
	}
	
    @Override
    public void save() {
        if (httpMessage == null) {
            return;
        }

		headerViews.save();
		bodyViews.save();
    }

	@Override
	public void addView(HttpPanelView view, Object options, FileConfiguration fileConfiguration) {
		if (options != null) {
			if (ViewComponent.HEADER.equals(options)) {
				headerViews.addView(view, fileConfiguration);
			} else if (ViewComponent.BODY.equals(options)) {
				bodyViews.addView(view, fileConfiguration);
			}
		}
	}

	@Override
	public void removeView(String viewName, Object options) {
		if (options != null) {
			if (ViewComponent.HEADER.equals(options)) {
				headerViews.removeView(viewName);
			} else if (ViewComponent.BODY.equals(options)) {
				bodyViews.removeView(viewName);
			}
		}
	}
	
	@Override
	public void clearView() {
		httpMessage = null;
				
		headerViews.clearView();
		bodyViews.clearView();
	}

	@Override
	public void clearView(boolean enableViewSelect) {
		clearView();
		
		setEnableViewSelect(enableViewSelect);
	}
	
	@Override
	public void setEnableViewSelect(boolean enableViewSelect) {
		headerViews.setEnableViewSelect(enableViewSelect);
		bodyViews.setEnableViewSelect(enableViewSelect);
	}

	@Override
	public void addDefaultViewSelector(HttpPanelDefaultViewSelector defaultViewSelector, Object options) {
		if (options != null) {
			if (ViewComponent.HEADER.equals(options)) {
				headerViews.addDefaultViewSelector(defaultViewSelector);
			} else if (ViewComponent.BODY.equals(options)) {
				bodyViews.addDefaultViewSelector(defaultViewSelector);
			}
		}
	}

	@Override
	public void removeDefaultViewSelector(String defaultViewSelectorName, Object options) {
		if (options != null) {
			if (ViewComponent.HEADER.equals(options)) {
				headerViews.removeDefaultViewSelector(defaultViewSelectorName);
			} else if (ViewComponent.BODY.equals(options)) {
				bodyViews.removeDefaultViewSelector(defaultViewSelectorName);
			}
		}
	}

	@Override
	public void setParentConfigurationKey(String configurationKey) {
		this.configurationKey = configurationKey;
		headerViews.setConfigurationKey(configurationKey);
		bodyViews.setConfigurationKey(configurationKey);
	}
	
	@Override
	public void loadConfig(FileConfiguration fileConfiguration) {
		splitMain.setDividerLocation(Model.getSingleton().getOptionsParam().getConfig().getInt(configurationKey + DIVIDER_LOCATION_CONFIG_KEY, -1));
		
		headerViews.loadConfig(fileConfiguration);
		bodyViews.loadConfig(fileConfiguration);
	}

	@Override
	public void saveConfig(FileConfiguration fileConfiguration) {
		Model.getSingleton().getOptionsParam().getConfig().setProperty(configurationKey + DIVIDER_LOCATION_CONFIG_KEY, Integer.valueOf(splitMain.getDividerLocation()));
		
		headerViews.saveConfig(fileConfiguration);
		bodyViews.saveConfig(fileConfiguration);
	}

	@Override
	public void setEditable(boolean editable) {
		if (isEditable != editable) {
			isEditable = editable;
			
			headerViews.setEditable(editable);
			bodyViews.setEditable(editable);
		}
	}

	@Override
	public void highlightHeader(SearchMatch sm) {
		headerViews.highlight(sm);
	}

	@Override
	public void highlightBody(SearchMatch sm) {
		bodyViews.highlight(sm);
	}

	@Override
	public void searchHeader(Pattern p, List<SearchMatch> matches) {
		headerViews.search(p, matches);
	}

	@Override
	public void searchBody(Pattern p, List<SearchMatch> matches) {
		bodyViews.search(p, matches);
	}
}
