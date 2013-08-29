/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.RegistrationForm;
import core.commands.SimpleFrontCommand;
import core.helpers.Form;
import java.lang.reflect.Field;

/**
 *
 * @author aldo
 */
public class RegisterCommand extends SimpleFrontCommand{
    public void process() {
        String method = method();
	if(method.equals("new")){
	    newRegistration();
	}else if(method.equals("save")){
	    saveRegistration();
	}
        
    }
    public void newRegistration(){
        initializeCsfrToken();
        render("/register/new","homepage");	
    }
    public void saveRegistration(){
        checkCsrfToken();        
        RegistrationForm f2 = (RegistrationForm)bind(RegistrationForm.class);
        if(!f2.validate()){
            String err = f2.errorMessages();
        }
        int y=0;
    }
    
}
