/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package core.security;

import core.app.AppSession;
import core.app.AppSessionManager;
import core.app.BaseService;
import core.entities.Roles;
import core.entities.UserRole;
import core.mappers.RoleMapper;
import core.mappers.UserRoleMapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aldo
 */
/*
if($required_role == $user_role)
			return TRUE;
		//check valid role	
		if(!in_array($user_role,array_keys($this->user_roles)))
			return FALSE;
		$parent = NULL;
		$role = $this->user_roles[$user_role];		
		$i=0;
		do{			
			$parent = $role['parent'];			
			if($parent==$required_role){
				return TRUE;
			}else{
				$role=$this->user_roles[$parent];
			}
			$i++;
			if($i==100)die('loop'.$required_role.'--'.$user_role);
		}while($role['parent']!=NULL);			
		return FALSE;
*/
public class SimpleRoleCheck extends BaseService{
    private Map<Long,Roles> roles = new HashMap<Long,Roles>();
    RoleMapper mapper;
    private RoleMapper getRoleMapper(){
        if(mapper==null){
            AppSession session = AppSessionManager.getSession();
            if(session==null)
                throw new RuntimeException("Session is null");
             mapper = (RoleMapper)session.getMapper(Roles.class);
        }
        return mapper;
    }
    UserRoleMapper userRoleMapper;
    private UserRoleMapper getUserRoleMapper(){
        if(userRoleMapper==null)
             userRoleMapper = (UserRoleMapper)AppSessionManager.getSession().getMapper(UserRole.class);
        return userRoleMapper;
    }
    public SimpleRoleCheck(){
        loadRoles();
    }
    public void loadRoles(){
        List roles = getRoleMapper().findAllRoles();
        for(Iterator it=roles.iterator();it.hasNext();){
            Roles role = (Roles)it.next();
            this.roles.put(role.getID(), role);
        }
    }
    public boolean check(String requiredRole,Long userId){
        Roles requiredrole = getRoleMapper().findByRoleName(requiredRole);
        List userRoles = getUserRoleMapper().findUserRoles(userId);
        if(userRoles==null){
            return false;
        }else{
            for(Iterator it=userRoles.iterator();it.hasNext();){
                UserRole ur = (UserRole)it.next();
                if(check(requiredrole.getID(),ur.getRoleId()))
                    return true;
            }
            return false;
        }
    }/*
    public boolean check(String requiredRole,String userRole){         
         Roles requiredrole = getRoleMapper().findByRoleName(requiredRole);
         Roles userrole = getRoleMapper().findByRoleName(userRole);
         if(requiredrole == null || userrole==null)
             return false;
         return this.check(requiredrole.getID(), userrole.getID());
    }*/
    public boolean check(Long requiredRole,Long userRole){
        if(requiredRole==userRole)
            return true;
        
        if(!roles.containsKey(userRole))
            return false;
        Long parent;
        Roles role = roles.get(userRole);
        int i=0;
        int maxIter = 100;        
        do{
            parent = role.getParentId();            
            if(parent.equals(requiredRole)){
                return true;
            }else{
                role = roles.get(parent);
            }
            i++;
            if(i==maxIter)
                break;
        }while(role!=null);
        return false;
    }
}
