/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.LoginForm;
import app.entities.UserProfileForm;
import app.services.UserAccountService;
import core.commands.BusinessTransactionCommand;

/**
 *
 * @author aldo
 */
public class UserprofileCommand extends BusinessTransactionCommand{
    public void process() {
        if(!checkLogin())return;
        
        String method = method();
	if(method.equals("index")){
	    profilePage();
	}else if(method.equals("save")){
	    saveProfilePage();
	}
    }
    public void profilePage(){
        initializeCsfrToken();
        UserProfileForm profileForm = new UserProfileForm();              
        render(profileForm,"/account/profile","dashboard");
    }
    public void saveProfilePage(){
        checkCsrfToken();        
        startNewBusinessTransaction();
        UserProfileForm profileForm = (UserProfileForm)bind(UserProfileForm.class);
        if(!profileForm.validate()){
            badRequest(profileForm);
            return;
        }
        UserAccountService service = new UserAccountService();
        if(!service.update(profileForm)){
            badRequest(profileForm);
            return;
        }
        if(!commitBusinessTransaction()){
            flash("error",getLastError());
            badRequest(profileForm);
            return;
        }
        flash("success","user profile updated");
        redirect("/userprofile/index");
    }
}
