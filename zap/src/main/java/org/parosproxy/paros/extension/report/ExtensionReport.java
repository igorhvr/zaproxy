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
// ZAP: 2011/11/20 Set order
// ZAP: 2012/03/17 Issue 282 Added getAuthor()

package org.parosproxy.paros.extension.report;

import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.CommandLineArgument;
import org.parosproxy.paros.extension.CommandLineListener;
import org.parosproxy.paros.extension.ExtensionAdaptor;
import org.parosproxy.paros.extension.ExtensionHook;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ExtensionReport extends ExtensionAdaptor implements CommandLineListener {

    private static final int ARG_LAST_SCAN_REPORT_IDX = 0;

    // ZAP: Changed to support XML reports as well
	private JMenuItem menuItemHtmlReport = null;
	private JMenuItem menuItemXmlReport = null;
	private CommandLineArgument[] arguments = new CommandLineArgument[1];
	// ZAP Added logger
	private Logger logger = Logger.getLogger(ExtensionReport.class);

    /**
     * 
     */
    public ExtensionReport() {
        super();
 		initialize();
    }

    /**
     * @param name
     */
    public ExtensionReport(String name) {
        super(name);
        this.setOrder(14);
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        this.setName("ExtensionReport");
			
	}
	public void hook(ExtensionHook extensionHook) {
	    super.hook(extensionHook);
	    if (getView() != null) {
	        //extensionHook.getHookMenu().addNewMenu(getMenuReport());
	        extensionHook.getHookMenu().addReportMenuItem(getMenuItemHtmlReport());
	        extensionHook.getHookMenu().addReportMenuItem(getMenuItemXmlReport());

	    }
        extensionHook.addCommandLine(getCommandLineArguments());

	}

	private JMenuItem getMenuItemHtmlReport() {
		if (menuItemHtmlReport == null) {
			menuItemHtmlReport = new JMenuItem();
			menuItemHtmlReport.setText(Constant.messages.getString("menu.report.html.generate"));	// ZAP: i18n
			menuItemHtmlReport.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    ReportLastScan report = new ReportLastScan();
				    report.generateHtml(getView(), getModel());
	                
				}
			});

		}
		return menuItemHtmlReport;
	}
	
	private JMenuItem getMenuItemXmlReport() {
		if (menuItemXmlReport == null) {
			menuItemXmlReport = new JMenuItem();
			menuItemXmlReport.setText(Constant.messages.getString("menu.report.xml.generate"));
			menuItemXmlReport.addActionListener(new java.awt.event.ActionListener() { 

				public void actionPerformed(java.awt.event.ActionEvent e) {    

				    ReportLastScan report = new ReportLastScan();
				    report.generateXml(getView(), getModel());
	                
				}
			});

		}
		return menuItemXmlReport;
	}
	
    /* (non-Javadoc)
     * @see org.parosproxy.paros.extension.CommandLineListener#execute(org.parosproxy.paros.extension.CommandLineArgument[])
     */
    public void execute(CommandLineArgument[] args) {

        if (arguments[ARG_LAST_SCAN_REPORT_IDX].isEnabled()) {
		    CommandLineArgument arg = arguments[ARG_LAST_SCAN_REPORT_IDX];
            ReportLastScan report = new ReportLastScan();
            String fileName = (String) arg.getArguments().get(0);
            try {
                report.generate(fileName, getModel(), "xml/report.html.xsl");
                System.out.println("Last Scan Report generated at " + fileName);
            } catch (Exception e) {
            	// ZAP: Log the exception
            	logger.error(e.getMessage(), e);
            }
        } else {
            return;
        }

    }

    private CommandLineArgument[] getCommandLineArguments() {
        arguments[ARG_LAST_SCAN_REPORT_IDX] = new CommandLineArgument("-last_scan_report", 1, null, "", "-last_scan_report [file_path]: Generate 'Last Scan Report' into the file_path provided.");
        return arguments;
    }
	
	

	@Override
	public String getAuthor() {
		return Constant.PAROS_TEAM;
	}
}
