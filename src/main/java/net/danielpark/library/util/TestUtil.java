package net.danielpark.library.util;

import android.support.annotation.VisibleForTesting;

/**
 * Created by namgyu.park on 2017. 10. 23..
 */

public final class TestUtil {

    private TestUtil(){}

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static boolean testMethod(int i) {
        return i == 0;
    }
}
