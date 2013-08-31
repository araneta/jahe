/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.RegistrationForm;
import core.commands.SimpleFrontCommand;
import core.helpers.HttpForm;

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
        
        RegistrationForm regForm = new RegistrationForm();       
        render(regForm,"/register/new","homepage");
    }
    public void saveRegistration(){
        checkCsrfToken();        
        RegistrationForm regForm = (RegistrationForm)bind(RegistrationForm.class);
        if(!regForm.validate()){
            badRequest(regForm);
        }
        int y=0;
    }
    
}
