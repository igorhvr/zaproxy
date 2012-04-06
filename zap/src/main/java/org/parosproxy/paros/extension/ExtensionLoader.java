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
// ZAP: 2011/12/14 Support for extension dependencies
// ZAP: 2012/02/18 Rationalised session handling
// ZAP: 2012/03/15 Reflected the change in the name of the method optionsChanged of
//      the class OptionsChangedListener. Changed the method destroyAllExtension() to 
//      save the configurations of the main http panels and save the configuration file.

package org.parosproxy.paros.extension;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.CommandLine;
import org.parosproxy.paros.common.AbstractParam;
import org.parosproxy.paros.control.Proxy;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.model.OptionsParam;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.view.AbstractParamDialog;
import org.parosproxy.paros.view.AbstractParamPanel;
import org.parosproxy.paros.view.SiteMapPanel;
import org.parosproxy.paros.view.TabbedPanel;
import org.parosproxy.paros.view.View;
import org.zaproxy.zap.view.SiteMapListener;

public class ExtensionLoader {

    private Vector<Extension> extensionList = new Vector<Extension>();
    private Vector<ExtensionHook> hookList = new Vector<ExtensionHook>();
    private Model model = null;

    private View view = null;
    // ZAP: Added logger
    private Logger logger = Logger.getLogger(ExtensionLoader.class);

    public ExtensionLoader(Model model, View view) {
        
        this.model = model;
        this.view = view;
    }

    public void addExtension(Extension extension) {
        extensionList.add(extension);
    }
    
