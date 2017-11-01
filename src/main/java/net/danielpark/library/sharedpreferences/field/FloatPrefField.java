package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class FloatPrefField extends AbstractPrefField<Float> {

    public FloatPrefField(SharedPreferences sharedPreferences, String key, Float defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    public Float getOr(Float defaultValue) {
        try {
            return Float.valueOf(this.sharedPreferences.getFloat(this.key, defaultValue.floatValue()));
        } catch (ClassCastException var5) {
            try {
                String e2 = this.sharedPreferences.getString(this.key, "" + defaultValue);
                return Float.valueOf(Float.parseFloat(e2));
            } catch (Exception var4) {
                throw var5;
            }
        }
    }

    protected void putInternal(Float value) {
        this.apply(this.edit().putFloat(this.key, value.floatValue()));
    }
}
