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
package org.zaproxy.zap.extension.search;

import java.awt.EventQueue;

import javax.swing.JMenuItem;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.model.Session;
import org.zaproxy.zap.extension.help.ExtensionHelp;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ExtensionSearch extends ExtensionAdaptor implements SessionChangedListener {

	public enum Type {All, URL, Request, Response, Header, Fuzz};

	private SearchPanel searchPanel = null;
    private JMenuItem menuSearch = null;
    private JMenuItem menuNext = null;
    private JMenuItem menuPrev = null;
    
    private SearchThread searchThread = null;

	/**
     * 
     */
    public ExtensionSearch() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionSearch(String name) {
        super(name);
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setName("ExtensionSearch");

	}
	
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    extensionHook.addSessionListener(this);
	    if (getView() != null) {
	        extensionHook.getHookView().addStatusPanel(getSearchPanel());
	        extensionHook.getHookMenu().addEditMenuItem(getMenuSearch());
	        extensionHook.getHookMenu().addEditMenuItem(getMenuNext());
	        extensionHook.getHookMenu().addEditMenuItem(getMenuPrev());
	        
	        getSearchPanel().setDisplayPanel(getView().getRequestPanel(), getView().getResponsePanel());

	        ExtensionHelp.enableHelpKey(getSearchPanel(), "ui.tabs.search");
	    }
	}
	
	private SearchPanel getSearchPanel() {
		if (searchPanel == null) {
			searchPanel = new SearchPanel();
			searchPanel.setExtension(this);
		}
		return searchPanel;
	}
	

	public void sessionChanged(final Session session)  {
	    if (EventQueue.isDispatchThread()) {
		    sessionChangedEventHandler(session);

	    } else {
	        
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	        		    sessionChangedEventHandler(session);
	                }
	            });
	        } catch (Exception e) {
	            
	        }
	    }
	}
	
	private void sessionChangedEventHandler(Session session) {
		this.getSearchPanel().resetSearchResults();
	}
	
	public void search(String filter, Type reqType) {
		this.search(filter, reqType, false, false);
	}
	
	public void search(String filter, Type reqType, boolean setToolbar, boolean inverse){
		
		this.searchPanel.resetSearchResults();
		
		if (setToolbar) {
			this.getSearchPanel().searchFocus();
			this.getSearchPanel().getRegExField().setText(filter);
			this.getSearchPanel().setSearchType(reqType);
		}
		
	    synchronized (this) {
	    	if (searchThread != null && searchThread.isAlive()) {
	    		searchThread.stopSearch();
	    		
	    		while (searchThread.isAlive()) {
	    			try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Ignore
					}
	    		}
	    	}
    		searchThread = new SearchThread(filter, reqType, searchPanel, inverse);
	    	searchThread.start();
	    	
	    }
	}

	private JMenuItem getMenuSearch() {
        if (menuSearch == null) {
        	menuSearch = new JMenuItem();
        	menuSearch.setText(Constant.messages.getString("menu.edit.search"));
        	menuSearch.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        			java.awt.event.KeyEvent.VK_H, java.awt.Event.CTRL_MASK, false));

        	menuSearch.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    searchPanel.searchFocus();
                }
            });
        }
        return menuSearch;
    }

    private JMenuItem getMenuNext() {
        if (menuNext == null) {
        	menuNext = new JMenuItem();
        	menuNext.setText(Constant.messages.getString("menu.edit.next"));
        	
        	menuNext.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        			java.awt.event.KeyEvent.VK_G, java.awt.Event.CTRL_MASK, false));

        	menuNext.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    searchPanel.highlightNextResult();
                }
            });
        }
        return menuNext;
    }

    private JMenuItem getMenuPrev() {
        if (menuPrev == null) {
        	menuPrev = new JMenuItem();
        	menuPrev.setText(Constant.messages.getString("menu.edit.previous"));

        	menuPrev.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    searchPanel.highlightPrevResult();
                }
            });
        }
        return menuPrev;
    }

  }