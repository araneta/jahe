/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.commands;

import core.commands.SimpleFrontCommand;

/**
 *
 * @author aldo
 */
public class HomeCommand extends SimpleFrontCommand{
     public void process() {
        String method = method();
	if(method.equals("index")){
	     render("/home/index","homepage");
	}        
    }
}
