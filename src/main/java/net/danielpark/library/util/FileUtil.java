package net.danielpark.library.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by namgyu.park on 2017. 11. 10..
 */

public final class FileUtil {

    private FileUtil() {

    }

    public static String saveToFile(String filename, String contents) {
        String storageState = Environment.getExternalStorageState();

        if(!storageState.equals(Environment.MEDIA_MOUNTED)) {
            throw new IllegalStateException("Media must be mounted");
        }

        File directory = Environment.getExternalStorageDirectory();
        File file = new File(directory, filename);
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(file, false);
            fileWriter.write(contents);
            fileWriter.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File createImageFile(Context context) {
        return createImageFile(context, "temp_image");
    }

    public static File createImageFile(Context context, String fileName) {
        String imageFileName = fileName + System.currentTimeMillis() + ".jpg";
        File root = context.getExternalCacheDir();  // use external cache directory

        if (root != null && !root.exists())
            root.mkdirs();

        return new File(root, imageFileName);
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     *
     * @param uri
     * @return
     */
    public static File getImageFileFromGallery(ContentResolver resolver, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = resolver.query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        if (column_index < 0)
            return null;

        String path = mCursor.getString(column_index);

        mCursor.close();

        if (StringUtil.isNullorEmpty(path))
            return null;
        return new File(path);
    }

    /**
     * Copy file
     *
     * @param srcFile  : source file to copy
     * @param destFile : output file to be pasted.
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        if (srcFile == null || destFile == null)
            return false;

        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            return false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
