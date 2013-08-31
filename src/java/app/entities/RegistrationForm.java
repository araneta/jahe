/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entities;

import core.helpers.Form;
import core.helpers.HttpForm;

/**
 *
 * @author aldo
 */
public class RegistrationForm extends HttpForm{
    public String firstName,lastName;
    public String email;
    public String password;
    
    public boolean validate(){
        
        if(firstName==null || firstName.isEmpty()){
            addError("firstName","First name is empty");            
        }
        if(lastName==null || lastName.isEmpty()){
            addError("lastName","Last name is empty");            
        }
        if(email==null || email.isEmpty()){
            addError("email","email is empty");            
        }
        if(password==null || password.isEmpty()){
            addError("password","password is empty");            
        }
        return !this.hasError();
    }
    
            
}
