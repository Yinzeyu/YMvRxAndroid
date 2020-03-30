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
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.view.ViewCompat
import com.yzy.baselibrary.extention.DeviceHelper.isEssentialPhone
import com.yzy.baselibrary.extention.DeviceHelper.isFlymeLowerThan8
import com.yzy.baselibrary.extention.DeviceHelper.isMIUI
import com.yzy.baselibrary.extention.DeviceHelper.isMIUIV5
import com.yzy.baselibrary.extention.DeviceHelper.isMIUIV6
import com.yzy.baselibrary.extention.DeviceHelper.isMIUIV7
import com.yzy.baselibrary.extention.DeviceHelper.isMIUIV8
import com.yzy.baselibrary.extention.DeviceHelper.isMIUIV9
import com.yzy.baselibrary.extention.DeviceHelper.isTablet
import com.yzy.baselibrary.extention.DeviceHelper.isZTKC2016
import com.yzy.baselibrary.extention.DeviceHelper.isZUKZ1
import com.yzy.baselibrary.extention.DisplayHelper.dp2px
import com.yzy.baselibrary.extention.NotchHelper.isNotchOfficialSupport
import java.lang.reflect.Field

/**
 * @author cginechen
 * @date 2016-03-27
 */
object StatusBarHelper {
    private const val STATUSBAR_TYPE_DEFAULT = 0
    private const val STATUSBAR_TYPE_MIUI = 1
    private const val STATUSBAR_TYPE_FLYME = 2
    private const val STATUSBAR_TYPE_ANDROID6 = 3 // Android 6.0
    private const val STATUS_BAR_DEFAULT_HEIGHT_DP = 25 // 大部分状态栏都是25dp

    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    var sVirtualDensity = -1f
    var sVirtualDensityDpi = -1f
    private var sStatusbarHeight = -1

    @StatusBarType
    private var mStatuBarType = STATUSBAR_TYPE_DEFAULT
    private var sTransparentValue: Int? = null
    fun translucent(activity: Activity) {
        translucent(activity.window)
    }

    fun translucent(window: Window) {
        translucent(window, 0x40000000)
    }

