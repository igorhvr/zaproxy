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
// ZAP: 2011/08/02 No longer switched on -sp flag
// ZAP: 2012/01/02 Separate param and attack
// ZAP: 2012/04/25 Added @Override annotation to all appropriate methods.

package org.parosproxy.paros.core.scanner.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.parosproxy.paros.core.scanner.AbstractAppParamPlugin;
import org.parosproxy.paros.core.scanner.Alert;
import org.parosproxy.paros.core.scanner.Category;
import org.parosproxy.paros.network.HttpMessage;



/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestInfoPotentialPathManipulation extends AbstractAppParamPlugin {


//    private static Pattern patternFilePath1 = Pattern.compile("\\A((/)[\\w\\.\\-]+)+(/)?\\z", PATTERN_PARAM);
//    private static Pattern patternFilePath2 = Pattern.compile("\\A[\\w\\.\\-]+((/)[\\w\\.\\-]+)+\\z", PATTERN_PARAM);

    private static Pattern[] patternFilePath = {
        Pattern.compile("\\A((/)[\\w\\.\\-~]+)+(/)?\\z", PATTERN_PARAM),
        Pattern.compile("\\A[\\w\\.\\-~]+((/)[\\w\\.\\-~]+)+\\z", PATTERN_PARAM),
        Pattern.compile("\\A((\\\\)[\\w\\.\\-~]+)+(\\\\)?\\z", PATTERN_PARAM),
        Pattern.compile("\\A[\\w\\.\\-]+((\\\\)[\\w\\.\\-~]+)+\\z", PATTERN_PARAM)

    };
    
    

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getId()
     */
    @Override
    public int getId() {
        return 00001;
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getName()
     */
    @Override
    public String getName() {
        return "Potential File Path Manipulation";
    }



    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getDependency()
     */
    @Override
    public String[] getDependency() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getDescription()
     */
    @Override
    public String getDescription() {
        
        String msg = "Possiblely there is a file path in the parameter.  You should try manual manipulation to check if there can be information exposure such as system files or program source code.  In that case the risk would be high.";
        return msg;
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getCategory()
     */
    @Override
    public int getCategory() {
        return Category.INFO_GATHER;
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getSolution()
     */
    @Override
    public String getSolution() {
        return "Make sure the file path parameter cannot be manipulated to read arbitrary files.  Restrict access to intended files only.";
        
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.Plugin#getReference()
     */
    @Override
    public String getReference() {
        return "";
        
    }

    /* (non-Javadoc)
     * @see com.proofsecure.paros.core.scanner.AbstractPlugin#init()
     */
    @Override
    public void init() {
 
    }
    
    @Override
    public void scan(HttpMessage msg, String param, String value) {

		
		// always try normal query first

//        HttpMessage newMsg = getNewMsg();
        String query = setParameter(msg, param, value);
        String matchedFilePath = null;
        Matcher matcher = null;
        
        for (int i=0;i<patternFilePath.length;i++) {
            matcher = patternFilePath[i].matcher(value);
            if (matcher.find()) {
                matchedFilePath = matcher.group(0);
                bingo(Alert.RISK_INFO, Alert.SUSPICIOUS, "", param, value, matchedFilePath , msg);
                return;
            }
        }
        
//        matcher = patternFilePath2.matcher(value);
//        if (matcher.find()) {
//            matchedFilePath = matcher.group(0);
//            bingo(Alert.RISK_INFO, Alert.SUSPICIOUS, "", (query == null || query.length() == 0)? "nil" : query, matchedFilePath , msg);
//            
//        }
        

		
	}
}
