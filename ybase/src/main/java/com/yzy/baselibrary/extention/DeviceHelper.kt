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
package com.yzy.baselibrary.extention

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
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

/**
 * @author cginechen
 * @date 2016-08-11
 */
object DeviceHelper {
    private const val TAG = "QMUIDeviceHelper"
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_FLYME_VERSION_NAME = "ro.build.display.id"
    private const val FLYME = "flyme"
    private const val ZTEC2016 = "zte c2016"
    private const val ZUKZ1 = "zuk z1"
    private const val ESSENTIAL = "essential"
    private val MEIZUBOARD = arrayOf("m9", "M9", "mx", "MX")
    private var sMiuiVersionName: String? = null
    private var sFlymeVersionName: String? = null
    private var sIsTabletChecked = false
    private var sIsTabletValue = false
    private val BRAND = Build.BRAND.toLowerCase(Locale.getDefault())
    private fun _isTablet(context: Context): Boolean {
        return context.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >=
                Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 判断是否为平板设备
     */
    fun isTablet(context: Context): Boolean {
        if (sIsTabletChecked) {
            return sIsTabletValue
        }
        sIsTabletValue = _isTablet(context)
        sIsTabletChecked = true
        return sIsTabletValue
    }

    /**
     * 判断是否是flyme系统
     */
    val isFlyme: Boolean = !TextUtils.isEmpty(sFlymeVersionName) && sFlymeVersionName!!.contains(FLYME)

    /**
     * 判断是否是MIUI系统
     */
    val isMIUI: Boolean = !TextUtils.isEmpty(sMiuiVersionName)

    val isMIUIV5: Boolean = "v5" == sMiuiVersionName

    val isMIUIV6: Boolean = "v6" == sMiuiVersionName

    val isMIUIV7: Boolean = "v7" == sMiuiVersionName

    val isMIUIV8: Boolean = "v8" == sMiuiVersionName

    val isMIUIV9: Boolean = "v9" == sMiuiVersionName

    val isFlymeLowerThan8: Boolean
        get() {
            var isLower = false
            if (sFlymeVersionName != null && sFlymeVersionName != "") {
                val pattern = Pattern.compile("(\\d+\\.){2}\\d")
                val matcher = pattern.matcher(sFlymeVersionName)
                if (matcher.find()) {
                    val versionString = matcher.group()
                    if (versionString != "") {
                        val version = versionString.split(".").toTypedArray()
                        if (version.isNotEmpty()) {
                            if (version[0].toInt() < 8) {
                                isLower = true
                            }
                        }
                    }
                }
            }
            return isMeizu && isLower
        }

    val isFlymeVersionHigher5_2_4: Boolean
        get() {
            var isHigher = true
            if (sFlymeVersionName != null && sFlymeVersionName != "") {
                val pattern =
                    Pattern.compile("(\\d+\\.){2}\\d")
                val matcher =
                    pattern.matcher(sFlymeVersionName)
                if (matcher.find()) {
                    val versionString = matcher.group()
                    if (versionString != "") {
                        val version =
                            versionString.split("\\.").toTypedArray()
                        if (version.size == 3) {
                            val majorVersion = version[0].toInt()
                            if (majorVersion < 5) {
                                isHigher = false
                            } else if (majorVersion == 5) {
                                val minorVersion = version[1].toInt()
                                if (minorVersion < 2) {
                                    isHigher = false
                                } else if (minorVersion == 2) {
                                    val patchVersion = version[2].toInt()
                                    if (patchVersion < 4) {
                                        isHigher = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return isMeizu && isHigher
        }

    val isMeizu: Boolean = isPhone(MEIZUBOARD) || isFlyme

    /**
     * 判断是否为小米
     * https://dev.mi.com/doc/?p=254
     */
    @SuppressLint("ConstantLocale")
    val isXiaomi: Boolean = Build.MANUFACTURER.toLowerCase(Locale.getDefault()) == "xiaomi"

    val isVivo: Boolean = BRAND.contains("vivo") || BRAND.contains("bbk")

    val isOppo: Boolean = BRAND.contains("oppo")

    val isHuawei: Boolean = BRAND.contains("huawei") || BRAND.contains("honor")

    val isEssentialPhone: Boolean = BRAND.contains("essential")

    /**
     * 判断是否为 ZUK Z1 和 ZTK C2016。
     * 两台设备的系统虽然为 android 6.0，但不支持状态栏icon颜色改变，因此经常需要对它们进行额外判断。
     */
    fun isZUKZ1(): Boolean {
        val board = Build.MODEL
        return board != null && board.toLowerCase(Locale.getDefault()).contains(ZUKZ1)
    }

    val isZTKC2016: Boolean
        get() {
            val board = Build.MODEL
            return board != null && board.toLowerCase(Locale.getDefault()).contains(ZTEC2016)
        }

    private fun isPhone(boards: Array<String>): Boolean {
        val board = Build.BOARD ?: return false
        for (board1 in boards) {
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
            checkOp(context, 24) // 24 是AppOpsManager.OP_SYSTEM_ALERT_WINDOW 的值，该值无法直接访问
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
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= Build.VERSION_CODES.KITKAT) {
            val manager =
                context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val method = manager.javaClass.getDeclaredMethod(
                    "checkOp",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType,
                    String::class.java
                )
                val property = method.invoke(
                    manager, op,
                    Binder.getCallingUid(), context.packageName
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
                fileInputStream = FileInputStream(
                    File(
                        Environment.getRootDirectory(),
                        "build.prop"
                    )
                )
                properties.load(fileInputStream)
            } catch (e: Exception) {
                Log.e(TAG, "read file error", e)
            } finally {
                close(fileInputStream)
            }
        }
        var clzSystemProperties: Class<*>? = null
        try {
            @SuppressLint("PrivateApi")
            clzSystemProperties = Class.forName("android.os.SystemProperties")
            val getMethod: Method = clzSystemProperties.getDeclaredMethod("get", String::class.java)
            // miui
            sMiuiVersionName = getLowerCaseName(properties, getMethod, KEY_MIUI_VERSION_NAME)
            //flyme
            sFlymeVersionName = getLowerCaseName(properties, getMethod, KEY_FLYME_VERSION_NAME)
        } catch (e: Exception) {
            Log.e(TAG, "read SystemProperties error", e)
        }
    }
}