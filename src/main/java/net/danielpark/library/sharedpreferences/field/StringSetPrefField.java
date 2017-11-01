package net.danielpark.library.sharedpreferences.field;

import android.content.SharedPreferences;
import net.danielpark.library.sharedpreferences.SharedPreferencesCompat;

import java.util.Set;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public class StringSetPrefField extends AbstractPrefField<Set<String>> {

    public StringSetPrefField(SharedPreferences sharedPreferences, String key, Set<String> defaultValue) {
        super(sharedPreferences, key, defaultValue);
    }

    @Override
    public Set<String> getOr(Set<String> defaultValue) {
        return SharedPreferencesCompat.getStringSet(sharedPreferences, key, defaultValue);
    }

    @Override
    protected void putInternal(Set<String> value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferencesCompat.putStringSet(editor, key, value);
        apply(editor);
    }
}
