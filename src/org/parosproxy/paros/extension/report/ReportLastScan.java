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
// ZAP: 2011/10/01 Fixed filename problem (issue 161)

package org.parosproxy.paros.extension.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.db.Database;
import org.parosproxy.paros.db.RecordAlert;
import org.parosproxy.paros.db.RecordScan;
import org.parosproxy.paros.extension.ViewDelegate;
import org.parosproxy.paros.model.Model;
import org.parosproxy.paros.view.View;

import edu.stanford.ejalbert.BrowserLauncher;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ReportLastScan {

	private Logger logger = Logger.getLogger(ReportLastScan.class);
    
    public ReportLastScan() {
        
    }

    

    private String getAlertXML(Database db, RecordScan recordScan) throws SQLException {

        Connection conn = null;
        PreparedStatement psAlert = null;
        StringBuffer sb = new StringBuffer();
        
        // prepare table connection
        try {
            conn = db.getDatabaseServer().getNewConnection();
            conn.setReadOnly(true);
            // ZAP: Changed to read all alerts and order by risk
            psAlert = conn.prepareStatement("SELECT ALERT.ALERTID FROM ALERT ORDER BY RISK, PLUGINID");
            //psAlert = conn.prepareStatement("SELECT ALERT.ALERTID FROM ALERT JOIN SCAN ON ALERT.SCANID = SCAN.SCANID WHERE SCAN.SCANID = ? ORDER BY PLUGINID");
            //psAlert.setInt(1, recordScan.getScanId());
            psAlert.executeQuery();
            ResultSet rs = psAlert.getResultSet();

            RecordAlert recordAlert = null;
            Alert alert = null;
            Alert lastAlert = null;

            StringBuffer sbURLs = new StringBuffer(100);
            String s = null;
            
            // get each alert from table
            while (rs.next()) {
                int alertId = rs.getInt(1);
                recordAlert = db.getTableAlert().read(alertId);
                alert = new Alert(recordAlert);
                
                // ZAP: Ignore false positives
                if (alert.getReliability() == Alert.FALSE_POSITIVE) {
                	continue;
                }

                if (lastAlert != null && 
                		(alert.getPluginId() != lastAlert.getPluginId() ||
                				alert.getRisk() != lastAlert.getRisk())) {
                    s = lastAlert.toPluginXML(sbURLs.toString());
                    sb.append(s);
                    sbURLs.setLength(0);
                }

                s = alert.getUrlParamXML();
                sbURLs.append(s);

                lastAlert = alert;

            }
            rs.close();

            if (lastAlert != null) {
                sb.append(lastAlert.toPluginXML(sbURLs.toString()));
            }
                

            
        } catch (SQLException e) {
        	logger.error(e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.close();
            }
            
        }
        
        //exit
        return sb.toString();
    }
    
    public File generate(String fileName, Model model, String xslFile) throws Exception {
        
	    StringBuffer sb = new StringBuffer(500);
	    // ZAP: Dont require scan to have been run

	    sb.append("<?xml version=\"1.0\"?>");
	    sb.append("<report>\r\n");
	    sb.append("Report generated at " + ReportGenerator.getCurrentDateTimeString() + ".\r\n");
	    sb.append(getAlertXML(model.getDb(), null));
	    sb.append("</report>");	
	    
	    File report = ReportGenerator.stringToHtml(sb.toString(), xslFile, fileName);
	    
	    
	    return report;
    }
    
	public void generateHtml(ViewDelegate view, Model model) {		

	    // ZAP: Allow scan report file name to be specified
	    try{
		    JFileChooser chooser = new JFileChooser(Model.getSingleton().getOptionsParam().getUserDirectory());
		    chooser.setFileFilter(new FileFilter() {
		           public boolean accept(File file) {
		                if (file.isDirectory()) {
		                    return true;
		                } else if (file.isFile() && 
		                		file.getName().toLowerCase().endsWith(".htm")) {
		                    return true;
		                } else if (file.isFile() && 
		                		file.getName().toLowerCase().endsWith(".html")) {
		                    return true;
		                }
		                return false;
		            }
		           public String getDescription() {
		               return Constant.messages.getString("file.format.html");
		           }
		    });
		    
			File file = null;
		    int rc = chooser.showSaveDialog(View.getSingleton().getMainFrame());
		    if(rc == JFileChooser.APPROVE_OPTION) {
	    		file = chooser.getSelectedFile();
	    		if (file != null) {
		            Model.getSingleton().getOptionsParam().setUserDirectory(chooser.getCurrentDirectory());
		    		String fileNameLc = file.getAbsolutePath().toLowerCase();
		    		if (! fileNameLc.endsWith(".htm") &&
		    				! fileNameLc.endsWith(".html")) {
		    		    file = new File(file.getAbsolutePath() + ".html");
		    		}
	    		}
	    		
	    		if (! file.getParentFile().canWrite()) {
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.write.error"),
									new Object[] {file.getAbsolutePath()}));
					return;
	    		}
    		
	    		File report = generate(file.getAbsolutePath(), model, "xml/report.html.xsl");
	    		if (report == null) {
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.unknown.error"),
									new Object[] {file.getAbsolutePath()}));
	    		    return;
	    		}
	    		
	    		try {
					BrowserLauncher bl = new BrowserLauncher();
					bl.openURLinBrowser("file://" + report.getAbsolutePath());
				} catch (Exception e) {
		        	logger.error(e.getMessage(), e);
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.complete.warning"),
									new Object[] {report.getAbsolutePath()}));
				}
		    }
  			
    	} catch (Exception e){
        	logger.error(e.getMessage(), e);
      		view.showWarningDialog("File creation error."); 
    	}
	}
	
	public void generateXml(ViewDelegate view, Model model) {		

	    // ZAP: Allow scan report file name to be specified
	    try{
		    JFileChooser chooser = new JFileChooser(Model.getSingleton().getOptionsParam().getUserDirectory());
		    chooser.setFileFilter(new FileFilter() {
		           public boolean accept(File file) {
		                if (file.isDirectory()) {
		                    return true;
		                } else if (file.isFile() && 
		                		file.getName().toLowerCase().endsWith(".xml")) {
		                    return true;
		                }
		                return false;
		            }
		           public String getDescription() {
		               return Constant.messages.getString("file.format.xml");
		           }
		    });
		    
			File file = null;
		    int rc = chooser.showSaveDialog(View.getSingleton().getMainFrame());
		    if(rc == JFileChooser.APPROVE_OPTION) {
	    		file = chooser.getSelectedFile();
	    		if (file != null) {
		            Model.getSingleton().getOptionsParam().setUserDirectory(chooser.getCurrentDirectory());
		    		String fileNameLc = file.getAbsolutePath().toLowerCase();
		    		if (! fileNameLc.endsWith(".xml")) {
		    		    file = new File(file.getAbsolutePath() + ".xml");
		    		}
	    		}

	    		if (! file.getParentFile().canWrite()) {
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.write.error"),
									new Object[] {file.getAbsolutePath()}));
					return;
	    		}

	    		File report = generate(file.getAbsolutePath(), model, "xml/report.xml.xsl");
	    		if (report == null) {
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.unknown.error"),
									new Object[] {file.getAbsolutePath()}));
	    		    return;
	    		}
	    		
	    		try {
					BrowserLauncher bl = new BrowserLauncher();
					bl.openURLinBrowser("file://" + report.getAbsolutePath());
				} catch (Exception e) {
		        	logger.error(e.getMessage(), e);
					view.showMessageDialog(
							MessageFormat.format(Constant.messages.getString("report.complete.warning"),
									new Object[] {report.getAbsolutePath()}));
				}
		    }
  			
    	} catch (Exception e){
        	logger.error(e.getMessage(), e);
      		view.showWarningDialog(Constant.messages.getString("report.unexpected.warning")); 
    	}
	}
}
