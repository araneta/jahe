/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author aldo
 */
public class Form<T> {
    private T value;
    private Class<T> template;
    public boolean validate(){return false;};
    private Map<String,List<String>> errors = new HashMap<String,List<String>>();
    
    public  Form<T> form(Class<T> x){    
        try{
            value = x.newInstance();            
            template = x;
        }catch(InstantiationException ex){
            throw new RuntimeException(ex);
        }catch(IllegalAccessException iex){
            throw new RuntimeException(iex);
        }
        return this;
    }
    public T get(){
        return value;
    }
    public Form<T> bind(HttpServletRequest request){
        Field[] fields = template.getFields();
        if(fields!=null){
            for(Field f : fields){
                Object o = request.getParameter(f.getName());
                
                if(o!=null){
                    try{
                        f.set(value, o);
                    }catch(IllegalAccessException e){
                        throw new RuntimeException(e);
                    }
                }
                
            }
        }
        try{
            Method m = template.getMethod("validate", new Class[] {});
            if(m!=null){
                m.invoke(value, new Object[] {});
            }
        }catch(NoSuchMethodException nsme){
            throw new RuntimeException(nsme);
        }catch(IllegalAccessException iae){
            throw new RuntimeException(iae);
        }catch(InvocationTargetException ite){
            throw new RuntimeException(ite);
        }
        
        return this;
    }
    
}
