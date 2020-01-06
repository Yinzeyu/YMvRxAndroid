package com.yzy.baselibrary.utils

import android.content.Context

import androidx.core.content.edit
import com.yzy.baselibrary.app.BaseApplication

/**
 * @author yzy.
 * @description
 */
object SharePreferencesUtils {
    private const val SHARED_PREFERENCES_NAME = "com.base.share.preference"

    @JvmStatic
    fun saveString( key: String, value: String) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        sp.edit { putString(key, value) }
    }

    @JvmStatic
    fun getString( key: String, default: String = ""): String {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return sp.getString(key, default) ?: ""
    }

    @JvmStatic
    fun saveInteger( key: String, value: Int) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        sp.edit { putInt(key, value) }
    }

    @JvmStatic
    fun getInteger( key: String, default: Int = 0): Int {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return sp.getInt(key, default)
    }

    @JvmStatic
    fun saveLong( key: String, value: Long) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        sp.edit { putLong(key, value) }
    }

    @JvmStatic
    fun getLong( key: String, default: Long = 0L): Long {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return sp.getLong(key, default)
    }

    @JvmStatic
    fun saveFloat( key: String, value: Float) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        sp.edit { putFloat(key, value) }
    }

    @JvmStatic
    fun getFloat( key: String, default: Float = 0F): Float {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sp.getFloat(key, default)
    }

    @JvmStatic
    fun saveBoolean( key: String, value: Boolean) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        sp.edit { putBoolean(key, value) }
    }

    @JvmStatic
    fun getBoolean( key: String, default: Boolean = false): Boolean {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sp.getBoolean(key, default)
    }

    @JvmStatic
    fun saveStringSet( key: String, value: MutableSet<String>?) {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        sp.edit { putStringSet(key, value) }
    }

    @JvmStatic
    fun getStringSet( key: String): MutableSet<String>? {
        val sp = BaseApplication.getApp().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sp.getStringSet(key, null)
    }
}