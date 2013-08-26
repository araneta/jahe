/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.sql.ResultSet;

import core.domainmodels.DomainObject;
import core.db.DB;
import core.db.KeyGenerator;
import core.helpers.AppSessionManager;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author aldo
 */
public abstract class AbstractMapper {
    protected String keyname;
    protected Map loadedMap = new HashMap();
    
    abstract protected String findStatement();
    
    protected DomainObject abstractFind(Long id){
	DomainObject result = (DomainObject) loadedMap.get(id);
	if(result!=null)return result;
	PreparedStatement findStatement = null;
	ResultSet rs = null;
	try{
	    findStatement = DB.prepare(findStatement());
	    findStatement.setLong(1,id.longValue());
	    rs = findStatement.executeQuery();
	    rs.next();
	    result = load(rs);
	    return result;
	}catch(SQLException ex){
	    throw new RuntimeException(ex);
	}finally{
	    DB.cleanUp(findStatement, rs);
	}
    }
   
    protected DomainObject load(ResultSet rs) throws SQLException{
	Long id = new Long(rs.getLong(1));
	if(loadedMap.containsKey(id))
	    return (DomainObject) loadedMap.get(id);
	DomainObject result = doLoad(id,rs);
	loadedMap.put(id, result);
	return result;
    }
    abstract protected DomainObject doLoad(Long id, ResultSet rs) throws SQLException;
    protected List loadAll(ResultSet rs) throws SQLException{
	List result = new ArrayList();
	while(rs.next()){
	    result.add(load(rs));
	}
	return result;
		
    }
    /*
     * find with many parameters
     */
    public List findMany(StatementSource source){
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try{
	    stmt = DB.prepare(source.sql());
	    for(int i=0; i<source.parameters().length; i++){
		stmt.setObject(i+1, source.parameters()[i]);
	    }
	    rs = stmt.executeQuery();
	    return loadAll(rs);
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    DB.cleanUp(stmt, rs);
	}
    }
    public Long findNextDatabaseId(){
	KeyGenerator keygen = new KeyGenerator( keyname, 1);	
	return keygen.nextKey();	
    }
    
    abstract protected String insertStatement();
    
    abstract protected void doInsert(DomainObject subject, PreparedStatement insertStatement)
	    throws SQLException;
    
    public Long insert(DomainObject subject){
	PreparedStatement insertStatement = null;
	try{
	   insertStatement = DB.prepare(insertStatement());
	   subject.setID(findNextDatabaseId());
	   insertStatement.setInt(1, subject.getID().intValue());
	   doInsert(subject,insertStatement);
	   insertStatement.execute();
	   loadedMap.put(subject.getID(), subject);
	   return subject.getID();
	}catch(SQLException e){
	    throw new RuntimeException(e);
	}finally{
	    DB.cleanUp(insertStatement, null);
	}
    }
    
}
