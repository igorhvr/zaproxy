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
package org.zaproxy.zap.view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.zaproxy.zap.extension.option.OptionsLocalePanel;
/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LocaleDialog extends AbstractDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private OptionsLocalePanel localePanel = null;
	private OptionsParam options = null;
	private JButton btnOK = null;
	private Logger logger = Logger.getLogger(LocaleDialog.class);

    /**
     * @throws HeadlessException
     */
    public LocaleDialog() throws HeadlessException {
        super();
 		initialize();
    }

    /**
     * @param arg0
     * @param arg1
     * @throws HeadlessException
     */
    public LocaleDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
 		initialize();
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getJPanel());
        this.pack();
        if (Model.getSingleton().getOptionsParam().getViewParam().getWmUiHandlingOption() == 0) {
        	this.setSize(406, 133);
        }
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5 = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.weighty = 1.0D;
			gridBagConstraints5.ipady = 2;
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.SOUTHEAST;
			jPanel.add(getViewPanel(), gridBagConstraints5);
			jPanel.add(getBtnOK(), gridBagConstraints6);
			
		}
		return jPanel;
	}
	
	private OptionsLocalePanel getViewPanel() {
		if (localePanel == null) {
			localePanel = new OptionsLocalePanel();
		}
		return localePanel;
	}
	
	public void init (OptionsParam options) {
		this.options = options;
		this.getViewPanel().initParam(options);
	}

	public void saveAndClose() {
		try {
			localePanel.saveParam(options);
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		
	    LocaleDialog.this.dispose();
	}
	/**
	 * This method initializes btnOK	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JButton();
			btnOK.setText(Constant.messages.getString("all.button.ok"));
			btnOK.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
					saveAndClose();
				}
			});

		}
		return btnOK;
	}

}
