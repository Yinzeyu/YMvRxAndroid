package com.yzy.baselibrary.extention

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.Utils
import kotlinx.coroutines.*
import java.lang.reflect.Method
import kotlin.math.max


/**
 * description : 产生随机颜色,使用时需要创建Color对象再用，如Color().random()
 *
 *@date 2019/7/15
 *@author: yzy.
 */
private val TAG = "GlobalLayoutEx"
//需要保存键盘高度
private var saveKeyboardHeight = 0
//方便判断改变的临时变量
private var tempKeyboardHeight = 0
//方便判断改变的临时变量
private var tempNavigationBarHeight = 0
//为了解决一直重复设置高度的问题
private var lastSetSetKeyboardHeight = 0

/**获取当前键盘高度*/
fun Fragment.getHeightKeyboard(): Int {
    val rect = Rect()
    //使用最外层布局填充，进行测算计算
    requireActivity().window.decorView.getWindowVisibleDisplayFrame(rect)
    val heightDiff =  requireActivity().window.decorView.height - (rect.bottom - rect.top)
    return max(0, heightDiff - BarUtils.getStatusBarHeight() - getBottomStatusHeight(requireActivity()))
}

/**添加键盘监听*/
fun Fragment.addListerKeyboard(
    naHeight: ((naHeight: Int) -> Unit)? = null,
    keyboardHeight: ((keyboardHeight: Int) -> Unit)? = null
) {
    val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val realKeyboardHeight = getHeightKeyboard()
        if (realKeyboardHeight != lastSetSetKeyboardHeight && requireActivity().window.decorView.height > 0
            && (realKeyboardHeight > 300 || realKeyboardHeight == 0)
        ) {
            lastSetSetKeyboardHeight = realKeyboardHeight

            val navigationHeight = getBottomStatusHeight(requireContext())
            if (navigationHeight != tempNavigationBarHeight) {
                naHeight?.invoke(navigationHeight)
                Log.i(TAG, "虚拟导航键高度=$naHeight")
                tempNavigationBarHeight = navigationHeight
            }
            if (realKeyboardHeight != tempKeyboardHeight) {
                keyboardHeight?.invoke(realKeyboardHeight)
                Log.i(TAG, "键盘高度=$realKeyboardHeight")
                tempKeyboardHeight = realKeyboardHeight
                if (realKeyboardHeight > 100) {
                    saveKeyboardHeight = realKeyboardHeight
                }
            }
        }
    }
    requireActivity(). window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
}

/**状态栏高度*/
val Fragment.mStatusBarHeight: Int
    get() {
        return Resources.getSystem().getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

//获取底部导航的高度
fun getBottomStatusHeight(context: Context): Int {
    return if (checkNavigationBarShow(context)) {
        val totalHeight = getDpi(context)
        val contentHeight = getScreenHeight(context)
        Log.e(TAG, "--显示虚拟导航了--")
        totalHeight - contentHeight
    } else {
        Log.e(TAG, "--没有虚拟导航 或者虚拟导航隐藏--")
        0
    }
}

//获取屏幕原始尺寸高度，包括虚拟功能键高度
fun getDpi(context: Context): Int {
    var dpi = 0
    val windowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val displayMetrics = DisplayMetrics()
    val c: Class<*>
    try {
        c = Class.forName("android.view.Display")
        val method: Method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
        method.invoke(display, displayMetrics)
        dpi = displayMetrics.heightPixels
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return dpi
}

//获取屏幕高度 不包含虚拟按键=
fun getScreenHeight(context: Context): Int {
    val dm: DisplayMetrics = context.resources.displayMetrics
    return dm.heightPixels
}

/**
 * 判断虚拟导航栏是否显示
 *
 * * @param context 上下文对象
 * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
 */
@SuppressLint("ObsoleteSdkInt", "PrivateApi")
fun checkNavigationBarShow(@NonNull context: Context): Boolean {
    var hasNavigationBar = false
    val rs = context.resources
    val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
    if (id > 0) {
        hasNavigationBar = rs.getBoolean(id)
    }
    try {
        val systemPropertiesClass = Class.forName("android.os.SystemProperties")
        val m = systemPropertiesClass.getMethod("get", String::class.java)
        val navBarOverride =
            m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
        //判断是否隐藏了底部虚拟导航
        var navigationBarIsMin = 0
        navigationBarIsMin = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Settings.System.getInt(context.contentResolver, "navigationbar_is_min", 0)
        } else {
            Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
        }
        if ("1" == navBarOverride || 1 == navigationBarIsMin) {
            hasNavigationBar = false
        } else if ("0" == navBarOverride) {
            hasNavigationBar = true
        }
    } catch (e: java.lang.Exception) {
    }
    return hasNavigationBar
}