/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzy.baselibrary.utils.bar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.Context
import android.content.res.Configuration
import android.os.Binder
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

/**
 * @author yzy
 * @date 2016-08-11
 */
@SuppressLint("ConstantLocale")
object DeviceHelper {
    private const val TAG = "QMUIDeviceHelper"
    private val MOLDBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private var sIsTabletChecked = false
    private var sIsTabletValue = false
    private val BRAND = Build.BRAND.toLowerCase(Locale.getDefault())

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    val isZUKZ1: Boolean = Build.MODEL.toLowerCase(Locale.getDefault()).contains("zuk z1")
    /**
     * 判断是否是flyme系统
     */
    val isFlyme: Boolean = !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains("flyme")

    /**
     * 判断是否是MIUI系统
     */
    val isMIUI: Boolean = !TextUtils.isEmpty(sMiuiVersionName)

    val isMIUIV5: Boolean = ("v5" == sMiuiVersionName)

    val isMIUIV6: Boolean = ("v6" == sMiuiVersionName)

    val isMIUIV7: Boolean = ("v7" == sMiuiVersionName)

    val isMIUIV8: Boolean = ("v8" == sMiuiVersionName)

    val isMIUIV9: Boolean = ("v9" == sMiuiVersionName)

    val isMeizu: Boolean = isPhone || isFlyme

    /**
     * 判断是否为小米
     * https://dev.mi.com/doc/?p=254
     */

    val isXiaomi: Boolean = Build.MANUFACTURER.toLowerCase(Locale.getDefault()) == "xiaomi"

    val isVivo: Boolean = BRAND.contains("vivo") || BRAND.contains("bbk")

    val isOppo: Boolean = BRAND.contains("oppo")

    val isHuawei: Boolean = BRAND.contains("huawei") || BRAND.contains("honor")

    val isEssentialPhone: Boolean = BRAND.contains("essential")

    val isZTKC2016: Boolean = Build.MODEL.toLowerCase(Locale.getDefault()).contains("zte c2016")
    /**
     * 判断是否为平板设备
     */
    fun isTablet(context: Context): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue =
            context.resources.configuration.screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
        sIsTabletChecked = true
        return sIsTabletValue
    }



    fun isFlLowerThan(majorVersion: Int): Boolean = isFlLowerThan(majorVersion, 0, 0)

    private fun isFlLowerThan(majorVersion: Int, minorVersion: Int, patchVersion: Int): Boolean {
        var isLower = false
        sFlymeVersionName?.let {
            if (it != "") {
                try {
                    val pattern =
                        Pattern.compile("(\\d+\\.){2}\\d")
                    val matcher =
                        pattern.matcher(it)
                    if (matcher.find()) {
                        val versionString = matcher.group()
                        if (versionString.isNotEmpty()) {
                            val version =
                                versionString.split("\\.".toRegex()).toTypedArray()
                            if (version.isNotEmpty()) {
                                if (version[0].toInt() < majorVersion) {
                                    isLower = true
                                }
                            }
                            if (version.size >= 2 && minorVersion > 0) {
                                if (version[1].toInt() < majorVersion) {
                                    isLower = true
                                }
                            }
                            if (version.size >= 3 && patchVersion > 0) {
                                if (version[2].toInt() < majorVersion) {
                                    isLower = true
                                }
                            }
                        }
                    }
                } catch (ignore: Throwable) {
                }
            }
        }

        return isMeizu && isLower
    }




    private val isPhone: Boolean
        get() {
            val board = Build.BOARD ?: return false
            for (board1 in MOLDBOARD) {
                if (board == board1) {
                    return true
                }
            }
            return false
        }

    /**
     * 判断悬浮窗权限（目前主要用户魅族与小米的检测）。
     */
    fun isFloatWindowOpAllowed(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context) // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
        } else {
            try {
                context.applicationInfo.flags and 1 shl 27 == 1 shl 27
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    @TargetApi(19)
    private fun checkOp(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method =
                    Objects.requireNonNull(manager).javaClass.getDeclaredMethod(
                        "checkOp",
                        Int::class.javaPrimitiveType,
                        Int::class.javaPrimitiveType,
                        String::class.java
                    )
                val property = method.invoke(
                    manager,
                    24,
                    Binder.getCallingUid(),
                    context.packageName
                ) as Int
                return AppOpsManager.MODE_ALLOWED == property
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    private fun getLowerCaseName(
        p: Properties,
        get: Method,
        key: String
    ): String? {
        var name = p.getProperty(key)
        if (name == null) {
            try {
                name = get.invoke(null, key) as String
            } catch (ignored: Exception) {
            }
        }
        if (name != null) name = name.toLowerCase(Locale.getDefault())
        return name
    }

    init {
        val properties = Properties()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // android 8.0，读取 /system/uild.prop 会报 permission denied
            var fileInputStream: FileInputStream? = null
            try {
             fileInputStream = FileInputStream(File(Environment.getRootDirectory(), "build.prop"))
               properties.load(fileInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "read file error$e")
            } finally {
                close(fileInputStream)
            }
        }
        var clzSystemProperties: Class<*>? = null
        try {
          clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod: Method =
           clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, "ro.miui.ui.version.name")
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod,  "ro.build.display.id")
        } catch (e: Exception) {
            Log.e(TAG, "read SystemProperties error$e")
        }
    }
    @JvmStatic
    fun close(c: Closeable?) {
        if (c != null) {
            try {
                c.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}