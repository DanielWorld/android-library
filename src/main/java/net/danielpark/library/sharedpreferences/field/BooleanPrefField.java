package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class BooleanPrefField extends AbstractPrefField<Boolean>{

    public BooleanPrefField(SharedPreferences sharedPreferences, String key, Boolean defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Boolean getOr(Boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    protected void putInternal(Boolean value) {
        apply(edit().putBoolean(key, value));
    }
}
