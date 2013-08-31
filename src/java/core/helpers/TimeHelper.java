/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import java.sql.Timestamp;

/**
 *
 * @author aldo
 */
public class TimeHelper {
    public static Timestamp UTCNow(){
	java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
    }
}
