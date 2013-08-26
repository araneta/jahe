/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
/**
 *
 * @author aldo
 */
public final class JDBCHelper {
 
  /**
   * Closes given result set hard
   */
  public static void close(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (Exception excp) {
        // Ignore, we don't care, we do care when we open it
      }
    }
  }
 
 
  /**
   * Closes given PreparedStatement set hard
   */
  public static void close(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
      } catch (Exception excp) {
        // Ignore, we don't care, we do care when we open it
      }
    }
  }
 
 
  /**
   * Closes given Connection set hard
   */
  public static void close(Connection cn) {
    if (cn != null) {
      try {
        cn.close();
      } catch (Exception excp) {
        // Ignore, we don't care, we do care when we open it
      }
    }
  }
 
 
  /**
   * Closes given Statement set hard
   */
  public static void close(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (Exception excp) {
        // Ignore, we don't care, we do care when we open it
      }
    }
  }
 
 
  public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
    close(rs);
    close(ps);
    close(conn);
  }
   /** Uses JNDI and Datasource (preferred style).   */
  public static Connection getJNDIConnection() {
    String DATASOURCE_CONTEXT = "java:comp/env/jdbc/java";
    
    Connection result = null;
    try {
      Context initialContext = new InitialContext();
      if ( initialContext == null){
	throw new RuntimeException("JNDI problem. Cannot get InitialContext.");        
      }
      DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
      if (datasource != null) {
        result = datasource.getConnection();
      }
      else {
        throw new RuntimeException("Failed to lookup datasource.");
      }
    }
    catch ( NamingException ex ) {	
      throw new RuntimeException(ex);
    }
    catch(SQLException ex){
      throw new RuntimeException(ex);
    }
    return result;
  }

  /** Uses DriverManager.  */
  public static Connection getSimpleConnection() {
    //See your driver documentation for the proper format of this string :
    String DB_CONN_STRING = "jdbc:mysql://localhost:3306/java";
    //Provided by your driver documentation. In this case, a MySql driver is used : 
    String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
    String USER_NAME = "juliex";
    String PASSWORD = "ui893djf";
    
    Connection result = null;
    try {
       Class.forName(DRIVER_CLASS_NAME).newInstance();
    }
    catch (Exception ex){
       log("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
    }

    try {
      result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
    }
    catch (SQLException e){
       log( "Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
    }
    return result;
  }

  private static void log(Object aObject){
    System.out.println(aObject);
  }
}