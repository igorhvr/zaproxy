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
 * 
 * Note that this extension ane the other classes in this package are heavily 
 * based on the orriginal Paros ExtensionSpider! 
 */
package org.zaproxy.zap.view;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.view.AbstractParamPanel;

public class SessionExcludeFromSpiderPanel extends AbstractParamPanel {

	public static final String PANEL_NAME = Constant.messages.getString("session.spider.exclude.title"); 
	private static final long serialVersionUID = -8337361808959321380L;
	
	private JPanel panelSession = null;
	private JTable tableIgnore = null;
	private JScrollPane jScrollPane = null;
	private SingleColumnTableModel model = null;
	
    public SessionExcludeFromSpiderPanel() {
        super();
 		initialize();
   }

    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setLayout(new CardLayout());
        this.setName(PANEL_NAME);
        this.add(getPanelSession(), getPanelSession().getName());
	}
	/**
	 * This method initializes panelSession	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getPanelSession() {
		if (panelSession == null) {

			panelSession = new JPanel();
			panelSession.setLayout(new GridBagLayout());
			panelSession.setName("Ignorespider");

			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	        java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

	        javax.swing.JLabel jLabel = new JLabel();

	        jLabel.setText(Constant.messages.getString("session.spider.label.ignore"));
	        gridBagConstraints1.gridx = 0;
	        gridBagConstraints1.gridy = 0;
	        gridBagConstraints1.gridheight = 1;
	        gridBagConstraints1.insets = new java.awt.Insets(10,0,5,0);
	        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
	        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	        gridBagConstraints1.weightx = 0.0D;

	        gridBagConstraints2.gridx = 0;
	        gridBagConstraints2.gridy = 1;
	        gridBagConstraints2.weightx = 1.0;
	        gridBagConstraints2.weighty = 1.0;
	        gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
	        gridBagConstraints2.ipadx = 0;
	        gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
	        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
	        panelSession.add(jLabel, gridBagConstraints1);
	        panelSession.add(getJScrollPane(), gridBagConstraints2);
		}
		return panelSession;
	}
	
	public void initParam(Object obj) {
	    Session session = (Session) obj;
	    getModel().setLines(session.getExcludeFromSpiderRegexs());
	}
	
	public void validateParam(Object obj) {
	    // no validation needed
	}
	
	public void saveParam (Object obj) throws Exception {
	    Session session = (Session) obj;
	    session.setExcludeFromSpiderRegexs(getModel().getLines());
	    // ZAP Save session details
	    if ( ! session.isNewState()) {
	    	Control.getSingleton().saveSession(session.getFileName());
	    }
	}
	
	private JTable getTableIgnore() {
		if (tableIgnore == null) {
			tableIgnore = new JTable();
			tableIgnore.setModel(getModel());
			tableIgnore.setRowHeight(18);
		}
		return tableIgnore;
	}
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTableIgnore());
			jScrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
		}
		return jScrollPane;
	}
	
	private SingleColumnTableModel getModel() {
		if (model == null) {
			model = new SingleColumnTableModel(Constant.messages.getString("session.spider.table.header.ignore"));
		}
		return model;
	}

	@Override
	public String getHelpIndex() {
		return "ui.dialogs.options.sessprop";
	}
}
