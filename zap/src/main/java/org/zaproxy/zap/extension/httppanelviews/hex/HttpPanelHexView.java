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
package org.zaproxy.zap.extension.httppanelviews.hex;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.commons.configuration.FileConfiguration;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelView;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModel;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelEvent;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelViewModelListener;
import org.zaproxy.zap.extension.httppanel.view.models.AbstractByteHttpPanelViewModel;

public class HttpPanelHexView implements HttpPanelView, HttpPanelViewModelListener {

	public static final String CONFIG_NAME = "Hex";
	
	private static final String CAPTION_NAME = Constant.messages.getString("http.panel.view.hex.name");
	
	private HttpPanelHexModel httpPanelHexModel = null;
	private JTable hexTableBody = null;
	private javax.swing.JScrollPane scrollHexTableBody = null;
	private boolean isEditable = false;
	private AbstractByteHttpPanelViewModel model;
	
	public HttpPanelHexView(AbstractByteHttpPanelViewModel model, boolean isEditable) {
		this.model = model;
		
		getHttpPanelHexModel().setEditable(isEditable);
		
		this.model.addHttpPanelViewModelListener(this);
	}
	
	@Override
	public String getName() {
		return CAPTION_NAME;
	}
	
	@Override
	public int getPosition() {
		return 0;
	}
	
	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}

	@Override
	public JScrollPane getPane() {
		if (scrollHexTableBody == null) {
			scrollHexTableBody = new javax.swing.JScrollPane();
			scrollHexTableBody.setName(CAPTION_NAME);
			scrollHexTableBody.setViewportView(getHexTableBody());
		}
		return scrollHexTableBody;
	}

	private JTable getHexTableBody() {
		if (hexTableBody == null) {
			hexTableBody = new JTable();
			hexTableBody.setName("");
			hexTableBody.setModel(getHttpPanelHexModel());

			hexTableBody.setGridColor(java.awt.Color.gray);
			hexTableBody.setIntercellSpacing(new java.awt.Dimension(1,1));
			hexTableBody.setRowHeight(18);
			
			hexTableBody.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			hexTableBody.getColumnModel().getColumn(0).setPreferredWidth(100);
			for (int i=1; i <= 17; i++) {
				hexTableBody.getColumnModel().getColumn(i).setPreferredWidth(30);
			}
			for (int i=17; i <= hexTableBody.getColumnModel().getColumnCount()-1; i++) {
				hexTableBody.getColumnModel().getColumn(i).setPreferredWidth(25);
			}
			
			hexTableBody.setCellSelectionEnabled(true);
			hexTableBody.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return hexTableBody;
	}

	private HttpPanelHexModel getHttpPanelHexModel() {
		if (httpPanelHexModel == null) {
			httpPanelHexModel = new HttpPanelHexModel();
		}
		return httpPanelHexModel;
	}

	@Override
	public boolean isEnabled(HttpMessage msg) {
		return true;
	}

	@Override
	public boolean hasChanged() {
		return getHttpPanelHexModel().hasChanged();
	}

	@Override
	public boolean isEditable() {
		return isEditable;
	}

	@Override
	public void setEditable(boolean editable) {
		getHttpPanelHexModel().setEditable(editable);
	}

	@Override
	public void dataChanged(HttpPanelViewModelEvent e) {
		getHttpPanelHexModel().setData( model.getData() );
	}
	
	@Override
	public void save() {
		model.setData(getHttpPanelHexModel().getData());
	}
	
	@Override
	public void setParentConfigurationKey(String configurationKey) {
	}
	
	@Override
	public void loadConfiguration(FileConfiguration fileConfiguration) {
	}
	
	@Override
	public void saveConfiguration(FileConfiguration fileConfiguration) {
	}
	
	@Override
	public void setSelected(boolean selected) {
		if (selected) {
			hexTableBody.requestFocusInWindow();
		}
	}
	
	@Override
	public HttpPanelViewModel getModel() {
		return model;
	}
}
