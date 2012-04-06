/*
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2003-2004 Chinotec Technologies Company
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Clarified Artistic License
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Clarified Artistic License for more details.
 * 
 * You should have received a copy of the Clarified Artistic License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.zaproxy.zap.extension.ascan;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.Plugin;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.core.scanner.Plugin.Level;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.view.AbstractParamPanel;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OptionsScannerPanel extends AbstractParamPanel {

	private static final long serialVersionUID = 1L;
	private JPanel panelScanner = null; 
	private JSlider sliderHostPerScan = null;
	private JSlider sliderThreadsPerHost = null;
	private JSlider sliderDelayInMs = null;
	private JLabel labelDelayInMsValue = null;
	private JCheckBox chkHandleAntiCrsfTokens = null;
	private JComboBox comboLevel = null;
	private JLabel labelLevelNotes = null;

    public OptionsScannerPanel() {
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
        this.setName(Constant.messages.getString("ascan.options.title"));
        this.setSize(314, 245);
        this.add(getPanelScanner(), getPanelScanner().getName());
	}

	private JPanel getPanelScanner() {
		if (panelScanner == null) {
			java.awt.GridBagConstraints gridBagConstraintsx = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints8c = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints8b = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints8a = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5b = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints5a = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			java.awt.GridBagConstraints gridBagConstraints1 = new GridBagConstraints();

			panelScanner = new JPanel();
			panelScanner.setLayout(new GridBagLayout());
			panelScanner.setSize(114, 132);
			panelScanner.setName("");
			
			JLabel jLabel = new JLabel(Constant.messages.getString("ascan.options.numHosts.label"));
			JLabel jLabel1 = new JLabel(Constant.messages.getString("ascan.options.numThreads.label"));
			JLabel jLabel2 = new JLabel(Constant.messages.getString("ascan.options.delayInMs.label"));
			JLabel jLabel3 = new JLabel(Constant.messages.getString("ascan.options.level.label"));
			JLabel jLabelx = new JLabel();
			
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.gridwidth = 3;

			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridwidth = 3;

			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.gridwidth = 3;

			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 3;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.ipady = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.gridwidth = 3;

			
			gridBagConstraints5a.gridx = 0;
			gridBagConstraints5a.gridy = 4;
			gridBagConstraints5a.ipadx = 0;
			gridBagConstraints5a.ipady = 0;
			gridBagConstraints5a.insets = new Insets(2,2,2,2);
			gridBagConstraints5a.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints5a.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5a.weightx = 1.0D;
			gridBagConstraints5a.gridwidth = 2;
			
			gridBagConstraints5b.gridx = 2;
			gridBagConstraints5b.gridy = 4;
			gridBagConstraints5b.ipadx = 0;
			gridBagConstraints5b.ipady = 0;
			gridBagConstraints5b.insets = new Insets(2,2,2,2);
			gridBagConstraints5b.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints5b.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5b.weightx = 1.0D;
			
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 5;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.ipadx = 0;
			gridBagConstraints6.ipady = 0;
			gridBagConstraints6.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints6.insets = new Insets(2,2,2,2);
			gridBagConstraints6.gridwidth = 3;

			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 6;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.ipadx = 0;
			gridBagConstraints7.ipady = 0;
			gridBagConstraints7.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints7.insets = new Insets(2,2,2,2);
			gridBagConstraints7.gridwidth = 3;

			gridBagConstraints8a.gridx = 0;
			gridBagConstraints8a.gridy = 7;
			gridBagConstraints8a.ipadx = 0;
			gridBagConstraints8a.ipady = 0;
			gridBagConstraints8a.insets = new Insets(2,2,2,2);
			gridBagConstraints8a.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints8a.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8a.weightx = 1.0D;
			
			gridBagConstraints8b.gridx = 1;
			gridBagConstraints8b.gridy = 7;
			gridBagConstraints8b.ipadx = 0;
			gridBagConstraints8b.ipady = 0;
			gridBagConstraints8b.insets = new Insets(2,2,2,2);
			gridBagConstraints8b.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints8b.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8b.weightx = 1.0D;

			gridBagConstraints8c.gridx = 2;
			gridBagConstraints8c.gridy = 7;
			gridBagConstraints8c.ipadx = 0;
			gridBagConstraints8c.ipady = 0;
			gridBagConstraints8c.insets = new Insets(2,2,2,2);
			gridBagConstraints8c.anchor = GridBagConstraints.NORTHWEST;
			gridBagConstraints8c.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8c.weightx = 1.0D;

			gridBagConstraintsx.gridx = 0;
			gridBagConstraintsx.gridy = 10;
			gridBagConstraintsx.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraintsx.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraintsx.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraintsx.weightx = 1.0D;
			gridBagConstraintsx.weighty = 1.0D;
			gridBagConstraintsx.gridwidth = 2;
		
			panelScanner.add(jLabel, gridBagConstraints1);
			panelScanner.add(getSliderHostPerScan(), gridBagConstraints2);
			panelScanner.add(jLabel1, gridBagConstraints3);
			panelScanner.add(getSliderThreadsPerHost(), gridBagConstraints4);
			
			panelScanner.add(jLabel2, gridBagConstraints5a);
			panelScanner.add(getLabelDelayInMsValue(), gridBagConstraints5b);
			panelScanner.add(getSliderDelayInMs(), gridBagConstraints6);
			panelScanner.add(getChkHandleAntiCSRFTokens(), gridBagConstraints7);

			panelScanner.add(jLabel3, gridBagConstraints8a);
			panelScanner.add(getComboLevel(), gridBagConstraints8b);
			panelScanner.add(getLabelLevelNotes(), gridBagConstraints8c);
			
			panelScanner.add(jLabelx, gridBagConstraintsx);

		}
		return panelScanner;
	}
	
	private JLabel getLabelLevelNotes() {
		if (labelLevelNotes == null) {
			labelLevelNotes = new JLabel();
		}
		return labelLevelNotes;
	}


	private JComboBox getComboLevel() {
		if (comboLevel == null) {
			comboLevel = new JComboBox();
			comboLevel.addItem(Constant.messages.getString("ascan.options.level.low"));
			comboLevel.addItem(Constant.messages.getString("ascan.options.level.medium"));
			comboLevel.addItem(Constant.messages.getString("ascan.options.level.high"));
			comboLevel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Set the explanation
				    if (comboLevel.getSelectedItem().equals(Constant.messages.getString("ascan.options.level.low"))) {
				    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.low.label"));
				    } else if (comboLevel.getSelectedItem().equals(Constant.messages.getString("ascan.options.level.medium"))) {
				    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.medium.label"));
				    } else {
				    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.high.label"));
				    }
				}});
		}
		return comboLevel;
	}


	public void initParam(Object obj) {
	    OptionsParam options = (OptionsParam) obj;
	    ScannerParam param = (ScannerParam) options.getParamSet(ScannerParam.class);
	    getSliderHostPerScan().setValue(param.getHostPerScan());
	    getSliderThreadsPerHost().setValue(param.getThreadPerHost());
	    getSliderDelayInMs().setValue(param.getDelayInMs());
	    setLabelDelayInMsValue(param.getDelayInMs());
	    getChkHandleAntiCSRFTokens().setSelected(param.getHandleAntiCSRFTokens());
	    switch (param.getLevel()) {
	    case LOW: 
	    	getComboLevel().setSelectedItem(Constant.messages.getString("ascan.options.level.low"));
	    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.low.label"));
	    	break;
	    case MEDIUM: 
	    	getComboLevel().setSelectedItem(Constant.messages.getString("ascan.options.level.medium")); 
	    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.medium.label"));
	    	break;
	    case HIGH: 
	    	getComboLevel().setSelectedItem(Constant.messages.getString("ascan.options.level.high")); 
	    	getLabelLevelNotes().setText(Constant.messages.getString("ascan.options.level.high.label"));
	    	break;
	    }
	}
	
	public void validateParam(Object obj) {
	    // no validation needed
	}
	
	public void saveParam (Object obj) throws Exception {
	    OptionsParam options = (OptionsParam) obj;
	    ScannerParam param = (ScannerParam) options.getParamSet(ScannerParam.class);
	    param.setHostPerScan(getSliderHostPerScan().getValue());
	    param.setThreadPerHost(getSliderThreadsPerHost().getValue());
	    param.setDelayInMs(getDelayInMs());
	    param.setHandleAntiCSRFTokens(getChkHandleAntiCSRFTokens().isSelected());
	    
	    Plugin.Level level = null;
	    if (comboLevel.getSelectedItem().equals(Constant.messages.getString("ascan.options.level.low"))) {
	    	level = Level.LOW;
	    } else if (comboLevel.getSelectedItem().equals(Constant.messages.getString("ascan.options.level.medium"))) {
	    	level = Level.MEDIUM;
	    } else {
	    	level = Level.HIGH;
	    }
	    param.setLevel(level);
	}
	
	/**
	 * This method initializes sliderHostPerScan	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getSliderHostPerScan() {
		if (sliderHostPerScan == null) {
			sliderHostPerScan = new JSlider();
			sliderHostPerScan.setMaximum(5);
			sliderHostPerScan.setMinimum(1);
			sliderHostPerScan.setMinorTickSpacing(1);
			sliderHostPerScan.setPaintTicks(true);
			sliderHostPerScan.setPaintLabels(true);
			sliderHostPerScan.setName("");
			sliderHostPerScan.setMajorTickSpacing(1);
			sliderHostPerScan.setSnapToTicks(true);
			sliderHostPerScan.setPaintTrack(true);
		}
		return sliderHostPerScan;
	}
	/**
	 * This method initializes sliderThreadsPerHost	
	 * 	
	 * @return javax.swing.JSlider	
	 */    
	private JSlider getSliderThreadsPerHost() {
		if (sliderThreadsPerHost == null) {
			sliderThreadsPerHost = new JSlider();
			sliderThreadsPerHost.setMaximum(Constant.MAX_HOST_CONNECTION);
			sliderThreadsPerHost.setMinimum(1);
			sliderThreadsPerHost.setValue(1);
			sliderThreadsPerHost.setPaintTicks(true);
			sliderThreadsPerHost.setPaintLabels(true);
			sliderThreadsPerHost.setMinorTickSpacing(1);
			sliderThreadsPerHost.setMajorTickSpacing(1);
			sliderThreadsPerHost.setSnapToTicks(true);
			sliderThreadsPerHost.setPaintTrack(true);
		}
		return sliderThreadsPerHost;
	}

	private JSlider getSliderDelayInMs() {
		if (sliderDelayInMs == null) {
			sliderDelayInMs = new JSlider();
			sliderDelayInMs.setMaximum(1000);
			sliderDelayInMs.setMinimum(0);
			sliderDelayInMs.setValue(0);
			sliderDelayInMs.setPaintTicks(true);
			sliderDelayInMs.setPaintLabels(true);
			sliderDelayInMs.setMinorTickSpacing(25);
			sliderDelayInMs.setMajorTickSpacing(100);
			sliderDelayInMs.setSnapToTicks(true);
			sliderDelayInMs.setPaintTrack(true);
			
			sliderDelayInMs.addChangeListener(new ChangeListener () {

				@Override
				public void stateChanged(ChangeEvent e) {
					setLabelDelayInMsValue(getSliderDelayInMs().getValue());
				}});

		}
		return sliderDelayInMs;
	}

    public int getDelayInMs() {
    	return this.sliderDelayInMs.getValue();
    }

	public void setLabelDelayInMsValue(int value) {
		if (labelDelayInMsValue == null) {
			labelDelayInMsValue = new JLabel();
		}
		
		// Snap to ticks
		value = ((value + 13) / 25) * 25;
		String val = null;
		if (value < 10) {
			val = "   " + value;
		} else if (value < 100) {
			val = "  " + value;
		} else if (value < 1000) {
			val = " " + value;
		} else {
			val = "" + value;
		}
		labelDelayInMsValue.setText(val);
	}

	public JLabel getLabelDelayInMsValue() {
		if (labelDelayInMsValue == null) {
			setLabelDelayInMsValue(getSliderDelayInMs().getValue());
		}
		return labelDelayInMsValue;
	}


	@Override
	public String getHelpIndex() {
		return "ui.dialogs.options.ascan";
	}

	private JCheckBox getChkHandleAntiCSRFTokens() {
		if (chkHandleAntiCrsfTokens == null) {
			chkHandleAntiCrsfTokens = new JCheckBox();
			chkHandleAntiCrsfTokens.setText(Constant.messages.getString("ascan.options.anticsrf.label"));
		}
		return chkHandleAntiCrsfTokens;
	}

}
