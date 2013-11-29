/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.domainmodels;

import core.app.AppSessionManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aldo
 */
public abstract class DomainObjectOOL extends DomainObject {  
    
    
    private Timestamp created;
    private String createdBy;
    private Timestamp modified;
    private String modifiedBy;
    private Version version;
   
    
    public DomainObjectOOL(long id, Version version) {
	this.id = id;
	this.version = version;
    }

    public DomainObjectOOL() {
	this(-1, null);
    }
    public void setSystemFields(Version version, Timestamp modified, String modifiedBy) {
	this.modified = modified;
	this.modifiedBy = modifiedBy;
	this.version = version;
    }
   

    public Timestamp getCreated() {
	return created;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public Timestamp getModified() {
	return modified;
    }

    public String getModifiedBy() {
	return modifiedBy;
    }

    public Version getVersion() {
	return version;
    }

    public void setCreated(Timestamp created) {
	this.created = created;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public void setModified(Timestamp modified) {
	this.modified = modified;
    }

    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    public void setVersion(Version version) {
	this.version = version;
    }
    /*unit of work*/
    protected void markNew(){
	AppSessionManager.getSession().registerNew(this);	
    }
    protected void markClean(){
	AppSessionManager.getSession().registerClean(this);
    }
    protected void markDirty(){
	AppSessionManager.getSession().registerDirty(this);
    }
    protected void markRemoved(){
	AppSessionManager.getSession().registerRemoved(this);
    }
}
