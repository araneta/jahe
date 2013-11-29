/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entities;

import core.db.ConnectionManager;
import core.db.KeyGenerator;
import core.domainmodels.DomainObjectOOL;
import core.domainmodels.Version;
import java.sql.SQLException;

/**
 *
 * @author aldo
 */
public class Book extends DomainObjectOOL {
   
    private Long id;
    private String title;
    private String author;
    
    /*
    public  Book(Long id,Version version,String title,String author){
	super(id,version);
	
	this.title = title;
	this.author = author;	
    }*/
    //to load an object
    public  Book(String title,String author){
	
	this.title = title;
	this.author = author;	
    }
    
    //to create new object
    public static Book create(String title,String author){
	Book b = new Book(title,author);
	b.markNew();
	return b;
    }
   
    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public void setTitle(String title){
	this.title = title;
	markDirty();
    }
    public void setAuthor(String author){
	this.author = author;
	markDirty();
    }
}
