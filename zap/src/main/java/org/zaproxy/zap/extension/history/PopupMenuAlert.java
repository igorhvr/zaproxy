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
package org.zaproxy.zap.extension.history;

import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.zaproxy.zap.view.PopupMenuHistoryReference;


public class PopupMenuAlert extends PopupMenuHistoryReference {

	private static final long serialVersionUID = 1L;
    private ExtensionHistory extension = null;

    /**
     * @param label
     */
    public PopupMenuAlert(String label) {
        super(label);
    }
	
	@Override
	public void performAction(HistoryReference href) throws Exception {
		extension.showAlertAddDialog(href);
	}

	public void setExtension(ExtensionHistory extension) {
		this.extension = extension;
	}

	@Override
	public boolean isEnableForInvoker(Invoker invoker) {
		switch (invoker) {
		case alerts:
			return false;
		case sites:
		case history:
		case ascan:
		case search:
		case fuzz:
		case bruteforce:
		default:
			return true;
		}
	}

	
}
