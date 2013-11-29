/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.mappers;

import core.entities.Roles;
import core.entities.UserRole;
import java.util.List;

/**
 *
 * @author aldo
 */
public class UserRoleMapper extends AbstractMapperOOL{
    public UserRoleMapper(){
	super("user_role");	
    }
    protected void loadDataMap() {
        dataMap = new MetaDataMap(UserRole.class, "user_role");
        dataMap.addColumn("user_id", "int", "userId");
        dataMap.addColumn("role_id", "int", "roleId");        
    }
    public List findUserRoles(Long userId){
        return findMany(new FindUserRoles(userId));
    }
    static class FindUserRoles implements StatementSource{	
        private Long userid;
        public FindUserRoles(Long userid){
            this.userid = userid;
        }
	public String sql(){
	    return "select * from user_role where user_id=? ";
	}
	public Object[] parameters(){	    
	    return new Object[] {userid};
	}
    }
}
