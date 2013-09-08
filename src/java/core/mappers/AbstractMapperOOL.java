/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

import core.db.ConnectionManager;
import core.db.KeyGenerator;
import core.domainmodels.DomainObjectOOL;
import core.helpers.AppSessionManager;
import core.helpers.TimeHelper;
import core.helpers.Version;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aldo
 */
public abstract class AbstractMapperOOL {
    protected String keyname;
    protected MetaDataMap dataMap;
    
    protected String selectStatement(){
        StringBuilder sb = new StringBuilder();
	sb.append("select ");
	sb.append(dataMap.columnList());
        sb.append(",created_by,created_date,version_id");
        sb.append(" from ");
	sb.append(dataMap.getTableName());
	sb.append(" where id=?");
	return sb.toString();
    }
    protected String insertStatement(){
	StringBuilder sb = new StringBuilder();
	sb.append("insert into ");
	sb.append(dataMap.getTableName());
        sb.append("(");
        sb.append(dataMap.columnList());
        sb.append(",created_by,created_date,version_id");
        sb.append(")");
	sb.append(" values (?");
	sb.append(dataMap.getInsertList());		
	sb.append(",?,?,?)");
	return sb.toString();
    }
    protected String updateStatement(){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(dataMap.getTableName());
        sb.append(dataMap.getUpdateList()); 
        sb.append(", modified_by=?");
	sb.append(", modified=?");
	sb.append(", version_id=?");
        sb.append(" where id=? and version_id=?");
	return sb.toString();
    }
    protected String deleteStatement(){
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(dataMap.getTableName());
        sb.append(" where id=?");
	return sb.toString();
    }
    
    protected Long getNextId(){	
	KeyGenerator keygen = new KeyGenerator(keyname,1);
	return keygen.nextKey();
	    
    }
    
    public AbstractMapperOOL(String keyname){
	this.keyname = keyname;
	
	//buildStatements();
        loadDataMap();
    }
    
    protected abstract void loadDataMap();
    
    public DomainObjectOOL find(Long id){
	DomainObjectOOL obj=null;
	//identity map disabled to test optimistic offline lock
	//obj = AppSessionManager.getSession().getIdentityMap().get(this.keyname
	//	,id);
	//if(obj == null){
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
            String loadSQL = selectStatement();
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
            Timestamp now = TimeHelper.UTCNow();
	    subject.setSystemFields(Version.create(), now, AppSessionManager.getSession().getUser());
	    subject.getVersion().insert();
	    String sql = insertStatement();
	    System.out.println("uuh"+sql);
	    
            insertStatement = conn.prepareStatement(sql);
            int index = 0;
            
            subject.setID(getNextId());
            insertStatement.setLong(++index, subject.getID());
            for (Iterator it = dataMap.getColumns();it.hasNext();) {
                ColumnMap column = (ColumnMap)it.next();
                insertStatement.setObject(++index, column.getValue(subject));
            }
	   
	   insertStatement.setString(++index,AppSessionManager.getSession().getUser());
	   insertStatement.setTimestamp(++index, now);	   
	   insertStatement.setLong(++index, subject.getVersion().getId());	   
	  
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
            Timestamp now = TimeHelper.UTCNow();
	    long versionid = subject.getVersion().getId();
	    Version v = subject.getVersion();
	    v.setModified(now);
	    v.setModifiedBy(AppSessionManager.getSession().getUser());
	    v.increment();
	    String sql = updateStatement();
            updateStatement = conn.prepareStatement(sql);	   	   	   
            int index = 0;
            for (Iterator it = dataMap.getColumns();it.hasNext();) {
                ColumnMap column = (ColumnMap)it.next();
                updateStatement.setObject(++index, column.getValue(subject));
            }
	   updateStatement.setString(++index,AppSessionManager.getSession().getUser());
	   updateStatement.setTimestamp(++index, now);
	   updateStatement.setLong(++index, subject.getVersion().getId());	   
	   updateStatement.setLong(++index, subject.getID());	
	   updateStatement.setLong(++index, versionid);	
	   
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
            String deleteSQL = deleteStatement();
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
        String table = dataMap.getTableName();
        String checkVersionSQL = "select version, modified_by, modified from " + table +" where id =?";
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
	//DomainObjectOOL result = doLoad(id,rs);
        DomainObjectOOL result;
        try{
            result = (DomainObjectOOL)dataMap.getDomainClass().newInstance();        
        }catch(InstantiationException ie){
            throw new RuntimeException(ie);
        }catch(IllegalAccessException iae){
            throw new RuntimeException(iae);
        }
        result.setID(id);
        loadFields(rs, result);

	//becareful		    
        int columnslength = dataMap.count();
        
	String createdBy =  rs.getString("created_by");
	Timestamp created = rs.getTimestamp("created_date");
	String modifiedBy = rs.getString("modified_by");
	Timestamp modified = rs.getTimestamp("modified_date");
	int versionid = rs.getInt("version_id");
	result.setCreatedBy(createdBy);
	result.setCreated(created);
	result.setModified(modified);
	result.setModifiedBy(modifiedBy);
	Version version = Version.find(Long.valueOf(versionid));
	result.setVersion(version);
	//AppSessionManager.getSession().getIdentityMap().put(keyname, result);
	return result;
    }
    private void loadFields(ResultSet rs, DomainObjectOOL result) throws SQLException {
        for (Iterator it = dataMap.getColumns();it.hasNext();) {
            ColumnMap column = (ColumnMap)it.next();
            Object columnValue = rs.getObject(column.getColumnName());
            column.setField(result, columnValue);
        }
    }
     protected List loadAll(ResultSet rs) throws SQLException{
	List result = new ArrayList();
	while(rs.next()){
	    result.add(load(rs));
	}
	return result;
		
    }
      public List findMany(StatementSource source){
	PreparedStatement stmt = null;
	ResultSet rs = null;
        Connection conn = null;
	try{
            conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement(source.sql());
	    
	    for(int i=0; i<source.parameters().length; i++){
		stmt.setObject(i+1, source.parameters()[i]);
	    }
	    rs = stmt.executeQuery();
	    return loadAll(rs);
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(rs,stmt,conn);
	}
    }
    public DomainObjectOOL find(StatementSource source){
	PreparedStatement stmt = null;
	ResultSet rs = null;
        Connection conn = null;
	try{
            conn = ConnectionManager.INSTANCE.getConnection();
            stmt = conn.prepareStatement(source.sql());
	    
	    for(int i=0; i<source.parameters().length; i++){
		stmt.setObject(i+1, source.parameters()[i]);
	    }
	    rs = stmt.executeQuery();
            if(rs.next())
                return load(rs);
            return null;
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    close(rs,stmt,conn);
	}
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


