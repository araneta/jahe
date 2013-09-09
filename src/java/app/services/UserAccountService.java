/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entities.LoginForm;
import app.entities.RegistrationForm;
import app.entities.User;
import app.entities.UserProfileForm;
import app.mappers.UserMapper;
import core.helpers.AppSessionManager;
import core.helpers.SecurityHelper;
import core.helpers.StringUtils;
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
    public String encryptPassword(String plaintextPass){
        String hashedPassword;
        try{
            hashedPassword = SecurityHelper.getSaltedHash(plaintextPass);
            return hashedPassword;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public boolean register(RegistrationForm form){
        //check if email already exist
        User user = findByEmail(form.email);
        if(user!=null){
            form.addError("email", "email already registered");
            return false;
        }
        //save to db
        String hashedPassword = encryptPassword(form.password);
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
                tuser.setLastLogin(TimeHelper.UTCNow());
                String userid = tuser.getID().toString();
                AppSessionManager.getSession().setUser(userid, tuser.getFirstName()+" "+tuser.getLastName());
                return true;
            }else{
                form.addError("email", "invalid password");
                return false;
            }
        }catch(Exception e){
            throw new RuntimeException(e);
            
        }
        
    }
    public User getActiveUser(){
        String activeUserId = AppSessionManager.getSession().getUserId();
        User user = getUserMapper().find(Long.parseLong(activeUserId));
        return user;
    }
    public boolean update(UserProfileForm profile){
        //get active user
        User activeUser = getActiveUser();
        if(activeUser==null)
            return false;
        if(profile.email.equals(activeUser.getEmail())){
            return false;
        }
        //update user data
        activeUser.setEmail(profile.email);
        activeUser.setFirstName(profile.firstName);
        activeUser.setLastName(profile.lastName);
        if(!StringUtils.isEmpty(profile.password))
            activeUser.setPassword(encryptPassword(profile.password));
        return true;
    }
}
