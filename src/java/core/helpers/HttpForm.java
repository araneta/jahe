/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aldo
 */
public  class HttpForm {
    public class FormField{
        public String name;
        public String label;
        public String info;
        public String value;
        public boolean hasError;
    }
    private boolean hasError = false;
    private Map<String,List<String>> errors = new HashMap<String,List<String>>();
    public boolean validate(){return true;}
    
    public boolean hasError(){
        return hasError;
    }
    public void addError(String field,String message){
        hasError = true;
        if(!errors.containsKey(field)){
            errors.put(field, new ArrayList<String>());
        }
        errors.get(field).add(message);
    }
    public String errorMessages(){
        StringBuilder sb = new StringBuilder();
        for(List<String> errorMsgs : errors.values()){
            if(errorMsgs==null)continue;
            
            for(String msg : errorMsgs){
                sb.append(msg);
            }
        }
        return sb.toString();
    }
    public FormField field(String name){
        FormField field = new FormField();
        try{
            field.name = name;
            field.value =  (String)this.getClass().getField(name).get(this);            
        }catch(NoSuchFieldException ne){
            throw new RuntimeException(ne);
        }catch(IllegalAccessException iae){
            throw new RuntimeException(iae);
        }    
        if(this.errors.containsKey(name)){
            field.hasError = true;
            List<String> msgs = this.errors.get(name);
            if(msgs!=null){
                StringBuilder sb = new StringBuilder();
                for(String s:msgs){
                    sb.append(s);
                    sb.append("<br />");
                }
                field.info = sb.toString();
            }
            
        }
        return field;
    }
    
}
