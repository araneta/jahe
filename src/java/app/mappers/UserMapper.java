/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.mappers;

import app.entities.User;
import core.domainmodels.DomainObjectOOL;
import core.mappers.AbstractMapperOOL;
import core.mappers.MetaDataMap;
import core.mappers.StatementSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author aldo
 */
public class UserMapper extends AbstractMapperOOL  {
    public static final String[] COLUMNS = {"id", "email", "first_name","last_name","password","last_login"};    
    public UserMapper(){
	super("user");	
    }
    protected void loadDataMap() {
        dataMap = new MetaDataMap(User.class, "user");
        dataMap.addColumn("email", "varchar", "email");
        dataMap.addColumn("first_name", "varchar", "firstName");
        dataMap.addColumn("last_name", "varchar", "lastName");
        dataMap.addColumn("password", "varchar", "password");
        dataMap.addColumn("last_login", "datetime", "lastLogin");
    }
    protected String findStatement(){
        return "select * from user";
    }   
    /*
    protected DomainObjectOOL doLoad(Long id,ResultSet rs) throws SQLException{	
	String email = rs.getString("email");
	String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
	String password = rs.getString("password");
        
	User u = new User(email,firstName,lastName,password);	
	return u;	
    }
    protected void doInsert(DomainObjectOOL subject, PreparedStatement stmt) 
	throws SQLException{
	User u = (User)subject;        
	stmt.setString(2, u.getEmail());
	stmt.setString(3, u.getFirstName());
        stmt.setString(4, u.getLastName());
        stmt.setString(5, u.getPassword());
    }
     protected void doUpdate(DomainObjectOOL subject, PreparedStatement stmt) 
	throws SQLException{
	User u = (User)subject;        
	stmt.setString(1, u.getEmail());
	stmt.setString(2, u.getFirstName());
        stmt.setString(3, u.getLastName());
        stmt.setString(4, u.getPassword());
        
    }
    */
    public User find(Long id){
	return (User) super.find(id);
    }
       
    public User findByEmail(String pattern){
	return (User)find(new FindByEmail(pattern));
    }
    public void updateLoginTime(int userid,Timestamp loginTime){
        //UpdateLoginTime
    }
    public void delete(User u){
        super.delete(u);
        u.getVersion().delete();
    }
    static class FindByEmail implements StatementSource{
	private String email;
	public FindByEmail(String email){
	    this.email = email;
	}
	public String sql(){
	    return "select * from user "
		    + " where upper(email) like upper(?) limit 1"
		    ;
	}
	public Object[] parameters(){
	    Object[] ret = {email};
	    return ret;
	}
    }
    static class UpdateLoginTime implements StatementSource {
        private int userid;
        private Timestamp loginTime;
        
        public UpdateLoginTime(int userid, Timestamp loginTime){
            this.userid = userid;
            this.loginTime = loginTime;                    
        }
        public String sql(){
            return "login_time=? where id = ?";
        }
        public Object[] parameters(){
            Object[] ret = {loginTime,userid};
            return ret;
        }
    }
}
