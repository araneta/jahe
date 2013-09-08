/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entities;

import core.domainmodels.DomainObjectOOL;
import java.sql.Timestamp;

/**
 *
 * @author aldo
 */
public class User extends DomainObjectOOL {   
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Timestamp lastLogin; 
    //to load an object
    public User(){
        
    }
    public  User(String email,String firstName,String lastName, String password){	
	this.email = email;
	this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
                
    }
    
    //to create new object
    public static User create(String email,String firstName,String lastName,String password){
	User b = new User(email,firstName,lastName,password);        
	b.markNew();
	return b;
    }
   
    public String getEmail(){return email;}
    public void setEmail(String email){
	this.email = email;
	markDirty();
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        markDirty();
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        markDirty();
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
        markDirty();
    }

    /**
     * @return the lastLogin
     */
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    /**
     * @param lastLogin the lastLogin to set
     */
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
        markDirty();
    }
    
    
}
