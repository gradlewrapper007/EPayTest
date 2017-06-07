package com.singh.sudhanshu.epaytest.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * Created by Sudhanshu on 05/06/17.
 */
public class PreferenceUtil {

    /**
     * PreferenceUtil singleton mInstance variable.
     */
    private static PreferenceUtil mInstance;

    /**
     * SharedPreferences object.
     */
    private SharedPreferences mSharedPrefs;

    /**
     * Preference editor object.
     */
    private Editor mPrefsEditor;

    /**
     * Constructor for PreferenceUtil.
     * 
     * @param context
     */
    private PreferenceUtil(final Context context, String name) {
        mSharedPrefs = context.getSharedPreferences(name, Activity.MODE_PRIVATE);
        mPrefsEditor = mSharedPrefs.edit();
    }

    /**
     * Initialize at starting of the application using application context.
     * 
     * @param application
     *            application object.
     */
    public static void init(final Application application, String preferenceName) {
        if (mInstance == null) {
            mInstance = new PreferenceUtil(application, preferenceName);
        }
    }

    /**
     * Get the PreferenceUtil singleton object.
     * 
     * @return PreferenceUtil object
     */
    public static PreferenceUtil getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("Must run init(Application application)"
                    + " before an mInstance can be obtained");
        }
        return mInstance;
    }

    /**
     * clear all the data from preferences..
     */
    public void clear() {
        mPrefsEditor.clear();
        mPrefsEditor.commit();
    }

    /**
     * remove data from preferences..
     * 
     * @param key
     */
    public void remove(String key) {
        mPrefsEditor.remove(key);
        mPrefsEditor.commit();
    }

    /**
     * To get the Stored string value in Preference.
     * 
     * @param key
     * @param defaultvalue
     * @return stored string value.
     */
    public String getStringValue(final String key, final String defaultvalue) {
        return mSharedPrefs.getString(key, defaultvalue);
    }

    /**
     * To store the string value in prefernce.
     * 
     * @param key
     * @param value
     */
    public void putStringValue(final String key, final String value) {
        mPrefsEditor.putString(key, value);
        mPrefsEditor.commit();
    }

    /**
     * To get the stored integer value in the preference.
     * 
     * @param key
     * @param defaultvalue
     * @return stored integer value.
     */
    public int getIntValue(final String key, final int defaultvalue) {
        return mSharedPrefs.getInt(key, defaultvalue);
    }

    /**
     * To stored the integer value in preference.
     * 
     * @param key
     * @param value
     */
    public void putIntValue(final String key, final int value) {
        mPrefsEditor.putInt(key, value);
        mPrefsEditor.commit();
    }

    /**
     * To get the stored long value in the preference.
     * 
     * @param key
     * @param defaultvalue
     * @return stored long value.
     */
    public long getLongValue(final String key, final long defaultvalue) {
        return mSharedPrefs.getLong(key, defaultvalue);
    }

    /**
     * To stored the long value in preference.
     * 
     * @param key
     * @param value
     */
    public void putLongValue(final String key, final long value) {
        mPrefsEditor.putLong(key, value);
        mPrefsEditor.commit();
    }

    /**
     * To get the stored boolean value from the preference.
     * 
     * @param key
     * @param defaultvalue
     * @return
     */
    public boolean getBooleanValue(final String key, final Boolean defaultvalue) {
        return mSharedPrefs.getBoolean(key, defaultvalue);
    }

    /**
     * To put the stored boolean value in preference.
     * 
     * @param key
     * @param value
     */
    public void putBooleanValue(final String key, final boolean value) {
        mPrefsEditor.putBoolean(key, value);
        mPrefsEditor.commit();
    }

}
