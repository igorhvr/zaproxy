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
package org.zaproxy.zap.extension.history;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionListener;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.extension.AbstractDialog;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Model;
import org.zaproxy.zap.extension.alert.AlertViewPanel;
import org.zaproxy.zap.extension.alert.ExtensionAlert;
/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AlertAddDialog extends AbstractDialog implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jPanel = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	
	private ExtensionHistory extension = null;

	private HistoryReference historyRef;
	
	private JScrollPane jScrollPane = null;
	private AlertViewPanel alertViewPanel = null;
	
    /**
     * @throws HeadlessException
     */
    public AlertAddDialog() throws HeadlessException  {
        super();
 		initialize();
    }

    /**
     * @param arg0
     * @param arg1
     * @throws HeadlessException
     */
    public AlertAddDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        initialize();
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setTitle(Constant.messages.getString("alert.add.title"));
        this.setContentPane(getJPanel());
        if (Model.getSingleton().getOptionsParam().getViewParam().getWmUiHandlingOption() == 0) {
        	this.setSize(407, 607);
        }
        this.addWindowListener(new java.awt.event.WindowAdapter() {   
        	public void windowOpened(java.awt.event.WindowEvent e) {    
        	} 

        	public void windowClosing(java.awt.event.WindowEvent e) {    
        	    btnCancel.doClick();
        	}
        });

		pack();
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints13 = new GridBagConstraints();

			javax.swing.JLabel jLabel2 = new JLabel();

			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();

			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.setPreferredSize(new java.awt.Dimension(450,650));
			jPanel.setMinimumSize(new java.awt.Dimension(450,650));
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 5;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints3.gridx = 2;
			gridBagConstraints3.gridy = 5;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,10);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;

			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 5;
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.weightx = 1.0D;
			gridBagConstraints13.insets = new java.awt.Insets(2,10,2,5);

			gridBagConstraints15.weightx = 1.0D;
			gridBagConstraints15.weighty = 1.0D;
			gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints15.gridwidth = 3;
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 2;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints15.ipadx = 0;
			gridBagConstraints15.ipady = 10;

			jPanel.add(getJScrollPane(), gridBagConstraints15);
			jPanel.add(jLabel2, gridBagConstraints13);
			jPanel.add(getBtnCancel(), gridBagConstraints2);
			jPanel.add(getBtnOk(), gridBagConstraints3);
		}
		return jPanel;
	}
	/**
	 * This method initializes btnStart	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText(Constant.messages.getString("alert.add.button.save"));
			btnOk.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {
					Alert alert = alertViewPanel.getAlert();
					try {
						ExtensionAlert extAlert = (ExtensionAlert) Control.getSingleton().getExtensionLoader().getExtension(ExtensionAlert.NAME);
						if (alert.getAlertId() >= 0) {
							// Its an existing alert so save it
							if (extAlert != null) {
								extAlert.updateAlert(alert);

								// Update alert display
								extAlert.displayAlert(alert);
								
								// Update alert tree
								extAlert.updateAlertInTree(alertViewPanel.getOriginalAlert(), alert);
							}

							// Update history tree
							if (historyRef != null) {
								historyRef.updateAlert(alert);
			                    extension.getHistoryList().notifyItemChanged(historyRef);
							}

						} else {
							historyRef.addAlert(alert);
		                    extension.getHistoryList().notifyItemChanged(historyRef);
						    // Raise it
							if (extAlert != null) {
								extAlert.alertFound(alert, historyRef);
							}
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					extension.hideAlertAddDialog();
				}
			});

		}
		return btnOk;
	}
	
	/**
	 * This method initializes btnStop	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(Constant.messages.getString("alert.add.button.cancel"));
			btnCancel.setEnabled(true);
			btnCancel.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    
				    extension.hideAlertAddDialog();
				}
			});

		}
		return btnCancel;
	}
	
	public void setPlugin(ExtensionHistory plugin) {
	    this.extension = plugin;
        plugin.getView().getSiteTreePanel().getTreeSite().addTreeSelectionListener(this);

	}
	
	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	}
	
	private AlertViewPanel getAlertViewPanel () {
		if (alertViewPanel == null) {
			alertViewPanel = new AlertViewPanel(true);
		}
		return this.alertViewPanel;
	}
	
	public void setAlert(Alert alert) {
		this.getAlertViewPanel().displayAlert(alert);
		if (alert.getHistoryRef() != null) {
			this.setHistoryRef(alert.getHistoryRef());
		} else {
			this.setHistoryRef(alert.getMessage().getHistoryRef());
		}
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			jScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setViewportView(getAlertViewPanel());
		}
		return jScrollPane;
	}

	public HistoryReference getHistoryRef() {
		return historyRef;
	}

	public void setHistoryRef(HistoryReference historyRef) {
		this.historyRef = historyRef;
		alertViewPanel.setHistoryRef(historyRef);
	}

}
