/*
 *
 * Paros and its related class files.
 * 
 * Paros is an HTTP/HTTPS proxy for assessing web application security.
 * Copyright (C) 2005 Chinotec Technologies Company
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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.ExtensionPopupMenuItem;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMessage;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PopupMenuExportResponse extends ExtensionPopupMenuItem {

    // ZAP: Added logger
    private static Logger log = Logger.getLogger(PopupMenuExportResponse.class);

    private ExtensionHistory extension = null;
    
    /**
     * 
     */
    public PopupMenuExportResponse() {
        super();
 		initialize();
    }

    /**
     * @param label
     */
    public PopupMenuExportResponse(String label) {
        super(label);
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setText(Constant.messages.getString("history.export.response.popup"));	// ZAP: i18n

        this.addActionListener(new java.awt.event.ActionListener() { 

        	public void actionPerformed(java.awt.event.ActionEvent e) {
        	    
                JList listLog = extension.getLogPanel().getListLog();
        	    Object[] obj = listLog.getSelectedValues();
        	    if (obj.length == 0) {
                    extension.getView().showWarningDialog(Constant.messages.getString("history.export.response.select.warning"));        	        	// ZAP: i18n
                    return;
        	    }

                if (obj.length > 1) {
                    extension.getView().showWarningDialog(Constant.messages.getString("history.export.response.single.warning"));	// ZAP: i18n
                    return;
                }

                HistoryReference ref = (HistoryReference) obj[0];
                HttpMessage msg = null;
                try {
                    msg = ref.getHttpMessage();
                } catch (Exception e1) {
                    extension.getView().showWarningDialog(Constant.messages.getString("history.export.response.read.warning"));	// ZAP: i18n
                    return;
                }
                
                if (msg.getResponseHeader().isEmpty() || msg.getResponseBody().length() == 0) {
                    extension.getView().showWarningDialog(Constant.messages.getString("history.export.response.body.warning"));	// ZAP: i18n
                    return;                    
                }
                    
        	    File file = getOutputFile(msg);
        	    if (file == null) {
        	        return;
        	    }
        	    
        	    boolean isAppend = true;
        	    if (file.exists()) {
                    int rc = extension.getView().showYesNoCancelDialog(Constant.messages.getString("file.overwrite.warning"));
                    if (rc == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (rc == JOptionPane.YES_OPTION) {
                        isAppend = false;
                    }
        	    }
                    
        	    BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(file, isAppend));
            	    for (int i=0; i<obj.length; i++) {
            	        exportHistory(msg, bos);
            	    }

                } catch (Exception e1) {
                    extension.getView().showWarningDialog(Constant.messages.getString("file.save.error") + file.getAbsolutePath() + ".");
                	// ZAP: Log exceptions
                	log.warn(e1.getMessage(), e1);
                } finally {
            	    try {
            	        bos.close();
            	    } catch (Exception e2) {
                    	// ZAP: Log exceptions
                    	log.warn(e2.getMessage(), e2);
            	    }
                }
        	}
        });

			
	}
	
    public boolean isEnableForComponent(Component invoker) {
        
        if (invoker.getName() != null && invoker.getName().equals("ListLog")) {
            try {
                JList list = (JList) invoker;
                if (list.getSelectedIndex() >= 0) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
            } catch (Exception e) {
            	// ZAP: Log exceptions
            	log.warn(e.getMessage(), e);
            }
            return true;
            
        }
        return false;
    }

    private JTree getTree(Component invoker) {
        if (invoker instanceof JTree) {
            JTree tree = (JTree) invoker;
            if (tree.getName().equals("treeSite")) {
                return tree;
            }
        }

        return null;
    }
    
    void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
    	
    private void exportHistory(HttpMessage msg, BufferedOutputStream bos) {

        try {
            if (!msg.getResponseHeader().isEmpty()) {

                bos.write(msg.getResponseBody().getBytes());
            }

        } catch (Exception e) {
        	// ZAP: Log exceptions
        	log.warn(e.getMessage(), e);
        }
        
    }
    
    private File getOutputFile(HttpMessage msg) {

        String filename = "";
        try {
            filename = msg.getRequestHeader().getURI().getPath();
            int pos = filename.lastIndexOf("/");
            filename = filename.substring(pos);
        } catch (Exception e) {
        }
        JFileChooser chooser = new JFileChooser(extension.getModel().getOptionsParam().getUserDirectory());
        if (filename.length() > 0) {
            chooser.setSelectedFile(new File(filename));            
        }

		File file = null;
	    int rc = chooser.showSaveDialog(extension.getView().getMainFrame());
	    if(rc == JFileChooser.APPROVE_OPTION) {
    		file = chooser.getSelectedFile();
    		if (file == null) {
    			return file;
    		}

            extension.getModel().getOptionsParam().setUserDirectory(chooser.getCurrentDirectory());

    		return file;
    		
	    }
	    return file;
    }
    

}
