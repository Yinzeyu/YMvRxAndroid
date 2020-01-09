package com.yzy.example.widget.imagewatcher.immersionbar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.DisplayCutout
import android.view.View
import com.yzy.example.widget.imagewatcher.immersionbar.ImmersionBar
import java.lang.reflect.InvocationTargetException

/**
 * 刘海屏判断
 * The type Notch utils.
 *
 * @author geyifeng
 * @date 2018 /11/14 12:09 AM
 */
object NotchUtils {
    /**
     * 系统属性
     * The constant SYSTEM_PROPERTIES.
     */
    private const val SYSTEM_PROPERTIES = "android.os.SystemProperties"
    /**
     * 小米刘海
     * The constant NOTCH_XIAO_MI.
     */
    private const val NOTCH_XIAO_MI = "ro.miui.notch"
    /**
     * 华为刘海
     * The constant NOTCH_HUA_WEI.
     */
    private const val NOTCH_HUA_WEI = "com.huawei.android.util.HwNotchSizeUtil"
    /**
     * VIVO刘海
     * The constant NOTCH_VIVO.
     */
    private const val NOTCH_VIVO = "android.util.FtFeature"
    /**
     * OPPO刘海
     * The constant NOTCH_OPPO.
     */
    private const val NOTCH_OPPO = "com.oppo.feature.screen.heteromorphism"

    /**
     * 判断是否是刘海屏
     * Has notch screen boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    fun hasNotchScreen(activity: Activity?): Boolean {
        return activity != null && (hasNotchAtXiaoMi(activity) ||
                hasNotchAtHuaWei(activity) ||
                hasNotchAtOPPO(activity) ||
                hasNotchAtVIVO(activity) ||
                hasNotchAtAndroidP(activity))
    }

    /**
     * 判断是否是刘海屏
     * Has notch screen boolean.
     *
     * @param view the view
     * @return the boolean
     */
    fun hasNotchScreen(view: View?): Boolean {
        return view != null && (hasNotchAtXiaoMi(view.context) ||
                hasNotchAtHuaWei(view.context) ||
                hasNotchAtOPPO(view.context) ||
                hasNotchAtVIVO(view.context) ||
                hasNotchAtAndroidP(view))
    }

    /**
     * Has notch at android p boolean.
     *
     * @param view the view
     * @return the boolean
     */
    private fun hasNotchAtAndroidP(view: View): Boolean {
        return getDisplayCutout(view) != null
    }

