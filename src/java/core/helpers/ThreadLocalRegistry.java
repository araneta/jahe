/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
import core.db.JDBCHelper;
import core.db.KeyGenerator;
/**
 *
 * @author aldo
 */

public class ThreadLocalRegistry  {
    private static ThreadLocal instances = new ThreadLocal();
    public static ThreadLocalRegistry getInstance(){
	return (ThreadLocalRegistry)instances.get();
    }
    public static void begin(){
	//Assert.isTrue(instances.get()==null);
	instances.set(new ThreadLocalRegistry());
    }
    public static void end(){
	//Assert.notNull(getInstance());
	instances.set(null);
    }
    private Connection connection = JDBCHelper.getJNDIConnection();
    
    public static Connection getConnection(){
	return getInstance().connection;
	
    }
}