    public void destroyAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).destroy();
        }

        // ZAP: Save the configurations of the main panels.
        view.getRequestPanel().saveConfig(model.getOptionsParam().getConfig());
        view.getResponsePanel().saveConfig(model.getOptionsParam().getConfig());
        
	    // ZAP: Save the configuration file.
		try {
			model.getOptionsParam().getConfig().save();
		} catch (ConfigurationException e) {
			logger.error("Error saving config", e);
		}
    }
    
    public Extension getExtension(int i) {
        return extensionList.get(i);
    }
    
    public Extension getExtension(String name) {
        if (name == null)
            return null;

        for (int i=0; i<extensionList.size(); i++) {
            Extension p = getExtension(i);
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        
        return null;
    }
    
    public int getExtensionCount() {
        return extensionList.size();
    }
    
    public void hookProxyListener(Proxy proxy) {
    	Iterator<ExtensionHook> iter = hookList.iterator();
    	while (iter.hasNext()) {
            ExtensionHook hook = iter.next();
            List<ProxyListener> listenerList = hook.getProxyListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    ProxyListener listener = listenerList.get(j);
                    if (listener != null) {
                        proxy.addProxyListener(listener);
                    }
                } catch (Exception e) {
                	// ZAP: Log the exception
                	logger.error(e.getMessage(), e);
                }
            }
    	}
    }
    
    // ZAP: Added support for site map listeners
    public void hookSiteMapListener(SiteMapPanel siteMapPanel) {
    	Iterator<ExtensionHook> iter = hookList.iterator();
    	while (iter.hasNext()) {
            ExtensionHook hook = iter.next();
            List<SiteMapListener> listenerList = hook.getSiteMapListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                	SiteMapListener listener = listenerList.get(j);
                    if (listener != null) {
                    	siteMapPanel.addSiteMapListenner(listener);
                    }
                } catch (Exception e) {
                	// ZAP: Log the exception
                	logger.error(e.getMessage(), e);
                }
            }
        }
    }
    
    public void optionsChangedAllPlugin(OptionsParam options) {
    	Iterator<ExtensionHook> iter = hookList.iterator();
    	while (iter.hasNext()) {
            ExtensionHook hook = iter.next();
            List<OptionsChangedListener> listenerList = hook.getOptionsChangedListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    OptionsChangedListener listener = listenerList.get(j);
                    if (listener != null) {
                    	// ZAP: reflected the change in name of the method optionsChanged.
                        listener.optionsChanged(options);
                    }
                } catch (Exception e) {
                	// ZAP: Log the exception
                	logger.error(e.getMessage(), e);
                }
            }
        }
    }
    
    public void runCommandLine() {
        ExtensionHook hook = null;
        Extension ext = null;
        for (int i=0; i<getExtensionCount(); i++) {
            ext = getExtension(i);
            hook = hookList.get(i);
            if (ext instanceof CommandLineListener) {
                CommandLineListener listener = (CommandLineListener) ext;
                listener.execute(hook.getCommandLineArgument());
            }
        }
    }
    
    public void sessionChangedAllPlugin(Session session) {
    	logger.debug("sessionChangedAllPlugin");
    	Iterator<ExtensionHook> iter = hookList.iterator();
    	while (iter.hasNext()) {
            ExtensionHook hook = iter.next();
            List<SessionChangedListener> listenerList = hook.getSessionListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    SessionChangedListener listener = listenerList.get(j);
                    if (listener != null) {
                        listener.sessionChanged(session);
                    }
                } catch (Exception e) {
                	// ZAP: Log the exception
                	logger.error(e.getMessage(), e);
                }
            }
            
        }
    }
    
    public void sessionAboutToChangeAllPlugin(Session session) {
    	logger.debug("sessionAboutToChangeAllPlugin");
    	Iterator<ExtensionHook> iter = hookList.iterator();
    	while (iter.hasNext()) {
            ExtensionHook hook = iter.next();
            List<SessionChangedListener> listenerList = hook.getSessionListenerList();
            for (int j=0; j<listenerList.size(); j++) {
                try {
                    SessionChangedListener listener = listenerList.get(j);
                    if (listener != null) {
                        listener.sessionAboutToChange(session);
                    }
                } catch (Exception e) {
                	// ZAP: Log the exception
                	logger.error(e.getMessage(), e);
                }
            }
        }
    }
    

    public void startAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).start();
        }
    }
    
    public void startLifeCycle() {
        initAllExtension();
        initModelAllExtension(model);
        initXMLAllExtension(model.getSession(), model.getOptionsParam());
        initViewAllExtension(view);
        
        hookAllExtension();
        startAllExtension();
        
    }
    
    public void stopAllExtension() {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).stop();
        }
        
    }
    
    private void addParamPanel(List panelList, AbstractParamDialog dialog) {
        AbstractParamPanel panel = null;
        String[] ROOT = {};
        for (int i=0; i<panelList.size(); i++) {
            try {
                panel = (AbstractParamPanel) panelList.get(i);
                dialog.addParamPanel(ROOT, panel, true);
            } catch (Exception e) {
            	// ZAP: Log the exception
            	logger.error(e.getMessage(), e);
            }
        }
        
    }
    
    private void addTabPanel(List<AbstractPanel> panelList, TabbedPanel tab) {
        AbstractPanel panel = null;
        for (int i=0; i<panelList.size(); i++) {
            try {
                panel = panelList.get(i);
                tab.add(panel, panel.getName());
                
        		// ZAP: added icon
        		tab.addTab(panel.getName() + " ", panel.getIcon(), panel);

                
            } catch (Exception e) {
            	// ZAP: Log the exception
            	logger.error(e.getMessage(), e);
            }
        }
    }


    
    
    private void hookAllExtension() {
        ExtensionHook extHook = null;
        for (int i=0; i<getExtensionCount(); i++) {
            try {
				extHook = new ExtensionHook(model, view);
				getExtension(i).hook(extHook);
				hookList.add(extHook);
				
				if (view != null) {
				    // no need to hook view if no GUI
				    hookView(view, extHook);
				    hookMenu(view, extHook);

				}
				hookOptions(extHook);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
        }
        
        if (view != null) {
            view.getMainFrame().getMainMenuBar().validate();
            view.getMainFrame().validate();
        }

    }
    
    /**
     * Hook command line listener with the command line processor
     * @param cmdLine
     */
    public void hookCommandLineListener (CommandLine cmdLine) throws Exception {
        Vector<CommandLineArgument[]> allCommandLineList = new Vector<CommandLineArgument[]>();
        for (int i=0; i<hookList.size(); i++) {
            ExtensionHook hook = hookList.get(i);
            CommandLineArgument[] arg = hook.getCommandLineArgument();
            if (arg.length > 0) {
                allCommandLineList.add(arg);
            }
        }
        
        cmdLine.parse(allCommandLineList);
    }
    
    private void hookMenu(View view, ExtensionHook hook) {

        if (view == null) {
            return;
        }
        
        if (hook.getHookMenu() == null) {
            return;
        }
        
        ExtensionHookMenu hookMenu = hook.getHookMenu();
        
        // init menus
        List<JMenuItem> list = null;
        JMenuItem item = null;
        JMenu menu = null;
        JMenu menuFile = view.getMainFrame().getMainMenuBar().getMenuFile();
        JMenu menuEdit = view.getMainFrame().getMainMenuBar().getMenuEdit();
        JMenu menuView = view.getMainFrame().getMainMenuBar().getMenuView();
        JMenu menuAnalyse = view.getMainFrame().getMainMenuBar().getMenuAnalyse();
        JMenu menuTools = view.getMainFrame().getMainMenuBar().getMenuTools();
        JMenu menuHelp = view.getMainFrame().getMainMenuBar().getMenuHelp();
        JMenu menuReport = view.getMainFrame().getMainMenuBar().getMenuReport();
        
        // process new menus

        JMenuBar bar = view.getMainFrame().getMainMenuBar();
        list = hookMenu.getNewMenus();
        for (int i=0; i<list.size(); i++) {
            menu = (JMenu) list.get(i);
            bar.add(menu, bar.getMenuCount()-2);	// 2 menus at the back (Tools/Help)
        }

        // process menu - File
        list = hookMenu.getFile();
        int existingCount = 2;
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuFile.addSeparator();
                continue;
            }

            menuFile.add(item, menuFile.getItemCount()-existingCount);
        }

        
        // process menu - Tools
        list = hookMenu.getTools();
        existingCount = 2;
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuTools.addSeparator();
                continue;
            }

            menuTools.add(item, menuTools.getItemCount()-existingCount);
        }

        // process Edit menu
        list = hookMenu.getEdit();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuEdit.addSeparator();
                continue;
            }
            menuEdit.add(item, menuEdit.getItemCount());
        }

        // process View menu
        list = hookMenu.getView();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuView.addSeparator();
                continue;
            }

            menuView.add(item, menuView.getItemCount());
        }

        // process Analyse menu
        list = hookMenu.getAnalyse();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuAnalyse.addSeparator();
                continue;
            }

            menuAnalyse.add(item, menuAnalyse.getItemCount());
        }
        
        // process popup menus
        
        list = hookMenu.getPopupMenus();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;

            view.getPopupList().add(item);
        }

        // ZAP: process Help menu
        list = hookMenu.getHelpMenus();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuHelp.addSeparator();
                continue;
            }

            menuHelp.add(item, menuHelp.getItemCount());
        }
        // ZAP: process Report menu
        list = hookMenu.getReportMenus();
        
        for (int i=0; i<list.size(); i++) {
            item = list.get(i);
            if (item == null) continue;
            if (item == ExtensionHookMenu.MENU_SEPARATOR) {
                menuReport.addSeparator();
                continue;
            }

            menuReport.add(item, menuReport.getItemCount());
        }
    }
    
    private void hookOptions(ExtensionHook hook) {
        Vector<AbstractParam> list = hook.getOptionsParamSetList();
        for (int i=0; i<list.size(); i++) {
            try {
                AbstractParam paramSet = list.get(i);
                model.getOptionsParam().addParamSet(paramSet);
            } catch (Exception e) {
            	// ZAP: Log the exception
            	logger.error(e.getMessage(), e);
            }
        }
    }


    
    private void hookView(View view, ExtensionHook hook) {
        if (view == null) {
            return;
        }
        
        ExtensionHookView pv = hook.getHookView();
        if (pv == null) {
            return;
        }
        
        addTabPanel(pv.getSelectPanel(), view.getWorkbench().getTabbedSelect());
        addTabPanel(pv.getWorkPanel(), view.getWorkbench().getTabbedWork());
        addTabPanel(pv.getStatusPanel(), view.getWorkbench().getTabbedStatus());
 
        addParamPanel(pv.getSessionPanel(), view.getSessionDialog(""));
        addParamPanel(pv.getOptionsPanel(), view.getOptionsDialog(""));
        
        
    }
    
    private void initAllExtension() {

        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).init();
        }
    }
    

    
    private void initModelAllExtension(Model model) {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initModel(model);
        }
        
    }

    private void initViewAllExtension(View view) {

        if (view == null) {
            return;
        }
        
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initView(view);
        }

    }

    private void initXMLAllExtension(Session session, OptionsParam options) {
        for (int i=0; i<getExtensionCount(); i++) {
            getExtension(i).initXML(session, options);
        }        
    }
}
