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
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.WindowManager
import com.yzy.baselibrary.utils.bar.DeviceHelper.isHuawei
import com.yzy.baselibrary.utils.bar.DeviceHelper.isOppo
import com.yzy.baselibrary.utils.bar.DeviceHelper.isVivo
import com.yzy.baselibrary.utils.bar.DeviceHelper.isXiaomi
import com.yzy.baselibrary.utils.bar.DisplayHelper.dp2px
import com.yzy.baselibrary.utils.bar.DisplayHelper.huaWeiIsNotchSetToShowInSetting

object NotchHelper {
    private const val TAG = "QMUINotchHelper"
    private const val NOTCH_IN_SCREEN_VOIO = 0x00000020
    private var sHasNotch: Boolean? = null
    private var sRotation0SafeInset: Rect? = null
    private var sRotation90SafeInset: Rect? = null
    private var sRotation180SafeInset: Rect? = null
    private var sRotation270SafeInset: Rect? = null
    private var sNotchSizeInHawei: IntArray? = null
    private var sHuaweiIsNotchSetToShow: Boolean = false

    @SuppressLint("PrivateApi")
    fun hasNotchInVivo(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val ftFeature = cl.loadClass("android.util.FtFeature")
            val methods = ftFeature.declaredMethods
            for (method in methods) {
                if (method.name.equals("isFeatureSupport", ignoreCase = true)) {
                    ret = method.invoke(ftFeature, NOTCH_IN_SCREEN_VOIO) as Boolean
                    break
                }
            }
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInVivo ClassNotFoundException")
        } catch (e: Exception) {
            Log.e(TAG, "hasNotchInVivo Exception")
        }
        return ret
    }

    private fun hasNotchInHuaWei(context: Context): Boolean {
        var hasNotch = false
        try {
            val cl = context.classLoader
            val clazz = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = clazz.getMethod("hasNotchInScreen")
            hasNotch = get.invoke(clazz) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "hasNotchInHuawei ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e(TAG, "hasNotchInHuawei NoSuchMethodException")
        } catch (e: Exception) {
            Log.e(TAG, "hasNotchInHuawei Exception")
        }
        return hasNotch
    }

    private fun hasNotchInOpPo(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    @SuppressLint("PrivateApi")
    fun hasNotchInXM(): Boolean {
        try {
            val spClass = Class.forName("android.os.SystemProperties")
            val getMethod = spClass.getDeclaredMethod(
                "getInt",
                String::class.java,
                Int::class.javaPrimitiveType
            )
            getMethod.isAccessible = true
            val hasNotch = getMethod.invoke(null, "ro.miui.notch", 0) as Int
            return hasNotch == 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun hasNotch(view: View): Boolean {
        sHasNotch?.let {
            if (isNotchOfficialSupport) {
                if (attachHasOfficialNotch(view)) {
                    return false
                }
            } else {
                sHasNotch = has3rdNotch(view.context)
            }
        }
        return sHasNotch ?: false
    }

    fun hasNotch(activity: Activity): Boolean {
        sHasNotch?.let {
            if (isNotchOfficialSupport) {
                val window = activity.window ?: return false
                val decorView = window.decorView
                if (attachHasOfficialNotch(decorView)) {
                    return false
                }
            } else {
                sHasNotch = has3rdNotch(activity)
            }
        }

        return sHasNotch ?: false
    }

    /**
     *
     * @param view
     * @return false indicates the failure to get the result
     */
    @TargetApi(28)
    private fun attachHasOfficialNotch(view: View): Boolean {
        val windowInsets = view.rootWindowInsets
        return if (windowInsets != null) {
            val displayCutout = windowInsets.displayCutout
            sHasNotch = displayCutout != null
            false
        } else {
            // view not attached, do nothing
            true
        }
    }

    private fun has3rdNotch(context: Context): Boolean = when {
        isHuawei -> {
            hasNotchInHuaWei(context)
        }
        isVivo -> {
            hasNotchInVivo(context)
        }
        isOppo -> {
            hasNotchInOpPo(context)
        }
        isXiaomi -> {
            hasNotchInXM()
        }
        else -> false
    }


    fun getSafeInsetTop(activity: Activity): Int = if (!hasNotch(activity)) { 0 } else (getSafeInsetRect(activity)?.top ?:0)

    fun getSafeInsetBottom(activity: Activity): Int =if (!hasNotch(activity)) { 0 } else (getSafeInsetRect(activity)?.bottom ?:0)

    fun getSafeInsetLeft(activity: Activity): Int = if (!hasNotch(activity)) { 0 } else  (getSafeInsetRect(activity)?.left ?:0)


    fun getSafeInsetRight(activity: Activity): Int =if (!hasNotch(activity)) { 0 } else  (getSafeInsetRect(activity)?.right ?:0)

    fun getSafeInsetTop(view: View): Int=if (!hasNotch(view)) { 0 } else (getSafeInsetRect(view)?.top ?:0)

    fun getSafeInsetBottom(view: View): Int  =if (!hasNotch(view)) { 0 } else (getSafeInsetRect(view)?.bottom ?:0)

    fun getSafeInsetLeft(view: View): Int= if (!hasNotch(view)) { 0 } else  (getSafeInsetRect(view)?.left ?:0)

    fun getSafeInsetRight(view: View): Int  =if (!hasNotch(view)) { 0 } else  (getSafeInsetRect(view)?.right ?:0)


    private fun clearAllRectInfo() {
        sRotation0SafeInset = null
        sRotation90SafeInset = null
        sRotation180SafeInset = null
        sRotation270SafeInset = null
    }

    private fun clearPortraitRectInfo() {
        sRotation0SafeInset = null
        sRotation180SafeInset = null
    }

    private fun clearLandscapeRectInfo() {
        sRotation90SafeInset = null
        sRotation270SafeInset = null
    }

    private fun getSafeInsetRect(activity: Activity): Rect? {
        if (isNotchOfficialSupport) {
            val rect = Rect()
            val decorView = activity.window.decorView
            getOfficialSafeInsetRect(decorView, rect)
            return rect
        }
        return get3rdSafeInsetRect(activity)
    }

    private fun getSafeInsetRect(view: View): Rect? {
        if (isNotchOfficialSupport) {
            val rect = Rect()
            getOfficialSafeInsetRect(view, rect)
            return rect
        }
        return get3rdSafeInsetRect(view.context)
    }

    @TargetApi(28)
    private fun getOfficialSafeInsetRect(
        view: View?,
        out: Rect
    ) {
        if (view == null) {
            return
        }
        val rootWindowInsets = view.rootWindowInsets ?: return
        val displayCutout = rootWindowInsets.displayCutout
        if (displayCutout != null) {
            out[displayCutout.safeInsetLeft, displayCutout.safeInsetTop, displayCutout.safeInsetRight] =
                displayCutout.safeInsetBottom
        }
    }

    private fun get3rdSafeInsetRect(context: Context): Rect? {
        // 全面屏设置项更改
        if (isHuawei) {
            val isHuaWeiNotchSetToShow = huaWeiIsNotchSetToShowInSetting(context)
            if (sHuaweiIsNotchSetToShow != isHuaWeiNotchSetToShow) {
                clearLandscapeRectInfo()
            }
            sHuaweiIsNotchSetToShow = isHuaWeiNotchSetToShow
        }
        val screenRotation =
            getScreenRotation(context)
        return when (screenRotation) {
            Surface.ROTATION_90 -> {
                if (sRotation90SafeInset == null) {
                    sRotation90SafeInset = getRectInfoRotation90(context)
                }
                sRotation90SafeInset
            }
            Surface.ROTATION_180 -> {
                if (sRotation180SafeInset == null) {
                    sRotation180SafeInset = getRectInfoRotation180(context)
                }
                sRotation180SafeInset
            }
            Surface.ROTATION_270 -> {
                if (sRotation270SafeInset == null) {
                    sRotation270SafeInset =
                        getRectInfoRotation270(context)
                }
                sRotation270SafeInset
            }
            else -> {
                if (sRotation0SafeInset == null) {
                    sRotation0SafeInset =
                        getRectInfoRotation0(context)
                }
                sRotation0SafeInset
            }
        }
    }

    private fun getRectInfoRotation0(context: Context): Rect {
        val rect = Rect()
        when {
            isVivo -> {
                // TODO vivo 显示与亮度-第三方应用显示比例
                rect.top = getNotchHeightInViVo(context)
                rect.bottom = 0
            }
            isOppo -> {
                // TODO OPPO 设置-显示-应用全屏显示-凹形区域显示控制
                rect.top = StatusBarHelper.getStatusBarHeight(context)
                rect.bottom = 0
            }
            isHuawei -> {
                val notchSize =
                    getNotchSizeInHuawei(context)
                rect.top = notchSize!![1]
                rect.bottom = 0
            }
            isXiaomi -> {
                rect.top = getNotchHeightInXM(context)
                rect.bottom = 0
            }
        }
        return rect
    }

    private fun getRectInfoRotation90(context: Context): Rect {
        val rect = Rect()
        when {
            isVivo -> {
                rect.left = getNotchHeightInViVo(context)
                rect.right = 0
            }
            isOppo -> {
                rect.left = StatusBarHelper.getStatusBarHeight(context)
                rect.right = 0
            }
            isHuawei -> {
                if (sHuaweiIsNotchSetToShow) {
                    rect.left = getNotchSizeInHuawei(context)?.get(1) ?: 0
                } else {
                    rect.left = 0
                }
                rect.right = 0
            }
            isXiaomi -> {
                rect.left =
                    getNotchHeightInXM(context)
                rect.right = 0
            }
        }
        return rect
    }

    private fun getRectInfoRotation180(context: Context): Rect {
        val rect = Rect()
        when {
            isVivo -> {
                rect.top = 0
                rect.bottom =
                    getNotchHeightInViVo(context)
            }
            isOppo -> {
                rect.top = 0
                rect.bottom = StatusBarHelper.getStatusBarHeight(context)
            }
            isHuawei -> {
                val notchSize = getNotchSizeInHuawei(context)
                rect.top = 0
                rect.bottom = notchSize!![1]
            }
            isXiaomi -> {
                rect.top = 0
                rect.bottom =
                    getNotchHeightInXM(context)
            }
        }
        return rect
    }

    private fun getRectInfoRotation270(context: Context): Rect {
        val rect = Rect()
        when {
            isVivo -> {
                rect.right = getNotchHeightInViVo(context)
                rect.left = 0
            }
            isOppo -> {
                rect.right = StatusBarHelper.getStatusBarHeight(context)
                rect.left = 0
            }
            isHuawei -> {
                if (sHuaweiIsNotchSetToShow) {
                    rect.right = getNotchSizeInHuawei(context)?.get(1) ?: 0
                } else {
                    rect.right = 0
                }
                rect.left = 0
            }
            isXiaomi -> {
                rect.right = getNotchHeightInXM(context)
                rect.left = 0
            }
        }
        return rect
    }

    fun getNotchSizeInHuawei(context: Context): IntArray? {
        if (sNotchSizeInHawei == null) {
            sNotchSizeInHawei = intArrayOf(0, 0)
            try {
                val cl = context.classLoader
                val clazz = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
                val get = clazz.getMethod("getNotchSize")
                sNotchSizeInHawei =
                    get.invoke(clazz) as IntArray
            } catch (e: ClassNotFoundException) {
                Log.e(TAG, "getNotchSizeInHuawei ClassNotFoundException")
            } catch (e: NoSuchMethodException) {
                Log.e(TAG, "getNotchSizeInHuawei NoSuchMethodException")
            } catch (e: Exception) {
                Log.e(TAG, "getNotchSizeInHuawei Exception")
            }
        }
        return sNotchSizeInHawei
    }

    fun getNotchWidthInXM(context: Context): Int {
        val resourceId = context.resources.getIdentifier("notch_width", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else -1
    }

    private fun getNotchHeightInXM(context: Context): Int {
        val resourceId = context.resources.getIdentifier("notch_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else DisplayHelper.getStatusBarHeight(context)
    }

    fun getNotchWidthInViVo(context: Context): Int = dp2px(context, 100)

    private fun getNotchHeightInViVo(context: Context): Int = dp2px(context, 27)

    /**
     * this method is private, because we do not need to handle tablet
     *
     * @param context
     * @return
     */
    private fun getScreenRotation(context: Context): Int {
        val w =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = w.defaultDisplay ?: return Surface.ROTATION_0
        return display.rotation
    }

    val isNotchOfficialSupport: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    /**
     * fitSystemWindows 对小米、vivo挖孔屏横屏挖孔区域无效
     * @param view
     * @return
     */
    fun needFixLandscapeNotchAreaFitSystemWindow(view: View): Boolean = (isXiaomi || isVivo) && hasNotch(view)

}