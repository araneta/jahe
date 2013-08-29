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
    public String name;
    public String email;
    public String password;
    
    public boolean validate(){
        
        if(name.isEmpty()){
            addError("name","name is empty");            
        }
        if(email.isEmpty()){
            addError("email","email is empty");            
        }
        if(password.isEmpty()){
            addError("password","pass is empty");            
        }
        return !this.hasError();
    }
    
            
}
