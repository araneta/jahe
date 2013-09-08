/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entities;

import core.helpers.HttpForm;

/**
 *
 * @author aldo
 */
public class LoginForm extends HttpForm {
    public String email;
    public String password;

    public boolean validate(){

        if(email==null || email.isEmpty()){
            addError("email","email is empty");            
        }else{
            //check valid email
            if(!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email)){
                addError("email","invalid email address");
            }
        }
        if(password==null || password.isEmpty()){
            addError("password","password is empty");            
        }
        return !this.hasError();
    }
}
