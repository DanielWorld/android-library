package net.danielpark.library.log;

import android.util.Log;

import net.danielpark.library.BuildConfig;

/**
 * Created by namgyu.park on 2017. 10. 21..
 */

public final class Logger {

    private static Logger mInstance;

    public static synchronized Logger getInstance() {
        if (mInstance == null)
            mInstance = new Logger();
        return mInstance;
    }

    public Logger() {}

    private boolean mLogFlag = BuildConfig.DEBUG;

    public void v(String TAG, String msg) {
        if (mLogFlag) {
            Log.v(TAG, msg);
        }
    }

    public void d(String TAG, String msg) {
        if (mLogFlag) {
            Log.d(TAG, msg);
        }
    }

    public void e(String TAG, String msg) {
        if (mLogFlag) {
            Log.e(TAG, msg);
        }
    }

    public void i(String TAG, String msg) {
        if (mLogFlag) {
            Log.i(TAG, msg);
        }
    }

    public void w(String TAG, String msg) {
        if (mLogFlag) {
            Log.w(TAG, msg);
        }
    }
}
