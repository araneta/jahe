/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import app.mappers.BookMapper;
import app.mappers.UserMapper;
import core.db.ConnectionManager;
import core.domainmodels.DomainObjectOOL;
import core.mappers.AbstractMapperOOL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aldo
 */
public class AppSession {
    private String user;
    private String id;
    private IdentityMap imap;
    //private BookMapper bookMapper = new BookMapper();
    private Connection connTrans;    
     /*unit of work*/
    private List reads = new ArrayList();
    private List newObjects = new ArrayList();
    private List dirtyObjects = new ArrayList();
    private List removedObjects = new ArrayList();
    private Map<Class, AbstractMapperOOL> mappers = new HashMap<Class, AbstractMapperOOL>();
    private String lastError;
    
    public AppSession(String user, String id, IdentityMap imap){
	this.user = user;
	this.id = id;
	this.imap = imap;	
	registerMappers();
    }
    public Connection getConnTrans(){
	return connTrans;
    }
    public String getLastError(){
        return lastError;
    }
    protected void registerMappers(){
	mappers.put(app.entities.Book.class, new BookMapper());
        mappers.put(app.entities.User.class, new UserMapper());
    }
    
    public IdentityMap getIdentityMap(){
	return this.imap;
    }
    public String getUser(){
	return user;
    }
    //public AbstractMapperOOL getMapper(String name){
    public AbstractMapperOOL getMapper(Class c){	
	//if(c==app.entities.Book.class)
	if(mappers.containsKey(c))
	    //return (AbstractMapperOOL)bookMapper;
	    return (AbstractMapperOOL) mappers.get(c);
	return null;
    }
    /*unitof work*/
    public void registerNew(DomainObjectOOL obj){
	newObjects.add(obj);
    }
    public void registerDirty(DomainObjectOOL obj){
	if(!dirtyObjects.contains(obj)&& !newObjects.contains(obj)){
	    dirtyObjects.add(obj);
	}
    }
    public void registerRemoved(DomainObjectOOL obj){
	if(newObjects.remove(obj))
	    return;
	dirtyObjects.remove(obj);
	if(!removedObjects.contains(obj)){
	    removedObjects.add(obj);
	}
    }
    public void registerClean(DomainObjectOOL obj){
	
    }
    public void registerRead(DomainObjectOOL object){
	reads.add(object);
    }
    private void insertNew(){
	for(Iterator objects = newObjects.iterator();objects.hasNext();){
	    DomainObjectOOL obj = (DomainObjectOOL) objects.next();
	    AbstractMapperOOL mapper =getMapper(obj.getClass());	    
	    mapper.insert(obj);
	}
    }
    private void deleteRemoved(){
	for(Iterator objects = removedObjects.iterator();objects.hasNext();){
	    DomainObjectOOL obj = (DomainObjectOOL) objects.next();	    
	    AbstractMapperOOL mapper =getMapper(obj.getClass());	    
	    mapper.delete(obj);
	}
    }
    private void updateDirty(){
	for(Iterator objects = dirtyObjects.iterator();objects.hasNext();){
	    DomainObjectOOL obj = (DomainObjectOOL) objects.next();	    
	    AbstractMapperOOL mapper =getMapper(obj.getClass());	    
	    mapper.update(obj);
	}
    }
    public boolean commit(){
	try{
	    connTrans = ConnectionManager.INSTANCE.getConnection();	    
	    connTrans.setAutoCommit(false);
	    insertNew();
	    updateDirty();
	    deleteRemoved();
	    connTrans.commit();
            return true;
	}catch(Exception e){
	    try{
		if(connTrans!=null)
		    connTrans.rollback();
		//System.out.println("rollback"+e.getMessage());
		
	    }catch(SQLException sqle){
		throw new RuntimeException(sqle);
	    }
            //throw new RuntimeException(e);
            lastError = e.getMessage();
            return false;
	    
	}finally{
	    try{
		if(connTrans!=null){
		    //conn.setAutoCommit(true);
		    connTrans.close();
		    connTrans = null;
		}
	    }catch(SQLException e){
		throw new RuntimeException(e);
	    }	    
	}
    }
}
