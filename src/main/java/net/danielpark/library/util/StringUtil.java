package net.danielpark.library.util;

/**
 * Created by namgyu.park on 2017. 10. 21..
 */

public final class StringUtil {

    private StringUtil() {}

    /**
     If parameter String is null or length == 0 or length without empty space == 0 then return true
     * @param str target String
     * @return <b>true</b> : if target String is null or empty or blank
     */
    public static boolean isNullorEmpty(String str){
        try {
            return (str == null || str.length() == 0 || str.trim().length() == 0 || str.trim().toLowerCase().equals("null"));
        } catch (Exception e){
            return true;
        }
    }
}
