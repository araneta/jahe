/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.app;

import app.mappers.BookMapper;

/**
 *
 * @author aldo
 */
public class AppSessionManager {
    private static ThreadLocal current = new ThreadLocal();
    
    public static AppSession getSession(){
	return (AppSession) current.get();
    }
    public static void setSession(AppSession session){
	current.set(session);
    }
    
}
