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
package org.zaproxy.zap.extension.stdmenus;

import org.parosproxy.paros.control.Control;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.extension.manualrequest.ManualRequestEditorDialog;
import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.view.PopupMenuHttpMessage;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PopupMenuResendMessage extends PopupMenuHttpMessage {

	private static final long serialVersionUID = 1L;
    private ExtensionHistory extension = null;

    /**
     * @param label
     */
    public PopupMenuResendMessage(String label) {
        super(label);
    }
    
    private ExtensionHistory getExtensionHistory() {
    	if (extension == null) {
    		extension = (ExtensionHistory) Control.getSingleton().getExtensionLoader().getExtension(ExtensionHistory.NAME);
    	}
    	return extension;
    }
	
	@Override
	public void performAction(HttpMessage msg) throws Exception {
	    ManualRequestEditorDialog dialog = getExtensionHistory().getResendDialog();
	    
    	// Dont clone. ManualRequestEditor will do it.
        dialog.setMessage(msg);
        dialog.setVisible(true);
	}

	public void setExtension(ExtensionHistory extension) {
		this.extension = extension;
	}

	@Override
	public boolean isEnableForInvoker(Invoker invoker) {
		return getExtensionHistory() != null;
	}

	
}
