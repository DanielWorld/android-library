package net.danielpark.library.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Check runtime permission using {@link EventBus}
 * <p>
 *     Make sure to subscribe {@link PermissionState}
 * </p>
 * <pre>
 *     \@Subscribe(threadMode = ThreadMode.Main)
 *     public void onWhateverEvent(PermissionState state) {
 *         ...
 *     }
 *
 *     ...
 *
 *     public void onStart() {
 *         super.onStart();
 *         eventbus.register(this);
 *     }
 *
 *     ...
 *
 *     public void onStop() {
 *         eventbus.unregister(this);
 *         super.onStop();
 *     }
 * </pre>
 * <br><br>
 * Created by namgyu.park on 2017. 10. 26..
 */

public final class PermissionChecker {

	private final int REQUEST_RUNTIME_PERMISSION = 10212;

	private final boolean isActivity;

	@Nullable
	private Activity activity;

	@NonNull
	private Fragment fragment;

	@NonNull
	private String[] permissions;

	@NonNull
	private EventBus eventBus;

	public enum PermissionState {
		Granted, Denied, Farewell
	}

	public PermissionChecker(@NonNull Activity activity) {
		this.activity = activity;
		this.isActivity = true;
	}

	public PermissionChecker(@NonNull Fragment fragment) {
		this.fragment = fragment;
		this.isActivity = false;
	}

	public PermissionChecker withPermissions(@NonNull String[] permissions) {
		this.permissions = permissions;
		return this;
	}

	public PermissionChecker withListeners(@NonNull EventBus eventBus) {
		this.eventBus = eventBus;
		return this;
	}

	public void check() {
		if (activity == null && fragment == null) return;
		if (permissions == null || permissions.length == 0)
			throw new RuntimeException("Make sure to set permission to request");
		if (eventBus == null)
			throw new RuntimeException("EventBus cannot be null!");

		if (Build.VERSION.SDK_INT >= 23) {

			final ArrayList<String> deniedPermissions = new ArrayList<>();
			for (String permission : permissions) {
				if (ContextCompat.checkSelfPermission(isActivity ? activity : fragment.getContext(), permission) != PackageManager.PERMISSION_GRANTED)
					deniedPermissions.add(permission);
			}

			if (deniedPermissions.isEmpty())
				eventBus.post(PermissionState.Granted);
			else {
				if (isActivity)
					activity.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_RUNTIME_PERMISSION);
				else
					fragment.requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_RUNTIME_PERMISSION);
			}
		}
		else {
			eventBus.post(PermissionState.Granted);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		if (requestCode == REQUEST_RUNTIME_PERMISSION) {
			PermissionState permissionState = PermissionState.Granted;

			for (int count = 0; count < permissions.length; count++) {
				if (grantResults[count] != PackageManager.PERMISSION_GRANTED) {
					if (isActivity) {
						if (!activity.shouldShowRequestPermissionRationale(permissions[count]))
							permissionState = PermissionState.Farewell;
						else
							permissionState = PermissionState.Denied;
					}
					else {
						if (!fragment.shouldShowRequestPermissionRationale(permissions[count]))
							permissionState = PermissionState.Farewell;
						else
							permissionState = PermissionState.Denied;
					}

					break;
				}
			}

			eventBus.post(permissionState);
		}
	}

}
