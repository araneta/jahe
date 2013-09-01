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
public class Bootstrap3Helper extends FormHelper{
    public String formGroup(String htmlInput,String name,String label,boolean hasError,String info){        
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"form-group ");
        if(hasError)
            sb.append("has-error");
        sb.append("\">");
        sb.append("<label class=\"control-label\" for=\"");
        sb.append(name);
        sb.append("\">");
        sb.append(label);
        sb.append("</label>");
        sb.append("<div class=\"controls\">");
        sb.append(htmlInput);        
        if(info!=null && !info.isEmpty()){
            sb.append("<span class=\"help-block\">");
            sb.append(info);
            sb.append("</span>");
        }
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }
    //http://stackoverflow.com/questions/14361517/mark-error-in-form-using-bootstrap
    public String formGroupText(String name,String label){
        FormField ffield = form.field(name);
        return formGroup(inputText(name,ffield.value," class=\"form-control\" id=\""+name+"\""),
            name,label,ffield.hasError,ffield.info);        
    }
    public String formGroupPassword(String name,String label){
        FormField ffield = form.field(name);
        return formGroup(inputPassword(name,ffield.value," class=\"form-control\" id=\""+name+"\""),
                name,label,ffield.hasError,ffield.info);
    }
     
    public String submit(String label){
        return "<button type=\"submit\" class=\"btn btn-default\">"+label+"</button>";
    }
    /*
     * @type : success, info, warning, danger
     */
    public String alert(String type,String message){
        return "<div class=\"alert alert-"+type+"\">"+message+"</div>";
    }
    public String flashText(){
        String text=null;
        String type = null;
        if(request.getAttribute("error")!=null){
            text = (String)request.getAttribute("error");
            type = "danger";
        }else if(request.getAttribute("success")!=null){
            text = (String)request.getAttribute("success");
            type = "success";
        }else if(request.getAttribute("info")!=null){
            text = (String)request.getAttribute("info");
            type = "info";
        }else if(request.getAttribute("warning")!=null){
            text = (String)request.getAttribute("swarning");
            type = "warning";
        }
        if(text==null)
            return "";
        return alert(type,text);
    }
}
