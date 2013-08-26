/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.commands;

import core.commands.SimpleFrontCommand;

/**
 *
 * @author aldo
 */
public class RegisterCommand extends SimpleFrontCommand{
    public void process() {
        String method = method();
	if(method.equals("new")){
	    newRegistration();
	}	
    }
    public void newRegistration(){
        render("/register/new","homepage");	
    }
}
