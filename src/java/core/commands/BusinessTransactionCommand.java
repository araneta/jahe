/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import core.helpers.AppSession;
import core.helpers.AppSessionManager;
import core.helpers.IdentityMap;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String user = request.getRemoteUser();
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
    
}
