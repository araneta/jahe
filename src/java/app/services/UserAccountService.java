/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entities.LoginForm;
import app.entities.RegistrationForm;
import app.entities.User;
import app.mappers.UserMapper;
import core.helpers.AppSessionManager;
import core.helpers.SecurityHelper;
import core.helpers.TimeHelper;

/**
 *
 * @author aldo
 */
public class UserAccountService {
    UserMapper mapper;
    private UserMapper getUserMapper(){
        if(mapper==null)
             mapper = (UserMapper)AppSessionManager.getSession().getMapper(User.class);
        return mapper;
    }
    public boolean register(RegistrationForm form){
        //check if email already exist
        User user = findByEmail(form.email);
        if(user!=null){
            form.addError("email", "email already registered");
            return false;
        }
        //save to db
        String hashedPassword;
        try{
            hashedPassword = SecurityHelper.getSaltedHash(form.password);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        user = User.create(form.email, form.firstName, form.lastName, hashedPassword);
        user.setCreated(TimeHelper.UTCNow());
        
        return true;
    }
    public User findByEmail(String email){
       return getUserMapper().findByEmail(email);        
    }
    public boolean login(LoginForm form){
        User tuser = findByEmail(form.email);
        if(tuser==null){
            form.addError("email", "user not found");
            return false;
        }
        try{
            if(SecurityHelper.check(form.password, tuser.getPassword())){
                return true;
            }else{
                form.addError("email", "invalid password");
                return false;
            }
        }catch(Exception e){
            throw new RuntimeException(e);
            
        }
        
    }
}
