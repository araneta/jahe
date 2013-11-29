/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import app.entities.User;
import app.entities.UserProfileForm;
import app.services.UserAccountService;
import core.commands.BusinessTransactionCommand;
import core.entities.Roles;

/**
 *
 * @author aldo
 */
public class UserprofileCommand extends BusinessTransactionCommand{
    public void process() {
        startNewBusinessTransaction();        
        if(!requiredRole("registered"))return;
        
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
        UserAccountService service = new UserAccountService();
        User user = service.findByID(Long.parseLong(this.getActiveUser()));
        profileForm.email = user.getEmail();
        profileForm.firstName = user.getFirstName();
        profileForm.lastName = user.getLastName();
        
        render(profileForm,"/account/profile","dashboard");
    }
    public void saveProfilePage(){
        checkCsrfToken();                
        
        UserProfileForm profileForm = (UserProfileForm)bind(UserProfileForm.class);
        if(!profileForm.validate()){
            badRequest(profileForm);
            return;
        }
        UserAccountService service = new UserAccountService();
        if(!service.update(getActiveUser(),profileForm)){
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
