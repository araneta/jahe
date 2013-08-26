/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;


import core.commands.Command;
import core.commands.SimpleFrontCommand;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author aldo
 */
public class SimpleFrontServlet extends HttpServlet {
    static Logger log = Logger.getLogger(SimpleFrontServlet.class);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
       System.out.println("Initialising log4j");
       String log4jLocation = config.getInitParameter("log4j-properties-location");

       ServletContext sc = config.getServletContext();

       if (log4jLocation == null) {
	  System.out.println("No log4j properites...");
	  BasicConfigurator.configure();
       } else {
	  String webAppPath = sc.getRealPath("/");
	  String log4jProp = webAppPath + log4jLocation;
	  File output = new File(log4jProp);

	  if (output.exists()) {
	     System.out.println("Initialising log4j with: " + log4jProp);
	     PropertyConfigurator.configure(log4jProp);
	  } else {
	     System.out.println("Find not found (" + log4jProp + ").");
	     BasicConfigurator.configure();
	  }
       }

       super.init(config);
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
	//SimpleFrontCommand command = getCommand(request);
	Command command = getCommand(request);
	command.init(getServletContext(), request, response);
	command.process();	    
	
	
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
	return "Short description";
    }// </editor-fold>
    
    private Command getCommand(HttpServletRequest request) {
	try{
	    return (core.commands.Command) getCommandClass(request).newInstance();
	}catch(IllegalAccessException e)    {
	    throw new RuntimeException(e);	   
	}catch(InstantiationException e){
	    throw new RuntimeException(e);	   
	}	
    }
    private Class getCommandClass(HttpServletRequest request){
	Class cls = null;	
	String input = request.getParameter("clsname");
	String className;
	if(!input.isEmpty()){
	    className = input.substring(0, 1).toUpperCase() + input.substring(1);
	}else
	{
	    return core.commands.UnknownCommand.class;
	}
	//log.info("cls:"+className);
	try
	{	    
	    cls = Class.forName("app.commands."+className+"Command");	    
	}
	catch(ClassNotFoundException e)
	{
	    cls = core.commands.UnknownCommand.class;
	}
	return cls;
    }
}
