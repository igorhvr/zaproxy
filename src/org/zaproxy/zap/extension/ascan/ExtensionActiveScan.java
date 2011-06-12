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
package org.zaproxy.zap.extension.ascan;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.proxy.ProxyListener;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.core.scanner.HostProcess;
import org.parosproxy.paros.core.scanner.ScannerParam;
import org.parosproxy.paros.db.RecordAlert;
import org.parosproxy.paros.db.RecordScan;
import org.parosproxy.paros.db.TableAlert;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;
import org.parosproxy.paros.extension.SessionChangedListener;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.extension.history.ManualRequestEditorDialog;
import org.parosproxy.paros.model.HistoryList;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.model.Session;
import org.parosproxy.paros.model.SiteMap;
import org.parosproxy.paros.model.SiteNode;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.api.API;
import org.zaproxy.zap.extension.help.ExtensionHelp;
import org.zaproxy.zap.view.SiteMapListener;
/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ExtensionActiveScan extends ExtensionAdaptor implements  
		SessionChangedListener, CommandLineListener, ProxyListener, SiteMapListener {
    
    private static final int ARG_SCAN_IDX = 0;
    
	private AlertTreeModel treeAlert = null;
	
	private JMenuItem menuItemPolicy = null;
	private AlertPanel alertPanel = null;
	private RecordScan recordScan = null;
	
	private ManualRequestEditorDialog manualRequestEditorDialog = null;
	private PopupMenuAlertEdit popupMenuAlertEdit = null;
	private PopupMenuActiveScanSites popupMenuActiveScanSites = null;
	private PopupMenuActiveScanNode popupMenuActiveScanNode = null;
	private PopupExcludeFromScanMenu popupExcludeFromScanMenu = null;
	// Shouldnt really be here...
	private PopupExcludeFromProxyMenu popupExcludeFromProxyMenu = null;
	
	private OptionsScannerPanel optionsScannerPanel = null;
	private ActiveScanPanel activeScanPanel = null;
	private ScannerParam scannerParam = null;
	private CommandLineArgument[] arguments = new CommandLineArgument[1];

    private PopupMenuScanHistory popupMenuScanHistory = null;
    
	private HistoryList historyList = null;

    private Logger logger = Logger.getLogger(ExtensionActiveScan.class);

    /**
     * 
     */
    public ExtensionActiveScan() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionActiveScan(String name) {
        super(name);
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setName("ExtensionActiveScan");
			
        API.getInstance().registerApiImplementor(new ActiveScanAPI(this));

	}

	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
            extensionHook.getHookMenu().addAnalyseMenuItem(getMenuItemPolicy());

            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuScanHistory());

            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuAlertEdit());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanSites());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupMenuActiveScanNode());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupExcludeFromProxyMenu());
            extensionHook.getHookMenu().addPopupMenuItem(getPopupExcludeFromScanMenu());

            extensionHook.getHookView().addStatusPanel(getAlertPanel());
            extensionHook.getHookView().addStatusPanel(getActiveScanPanel());
	        extensionHook.getHookView().addOptionPanel(getOptionsScannerPanel());
	        
	        this.getActiveScanPanel().setDisplayPanel(getView().getRequestPanel(), getView().getResponsePanel());

	    	ExtensionHelp.enableHelpKey(getAlertPanel(), "ui.tabs.alerts");
	    	ExtensionHelp.enableHelpKey(getActiveScanPanel(), "ui.tabs.ascan");
	    }
        extensionHook.addSessionListener(this);
        extensionHook.addProxyListener(this);
        extensionHook.addSiteMapListner(this);

        extensionHook.addOptionsParamSet(getScannerParam());
        extensionHook.addCommandLine(getCommandLineArguments());


	}
	
	private ActiveScanPanel getActiveScanPanel() {
		if (activeScanPanel == null) {
			activeScanPanel = new ActiveScanPanel(this);
		}
		return activeScanPanel;
	}
	
	void startScan(SiteNode startNode) {
		this.getActiveScanPanel().scanSite(startNode);
	}

    public void scannerComplete() {
    }
	
	/**
	 * This method initializes menuItemPolicy	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getMenuItemPolicy() {
		if (menuItemPolicy == null) {
			menuItemPolicy = new JMenuItem();
			menuItemPolicy.setText(Constant.messages.getString("menu.analyse.scanPolicy"));	// ZAP: i18n
			menuItemPolicy.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

					PolicyDialog dialog = new PolicyDialog(getView().getMainFrame());
				    dialog.initParam(getModel().getOptionsParam());
					int result = dialog.showDialog(false);
					if (result == JOptionPane.OK_OPTION) {
					    try {
			                getModel().getOptionsParam().getConfig().save();
			            } catch (ConfigurationException ce) {
			                ce.printStackTrace();
			                getView().showWarningDialog(Constant.messages.getString("scanner.save.warning"));	// ZAP: i18n
			                return;
			            }
					}					
				}
			});

		}
		return menuItemPolicy;
	}

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.ScannerListener#ScannerProgress(java.lang.String, com.proofsecure.paros.network.HttpMessage, int)
     */
    public void hostProgress(String hostAndPort, String msg, int percentage) {
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.ScannerListener#HostComplete(java.lang.String)
     */
    public void hostComplete(String hostAndPort) {
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.ScannerListener#hostNewScan(java.lang.String)
     */
    public void hostNewScan(String hostAndPort, HostProcess hostThread) {
    }
    
    public void alertFound(Alert alert, HistoryReference ref) {
        try {
        	if (ref == null) {
        		ref = alert.getHistoryRef();
        	}
        	if (ref == null) {
        		ref = new HistoryReference(getModel().getSession(), HistoryReference.TYPE_SCANNER, alert.getMessage());
        	}
            
            writeAlertToDB(alert, ref);
            addAlertToDisplay(alert, ref);
            
            // The node node may have a new alert flag...
        	this.nodeChanged(ref.getSiteNode());
        	
        } catch (Exception e) {
        	// ZAP: Print stack trace to Output tab
        	getView().getOutputPanel().append(e);
        }
    }
    
    private void nodeChanged(TreeNode node) {
    	if (node == null) {
    		return;
    	}
    	SiteMap siteTree = this.getModel().getSession().getSiteTree();
    	siteTree.nodeChanged(node);
    	nodeChanged(node.getParent());
    }

    private void addAlertToDisplay(final Alert alert, final HistoryReference ref) {
        if (getView() == null) {
    		// Running as a daemon
    		return;
    	}
	    if (EventQueue.isDispatchThread()) {
	    	addAlertToDisplayEventHandler(alert, ref);

	    } else {
	        
	        try {
	            EventQueue.invokeAndWait(new Runnable() {
	                public void run() {
	        	    	addAlertToDisplayEventHandler(alert, ref);
	                }
	            });
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	    }
    }

    private void addAlertToDisplayEventHandler (Alert alert, HistoryReference ref) {

        if (getView() != null) {
        	treeAlert.addPath(alert);
            getAlertPanel().expandRoot();
        }
        
		SiteMap siteTree = this.getModel().getSession().getSiteTree();
		SiteNode node = siteTree.findNode(alert.getMessage());
		if (ref != null && (node == null || ! node.hasAlert(alert))) {
			// Add new alerts to the site tree
			siteTree.addPath(ref);
	        ref.addAlert(alert);
		}
    }

	/**
	 * This method initializes alertPanel	
	 * 	
	 * @return com.proofsecure.paros.extension.scanner.AlertPanel	
	 */    
	AlertPanel getAlertPanel() {
		if (alertPanel == null) {
			alertPanel = new AlertPanel();
			alertPanel.setView(getView());
			alertPanel.setSize(345, 122);
			alertPanel.getTreeAlert().setModel(getTreeModel());
		}
		
		return alertPanel;
	}

	@Override
    public void initView(ViewDelegate view) {
    	super.initView(view);
    	getAlertPanel().setView(view);
    }

	
	// ZAP: Changed return type for getTreeModel
	private AlertTreeModel getTreeModel() {
	    if (treeAlert == null) {
	        treeAlert = new AlertTreeModel();
	    }
	    return treeAlert;
	}
	
	private void writeAlertToDB(Alert alert, HistoryReference ref) throws HttpMalformedHeaderException, SQLException {

	    TableAlert tableAlert = getModel().getDb().getTableAlert();
        int scanId = 0;
        if (recordScan != null) {
        	scanId = recordScan.getScanId();
        }
        RecordAlert recordAlert = tableAlert.write(
                scanId, alert.getPluginId(), alert.getAlert(), alert.getRisk(), alert.getReliability(),
                alert.getDescription(), alert.getUri(), alert.getParam(), alert.getOtherInfo(), alert.getSolution(), alert.getReference(),
        		ref.getHistoryId(), alert.getSourceHistoryId()
                );
        
        alert.setAlertId(recordAlert.getAlertId());
        
	}
	public void updateAlert(Alert alert) throws HttpMalformedHeaderException, SQLException {
		updateAlertInDB(alert);
		if (alert.getHistoryRef() != null) {
			this.nodeChanged(alert.getHistoryRef().getSiteNode());
		}
	}

	private void updateAlertInDB(Alert alert) throws HttpMalformedHeaderException, SQLException {

	    TableAlert tableAlert = getModel().getDb().getTableAlert();
	    tableAlert.update(alert.getAlertId(), alert.getAlert(), alert.getRisk(), 
	    		alert.getReliability(), alert.getDescription(), alert.getUri(),
	    		alert.getParam(), alert.getOtherInfo(), alert.getSolution(), 
	    		alert.getReference(), alert.getSourceHistoryId());
	}
	
	public void displayAlert (Alert alert) {
		this.alertPanel.getAlertViewPanel().displayAlert(alert);
	}
	
	public void updateAlertInTree(Alert originalAlert, Alert alert) {
		this.getTreeModel().updatePath(originalAlert, alert);
		
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
	            logger.error(e.getMessage(), e);
	        }
	    }
	}
	
	@SuppressWarnings("unchecked")
	private void sessionChangedEventHandler(Session session) {
	    AlertTreeModel tree = (AlertTreeModel) getAlertPanel().getTreeAlert().getModel();

	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getRoot();
	    
        while (root.getChildCount() > 0) {
            tree.removeNodeFromParent((MutableTreeNode) root.getChildAt(0));
        }
        // ZAP: Reset the alert counts
	    tree.recalcAlertCounts();
	    
	    try {
            refreshAlert(session);
            // ZAP: this prevent the UI getting corrupted
            tree.nodeStructureChanged(root);
        } catch (SQLException e) {
        	// ZAP: Print stack trace to Output tab
        	getView().getOutputPanel().append(e);
        }
		
		// clear all scans and add new hosts
		this.getActiveScanPanel().reset();
		SiteNode snroot = (SiteNode)session.getSiteTree().getRoot();
		Enumeration<SiteNode> en = snroot.children();
		while (en.hasMoreElements()) {
			String site = en.nextElement().getNodeName();
			if (site.indexOf("//") >= 0) {
				site = site.substring(site.indexOf("//") + 2);
			}
			this.getActiveScanPanel().addSite(site);
		}
	}

	private void refreshAlert(Session session) throws SQLException {
		SiteMap siteTree = this.getModel().getSession().getSiteTree();

	    TableAlert tableAlert = getModel().getDb().getTableAlert();
	    Vector<Integer> v = tableAlert.getAlertList();
	    
	    for (int i=0; i<v.size(); i++) {
	        int alertId = v.get(i).intValue();
	        RecordAlert recAlert = tableAlert.read(alertId);
	        Alert alert = new Alert(recAlert);
	        addAlertToDisplay(alert, alert.getHistoryRef());
	    }
    	siteTree.nodeStructureChanged((SiteNode)siteTree.getRoot());
	}

	/**
	 * This method initializes manualRequestEditorDialog	
	 * 	
	 * @return org.parosproxy.paros.extension.history.ManualRequestEditorDialog	
	 */    
	ManualRequestEditorDialog getManualRequestEditorDialog() {
		if (manualRequestEditorDialog == null) {
			manualRequestEditorDialog = new ManualRequestEditorDialog(getView().getMainFrame(), false, false, this);
			manualRequestEditorDialog.setTitle(Constant.messages.getString("manReq.resend.popup"));	// ZAP: i18n
			manualRequestEditorDialog.setSize(700, 800);
		}
		return manualRequestEditorDialog;
	}
	/**
	 * This method initializes popupMenuResend	
	 * 	
	 * @return org.parosproxy.paros.extension.scanner.PopupMenuResend	
	 */    
	private PopupMenuAlertEdit getPopupMenuAlertEdit() {
		if (popupMenuAlertEdit == null) {
			popupMenuAlertEdit = new PopupMenuAlertEdit();
			popupMenuAlertEdit.setExtension(this);
		}
		return popupMenuAlertEdit;
	}

	/**
	 * This method initializes optionsScannerPanel	
	 * 	
	 * @return org.parosproxy.paros.extension.scanner.OptionsScannerPanel	
	 */    
	private OptionsScannerPanel getOptionsScannerPanel() {
		if (optionsScannerPanel == null) {
			optionsScannerPanel = new OptionsScannerPanel();
		}
		return optionsScannerPanel;
	}
	/**
	 * This method initializes scannerParam	
	 * 	
	 * @return org.parosproxy.paros.core.scanner.ScannerParam	
	 */    
	protected ScannerParam getScannerParam() {
		if (scannerParam == null) {
			scannerParam = new ScannerParam();
		}
		return scannerParam;
	}
	
    /* (non-Javadoc)
     * @see org.parosproxy.paros.extension.CommandLineListener#execute(org.parosproxy.paros.extension.CommandLineArgument[])
     */
	// TODO
    public void execute(CommandLineArgument[] args) {
    	/*

        if (arguments[ARG_SCAN_IDX].isEnabled()) {
            System.out.println("Scanner started...");
            startScan();
        } else {
            return;
        }

        while (!getScanner().isStop()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Scanner completed.");

    */
    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_SCAN_IDX] = new CommandLineArgument("-scan", 0, null, "", "-scan : Run vulnerability scan depending on previously saved policy.");
        return arguments;
    }

    /**
     * This method initializes popupMenuScanHistory	
     * 	
     * @return org.parosproxy.paros.extension.scanner.PopupMenuScanHistory	
     */
	private PopupMenuScanHistory getPopupMenuScanHistory() {
        if (popupMenuScanHistory == null) {
            popupMenuScanHistory = new PopupMenuScanHistory();
            popupMenuScanHistory.setExtension(this);
        }
        return popupMenuScanHistory;
    }

	@Override
	public void onHttpRequestSend(HttpMessage msg) {
		// The panel will handle duplicates
		String site = msg.getRequestHeader().getHostName();
		if (msg.getRequestHeader().getHostPort() > 0 && msg.getRequestHeader().getHostPort() != 80) {
			site += ":" + msg.getRequestHeader().getHostPort();
		}
		this.getActiveScanPanel().addSite(site);
	}

	@Override
	public void onHttpResponseReceive(HttpMessage msg) {
		// Do nothing
	}

	@Override
	public void nodeSelected(SiteNode node) {
		// Event from SiteMapListenner
		this.getActiveScanPanel().nodeSelected(node);
	}

	private PopupMenuActiveScanSites getPopupMenuActiveScanSites() {
		if (popupMenuActiveScanSites == null) {
			popupMenuActiveScanSites = new PopupMenuActiveScanSites();
			popupMenuActiveScanSites.setExtension(this);
		}
		return popupMenuActiveScanSites;
	}

	private PopupMenuActiveScanNode getPopupMenuActiveScanNode() {
		if (popupMenuActiveScanNode == null) {
			popupMenuActiveScanNode = new PopupMenuActiveScanNode();
			popupMenuActiveScanNode.setExtension(this);
		}
		return popupMenuActiveScanNode;
	}
	
	private PopupExcludeFromScanMenu getPopupExcludeFromScanMenu() {
		if (popupExcludeFromScanMenu == null) {
			popupExcludeFromScanMenu = new PopupExcludeFromScanMenu();
		}
		return popupExcludeFromScanMenu;
	}

	private PopupExcludeFromProxyMenu getPopupExcludeFromProxyMenu() {
		if (popupExcludeFromProxyMenu == null) {
			popupExcludeFromProxyMenu = new PopupExcludeFromProxyMenu();
		}
		return popupExcludeFromProxyMenu;
	}

	public HistoryList getHistoryList() {
	    if (historyList == null) {
	        historyList = new HistoryList();
	    }
	    return historyList;
	}

	public boolean isScanning(SiteNode node) {
		return this.getActiveScanPanel().isScanning(node);
	}

	public void setExcludeList(List<String> urls) {
		this.getActiveScanPanel().setExcludeList(urls);
	}
}
