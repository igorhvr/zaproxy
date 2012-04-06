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
package org.zaproxy.zap.extension.brk;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.extension.AbstractPanel;
import org.parosproxy.paros.view.View;
/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class BreakPointsPanel extends AbstractPanel {
	
	private static final long serialVersionUID = 1L;

	public static final String PANEL_NAME = "breakPoints";
	
	private javax.swing.JPanel panelCommand = null;
	private javax.swing.JLabel jLabel = null;
	private JScrollPane jScrollPane = null;
	private javax.swing.JTable breakPointTable = null;
	private BreakPointsTableModel model = new BreakPointsTableModel();
	
	private static final String BRK_TABLE = "brk.table";
	private static final String PREF_COLUMN_WIDTH = "column.width";
	private final Preferences preferences;
	private final String prefnzPrefix = this.getClass().getSimpleName()+".";
	
	private static Logger log = Logger.getLogger(BreakPointsPanel.class);

    /**
     * 
     */
    public BreakPointsPanel() {
        super();
		this.preferences = Preferences.userNodeForPackage(getClass());
		
 		initialize();
    }

    /**
     * @param isEditable
     */
    /*
    public BreakPointsPanel(boolean isEditable) {
        super(isEditable);
 		initialize();
    }
    */
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private  void initialize() {
        this.setLayout(new CardLayout());
        this.setSize(474, 251);
        this.setName(Constant.messages.getString("brk.panel.title"));
		this.setIcon(new ImageIcon(getClass().getResource("/resource/icon/16/101.png")));	// 'red X' icon
        this.add(getPanelCommand(), getPanelCommand().getName());
	}
	/**

	 * This method initializes panelCommand	

	 * 	

	 * @return javax.swing.JPanel	

	 */    
	private javax.swing.JPanel getPanelCommand() {
		if (panelCommand == null) {

			panelCommand = new javax.swing.JPanel();
			panelCommand.setLayout(new java.awt.GridBagLayout());
			panelCommand.setName(Constant.messages.getString("brk.panel.title"));
			
			jLabel = getJLabel();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();

			// Better without this?
			//jLabel.setText("Break Points:");
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(0,0,0,0);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
			

			//panelCommand.add(jLabel, gridBagConstraints1);
			panelCommand.add(getJScrollPane(), gridBagConstraints2);
			
		}
		return panelCommand;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */    
	private javax.swing.JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText(" ");
		}
		return jLabel;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getBreakPoints());
			jScrollPane.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return jScrollPane;
	}

	protected JTable getBreakPoints() {
		if (breakPointTable == null) {
			breakPointTable = new JTable(model);

			breakPointTable.setColumnSelectionAllowed(false);
			breakPointTable.setCellSelectionEnabled(false);
			breakPointTable.setRowSelectionAllowed(true);

			breakPointTable.getColumnModel().getColumn(0).setMinWidth(20);
			breakPointTable.getColumnModel().getColumn(0).setMaxWidth(250);
			breakPointTable.getColumnModel().getColumn(0).setPreferredWidth(restoreColumnWidth(BRK_TABLE, 100));
			breakPointTable.getColumnModel().getColumn(0).addPropertyChangeListener(new ColumnResizedListener(BRK_TABLE));
			
			breakPointTable.getColumnModel().getColumn(1).setResizable(false);

			breakPointTable.getTableHeader().setReorderingAllowed(false);
			
			breakPointTable.setName(PANEL_NAME);
			breakPointTable.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 11));
			breakPointTable.setDoubleBuffered(true);
			breakPointTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			breakPointTable.addMouseListener(new java.awt.event.MouseAdapter() { 
			    public void mousePressed(java.awt.event.MouseEvent e) {

					if (SwingUtilities.isRightMouseButton(e)) {

						// Select table item
					    int row = breakPointTable.rowAtPoint( e.getPoint() );
					    if ( row < 0 || !breakPointTable.getSelectionModel().isSelectedIndex( row ) ) {
					    	breakPointTable.getSelectionModel().clearSelection();
					    	if ( row >= 0 ) {
					    		breakPointTable.getSelectionModel().setSelectionInterval( row, row );
					    	}
					    }
						
						View.getSingleton().getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			        }
			    }
			});
		}
		return breakPointTable;
	}
	
	private void selectRowAndEnsureVisible(int row) {
		if (row != -1) {
			breakPointTable.getSelectionModel().setSelectionInterval(row, row);
			breakPointTable.scrollRectToVisible(breakPointTable.getCellRect(row, 0, true));
		}
	}
	
	private void addBreakPointRow(String url) {
		model.addBreakPoint(url);
		selectRowAndEnsureVisible(model.getLastAddedRow());
	}
	
	private void editBreakPointRow(int row, String url) {
		model.editBreakPointAtRow(row, url);
		selectRowAndEnsureVisible(model.getLastEditedRow());
	}
	
	private void removeBreakPointRow(int row) {
		model.removeBreakPointAtRow(row);
	}
	
	void addBreakPoint(final String url) {
		if (EventQueue.isDispatchThread()) {
			addBreakPointRow(url);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					addBreakPointRow(url);
				}
			});
		} catch (Exception e) {
		}
	    
	}
	
	void editBreakPoint(final int row, final String url) {
		if (EventQueue.isDispatchThread()) {
			editBreakPointRow(row, url);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					editBreakPointRow(row, url);
				}
			});
		} catch (Exception e) {
		}
	    
	}

	public void removeBreakPoint(final int row) {
		if (EventQueue.isDispatchThread()) {
			removeBreakPointRow(row);
			return;
		}
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					removeBreakPointRow(row);
				}
			});
		} catch (Exception e) {
		}
	    
	}

	/**
	 * @param prefix
	 * @param width
	 */
	private final void saveColumnWidth(String prefix, int width) {
		if (width > 0) {
			if (log.isDebugEnabled()) log.debug("Saving preference " + prefnzPrefix+prefix + "." + PREF_COLUMN_WIDTH + "=" + width);
			this.preferences.put(prefnzPrefix+prefix + "." + PREF_COLUMN_WIDTH, Integer.toString(width));
			// immediate flushing
			try {
				this.preferences.flush();
			} catch (final BackingStoreException e) {
				log.error("Error while saving the preferences", e);
			}
		}
	}
	
	/**
	 * @param prefix
	 * @param fallback
	 * @return the width of the column OR fallback value, if there wasn't any preference.
	 */
	private final int restoreColumnWidth(String prefix, int fallback) {
		int result = fallback;
		final String sizestr = preferences.get(prefnzPrefix+prefix + "." + PREF_COLUMN_WIDTH, null);
		if (sizestr != null) {
			int width = 0;
			try {
				width = Integer.parseInt(sizestr.trim());
			} catch (final Exception e) {
				// ignoring, cause is prevented by default values;
			}
			if (width > 0 ) {
				result = width;
				if (log.isDebugEnabled()) log.debug("Restoring preference " + prefnzPrefix+prefix + "." + PREF_COLUMN_WIDTH + "=" + width);
			}
		}
		return result;
	}
	
	private final class ColumnResizedListener implements PropertyChangeListener {

		private final String prefix;
		
		public ColumnResizedListener(String prefix) {
			super();
			assert prefix != null;
			this.prefix = prefix;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TableColumn column = (TableColumn) evt.getSource();
			if (column != null) {
				if (log.isDebugEnabled()) log.debug(prefnzPrefix+prefix + "." + PREF_COLUMN_WIDTH + "=" + column.getWidth());
				saveColumnWidth(prefix, column.getWidth());
			}
		}
		
	}
}
