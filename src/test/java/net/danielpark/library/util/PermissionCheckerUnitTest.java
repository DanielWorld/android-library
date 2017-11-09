package net.danielpark.library.util;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;

import net.danielpark.library.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.when;

/**
 * Created by namgyu.park on 2017. 11. 9..
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class PermissionCheckerUnitTest {

    private Activity activity;
    private ShadowApplication application;
    private PermissionChecker permissionChecker;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(Activity.class).create().start().get();
        application = Shadows.shadowOf(activity.getApplication());
        permissionChecker = new PermissionChecker(activity);
    }

    @Test
    public void activityNotNull() throws Exception {
        assertThat(activity, is(notNullValue()));
    }

    @Test
    public void checkPermissions() throws Exception {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        application.grantPermissions(permissions);
        assertThat(permissionChecker.hasDeniedPermissions(permissions).isEmpty(), is(true));
        application.denyPermissions(permissions);
        assertThat(permissionChecker.hasDeniedPermissions(permissions).isEmpty(), is(false));
        application.grantPermissions(permissions);

        permissionChecker
                .withPermissions(permissions)
                .withListener(new PermissionChecker.OnPermissionCheckerListener() {
                    @Override
                    public void onPermissionCheckerResult(@NonNull PermissionChecker.PermissionState permissionState) {
                        assertThat(permissionState, is(PermissionChecker.PermissionState.Granted));
                    }
                })
                .check();
    }
}
