/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.mappers;

import java.sql.*;
import java.sql.ResultSet;

import core.mappers.StatementSource;

import app.entities.Book;
import core.db.ConnectionManager;
import core.db.DB;
import core.domainmodels.DomainObjectOOL;
import core.mappers.AbstractMapperOOL;
import java.util.List;
/**
 *
 * @author aldo
 */
public class BookMapper extends AbstractMapperOOL  {
    
    public static final String[] COLUMNS = {"id", "title", "author"};    
    public static final String findAllSql = "select * from book";
   
    public BookMapper(){
	super("book","book",COLUMNS);	
    }
    //protected String insertStatement(){
	//return "insert into book values(?,?,?, ?,?,?,?)";
    //}
    
    //protected String findStatement(){
	//return "select "+getColumns()+" from book where id=?";
    //}
   
    protected DomainObjectOOL doLoad(Long id,ResultSet rs) throws SQLException{	
	String title = rs.getString(2);
	String author = rs.getString(3);
	
	//long versionId = rs.getLong("version_id");
	//Version version = Version.find(versionId);
	Book b = new Book(title,author);
	//b.setVersion(version);
	return b;	
    }
    protected void doInsert(DomainObjectOOL subject, PreparedStatement stmt) 
	throws SQLException{
	Book b = (Book)subject;
	stmt.setString(2, b.getTitle());
	stmt.setString(3, b.getAuthor());
    }
     protected void doUpdate(DomainObjectOOL subject, PreparedStatement stmt) 
	throws SQLException{
	Book b = (Book)subject;
	stmt.setString(1, b.getTitle());
	stmt.setString(2, b.getAuthor());
    }
    
    public Book find(Long id){
	return (Book) super.find(id);
    }
    
    public List<Book> list(){			
	PreparedStatement statement = null;
	ResultSet rs = null;		
	Connection conn = null;
	try{	    
	    conn = ConnectionManager.INSTANCE.getConnection();	    
	    statement = conn.prepareStatement(findAllSql);
	    rs = statement.executeQuery();
	   return loadAll(rs);
	 }catch(SQLException ex){
	    throw new RuntimeException(ex);	
	} finally {  
	   //DB.cleanUp(statement, rs);	    		       
	    this.close(rs, statement, conn);
	}		   	
    }
    /*
    public List findByTitle(String pattern){
	return findMany(new FindByTitle(pattern));
    }*/
    
    static class FindByTitle implements StatementSource{
	private String title;
	public FindByTitle(String title){
	    this.title = title;
	}
	public String sql(){
	    return "select "+COLUMNS + " from book "
		    + " where upper(title) like upper(?)"
		    + " order by title";
	}
	public Object[] parameters(){
	    Object[] ret = {title};
	    return ret;
	}
    }
}
