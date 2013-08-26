/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aldo
 */
public abstract class SimpleFrontCommand implements Command{
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
    protected String method(){
	return request.getParameter("methodname");
    }
    protected String param(String name){
	return request.getParameter(name);
    }
    protected void render(String view, String template){
        request.setAttribute("p", view+".jsp");
        forward("/templates/"+template+".jsp");	
    }
}
