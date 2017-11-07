package net.danielpark.library;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import net.danielpark.library.util.AppUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by namgyu.park on 2017. 10. 28..
 */

@RunWith(AndroidJUnit4.class)
public class AppUtilInstrumentedTest {

    @Test
    public void getAppPackageName() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        // TODO: Only works in debug mode
        assertEquals("net.danielpark.library.test",
                AppUtil.getAppPackageName(appContext));
    }
}
