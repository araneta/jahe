/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.LoginForm;
import core.commands.BusinessTransactionCommand;

/**
 *
 * @author aldo
 */
public class LoginCommand extends BusinessTransactionCommand{
    public void process() {
        String method = method();
	if(method.equals("index")){
	    loginPage();
	}else if(method.equals("save")){
	    
	}
        
    }
    public void loginPage(){
        initializeCsfrToken();
        LoginForm loginForm = new LoginForm();       
        render(loginForm,"/account/login","homepage");
    }
    
}
