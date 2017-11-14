package net.danielpark.library.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static net.danielpark.library.util.Preconditions.checkNotNull;

/**
 * {@link android.support.v4.app.Fragment} 관련 유틸리티
 * <br><br>
 * Copyright (C) 2014-2017 daniel@bapul.net
 * Created by Daniel on 2017-02-01.
 */

public final class FragmentUtil {

	private FragmentUtil(){}

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, @IdRes int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    /**
     * The container view with id {@code frameId} is replaced by the {@code fragment}. The operation is
     * performed by the {@code fragmentManager}
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    public static void replaceFragment(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment, @IdRes int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commitNowAllowingStateLoss();
    }
}
