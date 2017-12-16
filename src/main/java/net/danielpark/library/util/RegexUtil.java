package net.danielpark.library.util;

import java.util.regex.Pattern;

/**
 * Created by Daniel Park on 2017. 12. 15..
 */

public final class RegexUtil {

    private RegexUtil() {

    }

    public static boolean isValidColorString(String colorString) {
        String colorRegex = "^#?([a-f0-9A-F]{6}|[a-f0-9A-F]{8})$";
        return Pattern.matches(colorRegex, colorString);
    }

    public static boolean isValidFloatString(String floatString) {
        // Daniel (2017-12-15 16:24:39) : accept from 0.? to 1.0
        String floatNumberRegex = "^(([0][.][0-9]+)|([1]([.][0]+|)))$";
        return Pattern.matches(floatNumberRegex, floatString);
    }
}
