/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
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

package org.zaproxy.zap.extension.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.parosproxy.paros.Constant;

public class OptionsSessionTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final String[] columnNames = {
				Constant.messages.getString("options.session.label.token")};
    
    private List<String> tokens = new ArrayList<String>();
    
    /**
     * 
     */
    public OptionsSessionTableModel() {
        super();
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return tokens.size();
    }

    public Object getValueAt(int row, int col) {
        return tokens.get(row);
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    public void setValueAt(Object value, int row, int col) {
    	tokens.set(row, (String)value);
        checkAndAppendNewRow();
        fireTableCellUpdated(row, col);
    }

    /**
     * @return Returns the tokens.
     */
    public List<String> getTokens() {
        String auth = null;
        for (int i=0; i<tokens.size();) {
            auth =  tokens.get(i);
            if (auth.equals("")) {
                tokens.remove(i);
                continue;
            }
            i++;
        }
        
        List<String> newList = new ArrayList<String>(tokens);
        return newList;
    }
    /**
     * @param tokens The tokens to set.
     */
    public void setTokens(List<String> tokens) {
		this.tokens = new ArrayList<String>();
    	if (tokens != null) {
    		for (String token : tokens) {
    			if ( ! this.tokens.contains(token)) {
    				// Ensure duplicated removed
    				this.tokens.add(token);
    			}
    		}
    		Collections.sort(this.tokens);
    	}
        checkAndAppendNewRow();
  	  	fireTableDataChanged();
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    private void checkAndAppendNewRow() {
        String auth = null;
        if (tokens.size() > 0) {
            auth =  tokens.get(tokens.size()-1);
            if (!auth.equals("")) {
                auth = "";
                tokens.add(auth);
            }
        } else {
            auth = "";
            tokens.add(auth);
        }
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
        return String.class;
        
    }
    
}
