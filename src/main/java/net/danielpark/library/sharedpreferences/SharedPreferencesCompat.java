package net.danielpark.library.sharedpreferences;

import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Copyright (c) 2014-2017 op7773hons@gmail.com
 * Created by Daniel Park on 2017-05-05.
 */

public abstract class SharedPreferencesCompat {

    private SharedPreferencesCompat() {
    }

    private static final Method APPLY_METHOD = findMethod(SharedPreferences.Editor.class, "apply");
    private static final Method GET_STRING_SET_METHOD = findMethod(SharedPreferences.class, "getStringSet", String.class, Set.class);
    private static final Method PUT_STRING_SET_METHOD = findMethod(SharedPreferences.Editor.class, "putStringSet", String.class, Set.class);

    public static void apply(SharedPreferences.Editor editor) {
        try {
            invoke(APPLY_METHOD, editor);
            return;
        } catch (NoSuchMethodException e) {
            editor.commit();
        }
    }

    public static Set<String> getStringSet(SharedPreferences preferences, String key, Set<String> defValues) {
        try {
            return invoke(GET_STRING_SET_METHOD, preferences, key, defValues);
        } catch (NoSuchMethodException e) {
            String serializedSet = preferences.getString(key, null);
            if (serializedSet == null) {
                return defValues;
            }
            return SetXmlSerializer.deserialize(serializedSet);
        }
    }

    public static void putStringSet(SharedPreferences.Editor editor, String key, Set<String> values) {
        try {
            invoke(PUT_STRING_SET_METHOD, editor, key, values);
        } catch (NoSuchMethodException e1) {
            editor.putString(key, SetXmlSerializer.serialize(values));
        }
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException unused) {
            // fall through
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Method method, Object obj, Object... args) throws NoSuchMethodException {
        if (method == null) {
            throw new NoSuchMethodException();
        }

        try {
            return (T) method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            // fall through
        } catch (IllegalArgumentException e) {
            // fall through
        } catch (InvocationTargetException e) {
            // fall through
        }

        throw new NoSuchMethodException(method.getName());
    }
}
