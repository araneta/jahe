/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.Book;
import app.mappers.BookMapper;
import core.commands.BusinessTransactionCommand;
import core.helpers.AppSessionManager;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author aldo
 */
public class BookCommand extends BusinessTransactionCommand/*SimpleFrontCommand*/{
    public void process() {	
	String method = method();
	if(method.equals("list")){
	    listBooks();
	}	
	else if(method.equals("add")){
	    addBook();
	}
	else if(method.equals("update")){
	    updateBook();
	}else if(method.equals("testupdate")){
	    testupdateBook();
	
	}else if(method.equals("testredirect")){
	    testRedirect();
	
	}else if(method.equals("viewredirect")){
	    viewRedirect();	
	}else if(method.equals("testuow")){
	    testUOW();	
	}else if(method.equals("testuowtwo")){
	    testUOW2();
	}
    }
    protected void listBooks(){	
	BookMapper am = new BookMapper();
	request.setAttribute("books",am.list());	
	forward("/views/book.jsp");	
    }
    protected void addBook(){
	startNewBusinessTransaction();
	Book b = new Book("b1","a1");
	BookMapper mapper = (BookMapper)AppSessionManager.getSession().getMapper(Book.class);
	mapper.insert(b);
	List<Book> books = mapper.list();
	request.setAttribute("books",books);	
	forward("/views/book.jsp");	    	
    }
     protected void updateBook(){
	startNewBusinessTransaction();	
	BookMapper mapper = (BookMapper)AppSessionManager.getSession().getMapper(Book.class);
	Book b = mapper.find(Long.valueOf(28));
	b.setTitle("1001mimpi");
	b.setAuthor("mbahsubut");
	mapper.update(b);
	List<Book> books = mapper.list();
	request.setAttribute("books",books);	
	forward("/views/book.jsp");	    	
    }
     //test optimistic offline lock
     //this will raise exception
     protected void testupdateBook(){
	//make sure to disable identity map 
	startNewBusinessTransaction();	
	BookMapper mapper = (BookMapper)AppSessionManager.getSession().getMapper(Book.class);
	Book bx = new Book("b1x","a1x");
	mapper.insert(bx);
	Book b = mapper.find(bx.getID());
	long v1 = b.getVersion().getValue();
	Book b2 = mapper.find(bx.getID());
	long v2 = b2.getVersion().getValue();
	b.setTitle("1001mimpi1");
	b.setAuthor("mbahsubut");
	mapper.update(b);
	long v11 = b.getVersion().getValue();
	long v22 = b2.getVersion().getValue();
	b2.setTitle("1001mimpi12");
	b2.setAuthor("mbahsubut2");
	mapper.update(b2);
	long v222 = b2.getVersion().getValue();
	
	List<Book> books = mapper.list();
	request.setAttribute("books",books);	
	forward("/views/book.jsp");	    	
    }
     //test flash message
     protected void testRedirect(){
	 startNewBusinessTransaction();	
	 request.setAttribute("flash.message", "Here is the news...");
	 try{
	    this.response.sendRedirect("viewredirect");
	 }catch(IOException io){
	     throw new RuntimeException(io);
	 }
     }
     protected void viewRedirect(){
	 continueBusinessTransaction();	
	 forward("/views/flash.jsp");	
     }
     protected void testUOW(){
	 //make sure to disable identity map 
	startNewBusinessTransaction();	
	BookMapper mapper = (BookMapper)AppSessionManager.getSession().getMapper(Book.class);
	Book bx = Book.create("martin kanginan","mr kanginan martin");	
	Book bx2 = mapper.find(Long.valueOf(60));
	bx2.setTitle("kiteb fisika mak");
	commitBusinessTransaction();
	List<Book> books = mapper.list();
	request.setAttribute("books",books);	
	forward("/views/book.jsp");	    
     }
     //test unit ofwork + optimistic offline lock
     //this will raise exception because updating the same object
     protected void testUOW2(){
	 //make sure to disable identity map 
	startNewBusinessTransaction();	
	BookMapper mapper = (BookMapper)AppSessionManager.getSession().getMapper(Book.class);
	Book bx = Book.create("pepakbosojowo", "martin fowlerle");	
	Book bx2 = mapper.find(Long.valueOf(60));	
	Book by2 = mapper.find(Long.valueOf(60));	
	bx2.setTitle("1001mimpi1 mbah");
	bx2.setAuthor("mbahsubur subur");
	
	by2.setTitle("1001mimpi1 le");
	by2.setAuthor("mbahsubur sabar");
	
	commitBusinessTransaction();
     }
}
