/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aldo
 */
public interface Command {
    public void init(ServletContext context,
	HttpServletRequest request,
	HttpServletResponse response);
	
    abstract public void process();
    
}
