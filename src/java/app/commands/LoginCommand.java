/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.LoginForm;
import app.entities.User;
import app.services.UserAccountService;
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
	}else if(method.equals("verify")){
	    verify();
	}
        
    }
    public void loginPage(){
        initializeCsfrToken();
        LoginForm loginForm = new LoginForm();       
        render(loginForm,"/account/login","homepage");
    }
    public void verify(){
        checkCsrfToken();        
        startNewBusinessTransaction();
        LoginForm loginForm = (LoginForm)bind(LoginForm.class);
        if(!loginForm.validate()){
            badRequest(loginForm);
            return;
        }
        UserAccountService service = new UserAccountService();
        User user = service.login(loginForm);
        if(user==null){
            badRequest(loginForm);
            return;
        }
        setActiveUser(user.getID().toString());
        if(!commitBusinessTransaction()){
            flash("error",getLastError());
            badRequest(loginForm);
            return;
        }
        flash("success","welcome");
        redirect("/userprofile/index");
    }
}
