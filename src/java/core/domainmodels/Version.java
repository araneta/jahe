/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.domainmodels;

import core.app.AppSessionManager;
import core.db.ConnectionManager;
import core.db.DB;
import core.db.KeyGenerator;
import core.helpers.TimeHelper;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;

/**
 *
 * @author aldo
 */
public class Version {
    private String keyname = "version";
    private Long id;
    private long value;
    private String modifiedBy;
    private Timestamp modified;
    private boolean locked;
    private boolean isNew;
    private static final String UPDATE_SQL = "Update version set value = ?, modifiedBy=?, modified=? "
	    + " where id=? and value=?";
    private static final String DELETE_SQL = "delete from version where id=? and value=?";
    private static final String INSERT_SQL = "insert into version values(?, ?, ?, ?)";
    private static final String LOAD_SQL = "select id, value, modifiedBy, modified from version"
	    + " where id=?";
    public Long getId(){
	return this.id;
    }
    private boolean isNew(){
	return this.isNew;
    }
    private boolean isLocked(){
	return this.locked;
    }
    public long getValue(){
	return this.value;
    }
    private String getModifiedBy(){
	return this.modifiedBy;
    }
    private Timestamp getModified(){
	return this.modified;
    }
    public void setModified(Timestamp t){
	this.modified = t;
    }
    public void setModifiedBy(String s){
	this.modifiedBy = s;
    }
    public Version(Long id, Long value,String modifiedBy,Timestamp modified){
	this.id = id;
	this.value = value;
	this.modifiedBy = modifiedBy;
	this.modified = modified;
    }    
    public static Version find(Long id){
	Version version = null;
	//version = AppSessionManager.getSession().getIdentityMap().getVersion(id);
	if(version==null){
	    version = load(id);
	}
	return version;
    }
    private static Version load(Long id){
	ResultSet rs = null;
	Connection conn = null;
	PreparedStatement pstmt = null;
	Version version = null;
	try{
	    conn = ConnectionManager.INSTANCE.getConnection();
	    pstmt = conn.prepareStatement(LOAD_SQL);
	    pstmt.setLong(1, id.longValue());
	    rs = pstmt.executeQuery();
	    if(rs.next()){
		long value = rs.getLong(2);
		String modifiedBy = rs.getString(3);
		Timestamp modified = rs.getTimestamp(4);
		version = new Version(id,value,modifiedBy,modified);
		//AppSessionManager.getSession().getIdentityMap().putVersion(version);
	    }else
	    {
		throw new RuntimeException("Version "+id+" not found");
	    }		
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    if (pstmt != null) {
	      try {
		pstmt.close();
	      } catch (Exception excp) {
		// Ignore, we don't care, we do care when we open it
	      }
	    }
	    if (conn != null) {
	      try {
		conn.close();
	      } catch (Exception excp) {
		// Ignore, we don't care, we do care when we open it
	      }
	    }
	}
	return version;	
    }
    protected static Long getNextId(){
	
	KeyGenerator keygen = new KeyGenerator("version",1);
	return keygen.nextKey();
	
	    
    } 	
    public static Version create(){	
	Version version = new Version(getNextId(),Long.valueOf(0),AppSessionManager.getSession().getUser(),TimeHelper.UTCNow());
	version.isNew = true;
	return version;
    }
    
    public void insert(){
	if(isNew()){
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    try{
		conn = AppSessionManager.getSession().getConnTrans();
		pstmt = conn.prepareStatement(INSERT_SQL);
		pstmt.setLong(1, this.getId().longValue());
		pstmt.setLong(2, this.getValue());
		pstmt.setString(3, this.getModifiedBy());
		pstmt.setTimestamp(4, this.getModified());
		pstmt.executeUpdate();
		//AppSessionManager.getSession().getIdentityMap().putVersion(this);
		isNew = false;
			
	    }catch(SQLException e){
		throw new RuntimeException(e);
	    }finally{
		close(pstmt);
	    }
	}
    }
    public void increment(){
	///if(!isLocked()){
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    try{
		conn = AppSessionManager.getSession().getConnTrans();
		pstmt = conn.prepareStatement(UPDATE_SQL);
		pstmt.setLong(1, value+1);
		pstmt.setString(2, getModifiedBy());
		pstmt.setTimestamp(3, getModified());
		pstmt.setLong(4, id.longValue());
		pstmt.setLong(5, value);
		int rowCount = pstmt.executeUpdate();
		if(rowCount==0){
		    throwConcurencyException();
		}
		value++;
		locked = true;
	    }catch(SQLException e){
		throw new RuntimeException(e);
	    }finally{
		close(pstmt);
	    }
	//}
    }
    public void delete(){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = AppSessionManager.getSession().getConnTrans();
            pstmt = conn.prepareStatement(DELETE_SQL);
            pstmt.setLong(1, id);
            int rowCount = pstmt.executeUpdate();
            if(rowCount==0){
                throwConcurencyException();
            }
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            close(pstmt);
        }
    }
    private void throwConcurencyException(){
	Version currentVersion = load(this.getId());
	throw new RuntimeException("version modified by "+currentVersion.modifiedBy+" at "+
		DateFormat.getDateTimeInstance().format(currentVersion.getModified())
		);
    }

     //clean up
    public void close(ResultSet rs) {
	if (rs != null) {
	  try {
	    rs.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
      }


      /**
       * Closes given PreparedStatement set hard
       */
      public void close(PreparedStatement ps) {
	if (ps != null) {
	  try {
	    ps.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
      }


      /**
       * Closes given Connection set hard
       */
      public void close(Connection cn) {
	if (cn != null) {
	  try {
	    cn.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
      }


      /**
       * Closes given Statement set hard
       */
      public void close(Statement stmt) {
	if (stmt != null) {
	  try {
	    stmt.close();
	  } catch (Exception excp) {
	    // Ignore, we don't care, we do care when we open it
	  }
	}
      }


      public void close(ResultSet rs, PreparedStatement ps, Connection conn) {
	close(rs);
	close(ps);    
	close(conn);
      }
}
