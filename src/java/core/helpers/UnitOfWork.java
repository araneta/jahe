/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import core.domainmodels.DomainObjectOOL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aldo
 */
public class UnitOfWork {
    private List reads = new ArrayList();
    private List newObjects = new ArrayList();
    private List dirtyObjects = new ArrayList();
    private List removedObjects = new ArrayList();
    
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
	    AppSessionManager.getSession().getMapper(obj.getClass()).insert(obj);
	}
    }
    private void deleteRemoved(){
	for(Iterator objects = removedObjects.iterator();objects.hasNext();){
	    DomainObjectOOL obj = (DomainObjectOOL) objects.next();	    
	    AppSessionManager.getSession().getMapper(obj.getClass()).delete(obj);
	}
    }
    private void updateDirty(){
	
    }
    private void rollbackSystemTransaction(){
	
    }
    public void commit(){
	try{
	    checkConsistentRead();
	    insertNew();
	    deleteRemoved();
	    updateDirty();
	}catch(Exception e){
	    rollbackSystemTransaction();
	}
    }
    public void checkConsistentRead(){
	for(Iterator iter = reads.iterator();iter.hasNext();){
	    DomainObjectOOL dependent = (DomainObjectOOL)iter.next();
	    dependent.getVersion().increment();
	}
    }
    
}
