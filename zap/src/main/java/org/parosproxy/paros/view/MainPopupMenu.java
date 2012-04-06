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
// ZAP: 2012/01/12 Reflected the rename of the class ExtensionPopupMenu to
//      ExtensionPopupMenuItem, added the methods addMenu(ExtensionPopupMenu menu),
//      removeMenu(ExtensionPopupMenu menu), handleMenu and handleMenuItem and
//      changed the method show to use the methods handleMenu and handleMenuItem.
// ZAP: 2012/02/19 Removed the Delete button
// ZAP: 2012/03/03 Moved popups to stdmenus extension

package org.parosproxy.paros.view;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.parosproxy.paros.extension.ExtensionHookMenu;
import org.parosproxy.paros.extension.ExtensionPopupMenuItem;
import org.zaproxy.zap.extension.ExtensionPopupMenu;

public class MainPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -3021348328961418293L;

	private List<JMenuItem> itemList = null;
//	private PopupFindMenu popupFindMenu = null;  //  @jve:decl-index=0:visual-constraint="125,151"
	private PopupDeleteMenu popupDeleteMenu = null;
	private PopupPurgeMenu popupPurgeMenu = null;
	// ZAP: Added support for submenus
    Map<String, JMenu> superMenus = new HashMap<String, JMenu>();
    /**
     * 
     */
    public MainPopupMenu() {
        super();
 		initialize();
   }

    /**
     * @param arg0
     */
    public MainPopupMenu(String arg0) {
        super(arg0);
    }
    
    public MainPopupMenu(List<JMenuItem> itemList) {
        this();
        this.itemList = itemList;
    }
    

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        //this.setVisible(true);
        
	    // added pre-set popup menu here
//        this.add(getPopupFindMenu());
        //this.add(getPopupDeleteMenu());
        this.add(getPopupPurgeMenu());
	}
	
	public synchronized void show(Component invoker, int x, int y) {
	    
	    ExtensionPopupMenuItem menuItem = null;
	    
	    for (int i=0; i<getComponentCount(); i++) {
	        try {
	            if (getComponent(i) != null && getComponent(i) instanceof ExtensionPopupMenuItem) {
	                menuItem = (ExtensionPopupMenuItem) getComponent(i);
	                // ZAP: prevents a NullPointerException when the treeSite doesn't have a node selected and a popup menu option (Delete/Purge) is selected
	                menuItem.setVisible(menuItem.isEnableForComponent(invoker));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    for (int i=0; i<itemList.size(); i++) {
	        if (itemList.get(i) instanceof ExtensionPopupMenuItem) {
	        	handleMenuItem(invoker, (ExtensionPopupMenuItem)itemList.get(i));
	        } else if(itemList.get(i) instanceof ExtensionPopupMenu) {
	        	handleMenu(invoker, (ExtensionPopupMenu)itemList.get(i));
	        }
	    }
	    super.show(invoker, x, y);
	}
	
	private void handleMenuItem(Component invoker, ExtensionPopupMenuItem menuItem) {
		try {
            if (menuItem == ExtensionHookMenu.POPUP_MENU_SEPARATOR) {
                this.addSeparator();
            } else {
	            if (menuItem.isEnableForComponent(invoker)) {		//ForComponent(invoker)) {
	            	if (menuItem.isSubMenu()) {
	            		if (menuItem.precedeWithSeparator()) {
	            			getSuperMenu(menuItem.getParentMenuName(), menuItem.getParentMenuIndex()).addSeparator();
	            		}
	            		getSuperMenu(menuItem.getParentMenuName(), menuItem.getParentMenuIndex()).add(menuItem);
	            		if (menuItem.succeedWithSeparator()) {
	            			getSuperMenu(menuItem.getParentMenuName(), menuItem.getParentMenuIndex()).add(menuItem);
	            		}
	            		
	            	} else {
	            		if (menuItem.precedeWithSeparator()) {
	    	                this.addSeparator();
	            		}
						if (menuItem.getMenuIndex() > menuItem.getComponentCount()) {
							this.add(menuItem);
						} else {
							this.add(menuItem, menuItem.getMenuIndex());
						}
	            		if (menuItem.succeedWithSeparator()) {
	    	                this.addSeparator();
	            		}
	            	}
	            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void handleMenu(Component invoker, ExtensionPopupMenu menu) {
		try {
            if (menu.isEnableForComponent(invoker)) {
            	if (menu.isSubMenu()) {
            		if (menu.precedeWithSeparator()) {
            			getSuperMenu(menu.getParentMenuName(), menu.getParentMenuIndex()).addSeparator();
            		}
            		getSuperMenu(menu.getParentMenuName(), menu.getParentMenuIndex()).add(menu);
            		if (menu.succeedWithSeparator()) {
            			getSuperMenu(menu.getParentMenuName(), menu.getParentMenuIndex()).addSeparator();
            		}
            		
            	} else {
            		if (menu.precedeWithSeparator()) {
    	                this.addSeparator();
            		}

            		this.add(menu, menu.getMenuIndex());
            		if (menu.succeedWithSeparator()) {
    	                this.addSeparator();
            		}
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	// ZAP: Added support for submenus
	private JMenu getSuperMenu (String name, int index) {
		JMenu superMenu = superMenus.get(name);
		if (superMenu == null) {
			superMenu = new JMenu(name);
			superMenus.put(name, superMenu);
			this.add(superMenu, index);
		}
		return superMenu;
		
	}

	/**
	 * This method initializes popupDeleteMenu	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getPopupDeleteMenu() {
		if (popupDeleteMenu == null) {
			popupDeleteMenu = new PopupDeleteMenu();
		}
		return popupDeleteMenu;
	}
	/**
	 * This method initializes popupPurgeMenu	
	 * 	
	 * @return org.parosproxy.paros.view.PopupPurgeMenu	
	 */    
	private PopupPurgeMenu getPopupPurgeMenu() {
		if (popupPurgeMenu == null) {
			popupPurgeMenu = new PopupPurgeMenu();
		}
		return popupPurgeMenu;
	}

	// ZAP: added addMenu and removeMenu to support dynamic changing of menus

	public void addMenu(ExtensionPopupMenuItem menu) {
		itemList.add(menu);
	}
	
	public void removeMenu(ExtensionPopupMenuItem menu) {
		itemList.remove(menu);
	}
	
	public void addMenu(ExtensionPopupMenu menu) {
		itemList.add(menu);
	}
	
	public void removeMenu(ExtensionPopupMenu menu) {
		itemList.remove(menu);
	}
	
}

