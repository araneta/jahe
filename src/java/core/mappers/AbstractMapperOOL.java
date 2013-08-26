/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

import core.db.ConnectionManager;
import core.db.KeyGenerator;
import core.domainmodels.DomainObjectOOL;
import core.helpers.AppSessionManager;
import core.helpers.Version;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aldo
 */
public abstract class AbstractMapperOOL {
    protected String keyname;
    protected String table;
    protected String[] columns;
    protected String loadSQL;    
    protected String deleteSQL;    
    protected String checkVersionSQL;
    
    
   
    protected String insertStatement(){
	StringBuilder sb = new StringBuilder();
	sb.append("insert into ");
	sb.append(table);
	sb.append("(");
	for (int i=0;i<columns.length;i++) {
	    sb.append(columns[i]);	    	    
	    sb.append(",");
	}
	sb.append("createdBy,created,versionid) values(");
	for (int i=0;i<columns.length;i++) {
	    sb.append("?,");	    	    
	}	
	sb.append("?,?,?)");
	return sb.toString();
    }
    
    protected String updateStatement(){
	StringBuilder sb = new StringBuilder();
	sb.append("update ");
	sb.append(table);
	sb.append(" set ");
	for (int i=1;i<columns.length;i++) {//skip id col
	    sb.append(columns[i]);
	    sb.append("=?,");
	}
	//sb.setLength(sb.length()-1);
	sb.append(" modifiedBy=?");
	sb.append(", modified=?");
	sb.append(", versionid=?");
	
	sb.append(" where id=? and versionid=?");
	return sb.toString();
    }
    
    protected Long getNextId(){	
	KeyGenerator keygen = new KeyGenerator(keyname,1);
	return keygen.nextKey();
	    
    }
    protected String getColumns(){
	StringBuilder sb = new StringBuilder();
	String delim = "";
	for (int i=0;i<columns.length;i++) {
	    sb.append(delim).append(columns[i]);
	    delim = ",";
	}
	return sb.toString();
    }
    public AbstractMapperOOL(String keyname, String table, String[] columns){
	this.keyname = keyname;
	this.table = table;
	this.columns =columns;
	
	buildStatements();
    }
    private void buildStatements(){
	loadSQL = "Select * from " + table +" where id=?";
	deleteSQL = "Select * from " + table +" where id=?";
	checkVersionSQL = "select version, modifiedby, modified from " + table +" where id =?";
    }
    protected abstract DomainObjectOOL doLoad(Long id,ResultSet rs)throws SQLException;
    protected abstract void doInsert(DomainObjectOOL subject, PreparedStatement stmt) throws SQLException;    
    protected abstract void doUpdate(DomainObjectOOL subject, PreparedStatement stmt) throws SQLException;    
    
