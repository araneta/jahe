/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aldo
 */
public class KeyGenerator {
    private Connection conn;
    private String keyName;
    private long nextId;
    private long maxId;
    private int incrementBy;
    public KeyGenerator(String keyName,int incrementBy){
	
	this.keyName = keyName;
	this.incrementBy = incrementBy;
	nextId = maxId = 0;
	try{
	    this.conn = ConnectionManager.INSTANCE.getConnection();
	    conn.setAutoCommit(false);
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}
	
    }
    public synchronized Long nextKey(){
	if(nextId==maxId)
	    reserveIds();
	return new Long(nextId++);
    }
    private void reserveIds(){
	PreparedStatement stmt = null;
	ResultSet rs = null;
	long newNextId;
	try{
	    stmt = conn.prepareStatement("select nextID from xkeys where name=? for update");
	    stmt.setString(1,keyName);
	    rs = stmt.executeQuery();
	    rs.next();
	    newNextId = rs.getLong(1);	    
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(rs);
	    close(stmt);
	}
	long newMaxId = newNextId + incrementBy;
	stmt = null;
	try{
	    stmt = conn.prepareStatement("update xkeys set nextid = ? where name = ?");
	    stmt.setLong(1, newMaxId);
	    stmt.setString(2, keyName);
	    stmt.executeUpdate();
	    conn.commit();
	    nextId = newNextId;
	    maxId = newMaxId;
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(rs);
	    close(stmt);
	}
    }
    public void close(PreparedStatement ps) {
	if (ps != null) {
	  try {
	    ps.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
    }
    public void close(ResultSet rs) {
	if (rs != null) {
	  try {
	    rs.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
    }
}
