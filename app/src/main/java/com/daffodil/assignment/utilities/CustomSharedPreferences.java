package com.daffodil.assignment.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Set;


public class CustomSharedPreferences implements SharedPreferences {
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;

    /*
     * The default constructor which setup resources with custom secret key.
     *
     * @param context        Application Context
     * @param preferenceName shared preference file name.
     * @param secretKey      key used encryption/decryption.
     */
    /* default */ CustomSharedPreferences(Context context, String preferenceName, String secretKey) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(preferenceName, 0);
    }

    /**
     * The default constructor which setup resources with latest secret key.
     *
     * @param context        Application Context
     * @param preferenceName shared preference file name.
     */
    public CustomSharedPreferences(Context context, String preferenceName) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(preferenceName, 0);
    }

    @Override
    public Editor edit() {
        return new Editor();
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        final String v = getString(key, null);
        return v != null ? Boolean.parseBoolean(v) : defValue;
    }

    @Override
    public float getFloat(String key, float defValue) {
        final String v = getString(key, null);
        return v != null ? Float.parseFloat(v) : defValue;
    }

    @Override
    public int getInt(String key, int defValue) {
        final String v = getString(key, null);
        return v != null ? Integer.parseInt(v) : defValue;
    }

    @Override
    public long getLong(String key, long defValue) {
        final String v = getString(key, null);
        return v != null ? Long.parseLong(v) : defValue;
    }

    @Override
    public String getString(String key, String defValue) {
        final String v = mSharedPreferences.getString(key, null);
        return v != null ? v : defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, @Nullable Set<String> set) {
        final Set<String> v = mSharedPreferences.getStringSet(s, null);

        return v != null ? v : set;
    }

    @Override
    public boolean contains(String s) {
        return mSharedPreferences.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public class Editor implements SharedPreferences.Editor {
        private final SharedPreferences.Editor mEditor;

        @SuppressLint("CommitPrefEdits")
            /* default */ Editor() {
            mEditor = mSharedPreferences.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            putString(key, Boolean.toString(value));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            putString(key, Float.toString(value));
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            putString(key, Integer.toString(value));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            putString(key, Long.toString(value));
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            try {
                mEditor.putString(key, value);
                commit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String s, @Nullable Set<String> set) {
            mEditor.putStringSet(s, set);
            return this;
        }

        @Override
        public void apply() {
            mEditor.apply();
        }

        @Override
        public Editor clear() {
            mEditor.clear();
            commit();
            return this;
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        public Editor remove(String s) {
            mEditor.remove(s);
            commit();
            return this;
        }
    }
}