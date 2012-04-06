package org.zaproxy.zap.view;

import java.awt.Color;

import org.zaproxy.zap.extension.search.SearchMatch;

/*
 * A search entry, which allows to highlight all occurences of a 
 * string in a textarea.
 * 
 * Consists of:
 *   search value ("__ZAP__", ...)
 *   search type (Request Head, Body, ...) [not used yet]
 *   highlight color
 *   status enabled or not
 *   
 * The UI which wants to implement highlights has to find the strings
 * for itself.
 * 
 * TODO:
 * Add Regex or similar, not only 
 */
public class HighlightSearchEntry {

	private String token;
	private SearchMatch.Location type;
	private Color color;
	private boolean active;
	
	public HighlightSearchEntry(String token, SearchMatch.Location type, Color color, boolean active) {
		this.token = token;
		this.type = type;
		this.color = color;
		this.active = active;
	}
	
	public HighlightSearchEntry() {
		this.token = "";
		this.type = SearchMatch.Location.REQUEST_HEAD;
		this.color = Color.red;
		this.active = true;
	}
	
	public Boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getToken() {
		return token;
	}
	
	public SearchMatch.Location getLocation() {
		return type;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setToken(String text) {
		this.token = text;
	}
	
	public void setType(SearchMatch.Location type) {
		this.type = type;
	}
	
	public SearchMatch.Location getType() {
		return type;
	}
}
