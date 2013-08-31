/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import static core.commands.BusinessTransactionCommand.CSRF_TOKEN;
import core.helpers.HttpForm;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
public abstract class SimpleFrontCommand implements Command{
    public static final String CSRF_TOKEN = "ctoken";
    
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
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
    protected void redirect(String url){
        try{
	    this.response.sendRedirect(url);
	 }catch(IOException io){
	     throw new RuntimeException(io);
	 }
    }
    protected void badRequest(HttpForm form){
        String referer = request.getHeader("Referer"); 
        //request.setAttribute("_badform", form);
        flash("_form",form);
        redirect(referer);
    }
    protected String method(){
	return request.getParameter("methodname");
    }
    protected String param(String name){
	return request.getParameter(name);
    }
    protected void render(String view, String template){        
        request.setAttribute("_p", view+".jsp");
        forward("/templates/"+template+".jsp");	
    }
    protected void render(HttpForm form, String view, String template){
        if(request.getAttribute("_form")==null)
            request.setAttribute("_form", form);
        render(view,template);
    }
    private String getToken()
    {
        String DEFAULT_PRNG = "SHA1PRNG"; //algorithm to generate key
        try{
            SecureRandom sr = SecureRandom.getInstance(DEFAULT_PRNG);            
            return "" + sr.nextLong();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }
    protected void initializeCsfrToken() {
        HttpSession httpSession = request.getSession();
        String csrfToken = (String)httpSession.getAttribute(CSRF_TOKEN);
        if(csrfToken==null || csrfToken.isEmpty()) {
            httpSession.setAttribute(CSRF_TOKEN, getToken());
        }
    }
    protected boolean checkCsrfToken(){
        HttpSession httpSession = request.getSession(false);
        String csrfToken = (String)httpSession.getAttribute(CSRF_TOKEN);
        String reqCsrfToken = (String)request.getParameter(CSRF_TOKEN);
        if(reqCsrfToken != null && csrfToken != null &&
            !reqCsrfToken.isEmpty() && !csrfToken.isEmpty()
            && csrfToken.equals(reqCsrfToken)) {
            return true;
        } else {
            throw new RuntimeException("Invalid security Token");
            //log("Invalid security Token. Supplied token: " + reqCsrfToken + ". Session token: " + csrfToken + ". IP: " + request.getRemoteAddr());
            //return false;
        }
        //return false;
    }
    protected Object bind(Class c){
        Object inst;
        try{
            inst = c.newInstance();
        }catch(InstantiationException ie){
            throw new RuntimeException(ie);
        }catch(IllegalAccessException iae){
            throw new RuntimeException(iae);
        }
        Field[] fields = c.getFields();
        
        if(fields!=null){
            for(Field f : fields){
                Object o = request.getParameter(f.getName());
                
                if(o!=null){
                    try{
                        f.set(inst, o);
                    }catch(IllegalAccessException e){
                        throw new RuntimeException(e);
                    }
                }
                
            }
        }
        return inst;
        
    }
    protected void flash(String key,Object message){
        request.setAttribute("flash."+key, message);
    }
}
