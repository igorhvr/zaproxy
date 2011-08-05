package org.zaproxy.zap.extension.brk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.view.TabbedPanel;

// Button notes
// BreakRequest button, if set all requests trapped
// BreakResponse button, ditto for responses
// If break point hit, Break tab gets focus and icon goes red
// Step button, only if break point hit, submits just this req/resp, breaks on next
// Continue button, only if break point hit, submits this req/resp and continues until next break point hit
// If BreakReq & Resp both selected Step and Continue buttons have same effect
// 

public class BreakPanelToolbarFactory {

	private LinkedList<JButton> btnContinueList = new LinkedList<JButton>();
	private LinkedList<JButton> btnStepList = new LinkedList<JButton>();
	private LinkedList<JButton> btnDropList = new LinkedList<JButton>();

	private LinkedList<JToggleButton> btnBreakRequestList = new LinkedList<JToggleButton>();
	private LinkedList<JToggleButton> btnBreakResponseList = new LinkedList<JToggleButton>();

	private LinkedList<JLabel> labelInfo = new LinkedList<JLabel>();

	private boolean cont = false;
	private boolean step = false;
	private boolean stepping = false;
	private boolean toBeDropped = false;
	private boolean isBreakRequest = false;
	private boolean isBreakResponse = false;
	
	private boolean isVirgin = true;
	private boolean isRequest = true;

	private BreakPanel breakPanel = null;

	
	public BreakPanelToolbarFactory() {

	}

	public BreakPanelToolbarFactory(BreakPanel breakPanel) {
		this.breakPanel = breakPanel;
	}

	private void setActiveIcon(boolean active) {
		if (breakPanel.getParent() instanceof TabbedPanel) {
			TabbedPanel parent = (TabbedPanel) breakPanel.getParent();
			if (active) {
				parent.setIconAt(
						parent.indexOfComponent(breakPanel),
						new ImageIcon(getClass().getResource("/resource/icon/16/101.png")));	// Red X
			} else {
				parent.setIconAt(
						parent.indexOfComponent(breakPanel), 
						new ImageIcon(getClass().getResource("/resource/icon/16/101grey.png")));	// Grey X
			}
		}
	}

	public void breakPointHit () {
		// This could have been via a break point, so force the serialisation
		resetRequestSerialization(true);

		// Set the active icon and reset the continue button
		this.setActiveIcon(true);
		setContinue(false);
	}


	public boolean isBreakRequest() {
		return isBreakRequest;
	}

	public boolean isBreakResponse() {
		return isBreakResponse;
	}

