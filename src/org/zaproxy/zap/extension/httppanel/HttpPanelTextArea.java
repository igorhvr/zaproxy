package org.zaproxy.zap.extension.httppanel;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.parosproxy.paros.network.HttpMessage;
import org.zaproxy.zap.extension.search.SearchMatch;

public class HttpPanelTextArea extends JTextArea {

	private HttpMessage httpMessage;
	private MessageType messageType;
	
	public enum MessageType {
		Header,
		Body,
		Full
	};
	
	
	public HttpPanelTextArea(HttpMessage httpMessage, MessageType messageType) {
		this.httpMessage = httpMessage;
		this.messageType = messageType;
	}

	public MessageType getMessageType() {
		return messageType;
	}
	
	public void setHttpMessage(HttpMessage httpMessage) {
		this.httpMessage = httpMessage;
	}
	
	public HttpMessage getHttpMessage() {
		return httpMessage;
	}
	
	public SearchMatch getTextSelection() {
		SearchMatch sm = null;
		
		if (messageType.equals(MessageType.Header)) {
			sm = new SearchMatch(
					httpMessage,
					SearchMatch.Locations.REQUEST_HEAD, 
					getSelectionStart(),
					getSelectionEnd());
		} else if (messageType.equals(MessageType.Body)) {
			sm = new SearchMatch(
					httpMessage,
					SearchMatch.Locations.REQUEST_BODY, 
					getSelectionStart(),
					getSelectionEnd());
			
		} else if (messageType.equals(MessageType.Full)) {
		}
		
		return sm;
	}
	
}