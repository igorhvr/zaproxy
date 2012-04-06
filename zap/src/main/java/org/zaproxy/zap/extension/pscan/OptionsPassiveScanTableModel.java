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
package org.zaproxy.zap.extension.pscan;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.parosproxy.paros.Constant;
import org.zaproxy.zap.extension.pscan.scanner.RegexAutoTagScanner;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OptionsPassiveScanTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] columnNames = {
		Constant.messages.getString("pscan.options.table.name"),
		Constant.messages.getString("pscan.options.table.type"),
		Constant.messages.getString("pscan.options.table.enabled")};
    
	private List <RegexAutoTagScanner> defns = new ArrayList<RegexAutoTagScanner>();
    //private Vector listAuth = new Vector();
    
    /**
     * 
     */
    public OptionsPassiveScanTableModel() {
        super();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 3;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return defns.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int col) {
    	RegexAutoTagScanner defn = defns.get(row);
        Object result = null;
        switch (col) {
        	case 0:	result = defn.getName();
        			break;
        	case 1: result = defn.getType();
        			break;
        	case 2: result = defn.isEnabled() ? "Y" : "N";
					break;
        	default: result = "";
        }
        return result;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    /**
     * @param listAuth The listAuth to set.
     */
    public void setScanDefns(List <RegexAutoTagScanner> defns) {
        this.defns = new ArrayList<RegexAutoTagScanner>(defns);
  	  	fireTableDataChanged();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
	public Class<String> getColumnClass(int c) {
        return String.class;
    }
    
}
