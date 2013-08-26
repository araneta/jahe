/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.domainmodels;

/**
 *
 * @author aldo
 */
public class DomainObject {
    protected Long id;
    
    public void setID(Long id){
	this.id = id;		
    }
    public Long getID(){
	return id;
    }
}
