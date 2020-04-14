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
    private val sp = BaseApplication.instance().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    @JvmStatic
    fun saveString( key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    @JvmStatic
    fun getString( key: String, default: String = ""): String {
        return sp.getString(key, default) ?: ""
    }

    @JvmStatic
    fun saveInteger( key: String, value: Int) {
        sp.edit { putInt(key, value) }
    }

    @JvmStatic
    fun getInteger( key: String, default: Int = 0): Int {
        return sp.getInt(key, default)
    }

    @JvmStatic
    fun saveLong( key: String, value: Long) {
        sp.edit { putLong(key, value) }
    }

    @JvmStatic
    fun getLong( key: String, default: Long = 0L): Long {
        return sp.getLong(key, default)
    }

    @JvmStatic
    fun saveFloat( key: String, value: Float) {
        sp.edit { putFloat(key, value) }
    }

    @JvmStatic
    fun getFloat( key: String, default: Float = 0F): Float {
        return sp.getFloat(key, default)
    }

    @JvmStatic
    fun saveBoolean( key: String, value: Boolean) {
        sp.edit { putBoolean(key, value) }
    }

    @JvmStatic
    fun getBoolean( key: String, default: Boolean = false): Boolean {
        return sp.getBoolean(key, default)
    }

    @JvmStatic
    fun saveStringSet( key: String, value: MutableSet<String>?) {
        sp.edit { putStringSet(key, value) }
    }

    @JvmStatic
    fun getStringSet( key: String): MutableSet<String>? {
        return sp.getStringSet(key, null)
    }
}