    public DomainObjectOOL find(Long id){
	DomainObjectOOL obj=null;
	//identity map disabled to test optimistic offline lock
	//obj = AppSessionManager.getSession().getIdentityMap().get(this.keyname
	//	,id);
	//if(obj == null){
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try{
		conn = ConnectionManager.INSTANCE.getConnection();
		//conn = getConnection();
		stmt = conn.prepareStatement(loadSQL);
		stmt.setLong(1, id.longValue());
		rs = stmt.executeQuery();
		if(rs.next()){
		    obj = load(rs);
		    
		}
	    }catch(SQLException e){
		throw new RuntimeException(e);
	    }finally{		
		close(rs,stmt,conn);
		
	    }
	//}
	return obj;
    }
    public Long insert(DomainObjectOOL subject){
	
	PreparedStatement insertStatement = null;
	Connection conn = null;
	try{
	    //conn = ConnectionManager.INSTANCE.getConnection();
	    conn = AppSessionManager.getSession().getConnTrans();
	    subject.setSystemFields(Version.create(), now(), AppSessionManager.getSession().getUser());
	    subject.getVersion().insert();
	    String sql = insertStatement();
	    System.out.println("uuh"+sql);
	    
	   insertStatement = conn.prepareStatement(sql);
	   subject.setID(getNextId());
	   insertStatement.setLong(1, subject.getID());
	   insertStatement.setString(columns.length+1,AppSessionManager.getSession().getUser());
	   insertStatement.setTimestamp(columns.length + 2, now());	   
	   insertStatement.setLong(columns.length + 3, subject.getVersion().getId());	   
	  
	   doInsert(subject,insertStatement);
	   insertStatement.execute();
	   AppSessionManager.getSession().getIdentityMap().put(this.keyname, subject);
	   return subject.getID();
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(insertStatement);
	}
    }
    public void update(DomainObjectOOL subject){	
	
	PreparedStatement updateStatement = null;
	Connection conn = null;
	try{
	    //conn = ConnectionManager.INSTANCE.getConnection();
	    conn = AppSessionManager.getSession().getConnTrans();
	    long versionid = subject.getVersion().getId();
	    Version v = subject.getVersion();
	    v.setModified(now());
	    v.setModifiedBy(AppSessionManager.getSession().getUser());
	    v.increment();
	    String sql = updateStatement();
	   updateStatement = conn.prepareStatement(sql);	   	   	   
	   updateStatement.setString(columns.length,AppSessionManager.getSession().getUser());
	   updateStatement.setTimestamp(columns.length + 1, now());
	   updateStatement.setLong(columns.length + 2, subject.getVersion().getId());	   
	   updateStatement.setLong(columns.length + 3, subject.getID());	
	   updateStatement.setLong(columns.length + 4, versionid);	
	   doUpdate(subject,updateStatement);
	   updateStatement.execute();
	   AppSessionManager.getSession().getIdentityMap().put(this.keyname, subject);	   
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(updateStatement);
	}
    }
    public void delete(DomainObjectOOL object){
	Connection conn = null;
	PreparedStatement stmt = null;
	try{
	    //conn = ConnectionManager.INSTANCE.getConnection();
	    conn = AppSessionManager.getSession().getConnTrans();
	    object.getVersion().increment();
	    AppSessionManager.getSession().getIdentityMap().remove(this.keyname,object.getID());
	
	    stmt = conn.prepareStatement(deleteSQL);
	    stmt.setLong(1, object.getID().longValue());
	    int rowCount = stmt.executeUpdate();
	    if(rowCount==0){
		throwConcurencyException(object);
	    }
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(stmt);
	}
    }
    protected void throwConcurencyException(DomainObjectOOL object) throws SQLException{
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try{
	    //conn = ConnectionManager.INSTANCE.getConnection();
	    conn = AppSessionManager.getSession().getConnTrans();
	    stmt = conn.prepareStatement(checkVersionSQL);
	    stmt.setInt(1,(int)object.getID().longValue());
	    rs = stmt.executeQuery();
	    if(rs.next()){
		int version = rs.getInt(1);
		String modifiedBy = rs.getString(2);
		Timestamp modified = rs.getTimestamp(3);
		if(version>object.getVersion().getValue()){
		    String when = DateFormat.getDateInstance().format(modified);
		    throw new RuntimeException(table + " "+object.getID()+ " modified by "+
			    modifiedBy+ " at "+when);
		}else{
		    throw new RuntimeException("unexpected error checking timestamp");
		}
	    }else{
		 throw new RuntimeException(table + " "+object.getID()+ " has been deleted ");
	    }
	}finally{
	     close(rs,stmt,null);
	}
    }
    
    protected DomainObjectOOL load(ResultSet rs) throws SQLException{
	Long id = new Long(rs.getLong(1));
 	//if(AppSessionManager.getSession().getIdentityMap().contains(keyname, id))
	//    return (DomainObjectOOL) AppSessionManager.getSession().getIdentityMap().get(keyname, id);
	DomainObjectOOL result = doLoad(id,rs);
	result.setID(id);
	//becareful		    
	String createdBy =  rs.getString(columns.length+1);
	Timestamp created = rs.getTimestamp(columns.length + 2);
	String modifiedBy = rs.getString(columns.length+3);
	Timestamp modified = rs.getTimestamp(columns.length + 4);
	int versionid = rs.getInt(columns.length + 5);
	result.setCreatedBy(createdBy);
	result.setCreated(created);
	result.setModified(modified);
	result.setModifiedBy(modifiedBy);
	Version version = Version.find(Long.valueOf(versionid));
	result.setVersion(version);
	//AppSessionManager.getSession().getIdentityMap().put(keyname, result);
	return result;
    }
     protected List loadAll(ResultSet rs) throws SQLException{
	List result = new ArrayList();
	while(rs.next()){
	    result.add(load(rs));
	}
	return result;
		
    }
     private  Timestamp now(){
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
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


