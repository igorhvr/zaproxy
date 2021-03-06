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
// ZAP: 2011/04/16 i18n
// ZAP: 2012/04/25 Added type arguments to generic types, removed unnecessary
// casts, removed unused variable and added @Override annotation to all
// appropriate methods.

package org.parosproxy.paros.extension.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.URI;
import org.parosproxy.paros.Constant;
import org.parosproxy.paros.network.HttpMessage;
import org.parosproxy.paros.network.HttpRequestHeader;


/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FilterLogGetQuery extends FilterAdaptor {

    private static final String delim = "\t";   
    private static final String CRLF = "\r\n";
    private File outFile;		    
    private Pattern pSeparator	= Pattern.compile("([^=&]+)[=]([^=&]*)"); 
    private Matcher matcher2;
    private BufferedWriter writer = null;
    private long lastWriteTime = System.currentTimeMillis();
    
    /* (non-Javadoc)
     * @see com.proofsecure.paros.extension.filter.AbstractFilter#getId()
     */
    @Override
    public int getId() {
        return 20;
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.extension.filter.AbstractFilter#getName()
     */
    @Override
    public String getName() {
        return Constant.messages.getString("filter.loggets.name") + getLogFileName();
        
    }

    public void init() {
     	outFile = new File(getLogFileName());
     	
    }

    protected String getLogFileName() {
        return "filter/get.xls";
    }
    
    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.proxy.ProxyListener#onHttpRequestSend(com.proofsecure.paros.network.HttpMessage)
     */
    @Override
    public void onHttpRequestSend(HttpMessage httpMessage) {

        HttpRequestHeader reqHeader = httpMessage.getRequestHeader();
        
        if (reqHeader != null && reqHeader.isText() && !reqHeader.isImage()){
            if (reqHeader.getMethod().equalsIgnoreCase(HttpRequestHeader.GET)){
                try{
                    
                    URI uri = reqHeader.getURI();
                    
                    // ZAP: Removed unused variable (int pos).
                    
                    String firstline;
                    
                    URI newURI = (URI) uri.clone();
                    String query = newURI.getQuery();
                    if (query != null) {
                        newURI.setQuery(null);
                        firstline = newURI.toString();
                        // ZAP: Added type arguments.
                        Hashtable<String, String> param = parseParameter(query);
                        writeLogFile(firstline,param);
                    } else {
                        firstline = uri.toString();
                        writeLogFile(firstline,null);				
                    }
                    
                    
                    
                }catch(Exception aa){
                    aa.printStackTrace();
                }
            }
            
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.proxy.ProxyListener#onHttpResponseReceive(com.proofsecure.paros.network.HttpMessage)
     */
    @Override
    public void onHttpResponseReceive(HttpMessage httpMessage) {
        
    }
    
    // ZAP: Added type arguments.
    protected synchronized void writeLogFile(String line, Hashtable<String, String> param){
        // write to default file
        try{
            
            if (getWriter() != null) {
                
                getWriter().write(line + CRLF);
            }
            
            if (param!=null){
                // ZAP: Added type argument.
                Enumeration<String> v = param.keys();
                while (v.hasMoreElements()) {
                    // ZAP: Removed unnecessary cast.
                    String name = v.nextElement();
                    // ZAP: Removed unnecessary cast.
                    String value = param.get(name);
                    getWriter().write(delim + name + delim + value + CRLF);		        		           
                }    		
            }

            lastWriteTime = System.currentTimeMillis();
            
        }catch(IOException ae){
        }
        
    }
    
    // ZAP: Added type arguments.
    protected Hashtable<String, String> parseParameter(String param){
        // ZAP: Added type arguments.
        Hashtable<String, String> table = new Hashtable<String, String>();
        
        try{	  
            matcher2 = pSeparator.matcher(param);
            while (matcher2.find()){
                // start of a request
                table.put(matcher2.group(1), matcher2.group(2));
                
            }
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return table;
        
    }
    
    @Override
    public synchronized void timer() {
        // 5s elapse and no more write.  close file.
        if (writer != null && System.currentTimeMillis() > lastWriteTime + 5000) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
            }            
        }
    }
    
    private synchronized BufferedWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new BufferedWriter(new FileWriter(outFile,true));            
        }
        return writer;
    }
    
}