	public JButton getBtnStep() {
		JButton btnStep;

		btnStep = new JButton();
		btnStep.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/143.png")));
		btnStep.setToolTipText(Constant.messages.getString("brk.toolbar.button.step"));
		btnStep.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {    
				setStep(true);
			}
		});
		// Default to disabled
		btnStep.setEnabled(false);

		btnStepList.add(btnStep);
		return btnStep;
	}

	/**
	 * This method initializes btnContinue	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getBtnContinue() {
		JButton btnContinue;

		btnContinue = new JButton();
		btnContinue.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/131.png")));
		btnContinue.setToolTipText(Constant.messages.getString("brk.toolbar.button.cont"));
		btnContinue.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				setContinue(true);
			}
		});
		// Default to disabled
		btnContinue.setEnabled(false);

		btnContinueList.add(btnContinue);	
		return btnContinue;
	}

	/**
	 * This method initializes btnDrop	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JButton getBtnDrop() {
		JButton btnDrop;

		btnDrop = new JButton();
		btnDrop.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/150.png")));
		btnDrop.setToolTipText(Constant.messages.getString("brk.toolbar.button.bin"));
		btnDrop.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				toBeDropped = true;
				setContinue(true);
			}
		});
		// Default to disabled
		btnDrop.setEnabled(false);

		btnDropList.add(btnDrop);

		return btnDrop;
	}
	
	/**
	 * This method initializes btnContinue	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	public JToggleButton getBtnBreakRequest() {
		JToggleButton btnBreakRequest;

		btnBreakRequest = new JToggleButton();
		isBreakRequest = false;

		btnBreakRequest.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {    
				// Toggle button
				toggleBreakRequest();
			}
		});

		btnBreakRequestList.add(btnBreakRequest);
		updateBreakRequestBtn();

		return btnBreakRequest;
	}

	/**
	 * This method initializes btnContinue	
	 * 	
	 * @return javax.swing.JButton	
	 */ 
	public JToggleButton getBtnBreakResponse() {
		JToggleButton btnBreakResponse;

		btnBreakResponse = new JToggleButton();
		isBreakResponse = false;

		btnBreakResponse.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {    
				// Toggle button
				toggleBreakResponse();
			}
		});

		btnBreakResponseList.add(btnBreakResponse);
		updateBreakResponseBtn();

		return btnBreakResponse;
	}

	public boolean isStepping() {
		return stepping;
	}


	private void resetRequestSerialization (boolean forceSerialize) {
		if (Control.getSingleton() == null) {
			// Still in setup
			return;
		}
		// If forces or either break buttons are pressed force the proxy to submit requests and responses serially 
		if (forceSerialize || isBreakRequest() || isBreakResponse()) {
			Control.getSingleton().getProxy().setSerialize(true);
		} else {
			Control.getSingleton().getProxy().setSerialize(true);
		}
	}

	private void toggleBreakRequest() {
		isBreakRequest = !isBreakRequest();
		resetRequestSerialization(false);

		updateBreakRequestBtn();
		updateInfoLabel();
	}

	private void toggleBreakResponse() {
		isBreakResponse = !isBreakResponse();
		resetRequestSerialization(false);

		updateBreakResponseBtn();
		updateInfoLabel();
	}

	private void updateInfoLabel() {
	/*	String label = "";
		
		if (isRequest) {
			label = "Request: ";
		} else {
			label = "Response: ";
		}
	
		
		if (isVirgin) {
			label += "fresh";
		} else {
			label += "rotten";
		}
		*/
	}
	
	private void updateBreakRequestBtn() {
		if (isBreakRequest()) {
			for(JToggleButton btnBreakRequest: btnBreakRequestList) {
				btnBreakRequest.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/105r.png")));
				btnBreakRequest.setToolTipText(Constant.messages.getString("brk.toolbar.button.request.unset"));
				btnBreakRequest.setSelected(true);
			}
		} else {
			for(JToggleButton btnBreakRequest: btnBreakRequestList) {
				btnBreakRequest.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/105.png")));
				btnBreakRequest.setToolTipText(Constant.messages.getString("brk.toolbar.button.request.set"));
				btnBreakRequest.setSelected(false);
			}
		}
	}

	private void updateBreakResponseBtn() {
		if (isBreakResponse()) {
			for(JToggleButton btnBreakResponse: btnBreakResponseList) {
				btnBreakResponse.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/106r.png")));
				btnBreakResponse.setToolTipText(Constant.messages.getString("brk.toolbar.button.response.unset"));
				btnBreakResponse.setSelected(true);
			}
		} else {
			for(JToggleButton btnBreakResponse: btnBreakResponseList) {
				btnBreakResponse.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/106.png")));
				btnBreakResponse.setToolTipText(Constant.messages.getString("brk.toolbar.button.response.set"));
				btnBreakResponse.setSelected(false);
			}
		}
	}


	/**
	 * @return Returns the true if the message (request or response) should be held (ie not submited)
	 */
	public boolean isHoldMessage() {
		if (step) {
			// Only works one time, until its pressed again
			stepping = true;
			step = false;
			return false;
		}
		if (cont) {
			// They've pressed the continue button, stop stepping
			stepping = false;
			resetRequestSerialization(false);
			return false;
		}
		return true;
	}

	public boolean isContinue() {
		return cont;
	}

	/**
	 * @param isContinue The isContinue to set.
	 */
	private void setContinue(boolean isContinue) {
		this.cont = isContinue;


		for(JButton btnStep: btnStepList) {
			btnStep.setEnabled( ! isContinue);
		}

		for(JButton btnContinue: btnContinueList) {
			btnContinue.setEnabled( ! isContinue);
		}

		for(JButton btnDrop: btnDropList) {
			btnDrop.setEnabled( ! isContinue);
		}

		if (isContinue) {
			this.setActiveIcon(false);
		}
	}

	private void setStep(boolean isStep) {
		step = isStep;

		for(JButton btnStep: btnStepList) {
			btnStep.setEnabled( ! isStep);
		}

		for(JButton btnContinue: btnContinueList) {
			btnContinue.setEnabled( ! isStep);
		}

		for(JButton btnDrop: btnDropList) {
			btnDrop.setEnabled( ! isStep);
		}

		if (isStep) {
			this.setActiveIcon(false);
		}
	}

	public void gotMessage(boolean isRequest) {
		this.isRequest = isRequest; 
		this.isVirgin = true;
		updateInfoLabel();
	}

	public boolean isToBeDropped() {
		boolean drop = toBeDropped;
		toBeDropped = false;
		return drop;
	}

}
