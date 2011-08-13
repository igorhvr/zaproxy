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
package org.zaproxy.zap.extension.ascan;

import javax.swing.tree.DefaultTreeModel;

import org.parosproxy.paros.Constant;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.view.View;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
class AlertTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;
	private int totalInfo = 0;
	private int totalLow = 0;
	private int totalMedium = 0;
	private int totalHigh = 0;
	
    AlertTreeModel() {
        super(new AlertNode(-1, Constant.messages.getString("alerts.tree.title")));	// ZAP: i18n
        
    }
    
    public void recalcAlertCounts() {
    	AlertNode parent = (AlertNode) getRoot();
    	totalInfo = 0;
    	totalLow = 0;
    	totalMedium = 0;
    	totalHigh = 0;
    	if (parent != null) {
            for (int i=0; i<parent.getChildCount(); i++) {
                AlertNode child = (AlertNode) parent.getChildAt(i);
                
            	switch (child.getRisk()) {
            	case Alert.RISK_INFO:
                    View.getSingleton().getMainFrame().getMainFooterPanel().setAlertInfo(++totalInfo);
                    break;
            	case Alert.RISK_LOW:
                    View.getSingleton().getMainFrame().getMainFooterPanel().setAlertLow(++totalLow);
                    break;
            	case Alert.RISK_MEDIUM:
                    View.getSingleton().getMainFrame().getMainFooterPanel().setAlertMedium(++totalMedium);
                    break;
            	case Alert.RISK_HIGH:
                    View.getSingleton().getMainFrame().getMainFooterPanel().setAlertHigh(++totalHigh);
                    break;
            	}
            }
    	}
        View.getSingleton().getMainFrame().getMainFooterPanel().setAlertInfo(totalInfo);
        View.getSingleton().getMainFrame().getMainFooterPanel().setAlertLow(totalLow);
        View.getSingleton().getMainFrame().getMainFooterPanel().setAlertMedium(totalMedium);
        View.getSingleton().getMainFrame().getMainFooterPanel().setAlertHigh(totalHigh);
    }
    
    private String getRiskString (Alert alert) {
    	// Note that the number comments are to ensure the right ordering in the tree :)
    	if (alert.getReliability() == Alert.FALSE_POSITIVE) {
    		return "<html><!--5--><img src=\"" + Constant.OK_FLAG_IMAGE_URL + "\">&nbsp;" + alert.getAlert() + "<html>";
    	}
    	switch (alert.getRisk()) {
    	case Alert.RISK_INFO:
    		return "<html><!--4--><img src=\"" + Constant.INFO_FLAG_IMAGE_URL + "\">&nbsp;" + alert.getAlert() + "<html>";
    	case Alert.RISK_LOW:
    		return "<html><!--3--><img src=\"" + Constant.LOW_FLAG_IMAGE_URL + "\">&nbsp;" + alert.getAlert() + "<html>";
    	case Alert.RISK_MEDIUM:
    		return "<html><!--2--><img src=\"" + Constant.MED_FLAG_IMAGE_URL + "\">&nbsp;" + alert.getAlert() + "<html>";
    	case Alert.RISK_HIGH:
    		return "<html><!--1--><img src=\"" + Constant.HIGH_FLAG_IMAGE_URL + "\">&nbsp;" + alert.getAlert() + "<html>";
        default:
        	return alert.getAlert();
    	}
    }
    
    /**
     * 
     * @param msg
     * @return true if the node is added.  False if not.
     */
    synchronized void addPath(Alert alert) {
        
        AlertNode parent = (AlertNode) getRoot();
        String alertNodeName = getRiskString(alert);
        
        try {
            parent = findAndAddChild(parent, alertNodeName, alert);
            parent = findAndAddLeaf(parent, alert.getUri().toString(), alert);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private AlertNode findLeafNodeForAlert(AlertNode parent, Alert alert) {
        for (int i=0; i<parent.getChildCount(); i++) {
            AlertNode child = (AlertNode) parent.getChildAt(i);
            if (child.getChildCount() == 0) {
            	// Its a leaf node
	        	if (child.getUserObject() != null && 
	        			((Alert)child.getUserObject()).getAlertId() == alert.getAlertId()) {
	        		return child;
	        	}
            } else {
            	// check its children
	        	AlertNode node = findLeafNodeForAlert(child, alert);
	        	if (node != null) {
	        		return node;
	        	}
            }
        }
    	return null;
    }
    
    synchronized void updatePath(Alert originalAlert, Alert alert) {

        AlertNode node = findLeafNodeForAlert((AlertNode) getRoot(), alert);
        if (node != null) {
        	
        	// Remove the old version
        	AlertNode parent = (AlertNode) node.getParent();

        	this.removeNodeFromParent(node);
            nodeStructureChanged(parent);
        	
        	if (parent.getChildCount() == 0) {
        		// Parent has no other children, remove it also
        		this.removeNodeFromParent(parent);
                nodeStructureChanged((AlertNode) this.getRoot());
        	}
        }
        // Add it back in again
        this.addPath(alert);
        recalcAlertCounts();
    }
    
 
    
    private AlertNode findAndAddChild(AlertNode parent, String nodeName, Alert alert) {
        AlertNode result = findChild(parent, nodeName);
        if (result == null) {
            AlertNode newNode = new AlertNode(alert.getRisk(), nodeName);
            if (alert.getReliability() == Alert.FALSE_POSITIVE) {
            	// Special case!
            	newNode.setRisk(-1);
            }
            int pos = parent.getChildCount();
            for (int i=0; i< parent.getChildCount(); i++) {
                if (nodeName.compareToIgnoreCase(parent.getChildAt(i).toString()) <= 0) {
                    pos = i;
                    break;
                }
            }
            insertNodeInto(newNode, parent, pos);
            result = newNode;
            result.setUserObject(alert);
        }
        return result;
    }

    private AlertNode findAndAddLeaf(AlertNode parent, String nodeName, Alert alert) {
        AlertNode result = findLeaf(parent, nodeName, alert);
        
        if (result == null) {
            AlertNode newNode = new AlertNode(alert.getRisk(), nodeName);
            if (alert.getReliability() == Alert.FALSE_POSITIVE) {
            	// Special case!
            	newNode.setRisk(-1);
            }
            int pos = parent.getChildCount();
            for (int i=0; i< parent.getChildCount(); i++) {
            	String childName = ((AlertNode)parent.getChildAt(i)).getNodeName();
                if (nodeName.compareToIgnoreCase(childName) <= 0) {
                    pos = i;
                    break;
                    
                }
            }
            
            insertNodeInto(newNode, parent, pos);
            result = newNode;
            result.setUserObject(alert);
        	this.nodeChanged(newNode);
        }
        recalcAlertCounts();
        return result;
    }

    
    private AlertNode findChild(AlertNode parent, String nodeName) {
        for (int i=0; i<parent.getChildCount(); i++) {
            AlertNode child = (AlertNode) parent.getChildAt(i);
            if (child.getNodeName().equals(nodeName)) {
                return child;
            }
        }
        return null;
    }

    private AlertNode findLeaf(AlertNode parent, String nodeName, Alert alert) {
        for (int i=0; i<parent.getChildCount(); i++) {
            AlertNode child = (AlertNode) parent.getChildAt(i);
            if (child.getNodeName().equals(nodeName)) {
                if (child.getUserObject() == null) {
                    return null;
                }
                
                Alert tmp = (Alert) child.getUserObject();

                if (tmp.getParam().equals(alert.getParam())) {;
                	return child;
                }
            }
        }
        return null;
    }

    public synchronized void deletePath(Alert alert) {

        AlertNode node = findLeafNodeForAlert((AlertNode) getRoot(), alert);
        if (node != null) {
        	
        	// Remove it
        	AlertNode parent = (AlertNode) node.getParent();

        	this.removeNodeFromParent(node);
            nodeStructureChanged(parent);
        	
        	if (parent.getChildCount() == 0) {
        		// Parent has no other children, remove it also
        		this.removeNodeFromParent(parent);
                nodeStructureChanged((AlertNode) this.getRoot());
        	}
        }
        recalcAlertCounts();
    }
    
}
