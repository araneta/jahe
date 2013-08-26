/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.commands;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 *
 * @author aldo
 */
public class UnknownCommand extends SimpleFrontCommand{
    public void process() {
	forward("/unknown.jsp");
	
    }
}
