package net.danielpark.library.sharedpreferences;

import android.content.SharedPreferences;

import net.danielpark.library.sharedpreferences.field.BooleanPrefField;
import net.danielpark.library.sharedpreferences.field.FloatPrefField;
import net.danielpark.library.sharedpreferences.field.IntPrefField;
import net.danielpark.library.sharedpreferences.field.LongPrefField;
import net.danielpark.library.sharedpreferences.field.StringPrefField;
import net.danielpark.library.sharedpreferences.field.StringSetPrefField;

import java.util.Set;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public abstract class SharedPrefHelper {

    private final SharedPreferences sharedPreferences;

    public SharedPrefHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public final SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public final void clear() {
        sharedPreferences.edit().clear().apply();
    }

    // Daniel (2017-01-19 15:22:44): String, Boolean, Float, Long, Int 지원
    protected IntPrefField intField(String key, int defaultValue) {
        return new IntPrefField(sharedPreferences, key, defaultValue);
    }

    protected StringPrefField stringField(String key, String defaultValue) {
        return new StringPrefField(sharedPreferences, key, defaultValue);
    }

    protected StringSetPrefField stringSetField(String key, Set<String> defaultValue) {
        return new StringSetPrefField(sharedPreferences, key, defaultValue);
    }

    protected BooleanPrefField booleanField(String key, boolean defaultValue) {
        return new BooleanPrefField(sharedPreferences, key, defaultValue);
    }

    protected FloatPrefField floatField(String key, float defaultValue) {
        return new FloatPrefField(sharedPreferences, key, defaultValue);
    }

    protected LongPrefField longField(String key, long defaultValue) {
        return new LongPrefField(sharedPreferences, key, defaultValue);
    }

}
