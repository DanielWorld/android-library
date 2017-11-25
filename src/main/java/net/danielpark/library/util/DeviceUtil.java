package net.danielpark.library.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by Daniel Park on 2017. 11. 21..
 */

public final class DeviceUtil {

    private DeviceUtil() {

    }

    /**
     * Get device resolution
     * @param context {@link Context}
     * @return device resolution as {@link Display}
     * @throws Exception error
     */
    public static Display getResolutionSize(Context context) throws Exception {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display;
    }

    /**
     * Get device resolution width. return 0 if it fails <br>
     *     if it is LANDSCAPE MODE change height to width
     * @param context {@link Context}
     * @return device resolution width
     */
    public static int getResolutionWidth(Context context){
        try {
            return getResolutionSize(context).getWidth();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get device resolution height. return 0 if it fails <br>
     *     if it is LANDSCAPE MODE change width to height
     * @param context {@link Context}
     * @return  device resolution height
     */
    public static int getResolutionHeight(Context context){
        try{
            return getResolutionSize(context).getHeight();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get device inches
     * @param context {@link Context}
     * @return get actual device's inches.
     */
    public static double getDeviceInches(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getResolutionSize(context).getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            double wi = (double) width / dm.xdpi;
            double hi = (double) height / dm.ydpi;
            double x = Math.pow(wi, 2);
            double y = Math.pow(hi, 2);

            return Math.sqrt(x + y);
        } catch (Exception e) {
            return 0;
        }
    }
}
