/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.commands.admin;

import app.entities.AuthorForm;
import app.services.AuthorService;
import core.commands.BusinessTransactionCommand;

/**
 *
 * @author aldo
 */
public class AuthorsCommand extends BusinessTransactionCommand/*SimpleFrontCommand*/{
    public void process() {	
	String method = method();
	if(method.equals("list")){
	    
	}	
	else if(method.equals("new")){
	   newAuthor();
	}
	else if(method.equals("save")){
            save();
        }	    
    }
    private void newAuthor(){
        initializeCsfrToken();        
        AuthorForm authorForm = new AuthorForm();       
        render(authorForm,"/admin/authors/new","admin");
    }
    private void save(){
        checkCsrfToken();        
        startNewBusinessTransaction();
        AuthorForm form = (AuthorForm)bind(AuthorForm.class);
        if(!form.validate()){
            badRequest(form);
            return;
        }
        AuthorService service = new AuthorService();
        //if(!service.register(form)){
            //badRequest(form);
            //return;
        //}
        if(!commitBusinessTransaction()){
            flash("error",getLastError());
            badRequest(form);
            return;
        }
        flash("success","Author created");
        redirect("/admin/authors/new");
    }
}