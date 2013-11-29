/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.entities;

import core.helpers.HttpForm;

/**
 *
 * @author aldo
 */
public class AuthorForm extends HttpForm {
    public String firstName,lastName;    

    public boolean validate(){

        if(firstName==null || firstName.isEmpty()){
            addError("firstName","first name is empty");            
        }
        if(lastName==null || lastName.isEmpty()){
            addError("lastName","last name is empty");            
        }
        return !this.hasError();
    }
}