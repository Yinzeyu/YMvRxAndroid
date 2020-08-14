@file:Suppress("UNCHECKED_CAST")

package com.yzy.baselibrary.utils

import android.content.Context
import androidx.core.content.edit
import com.yzy.baselibrary.app.BaseApplication

/**
 * @author yzy.
 * @description sp 管理工具类
 */
private val sp = BaseApplication.instance().getSharedPreferences("com.base.share.preference", Context.MODE_PRIVATE)

fun <T> getSpValue(
    key: String,
    defaultVal: T
): T {
    return when (defaultVal) {
        is Boolean -> sp.getBoolean(key, defaultVal) as T
        is String -> sp.getString(key, defaultVal) as T
        is Int -> sp.getInt(key, defaultVal) as T
        is Long -> sp.getLong(key, defaultVal) as T
        is Float -> sp.getFloat(key, defaultVal) as T
        is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
        else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
    }
}

fun  putSpValue(
    key: String,
    value: Any
) {
    sp.edit(commit = false) {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException("Unrecognized value $value")
        }
    }
}

fun removeSpValue(key: String) {
    sp.edit(commit = false) {
        remove(key)
    }
}

fun clearSpValue() {
    sp.edit(commit = false) {
        clear()
    }
}