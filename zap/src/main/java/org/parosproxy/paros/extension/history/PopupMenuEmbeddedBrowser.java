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
// ZAP: 2012/01/12 Reflected the rename of the class ExtensionPopupMenu to
//                 ExtensionPopupMenuItem
package org.parosproxy.paros.extension.history;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTree;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionPopupMenuItem;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMessage;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PopupMenuEmbeddedBrowser extends ExtensionPopupMenuItem {

	private static final long serialVersionUID = 1L;
	private ExtensionHistory extension = null;
    private Component lastInvoker = null;
    // ZAP: Changed to support BrowserLauncher
    private BrowserLauncher launcher = null;
    private boolean supported = true;

	/**
     * 
     */
    public PopupMenuEmbeddedBrowser() {
        super();
 		initialize();
    }

    /**
     * @param label
     */
    public PopupMenuEmbeddedBrowser(String label) {
        super(label);
        initialize();
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setText(Constant.messages.getString("history.browser.popup"));

        this.setActionCommand("");
        
        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
                HistoryReference ref = null;
                if (lastInvoker == null) {
                    return;
                }
                if (lastInvoker.getName().equalsIgnoreCase("ListLog")) {
                    JList listLog = extension.getLogPanel().getListLog();
                    
                    ref = (HistoryReference) listLog.getSelectedValue();
                    showBrowser(ref);                                   

                } else if (lastInvoker.getName().equals("treeSite")) {
                    JTree tree = (JTree) lastInvoker;
                    SiteNode node = (SiteNode) tree.getLastSelectedPathComponent();
                    ref = node.getHistoryReference();
                    showBrowser(ref);
                }
        	}
        });
			
	}
	
	private BrowserLauncher getBrowserLauncher() {
		if (! supported) {
			return null;
		}
		if (launcher == null) {
			try {
				launcher = new BrowserLauncher();
			} catch (BrowserLaunchingInitializingException e) {
				supported = false;
			} catch (UnsupportedOperatingSystemException e) {
				supported = false;
			}
		}
		return launcher;
	}
	
    private void showBrowser(HistoryReference ref) {
    	if (! supported) {
    		return;
    	}
        HttpMessage msg = null;
        try {
            msg = ref.getHttpMessage();
            this.getBrowserLauncher().openURLinBrowser(msg.getRequestHeader().getURI().toString());

        } catch (Exception e) {
            extension.getView().showWarningDialog(Constant.messages.getString("history.browser.warning"));
        }
        
    }

    
    public boolean isEnableForComponent(Component invoker) {
        lastInvoker = null;

        if ( ! supported) {
        	return false;
        }
        
        if (invoker.getName() == null) {
            return false;
        }
        
        if (invoker.getName().equalsIgnoreCase("ListLog")) {
            JList list = (JList) invoker;
            if (list.getSelectedIndex() >= 0) {
                this.setEnabled(true);
            } else {
                this.setEnabled(false);
            }
            lastInvoker = invoker;
            return true;
        } else if (invoker.getName().equals("treeSite")) {
        	JTree tree = (JTree) invoker;
        	lastInvoker = tree;
            SiteNode node = (SiteNode) tree.getLastSelectedPathComponent();
            this.setEnabled(node != null && node.getHistoryReference() != null);
            return true;
        }
        return false;
    }
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
	
}
