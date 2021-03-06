package net.danielpark.library.util;

import android.content.Context;

/**
 * Created by namgyu.park on 2017. 10. 28..
 */

public final class AppUtil {

    private AppUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get application package name
     *
     * @param context  {@link Context}
     * @return  package name
     * @throws Exception        throws exception
     */
    public static String getAppPackageName(Context context) throws Exception {
        return context.getPackageName();
    }
}
