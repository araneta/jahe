/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.helpers;

/**
 *
 * @author aldo
 */
public class StringUtils {

    /**
     * <code>\u000a</code> linefeed LF ('\n').
     *
     * @see <a
     * href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF:
     * Escape Sequences for Character and String Literals</a>
     * @since 2.2
     */
    public static final char LF = '\n';
    /**
     * <code>\u000d</code> carriage return CR ('\r').
     *
     * @see <a
     * href="http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#101089">JLF:
     * Escape Sequences for Character and String Literals</a>
     * @since 2.2
     */
    public static final char CR = '\r';
    // Chopping
    //-----------------------------------------------------------------------

    /**
     * <p>Remove the last character from a String.</p>
     *
     * <p>If the String ends in
     * <code>\r\n</code>, then remove both of them.</p>
     *
     * <pre>
     * StringUtils.chop(null)          = null
     * StringUtils.chop("")            = ""
     * StringUtils.chop("abc \r")      = "abc "
     * StringUtils.chop("abc\n")       = "abc"
     * StringUtils.chop("abc\r\n")     = "abc"
     * StringUtils.chop("abc")         = "ab"
     * StringUtils.chop("abc\nabc")    = "abc\nab"
     * StringUtils.chop("a")           = ""
     * StringUtils.chop("\r")          = ""
     * StringUtils.chop("\n")          = ""
     * StringUtils.chop("\r\n")        = ""
     * </pre>
     *
     * @param str the String to chop last character from, may be null
     * @return String without last character, <code>null</code> if null String
     * input
     */
    public static String chop(String str) {
        if (str == null) {
            return null;
        }
        int strLen = str.length();
        if (strLen < 2) {
            return "";
        }
        int lastIdx = strLen - 1;
        String ret = str.substring(0, lastIdx);
        char last = str.charAt(lastIdx);
        if (last == LF) {
            if (ret.charAt(lastIdx - 1) == CR) {
                return ret.substring(0, lastIdx - 1);
            }
        }
        return ret;
    }
    // Empty checks
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().</p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
