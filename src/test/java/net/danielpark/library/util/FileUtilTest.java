package net.danielpark.library.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowEnvironment;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by namgyu.park on 2017. 11. 10..
 */

@RunWith(RobolectricTestRunner.class)
public class FileUtilTest {

    @Test
    public void saveToFile() {
        String filename = "test.txt";
        String expectedContents = "test contents";

        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
        String absolutePath = FileUtil.saveToFile(filename, expectedContents);

        File expectedFile = new File(absolutePath);
        Assert.assertTrue(expectedFile.exists());
        expectedFile.delete();
    }

    @Test(expected = IllegalStateException.class)
    public void saveToFile_mediaUnmounted() {
        String filename = "test.txt";
        String expectedContents = "test contents";

        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_UNMOUNTED);
        FileUtil.saveToFile(filename, expectedContents);
    }
}
