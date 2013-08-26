/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import core.commands.BusinessTransactionCommand;

/**
 *
 * @author aldo
 */
public class RegisterCommand extends BusinessTransactionCommand{
    public void process() {
        String method = method();
	if(method.equals("new")){
	    newRegistration();
	}	
    }
    public void newRegistration(){
        forward("/register/new.jsp");	
    }
}
