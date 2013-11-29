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
    
    public User find(Long id){
	return (User) super.find(id);
    }
       
    public User findByEmail(String pattern){
	return (User)find(new FindByEmail(pattern));
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
    
}
