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
package org.zaproxy.zap.model;

import javax.swing.ListModel;

import org.parosproxy.paros.model.SiteNode;


public interface GenericScanner extends Runnable {

	public void stopScan();

	public boolean isStopped();

	public String getSite();
	
	public int getProgress ();

	public int getMaximum ();
	
	public void pauseScan();

	public void resumeScan();
	
	public boolean isPaused();
	
	public boolean isAlive();
	
	public void start();

	public SiteNode getStartNode();
	
	public void setStartNode(SiteNode startNode);
	
	public ListModel getList();

	public void reset();

}
