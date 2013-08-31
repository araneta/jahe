/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entities.Book;
import app.entities.RegistrationForm;
import app.entities.User;
import app.mappers.BookMapper;
import app.mappers.UserMapper;
import core.helpers.AppSessionManager;
import core.helpers.SecurityHelper;
import core.helpers.TimeHelper;

/**
 *
 * @author aldo
 */
public class UserRegistrationService {
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
}
