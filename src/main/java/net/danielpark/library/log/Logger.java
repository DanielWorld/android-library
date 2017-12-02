package net.danielpark.library.log;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import net.danielpark.library.BuildConfig;

/**
 * Created by namgyu.park on 2017. 10. 21..
 */

public final class Logger {

    private Logger() {}

    private static boolean mLogFlag = BuildConfig.DEBUG;

    public static void setLogState(boolean isLogEnabled) {
        mLogFlag = isLogEnabled;
    }

    /**
     * This is useful format for Logging. <br>
     *     Represents that log's position in detail.
     * @param message
     * @return
     */
    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[[");
        sb.append(ste.getFileName());
        sb.append(">");
        sb.append(ste.getMethodName());
        sb.append(">#");
        sb.append(ste.getLineNumber());
        sb.append("]] ");
        sb.append(message);

        return sb.toString();
    }

    public static void v(String TAG, String msg) {
        if (mLogFlag) {
            Log.v(TAG, buildLogMsg(msg));
        }
    }

    public static void d(String TAG, String msg) {
        if (mLogFlag) {
            Log.d(TAG, buildLogMsg(msg));
        }
    }

    public static void e(String TAG, String msg) {
        if (mLogFlag) {
            Log.e(TAG, buildLogMsg(msg));
        }
    }

    public static void i(String TAG, String msg) {
        if (mLogFlag) {
            Log.i(TAG, buildLogMsg(msg));
        }
    }

    public static void w(String TAG, String msg) {
        if (mLogFlag) {
            Log.w(TAG, buildLogMsg(msg));
        }
    }

    public static void v(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.v(TAG, buildLogMsg(msg), throwable);
        }
    }

    public static void d(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.d(TAG, buildLogMsg(msg), throwable);
        }
    }

    public static void e(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.e(TAG, buildLogMsg(msg), throwable);
        }
    }

    public static void i(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.i(TAG, buildLogMsg(msg), throwable);
        }
    }

    public static void w(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.w(TAG, buildLogMsg(msg), throwable);
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static String vPrint(String TAG, String msg) {
        return TAG + buildLogMsg(msg);
    }
}
