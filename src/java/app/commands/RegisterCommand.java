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
        //render("/register/new","homepage");
        RegistrationForm f2 = new RegistrationForm();
        f2.name = "pak bondan";
        render(f2,"/register/new","homepage");
    }
    public void saveRegistration(){
        checkCsrfToken();        
        RegistrationForm f2 = (RegistrationForm)bind(RegistrationForm.class);
        if(!f2.validate()){
            badRequest(f2);
        }
        int y=0;
    }
    
}
