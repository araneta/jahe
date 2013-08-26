/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException; 
import core.db.JDBCHelper;
import core.helpers.ThreadLocalRegistry;
import core.helpers.ThreadLocalRegistry;
/**
 *
 * @author aldo
 */
public class DB {
    
    public static PreparedStatement prepare(String sql) throws SQLException{
	PreparedStatement ps = null;
	try{
	    ThreadLocalRegistry.begin();
	    ps = ThreadLocalRegistry.getConnection().prepareStatement(sql);
	}finally{
	    ThreadLocalRegistry.end();
	}
	return ps;
    }
    public static void cleanUp(PreparedStatement ps,ResultSet rs){
	JDBCHelper.close(rs);  
	JDBCHelper.close(ps);  
    }
    public static void cleanUp(PreparedStatement ps,ResultSet rs,Connection conn){
	JDBCHelper.close(rs);  
	JDBCHelper.close(ps);  
	JDBCHelper.close(conn);
    }
    public static Connection newConnection(){
	return JDBCHelper.getJNDIConnection();
    }
}
