/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author aldo
 */
public class TimeHelper {
    public static Timestamp UTCNow(){
	//java.util.Date today = new java.util.Date();
	//return new java.sql.Timestamp(today.getTime());
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        //Local time zone   
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        //Time in GMT
        
        Long now=null;
        try{
            now = dateFormatLocal.parse( dateFormatGmt.format(new Date()) ).getTime();
        }catch(ParseException pe){
            throw new RuntimeException(pe);
        }
        return new java.sql.Timestamp(now);


    }
}
