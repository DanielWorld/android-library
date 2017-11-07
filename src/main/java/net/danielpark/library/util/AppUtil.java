package net.danielpark.library.util;

import android.content.Context;

/**
 * Created by namgyu.park on 2017. 10. 28..
 */

public final class AppUtil {

    private AppUtil(){}

    /**
     * Get application package name
     *
     * @return package name
     */
    public static String getAppPackageName(Context context) throws Exception {
        return context.getPackageName();
    }
}
