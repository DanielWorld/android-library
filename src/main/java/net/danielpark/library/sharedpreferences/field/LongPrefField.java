package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class LongPrefField extends AbstractPrefField<Long> {

    public LongPrefField(SharedPreferences sharedPreferences, String key, Long defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    public Long getOr(Long defaultValue) {
        try {
            return Long.valueOf(this.sharedPreferences.getLong(this.key, defaultValue.longValue()));
        } catch (ClassCastException var5) {
            try {
                String e2 = this.sharedPreferences.getString(this.key, "" + defaultValue);
                return Long.valueOf(Long.parseLong(e2));
            } catch (Exception var4) {
                throw var5;
            }
        }
    }

    protected void putInternal(Long value) {
        this.apply(this.edit().putLong(this.key, value.longValue()));
    }
}
