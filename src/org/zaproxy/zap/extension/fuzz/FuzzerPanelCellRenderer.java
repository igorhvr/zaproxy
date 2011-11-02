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
package org.zaproxy.zap.extension.fuzz;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.parosproxy.paros.network.HttpMessage;

public class FuzzerPanelCellRenderer extends JPanel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	private JLabel txtId = null;
    private JLabel txtMethod = null;
    private JLabel txtURI = null;
    private JLabel txtStatus = null;
    private JLabel txtReason = null;
    private JLabel txtRTT = null;
    private JLabel txtSize = null;
	private JLabel txtNote = null;
	private JLabel txtFlag = null;
	private JLabel txtFuzz = null;


    /**
     * This is the default constructor
     */
    public FuzzerPanelCellRenderer() {
        super();

        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
    	// ZAP: Added flag image
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints7.gridx = 9;
        gridBagConstraints7.gridy = 0;
        gridBagConstraints7.weightx = 1.05D;
        gridBagConstraints7.ipadx = 4;
        gridBagConstraints7.ipady = 1;
        //gridBagConstraints7.insets = new java.awt.Insets(0,2,0,0);
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;

        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints6.gridx = 8;
        gridBagConstraints6.gridy = 0;
        gridBagConstraints6.weightx = 0.25D;
        gridBagConstraints6.ipadx = 4;
        gridBagConstraints6.ipady = 1;
        //gridBagConstraints6.insets = new java.awt.Insets(0,2,0,0);
        gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;

        // ZAP: Added notes image
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints5.gridx = 7;
        gridBagConstraints5.gridy = 0;
        gridBagConstraints5.weightx = 0.0D;
        gridBagConstraints5.ipadx = 4;
        gridBagConstraints5.ipady = 1;
        //gridBagConstraints5.insets = new java.awt.Insets(0,2,0,0);
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
        
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 6;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.weightx = 0.0D;
        gridBagConstraints4.ipadx = 0;
        gridBagConstraints4.ipady = 1;
        //gridBagConstraints4.insets = new java.awt.Insets(0,2,0,0);
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.weightx = 0.0D;
        gridBagConstraints3.ipadx = 4;
        gridBagConstraints3.ipady = 1;
        gridBagConstraints3.gridx = 5;
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 4;
        gridBagConstraints21.ipadx = 4;
        gridBagConstraints21.ipady = 1;
        gridBagConstraints21.weightx = 0.0D;
        gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints21.gridy = 0;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints11.gridy = 0;
        gridBagConstraints11.weightx = 0.0D;
        gridBagConstraints11.ipadx = 4;
        gridBagConstraints11.ipady = 1;
        gridBagConstraints11.gridx = 3;
        txtSize = new JLabel();
        txtSize.setText(" ");
        txtSize.setBackground(java.awt.SystemColor.text);
        txtSize.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtSize.setPreferredSize(new java.awt.Dimension(70,15));
        txtSize.setMinimumSize(new java.awt.Dimension(70,15));
        txtSize.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtSize.setOpaque(true);
        txtRTT = new JLabel();
        txtRTT.setText(" ");
        txtRTT.setBackground(java.awt.SystemColor.text);
        txtRTT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtRTT.setPreferredSize(new java.awt.Dimension(55,15));
        txtRTT.setMinimumSize(new java.awt.Dimension(55,15));
        txtRTT.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtRTT.setOpaque(true);
        txtReason = new JLabel();
        txtReason.setText(" ");
        txtReason.setBackground(java.awt.SystemColor.text);
        txtReason.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtReason.setPreferredSize(new java.awt.Dimension(85,15));
        txtReason.setMinimumSize(new java.awt.Dimension(85,15));
        txtReason.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtReason.setOpaque(true);
        txtReason.setVisible(true);
        txtStatus = new JLabel();
        txtStatus.setText(" ");
        txtStatus.setBackground(java.awt.SystemColor.text);
        txtStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtStatus.setPreferredSize(new java.awt.Dimension(30,15));
        txtStatus.setMinimumSize(new java.awt.Dimension(30,15));
        txtStatus.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtStatus.setOpaque(true);
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints2.gridy = 0;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.weightx = 0.75D;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.ipadx = 4;
        gridBagConstraints2.ipady = 1;
        gridBagConstraints2.gridx = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints1.weightx = 0.0D;
        gridBagConstraints1.ipadx = 4;
        gridBagConstraints1.ipady = 1;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0,0,0,0);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.0D;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.gridx = 0;
        txtURI = new JLabel();
        txtURI.setText(" ");
        txtURI.setBackground(java.awt.SystemColor.text);
        txtURI.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtURI.setPreferredSize(new java.awt.Dimension(420,15));
        txtURI.setMinimumSize(new java.awt.Dimension(420,15));
        txtURI.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtURI.setOpaque(true);
        txtMethod = new JLabel();
        txtMethod.setText(" ");
        txtMethod.setBackground(java.awt.SystemColor.text);
        txtMethod.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtMethod.setPreferredSize(new java.awt.Dimension(45,15));
        txtMethod.setMinimumSize(new java.awt.Dimension(45,15));
        txtMethod.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtMethod.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtMethod.setOpaque(true);
        txtId = new JLabel();
        txtId.setText(" ");
        txtId.setBackground(java.awt.SystemColor.text);
        txtId.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtId.setPreferredSize(new java.awt.Dimension(40,15));
        txtId.setMinimumSize(new java.awt.Dimension(40,15));
        txtId.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtId.setOpaque(true);
        
        txtFlag = new JLabel();
        txtFlag.setText("");
        txtFlag.setBackground(java.awt.SystemColor.text);
        txtFlag.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtFlag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtFlag.setPreferredSize(new java.awt.Dimension(20,15));
        txtFlag.setMinimumSize(new java.awt.Dimension(20,15));
        txtFlag.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtFlag.setOpaque(true);

        txtNote = new JLabel();
        txtNote.setText("");
        txtNote.setBackground(java.awt.SystemColor.text);
        txtNote.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtNote.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtNote.setPreferredSize(new java.awt.Dimension(20,15));
        txtNote.setMinimumSize(new java.awt.Dimension(20,15));
        txtNote.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtNote.setOpaque(true);
        
        txtFuzz = new JLabel();
        txtFuzz.setText("");
        txtFuzz.setBackground(java.awt.SystemColor.text);
        txtFuzz.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txtFuzz.setPreferredSize(new java.awt.Dimension(70,15));
        txtFuzz.setMinimumSize(new java.awt.Dimension(70,15));
        txtFuzz.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        txtFuzz.setOpaque(true);

        this.setLayout(new GridBagLayout());
        this.setSize(328, 11);
        this.setFont(new java.awt.Font("Default", java.awt.Font.PLAIN, 12));
        this.add(txtId, gridBagConstraints);
        this.add(txtMethod, gridBagConstraints1);
        this.add(txtURI, gridBagConstraints2);
        this.add(txtStatus, gridBagConstraints11);
        this.add(txtReason, gridBagConstraints21);
        this.add(txtRTT, gridBagConstraints3);
        this.add(txtFlag, gridBagConstraints4);
        this.add(txtNote, gridBagConstraints5);
        this.add(txtSize, gridBagConstraints6);
        this.add(txtFuzz, gridBagConstraints7);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        HttpMessage msg = (HttpMessage) value;
        
        // The fuzz payload is recorded in the note
    	String note = msg.getNote();
    	txtFlag.setIcon(null);
        if (note != null && note.length() > 0) {
        	if (msg.getResponseBody().toString().indexOf(note) >= 0) {
        		// Found the exact payload - flag it
        		txtFlag.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/099.png")));	// Yellow fuzzy circle
        	}
        }

        txtMethod.setText(msg.getRequestHeader().getMethod());
        txtURI.setText(msg.getRequestHeader().getURI().toString());
        txtStatus.setText(Integer.toString(msg.getResponseHeader().getStatusCode()));
        txtReason.setText(msg.getResponseHeader().getReasonPhrase());
        txtRTT.setText(msg.getTimeElapsedMillis()+"ms");
        txtSize.setText(""+msg.getResponseBody().toString().length());
        txtFuzz.setText(note);
            
        
        if (isSelected) {
            txtId.setBackground(list.getSelectionBackground());
            txtId.setForeground(list.getSelectionForeground());
            txtMethod.setBackground(list.getSelectionBackground());
            txtMethod.setForeground(list.getSelectionForeground());
            txtURI.setBackground(list.getSelectionBackground());
            txtURI.setForeground(list.getSelectionForeground());
            txtStatus.setBackground(list.getSelectionBackground());
            txtStatus.setForeground(list.getSelectionForeground());
            txtReason.setBackground(list.getSelectionBackground());
            txtReason.setForeground(list.getSelectionForeground());
            txtRTT.setBackground(list.getSelectionBackground());
            txtRTT.setForeground(list.getSelectionForeground());
            txtSize.setBackground(list.getSelectionBackground());
            txtSize.setForeground(list.getSelectionForeground());
            txtNote.setBackground(list.getSelectionBackground());
            txtNote.setForeground(list.getSelectionForeground());
            txtFlag.setBackground(list.getSelectionBackground());
            txtFlag.setForeground(list.getSelectionForeground());
            txtFuzz.setBackground(list.getSelectionBackground());
            txtFuzz.setForeground(list.getSelectionForeground());

        } else {
            Color darker = new Color(list.getBackground().getRGB() & 0xFFECECEC);
            
            txtId.setBackground(list.getBackground());
            txtId.setForeground(list.getForeground());
            txtMethod.setBackground(darker);
            txtMethod.setForeground(list.getForeground());
            txtURI.setBackground(list.getBackground());
            txtURI.setForeground(list.getForeground());
            txtStatus.setBackground(darker);
            txtStatus.setForeground(list.getForeground());
            txtReason.setBackground(list.getBackground());
            txtReason.setForeground(list.getForeground());
            txtRTT.setBackground(darker);
            txtRTT.setForeground(list.getForeground());
            txtSize.setBackground(list.getBackground());
            txtSize.setForeground(list.getForeground());
            txtNote.setBackground(list.getBackground());
            txtNote.setForeground(list.getForeground());
            txtFlag.setBackground(list.getBackground());
            txtFlag.setForeground(list.getForeground());
            txtFuzz.setBackground(list.getBackground());
            txtFuzz.setForeground(list.getForeground());

        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        return this;
        
    }

}
