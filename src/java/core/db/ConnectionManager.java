/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.db;

import java.sql.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.naming.*;
import org.apache.tomcat.jdbc.pool.DataSource;
/**
 *
 * @author aldo
 */
/*
public enum ConnectionManager {
    INSTANCE;

    private DataSource ds = null;
    private Lock connectionLock = new ReentrantLock();

    ConnectionManager() {
      try {
         final Context initCtx = new InitialContext();
         final Context envCtx = (Context) initCtx.lookup("java:comp/env");
         ds = (DataSource) envCtx.lookup("jdbc/java");
      } catch (NamingException e) {
         e.printStackTrace();
      }
    }

   public Connection getConnection(){ //throws SQLException {
      if(ds == null) return null;

      Connection conn = null;
      connectionLock.lock();
      try {
          conn = ds.getConnection();
	  
      }catch(SQLException sqle)
      {
	  throw new RuntimeException(sqle);
      }finally {
          connectionLock.unlock();
      }

      return conn;
   }
}
 * 
 */
public enum ConnectionManager {
    INSTANCE;

    private DataSource ds = null;
    private Lock connectionLock = new ReentrantLock();

    ConnectionManager() {
      try {
         final Context initCtx = new InitialContext();
         final Context envCtx = (Context) initCtx.lookup("java:comp/env");
         ds = (DataSource) envCtx.lookup("jdbc/java");
      } catch (NamingException e) {
         e.printStackTrace();
      }
    }

   public Connection getConnection(){ //throws SQLException {
      if(ds == null) return null;

      Connection conn = null;
      try {
	Future<Connection> future = ds.getConnectionAsync();
	while (!future.isDone()) {
	    System.out.println("Connection is not yet available. Do some background work");
	    try {
		Thread.sleep(100); //simulate work
	    }catch (InterruptedException x) {
		Thread.currentThread().interrupt();
	    }
	}
	conn = future.get(); //should return instantly
      }catch(SQLException sqle)
      {
	  throw new RuntimeException(sqle);
      }catch(InterruptedException ie){
	  throw new RuntimeException(ie);
      }catch(ExecutionException ie){
	  throw new RuntimeException(ie);
      }

      return conn;
   }
}