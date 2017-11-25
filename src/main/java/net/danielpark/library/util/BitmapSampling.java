package net.danielpark.library.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Manage to bitmap's sampling
 * <br><br>
 * Created by namgyu.park on 2017. 11. 11..
 */

public final class BitmapSampling {
    private static final int DEFAULT_MAX_WIDTH = 2048;
    private static final int DEFAULT_MAX_HEIGHT = 2048;

    private BitmapSampling() {
        throw new UnsupportedOperationException();
    }

    public static Bitmap getBitmap(File file) {
        if (file == null || file.isDirectory() || !file.exists())
            throw new RuntimeException("File path is null or incorrect!");

        return getBitmap(file, DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT, null);
    }

    public static Bitmap getBitmap(File file, int reqWidth, int reqHeight, BitmapFactory.Options options) {
        if (file == null || file.isDirectory() || !file.exists())
            throw new RuntimeException("File path is null or incorrect!");

        if (options == null)
            options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap getBitmap(InputStream in) {
        if (in == null)
            throw new RuntimeException("InputStream cannot be null!");

        return getBitmap(in, DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT, null);
    }

    public static Bitmap getBitmap(InputStream in, int reqWidth, int reqHeight, BitmapFactory.Options options) {
        if (in == null)
            throw new RuntimeException("InputStream cannot be null!");

        byte[] dataArray = getSizeData(in);

        if (options == null)
            options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(dataArray, 0, dataArray.length, options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(dataArray, 0, dataArray.length, options);
    }

    public static Bitmap getBitmap(Resources res, @DrawableRes int drawableRes, int reqWidth, int reqHeight) {
        if (res == null)
            throw new RuntimeException("Resources cannot be null!");

        return getBitmap(res, drawableRes, reqWidth, reqHeight, null);
    }

    public static Bitmap getBitmap(Resources res, @DrawableRes int drawableRes, int reqWidth, int reqHeight, BitmapFactory.Options options) {

        if (options == null)
            options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, drawableRes, options);
        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, drawableRes, options);
    }

    // ----------------------------

    private static int calculateSampleSize(int bitmapW, int bitmapH, int requestWidth, int requestHeight) {
        int reqWidth = requestWidth;
        int reqHeight = requestHeight;

        // Raw height and width of image
        final int bitmapWidth = bitmapW;
        final int bitmapHeight = bitmapH;
        int inSampleSize = 1;

        if (reqWidth * reqHeight <= 0) {
            return inSampleSize;
        }

        if (bitmapWidth > reqWidth || bitmapHeight > reqHeight) {
            final int halfHeight = bitmapHeight / 2;
            final int halfWidth = bitmapWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) > reqWidth || (halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static int calculateSampleSize(BitmapFactory.Options options, int w, int h) {
        return calculateSampleSize(options.outWidth, options.outHeight, w, h);
    }

    /**
     * Daniel (2016-01-14 12:11:22): get byte[] from InputStream
     *
     * @param is {@link InputStream}
     * @return byte[]
     */
    private static byte[] getSizeData(InputStream is) {
        ByteArrayOutputStream byteBuffer;
        try {
            byteBuffer = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            return new byte[]{};
        }
    }
}
