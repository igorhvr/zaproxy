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
// ZAP: 2012/04/23 Added @Override annotation to the appropriate method.
package org.parosproxy.paros.db;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableParam extends AbstractTable { 
    
    private static final String PARAMID	= "PARAMID";
    private static final String SITE	= "SITE";
    private static final String TYPE	= "TYPE";
    private static final String NAME	= "NAME";
    private static final String USED	= "USED";
    private static final String FLAGS	= "FLAGS";
    private static final String VALUES	= "VALS";
    
    private PreparedStatement psRead = null;
    private PreparedStatement psInsert1 = null;
    private CallableStatement psInsert2 = null;
    private PreparedStatement psUpdate = null;
    private PreparedStatement psGetAll = null;

    public TableParam() {
        
    }
        
    @Override
    protected void reconnect(Connection conn) throws SQLException {
    	
        ResultSet rs = conn.getMetaData().getTables(null, null, "PARAM", null);
        if ( ! rs.next()) {
        	// Need to create the table
        	
            PreparedStatement stmt = conn.prepareStatement(
            		"CREATE cached TABLE PARAM (paramid bigint generated by default as identity (start with 1), site varchar not null, " +
            		"type varchar not null, name varchar not null, used int not null, flags varchar not null, vals varchar not null)");
            stmt.execute();
        }
        rs.close();

        psRead	= conn.prepareStatement("SELECT * FROM PARAM WHERE " + PARAMID + " = ?");
        
        psInsert1 = conn.prepareStatement("INSERT INTO PARAM (" + SITE + "," + TYPE + "," + NAME + "," + USED + "," + FLAGS + "," + VALUES + ") VALUES (?, ?, ?, ?, ?, ?)");
        psInsert2 = conn.prepareCall("CALL IDENTITY();");

        psUpdate = conn.prepareStatement("UPDATE PARAM SET " +
        		USED + " = ?," + 
        		FLAGS + " = ?," + 
        		VALUES + " = ? " +
        		"WHERE " + PARAMID + " = ?");

        psGetAll = conn.prepareStatement("SELECT * FROM PARAM");

    }
  
	public synchronized RecordParam read(long urlId) throws SQLException {
		psRead.setLong(1, urlId);
		
		ResultSet rs = psRead.executeQuery();
		RecordParam result = build(rs);
		rs.close();
		return result;
	}
	
    public List<RecordParam> getAll () throws SQLException {
    	List<RecordParam> result = new ArrayList<RecordParam>();
    	ResultSet rs = psGetAll.executeQuery();
    	while (rs.next()) {
    		result.add(new RecordParam(rs.getLong(PARAMID), rs.getString(SITE), rs.getString(TYPE),  
    				rs.getString(NAME), rs.getInt(USED), rs.getString(FLAGS), rs.getString(VALUES)));
    	}
    	rs.close();
    	
    	return result;
    }

    public synchronized RecordParam insert(String site, String type, String name, int used, String flags,
			String values) throws SQLException {
    	psInsert1.setString(1, site);
        psInsert1.setString(2, type);
        psInsert1.setString(3, name);
        psInsert1.setInt(4, used);
        psInsert1.setString(5, flags);
        psInsert1.setString(6, values);
        psInsert1.executeUpdate();
        
		ResultSet rs = psInsert2.executeQuery();
		rs.next();
		long id = rs.getLong(1);
		rs.close();
		return read(id);
    }
    
    public synchronized void update(long paramId, int used, String flags,
			String values) throws SQLException {
        psUpdate.setInt(1, used);
        psUpdate.setString(2, flags);
        psUpdate.setString(3, values);
        psUpdate.setLong(4, paramId);
        psUpdate.executeUpdate();
    }
    
    private RecordParam build(ResultSet rs) throws SQLException {
        RecordParam rt = null;
        if (rs.next()) {
            rt = new RecordParam(rs.getLong(PARAMID), rs.getString(SITE), rs.getString(TYPE), 
            		rs.getString(NAME), rs.getInt(USED), rs.getString(FLAGS), rs.getString(VALUES));            
        }
        return rt;
    }

}