    private fun supportTranslucent(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
                && !(isEssentialPhone && Build.VERSION.SDK_INT < 26))
    }

    /**
     * 沉浸式状态栏。
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android。
     *
     * @param activity 需要被设置沉浸式状态栏的 Activity。
     */
    fun translucent(activity: Activity, @ColorInt colorOn5x: Int) {
        val window = activity.window
        translucent(window, colorOn5x)
    }

    @TargetApi(19)
    fun translucent(window: Window, @ColorInt colorOn5x: Int) {
        if (!supportTranslucent()) {
            // 版本小于4.4，绝对不考虑沉浸式
            return
        }
        if (isNotchOfficialSupport) {
            handleDisplayCutoutMode(window)
        }

        // 小米和魅族4.4 以上版本支持沉浸式
        // 小米 Android 6.0 ，开发版 7.7.13 及以后版本设置黑色字体又需要 clear FLAG_TRANSLUCENT_STATUS, 因此还原为官方模式
        if (isFlymeLowerThan8 || isMIUI && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = colorOn5x
            }
        }
    }

    @TargetApi(28)
    private fun handleDisplayCutoutMode(window: Window) {
        val decorView = window.decorView
        if (ViewCompat.isAttachedToWindow(decorView)) {
            realHandleDisplayCutoutMode(window, decorView)
        } else {
            decorView.addOnAttachStateChangeListener(object :
                View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    realHandleDisplayCutoutMode(window, v)
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
        }
    }

    @TargetApi(28)
    private fun realHandleDisplayCutoutMode(
        window: Window,
        decorView: View
    ) {
        if (decorView.rootWindowInsets != null &&
            decorView.rootWindowInsets.displayCutout != null
        ) {
            val params = window.attributes
            params.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = params
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     */
    fun setStatusBarLightMode(activity: Activity?): Boolean {
        if (activity == null) return false
        // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
        if (isZTKC2016) {
            return false
        }
        if (mStatuBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatuBarType)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl && MIUISetStatusBarLightMode(activity.window, true)
            ) {
                mStatuBarType = STATUSBAR_TYPE_MIUI
                return true
            } else if (FlymeSetStatusBarLightMode(activity.window, true)) {
                mStatuBarType = STATUSBAR_TYPE_FLYME
                return true
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Android6SetStatusBarLightMode(activity.window, true)
                mStatuBarType = STATUSBAR_TYPE_ANDROID6
                return true
            }
        }
        return false
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private fun setStatusBarLightMode(
        activity: Activity,
        @StatusBarType type: Int
    ): Boolean {
        return when (type) {
            STATUSBAR_TYPE_MIUI -> {
                MIUISetStatusBarLightMode(activity.window, true)
            }
            STATUSBAR_TYPE_FLYME -> {
                FlymeSetStatusBarLightMode(activity.window, true)
            }
            STATUSBAR_TYPE_ANDROID6 -> {
                Android6SetStatusBarLightMode(activity.window, true)
            }
            else -> false
        }
    }

    /**
     * 设置状态栏白色字体图标
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     */
    fun setStatusBarDarkMode(activity: Activity?): Boolean {
        if (activity == null) return false
        if (mStatuBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状态，不需要处理
            return true
        }
        if (mStatuBarType == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.window, false)
        } else if (mStatuBarType == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.window, false)
        } else if (mStatuBarType == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.window, false)
        }
        return true
    }

    @TargetApi(23)
    private fun changeStatusBarModeRetainFlag(window: Window, out: Int): Int {
        @Suppress("NAME_SHADOWING") var out = out
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        return out
    }

    fun retainSystemUiFlag(window: Window, out: Int, type: Int): Int {
        @Suppress("NAME_SHADOWING") var out = out
        val now = window.decorView.systemUiVisibility
        if (now and type == type) {
            out = out or type
        }
        return out
    }

    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private fun Android6SetStatusBarLightMode(
        window: Window,
        light: Boolean
    ): Boolean {
        val decorView = window.decorView
        var systemUi =
            if (light) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        systemUi = changeStatusBarModeRetainFlag(window, systemUi)
        decorView.systemUiVisibility = systemUi
        if (isMIUIV9) {
            // MIUI 9 低于 6.0 版本依旧只能回退到以前的方案
            // https://github.com/Tencent/QMUI_Android/issues/160
            MIUISetStatusBarLightMode(window, light)
        }
        return true
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    @SuppressLint("PrivateApi")
    fun MIUISetStatusBarLightMode(
        window: Window?,
        light: Boolean
    ): Boolean {
        var result = false
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams =
                    Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field =
                    layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (light) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //清除黑色字体
                }
                result = true
            } catch (ignored: Exception) {
            }
        }
        return result
    }

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9 && Android 6 之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private val isMIUICustomStatusBarLightModeImpl: Boolean
        private get() = if (isMIUIV9 && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true
        } else isMIUIV5 || isMIUIV6 ||
                isMIUIV7 || isMIUIV8

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(
        window: Window?,
        light: Boolean
    ): Boolean {
        var result = false
        if (window != null) {
            // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
            Android6SetStatusBarLightMode(window, light)
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (light) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (ignored: Exception) {
            }
        }
        return result
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    fun isFullScreen(activity: Activity): Boolean {
        var ret = false
        try {
            val attrs = activity.window.attributes
            ret = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret
    }

    /**
     * API19之前透明状态栏：获取设置透明状态栏的system ui visibility的值，这是部分有提供接口的rom使用的
     * http://stackoverflow.com/questions/21865621/transparent-status-bar-before-4-4-kitkat
     */
    fun getStatusBarAPITransparentValue(context: Context): Int? {
        if (sTransparentValue != null) {
            return sTransparentValue
        }
        val systemSharedLibraryNames = context.packageManager
            .systemSharedLibraryNames
        var fieldName: String? = null
        for (lib in systemSharedLibraryNames) {
            if ("touchwiz" == lib) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND"
            } else if (lib.startsWith("com.sonyericsson.navigationbar")) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT"
            }
        }
        if (fieldName != null) {
            try {
                val field =
                    View::class.java.getField(fieldName)
                if (field != null) {
                    val type = field.type
                    if (type == Int::class.javaPrimitiveType) {
                        sTransparentValue = field.getInt(null)
                    }
                }
            } catch (ignored: Exception) {
            }
        }
        return sTransparentValue
    }

    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    fun supportTransclentStatusBar6(): Boolean {
        return !(isZUKZ1() || isZTKC2016)
    }

    /**
     * 获取状态栏的高度。
     */
    fun getStatusbarHeight(context: Context): Int {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context)
        }
        return sStatusbarHeight
    }

    private fun initStatusBarHeight(context: Context) {
        val clazz: Class<*>
        var obj: Any? = null
        var field: Field? = null
        try {
            clazz = Class.forName("com.android.internal.R\$dimen")
            obj = clazz.newInstance()
            if (DeviceHelper.isMeizu) {
                try {
                    field = clazz.getField("status_bar_height_large")
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height")
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        if (field != null && obj != null) {
            try {
                val id = field[obj].toString().toInt()
                sStatusbarHeight =
                    context.resources.getDimensionPixelSize(id)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        if (isTablet(context)
            && sStatusbarHeight > dp2px(
                context,
                STATUS_BAR_DEFAULT_HEIGHT_DP
            )
        ) {
            //状态栏高度大于25dp的平板，状态栏通常在下方
            sStatusbarHeight = 0
        } else {
            if (sStatusbarHeight <= 0) {
                sStatusbarHeight = if (sVirtualDensity == -1f) {
                    dp2px(
                        context,
                        STATUS_BAR_DEFAULT_HEIGHT_DP
                    )
                } else {
                    (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f).toInt()
                }
            }
        }
    }

    fun setVirtualDensity(density: Float) {
        sVirtualDensity = density
    }

    fun setVirtualDensityDpi(densityDpi: Float) {
        sVirtualDensityDpi = densityDpi
    }

    @IntDef(
        STATUSBAR_TYPE_DEFAULT,
        STATUSBAR_TYPE_MIUI,
        STATUSBAR_TYPE_FLYME,
        STATUSBAR_TYPE_ANDROID6
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    private annotation class StatusBarType
}