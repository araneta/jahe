/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.domainmodels;

import core.domainmodels.Version;
import app.entities.Book;
import core.domainmodels.DomainObjectOOL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aldo
 * An Identity Map keeps a record of all objects that have been read from the 
 * database in a single business transaction. Whenever you want an object, you 
 * check the Identity Map first to see if you already have it.
 */
public class IdentityMap{    
    
    private Map<Long, Book> books = new HashMap<Long, Book>();
    private Map<Long, Version> versions = new HashMap<Long,Version>();
    
    public void addBook(Book b) {
	books.put(b.getID(), b);
    }

    public Book getBook(long id) {
	return books.get(id);
    }    
    public void removeBook(long id){
	if(books.containsKey(id))
	    books.remove(id);
    }
    public DomainObjectOOL get(String keyName, Long id){
	if(keyName.equals("book")){
	    return getBook(id);
	}
	return null;
    }
    public boolean contains(String keyName, Long id){
	if(keyName.equals("book")){
	    return books.containsKey(id);
	}
	return false;
    }
    public void put(String keyName, DomainObjectOOL o){
	if(keyName.equals("book")){	    
	    addBook((Book)o);
	}
    }
    public void remove(String keyName, Long id){
	if(keyName.equals("book")){	    
	    removeBook(id);
	}
    }
    public Version getVersion(Long id){
	if(versions.containsKey(id))	
	    return versions.get(id);
	return null;
    }
    public void putVersion(Version v){
	versions.put(v.getId(), v);
    }
}
