package net.danielpark.library.log;

import android.util.Log;

import net.danielpark.library.BuildConfig;

/**
 * Created by namgyu.park on 2017. 10. 21..
 */

public final class Logger {

    private boolean mLogFlag = BuildConfig.DEBUG;

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

    public void v(String TAG, String msg) {
        if (mLogFlag) {
            Log.v(TAG, buildLogMsg(msg));
        }
    }

    public void d(String TAG, String msg) {
        if (mLogFlag) {
            Log.d(TAG, buildLogMsg(msg));
        }
    }

    public void e(String TAG, String msg) {
        if (mLogFlag) {
            Log.e(TAG, buildLogMsg(msg));
        }
    }

    public void i(String TAG, String msg) {
        if (mLogFlag) {
            Log.i(TAG, buildLogMsg(msg));
        }
    }

    public void w(String TAG, String msg) {
        if (mLogFlag) {
            Log.w(TAG, buildLogMsg(msg));
        }
    }

    public void v(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.v(TAG, buildLogMsg(msg), throwable);
        }
    }

    public void d(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.d(TAG, buildLogMsg(msg), throwable);
        }
    }

    public void e(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.e(TAG, buildLogMsg(msg), throwable);
        }
    }

    public void i(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.i(TAG, buildLogMsg(msg), throwable);
        }
    }

    public void w(String TAG, String msg, Throwable throwable) {
        if (mLogFlag) {
            Log.w(TAG, buildLogMsg(msg), throwable);
        }
    }
}
