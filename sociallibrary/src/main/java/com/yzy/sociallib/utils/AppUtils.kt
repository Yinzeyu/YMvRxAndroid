package com.yzy.sociallib.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * description:
 * @date 2019/7/15
 * @author: yzy.
 */
object AppUtils {

    /**
     * 判断应用是否安装
     */
    fun isAppInstalled(name: String, context: Context): Boolean {
        var isInstalled: Boolean
        val manager = context.packageManager
        try {
            val applicationInfo = manager.getApplicationInfo(name, 0) ?: null
            isInstalled = applicationInfo != null
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            isInstalled = false
        }
        return isInstalled
    }
}