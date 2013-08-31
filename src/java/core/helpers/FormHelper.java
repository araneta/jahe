/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aldo
 */
public class FormHelper {
    protected HttpForm form;
    private HttpServletRequest request;
    private HttpServletResponse response;
    public void init(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
        this.form = (HttpForm)request.getAttribute("_form");
    }
    
    public String csrfField(){
        return hiddenField("ctoken",(String)request.getSession(false).getAttribute("ctoken"));
    }
    public String inputText(String name,String value){
        if(value==null) value="";
        return "<input type=\"text\" name=\""+name+"\" value=\""+value+"\" />";
    }
    public String inputText(String name, String value, String other){
        if(value==null) value="";
        return "<input type=\"text\" name=\""+name+"\" value=\""+value+"\" "+other+" />";
    }
    public String inputPassword(String name,String value){
        if(value==null) value="";
        return "<input type=\"password\" name=\""+name+"\" value=\""+value+"\" />";
    }
    public String inputPassword(String name, String value, String other){
        if(value==null) value="";
        return "<input type=\"password\" name=\""+name+"\" value=\""+value+"\" "+other+" />";
    }
    public String hiddenField(String name, String value){
        if(value==null) value="";
        return "<input type=\"hidden\" name=\""+name+"\" value=\""+value+"\" />";
    }
    public String submit(String label){
        return "<input type=\"submit\" value=\""+label+"\" />";
    }
}
