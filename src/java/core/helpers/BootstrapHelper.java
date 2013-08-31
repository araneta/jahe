/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import core.helpers.HttpForm.FormField;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aldo
 */
public class BootstrapHelper extends FormHelper{
    //http://stackoverflow.com/questions/14361517/mark-error-in-form-using-bootstrap
    public String inputText(String field,String label){
        FormField ffield = form.field(field);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"control-group ");
        if(ffield.hasError)
            sb.append("error");
        sb.append("\">");
        sb.append("<label class=\"control-label\" for=\"");
        sb.append(field);
        sb.append("\">");
        sb.append(label);
        sb.append("</label>");
        sb.append("<div class=\"controls\">");
        sb.append(super.inputText(field,ffield.value,"id=\""+field+"\""));        
        if(ffield.info!=null && !ffield.info.isEmpty()){
            sb.append("<span class=\"help-inline\">");
            sb.append(ffield.info);
            sb.append("</span>");
        }
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
}
