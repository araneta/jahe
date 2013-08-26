/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import core.helpers.AppSession;
import core.helpers.AppSessionManager;
import core.helpers.IdentityMap;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aldo
 */
public abstract class BusinessTransactionCommand implements Command{
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    public static final String APP_SESSION = "App session";
    public void init(ServletContext context,
	HttpServletRequest request,
	HttpServletResponse response){
	this.context = context;
	this.request = request;
	this.response = response;
	//String procName = request.getParameter("methodname");
	//String params = request.getParameter("prm");
    }
    abstract public void process();
    protected void forward(String target)
    {
	RequestDispatcher dispatcher = context.getRequestDispatcher(target);	
	try{
	    dispatcher.forward(request, response);		
	}catch(ServletException ex)    {
	     throw new RuntimeException(ex);	
	}catch(IOException ex)    {
	     throw new RuntimeException(ex);
	}
    }
    protected String method(){
	return request.getParameter("methodname");
    }
    protected String param(String name){
	return request.getParameter(name);
    }
    protected void startNewBusinessTransaction(){
	HttpSession httpSession = request.getSession(true);
	AppSession appSession = (AppSession)httpSession.getAttribute(APP_SESSION);
	appSession = new AppSession(request.getRemoteUser(), httpSession.getId(), new IdentityMap());
	AppSessionManager.setSession(appSession);
	httpSession.setAttribute(APP_SESSION, appSession);
    }
    protected void continueBusinessTransaction(){
	HttpSession httpSession = request.getSession();
	AppSession appSession = (AppSession) httpSession.getAttribute(APP_SESSION);
	AppSessionManager.setSession(appSession);
    }
    protected void commitBusinessTransaction(){
	AppSessionManager.getSession().commit();
    }
    
}
