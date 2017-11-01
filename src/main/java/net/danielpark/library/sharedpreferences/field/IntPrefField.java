package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class IntPrefField extends AbstractPrefField<Integer> {

    public IntPrefField(SharedPreferences sharedPreferences, String key, Integer defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Integer getOr(Integer defaultValue) {
        try {
            return sharedPreferences.getInt(key, defaultValue);
        } catch (ClassCastException e) {
            // The pref could be a String, if that is the case try this
            // recovery bit
            try {
                String value = sharedPreferences.getString(key, "" + defaultValue);
                return Integer.parseInt(value);
            } catch (Exception e2) {
                // our recovery bit failed. The problem is elsewhere. Send the
                // original error
                throw e;
            }
        }

    }

    @Override
    protected void putInternal(Integer value) {
        apply(edit().putInt(key, value));
    }
}
