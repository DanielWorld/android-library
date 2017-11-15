package net.danielpark.library.util;

import android.content.Context;

import net.danielpark.library.log.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by namgyu.park on 2017. 11. 7..
 */

@RunWith(RobolectricTestRunner.class)
public class AppUtilUnitTest {

    private Context context;

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void getAppPackageName() throws Exception {
        assertThat(AppUtil.getAppPackageName(context), is("net.danielpark.library"));
    }
}
