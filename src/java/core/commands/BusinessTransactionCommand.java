/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import core.app.AppSession;
import core.app.AppSessionManager;
import core.domainmodels.IdentityMap;
import core.helpers.StringUtils;
import core.security.SimpleRoleCheck;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aldo
 */
/*public abstract class BusinessTransactionCommand implements Command{*/
public abstract class BusinessTransactionCommand extends SimpleFrontCommand{
    public static final String APP_SESSION = "App session";
    protected void startNewBusinessTransaction(){
	HttpSession httpSession = request.getSession(true);
        String user = getActiveUser();//request.getRemoteUser();
        if(user==null)
            user = "anonymouse";
	AppSession appSession = (AppSession)httpSession.getAttribute(APP_SESSION);
	appSession = new AppSession(user, httpSession.getId(), new IdentityMap());
	AppSessionManager.setSession(appSession);
	httpSession.setAttribute(APP_SESSION, appSession);
        
    }
    protected void continueBusinessTransaction(){
	HttpSession httpSession = request.getSession();
	AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
	AppSessionManager.setSession(appSession);
    }
    protected boolean commitBusinessTransaction(){
	return AppSessionManager.getSession().commit();
    }
    protected String getLastError(){
        return AppSessionManager.getSession().getLastError();
    }
    protected boolean checkLogin(){
       
        if(StringUtils.isEmpty(getActiveUser()))
        {
            redirect("/login/index");            
            return false;
        }	
        return true;
    }
    public void setActiveUser(String userid){
        request.getSession(false).setAttribute("activeuser", userid);
    }
    public String getActiveUser(){
        return (String)request.getSession(false).getAttribute("activeuser");
    }
    public boolean requiredRole(String role){
        if(!checkLogin())
            return false;
        SimpleRoleCheck src = new SimpleRoleCheck();
        if(src.check(role, Long.parseLong(getActiveUser()))){
            return true;
        }else
        {
            redirect("/login/index");            
            return false;
        }
    }
}
