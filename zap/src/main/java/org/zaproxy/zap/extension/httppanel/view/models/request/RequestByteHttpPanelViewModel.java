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
package org.zaproxy.zap.extension.httppanel.view.models.request;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.zaproxy.zap.extension.httppanel.view.models.AbstractByteHttpPanelViewModel;

public class RequestByteHttpPanelViewModel extends AbstractByteHttpPanelViewModel {
	
	private static Logger log = Logger.getLogger(RequestByteHttpPanelViewModel.class);

	@Override
	public byte[] getData() {
		if (httpMessage == null)  {
			return new byte[0];
		}
		
		byte[] headerBytes = httpMessage.getRequestHeader().toString().getBytes();
		byte[] bodyBytes = httpMessage.getRequestBody().getBytes();
		
		byte[] bytes = new byte[headerBytes.length + bodyBytes.length];
		
		System.arraycopy(headerBytes, 0, bytes, 0, headerBytes.length);
		System.arraycopy(bodyBytes, 0, bytes, headerBytes.length, bodyBytes.length);
		
		return bytes;
	}

	@Override
	public void setData(byte[] data) {
		int pos = findHeaderLimit(data);
		
		if (pos == -1) {
			log.warn("Could not Save Header, limit not found. Header: " + new String(data));
			return;
		}
		
		try {
			httpMessage.setRequestHeader(new String(data, 0, pos));
		} catch (HttpMalformedHeaderException e) {
			log.warn("Could not Save Header: " + new String(data, 0, pos), e);
		}
		
		httpMessage.getRequestBody().setBody(ArrayUtils.subarray(data, pos, data.length));
	}
	
	private int findHeaderLimit(byte[] data) {
		boolean lastIsCRLF = false;
		boolean lastIsCR = false;
		boolean lastIsLF = false;
		int pos = -1;
		
		for(int i = 0; i < data.length; ++i) {
			if (!lastIsCR && data[i] == '\r') {
				lastIsCR = true;
			} else if (!lastIsLF && data[i] == '\n') {
				if (lastIsCRLF) {
					pos = i;
					break;
				}
				
				lastIsCRLF = true;
				lastIsCR = false;
				lastIsLF = false;
			} else {
				lastIsCR = false;
				lastIsLF = false;
				lastIsCRLF = false;
			}
		}
		
		return pos;
	}

}
