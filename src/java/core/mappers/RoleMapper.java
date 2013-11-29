/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.mappers;

import core.entities.Roles;
import java.util.List;

/**
 *
 * @author aldo
 */
public class RoleMapper extends AbstractMapperOOL  {
    
    public RoleMapper(){
	super("roles");	
    }
    protected void loadDataMap() {
        dataMap = new MetaDataMap(Roles.class, "roles");
        dataMap.addColumn("role_name", "varchar", "roleName");
        dataMap.addColumn("parent_id", "int", "parentId");        
    }
    public List findAllRoles(){
        return findMany(new FindAllRoles() );
    }
    public Roles findByRoleName(String role){
        return (Roles)find(new FindRoleByName(role));
    }
    static class FindAllRoles implements StatementSource{	
	public String sql(){
	    return "select * from roles ";
	}
	public Object[] parameters(){	    
	    return null;
	}
    }
    static class FindRoleByName implements StatementSource{	
        private String roleName;
        public FindRoleByName(String roleName){
            this.roleName = roleName;
        }
	public String sql(){
	    return "select * from roles where role_name=? ";
	}
	public Object[] parameters(){	    
	    return new Object[] {roleName};
	}
    }
   
}
