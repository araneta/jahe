/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.entities;

import core.domainmodels.DomainObjectOOL;

/**
 *
 * @author aldo
 */
public class Roles extends DomainObjectOOL {
    
    private String roleName;
    private long parentId;

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
        markDirty();
    }

    /**
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
        markDirty();
    }
     //to load an object
    public Roles(){}
    public Roles(String roleName,Long parent){
	
	this.roleName = roleName;
	this.parentId = parent;
    }
    
    //to create new object
    public static Roles create(String roleName,Long parent){
	Roles b = new Roles(roleName,parent);
	b.markNew();
	return b;
    }
}