    /**
     * Android P 刘海屏判断
     * Has notch at android p boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    private fun hasNotchAtAndroidP(activity: Activity): Boolean {
        return getDisplayCutout(activity) != null
    }

    /**
     * 获得DisplayCutout
     * Gets display cutout.
     *
     * @param activity the activity
     * @return the display cutout
     */
    private fun getDisplayCutout(activity: Activity?): DisplayCutout? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (activity != null) {
                val window = activity.window
                if (window != null) {
                    val windowInsets = window.decorView.rootWindowInsets
                    if (windowInsets != null) {
                        return windowInsets.displayCutout
                    }
                }
            }
        }
        return null
    }

    private fun getDisplayCutout(view: View?): DisplayCutout? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (view != null) {
                val windowInsets = view.rootWindowInsets
                if (windowInsets != null) {
                    return windowInsets.displayCutout
                }
            }
        }
        return null
    }

    /**
     * 小米刘海屏判断.
     * Has notch at xiao mi int.
     *
     * @param context the context
     * @return the int
     */
    private fun hasNotchAtXiaoMi(context: Context?): Boolean {
        var result = 0
        if ("Xiaomi" == Build.MANUFACTURER) {
            try {
                val classLoader = context!!.classLoader
                @SuppressLint("PrivateApi") val aClass =
                    classLoader.loadClass(SYSTEM_PROPERTIES)
                val method = aClass.getMethod(
                    "getInt",
                    String::class.java,
                    Int::class.javaPrimitiveType
                )
                result = method.invoke(aClass, NOTCH_XIAO_MI, 0) as Int
            } catch (ignored: NoSuchMethodException) {
            } catch (ignored: IllegalAccessException) {
            } catch (ignored: InvocationTargetException) {
            } catch (ignored: ClassNotFoundException) {
            }
        }
        return result == 1
    }

    /**
     * 华为刘海屏判断
     * Has notch at hua wei boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private fun hasNotchAtHuaWei(context: Context?): Boolean {
        var result = false
        try {
            val classLoader = context!!.classLoader
            val aClass = classLoader.loadClass(NOTCH_HUA_WEI)
            val get = aClass.getMethod("hasNotchInScreen")
            result = get.invoke(aClass) as Boolean
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: Exception) {
        }
        return result
    }

    /**
     * VIVO刘海屏判断
     * Has notch at vivo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private fun hasNotchAtVIVO(context: Context?): Boolean {
        var result = false
        try {
            val classLoader = context!!.classLoader
            @SuppressLint("PrivateApi") val aClass =
                classLoader.loadClass(NOTCH_VIVO)
            val method =
                aClass.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            result = method.invoke(aClass, 0x00000020) as Boolean
        } catch (ignored: ClassNotFoundException) {
        } catch (ignored: NoSuchMethodException) {
        } catch (ignored: Exception) {
        }
        return result
    }

    /**
     * OPPO刘海屏判断
     * Has notch at oppo boolean.
     *
     * @param context the context
     * @return the boolean
     */
    private fun hasNotchAtOPPO(context: Context?): Boolean {
        return try {
            context!!.packageManager.hasSystemFeature(NOTCH_OPPO)
        } catch (ignored: Exception) {
            false
        }
    }

    /**
     * 获得刘海屏高度
     * Notch height int.
     *
     * @param activity the activity
     * @return the int
     */
    fun getNotchHeight(activity: Activity?): Int {
        var notchHeight = 0
        val statusBarHeight: Int = ImmersionBar.Companion.getStatusBarHeight(activity!!)
        val displayCutout = getDisplayCutout(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && displayCutout != null) {
            notchHeight =
                if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    displayCutout.safeInsetTop
                } else {
                    if (displayCutout.safeInsetLeft == 0) {
                        displayCutout.safeInsetRight
                    } else {
                        displayCutout.safeInsetLeft
                    }
                }
        } else {
            if (hasNotchAtXiaoMi(activity)) {
                notchHeight = getXiaoMiNotchHeight(activity)
            }
            if (hasNotchAtHuaWei(activity)) {
                notchHeight = getHuaWeiNotchSize(activity)[1]
            }
            if (hasNotchAtVIVO(activity)) {
                notchHeight = dp2px(activity, 32)
                if (notchHeight < statusBarHeight) {
                    notchHeight = statusBarHeight
                }
            }
            if (hasNotchAtOPPO(activity)) {
                notchHeight = 80
                if (notchHeight < statusBarHeight) {
                    notchHeight = statusBarHeight
                }
            }
        }
        return notchHeight
    }

    /**
     * Gets xiao mi notch height.
     *
     * @param context the context
     * @return the xiao mi notch height
     */
    private fun getXiaoMiNotchHeight(context: Context?): Int {
        val resourceId =
            context!!.resources.getIdentifier("notch_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * Get hua wei notch size int [ ].
     *
     * @param context the context
     * @return the int [ ]
     */
    private fun getHuaWeiNotchSize(context: Context?): IntArray {
        val ret = intArrayOf(0, 0)
        return try {
            val cl = context!!.classLoader
            val aClass = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = aClass.getMethod("getNotchSize")
            get.invoke(aClass) as IntArray
        } catch (ignored: ClassNotFoundException) {
            ret
        } catch (ignored: NoSuchMethodException) {
            ret
        } catch (ignored: Exception) {
            ret
        }
    }

    /**
     * Dp 2 px int.
     *
     * @param context the context
     * @param dpValue the dp value
     * @return the int
     */
    private fun dp2px(context: Context?, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue.toFloat(),
            context!!.resources.displayMetrics
        ).toInt()
    }
}