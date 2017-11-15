package net.danielpark.library.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        throw new UnsupportedOperationException();
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
     * Get the uri's file path <br>
     *     <p>If uri is null, then get the latest image's path </p>
     *     <p>Google photo app has unique rule. so make sure to check. </p>
     * @param context {@link Context}
     * @param uri       image uri
     * @return      target file
     */
    @Nullable
    public static File getImageFileFromGallery(Context context, Uri uri) {
        ContentResolver resolver = context.getContentResolver();

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

        if (StringUtil.isNullorEmpty(path)) {
            if (uri.toString().startsWith("content://com.google.android.apps.photos.content")) {
                return writeCloudImageToFile(context, uri, resolver);
            }
            else {
                // Sorry. You gotta add another support app.
                return null;
            }

        }
        return new File(path);
    }

    /**
     * This is for google photo app.
     * @param imageUri  image uri
     * @return copied new file
     */
    @Nullable
    private static File writeCloudImageToFile(Context context, @NonNull Uri imageUri, @NonNull ContentResolver contentResolver) {
        try {
            InputStream is = contentResolver.openInputStream(imageUri);
            File outputFile = FileUtil.createImageFile(context);

            if (copyToFile(is, outputFile)) {
                return outputFile;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Copy file
     *
     * @param srcFile  : source file to copy
     * @param destFile : output file to be pasted.
     * @return return <code>true</code> if it's copied successfully
     */
    public static boolean copyFile(File srcFile, File destFile) {
        if (srcFile == null || destFile == null)
            return false;

        boolean result = false;
        try {
            InputStream is = new FileInputStream(srcFile);
            try {
                result = copyToFile(is, destFile);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            return false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     *
     * @param inputStream       input stream
     * @param destFile          temp file where to be pasted
     * @return return <code>true</code> if it's copied successfully
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
