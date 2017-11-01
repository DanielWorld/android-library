package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class StringPrefField extends AbstractPrefField<String>{

    public StringPrefField(SharedPreferences sharedPreferences, String key, String defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public String getOr(String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    protected void putInternal(String value) {
        apply(edit().putString(key, value));
    }
}
