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
// ZAP: 2011/04/16 Support for running ZAP as a daemon
// ZAP: 2011/05/09 Support for API
// ZAP: 2011/05/15 Support for exclusions
// ZAP: 2011/10/29 Support for parameters
// ZAP: 2011/11/20 Changed to use ExtensionFactory
// ZAP: 2012/02/18 Rationalised session handling

package org.parosproxy.paros.control;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.parosproxy.paros.core.scanner.PluginFactory;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SessionListener;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.control.ExtensionFactory;

/**
 *
 * Overall control with interaction on model and view.
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Control extends AbstractControl implements SessionListener {

    private static Logger log = Logger.getLogger(Control.class);

    private static Control control = null;
    private Proxy proxy = null;
    private MenuFileControl menuFileControl = null;
    private MenuToolsControl menuToolsControl = null;
    
    private SessionListener lastCallback = null;
    
    private Control(Model model, View view) {
        super(model, view);
        init();
    }
    
    private void init() {
        
        PluginFactory.loadAllPlugin(model.getOptionsParam().getConfig());
        		
		// start plugin loading
		loadExtension();
		
		// ZAP: Start proxy even if no view
	    getProxy();
	    getExtensionLoader().hookProxyListener(getProxy());
		
		if (view != null) {
		    // ZAP: Add site map listeners
		    getExtensionLoader().hookSiteMapListener(view.getSiteTreePanel());
		}
		
		getProxy().startServer();
    }
    
    public Proxy getProxy() {
        if (proxy == null) {
            proxy = new Proxy(model);
        }
        
        return proxy;
    }
    
    protected void addExtension() {
    	ExtensionFactory.loadAllExtension(getExtensionLoader(), model.getOptionsParam().getConfig());
    }
    
    public MenuFileControl getMenuFileControl() {
        if (menuFileControl == null) {
            menuFileControl = new MenuFileControl(model, view, this);
        }

        return menuFileControl;
    }
    

    public MenuToolsControl getMenuToolsControl() {
        if (menuToolsControl == null) {
            menuToolsControl = new MenuToolsControl(model, view, this);                
        }
        return menuToolsControl;
    }

    /**
     * Override inherited shutdown to add stopping proxy servers.
     */
    public void shutdown(boolean compact) {
        getProxy().stopServer();
        super.shutdown(compact);
    }
    
    public static Control getSingleton() {

        return control;
    }

    public static void initSingletonWithView() {
        control = new Control(Model.getSingleton(), View.getSingleton());
    }
    
    public static void initSingletonWithoutView() {
        control = new Control(Model.getSingleton(), null);
    }

    
    public void runCommandLineNewSession(String fileName) throws Exception {
	    log.debug("runCommandLineNewSession " + fileName);
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		
    	Session session = Model.getSingleton().getSession();
    	try {
    		Model.getSingleton().openSession(fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		if (!fileName.endsWith(".session")) {
		    fileName += ".session";
		}
		Model.getSingleton().saveSession(fileName);
	    log.info("New session file created");
		control.getExtensionLoader().sessionChangedAllPlugin(session);
		getExtensionLoader().runCommandLine();
	}
    
    public void runCommandLineOpenSession(String fileName) {
	    log.debug("runCommandLineOpenSession " + fileName);
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		
    	Session session = Model.getSingleton().getSession();
    	try {
    		Model.getSingleton().openSession(fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	    log.info("New session file created");
		control.getExtensionLoader().sessionChangedAllPlugin(session);
    }

    public void setExcludeFromProxyUrls(List<String> urls) {
		this.getProxy().setIgnoreList(urls);
    }
    
    public void openSession(final File file, final SessionListener callback) {
	    log.info("Open Session");
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		lastCallback = callback;
    	model.openSession(file, this);
		// The session is opened in a thread, so notify the listeners via the callback
    }

	public Session newSession() {
	    log.info("New Session");
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		Session session = model.newSession();
		getExtensionLoader().sessionChangedAllPlugin(session);

		return session;
	}
	
    public void saveSession(final String fileName) {
    	this.saveSession(fileName, null);
    }

    public void saveSession(final String fileName, final SessionListener callback) {
	    log.info("Save Session");
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		lastCallback = callback;
		model.saveSession(fileName, this);
		// The session is saved in a thread, so notify the listeners via the callback
    }

	
	public void discardSession() {
	    log.info("Discard Session");
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		model.discardSession();
		getExtensionLoader().sessionChangedAllPlugin(null);
	}

	public void createAndOpenUntitledDb() throws ClassNotFoundException, Exception {
	    log.info("Create and Open Untitled Db");
		getExtensionLoader().sessionAboutToChangeAllPlugin(null);
		model.createAndOpenUntitledDb();
		getExtensionLoader().sessionChangedAllPlugin(model.getSession());
	}

	@Override
	public void sessionOpened(File file, Exception e) {
		getExtensionLoader().sessionChangedAllPlugin(model.getSession());
		if (lastCallback != null) {
			lastCallback.sessionOpened(file, e);
			lastCallback = null;
		}
		
	}

	@Override
	public void sessionSaved(Exception e) {
		getExtensionLoader().sessionChangedAllPlugin(model.getSession());
		if (lastCallback != null) {
			lastCallback.sessionSaved(e);
			lastCallback = null;
		}
	}
}
