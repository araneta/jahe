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
public abstract class HttpForm {
    private boolean hasError = false;
    private Map<String,List<String>> errors = new HashMap<String,List<String>>();
    public abstract boolean validate();
    
    public boolean hasError(){
        return hasError;
    }
    protected void addError(String field,String message){
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
}
