package com.yzy.baselibrary.extention

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.yzy.baselibrary.R
import java.lang.reflect.Field
import java.lang.reflect.Method


const val DEFAULT_STATUS_BAR_ALPHA = 112
private val FAKE_STATUS_BAR_VIEW_ID: Int = R.id.bar_fake_status_bar_view
private val FAKE_TRANSLUCENT_VIEW_ID: Int = R.id.bar_translucent_view
private const val TAG_KEY_HAVE_SET_OFFSET = -123

/**
 * 设置状态栏颜色
 *
 * @param activity 需要设置的 activity
 * @param color    状态栏颜色值
 */
fun setColor(activity: Activity, @ColorInt color: Int) {
    setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA)
}

/**
 * 设置状态栏颜色
 *
 * @param activity       需要设置的activity
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun setColor(activity: Activity, @ColorInt color: Int, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    activity.window.statusBarColor = calculateStatusColor(color, statusBarAlpha)
}

/**
 * 为滑动返回界面设置状态栏颜色
 *
 * @param activity 需要设置的activity
 * @param color    状态栏颜色值
 */
fun setColorForSwipeBack(activity: Activity, color: Int) {
    setColorForSwipeBack(
        activity,
        color,
        DEFAULT_STATUS_BAR_ALPHA
    )
}

/**
 * 为滑动返回界面设置状态栏颜色
 *
 * @param activity       需要设置的activity
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun setColorForSwipeBack(activity: Activity, @ColorInt color: Int, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
        val contentView = activity.findViewById<View>(R.id.content) as ViewGroup
        val rootView = contentView.getChildAt(0)
        val statusBarHeight = activity.mStatusBarHeight
        if (rootView != null && rootView is CoordinatorLayout) {
            rootView.setStatusBarBackgroundColor(calculateStatusColor(color, statusBarAlpha))
        } else {
            contentView.setPadding(0, statusBarHeight, 0, 0)
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha))
        }
        setTransparentForWindow(activity)
}

/**
 * 设置状态栏纯色 不加半透明效果
 *
 * @param activity 需要设置的 activity
 * @param color    状态栏颜色值
 */
fun setColorNoTranslucent(activity: Activity, @ColorInt color: Int) {
    setColor(activity, color, 0)
}

/**
 * 使状态栏半透明
 *
 *
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 *
 * @param activity 需要设置的activity
 */
fun setTranslucent(activity: Activity) {
    setTranslucent(activity, DEFAULT_STATUS_BAR_ALPHA)
}

/**
 * 使状态栏半透明
 *
 *
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 *
 * @param activity       需要设置的activity
 * @param statusBarAlpha 状态栏透明度
 */
fun setTranslucent(activity: Activity, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    setTransparent(activity)
    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 针对根布局是 CoordinatorLayout, 使状态栏半透明
 *
 *
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 *
 * @param activity       需要设置的activity
 * @param statusBarAlpha 状态栏透明度
 */
fun setTranslucentForCoordinatorLayout(activity: Activity, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    transparentStatusBar(activity)
    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 设置状态栏全透明
 *
 * @param activity 需要设置的activity
 */
fun setTransparent(activity: Activity) {
    transparentStatusBar(activity)
    setRootView(activity)
}

/**
 * 使状态栏透明(5.0以上半透明效果,不建议使用)
 *
 *
 * 适用于图片作为背景的界面,此时需要图片填充到状态栏
 *
 * @param activity 需要设置的activity
 */
@Deprecated("")
fun setTranslucentDiff(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setRootView(activity)
}

/**
 * 为DrawerLayout 布局设置状态栏变色
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 * @param color        状态栏颜色值
 */
fun setColorForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int) {
    setColorForDrawerLayout(activity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA)
}

/**
 * 为DrawerLayout 布局设置状态栏颜色,纯色
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 * @param color        状态栏颜色值
 */
fun setColorNoTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int) {
    setColorForDrawerLayout(activity, drawerLayout, color, 0)
}

/**
 * 为DrawerLayout 布局设置状态栏变色
 *
 * @param activity       需要设置的activity
 * @param drawerLayout   DrawerLayout
 * @param color          状态栏颜色值
 * @param statusBarAlpha 状态栏透明度
 */
fun setColorForDrawerLayout(
    activity: Activity, drawerLayout: DrawerLayout, @ColorInt color: Int,
    @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int
) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = Color.TRANSPARENT
    // 生成一个状态栏大小的矩形
// 添加 statusBarView 到布局中
    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    val fakeStatusBarView: View = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID)
    if (fakeStatusBarView.visibility == GONE) {
        fakeStatusBarView.visibility = VISIBLE
    }
    fakeStatusBarView.setBackgroundColor(color)
    // 内容布局不是 LinearLayout 时,设置padding top
    if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
        contentLayout.getChildAt(1)
            .setPadding(
                contentLayout.paddingLeft,
                activity.mStatusBarHeight + contentLayout.paddingTop,
                contentLayout.paddingRight,
                contentLayout.paddingBottom
            )
    }
    // 设置属性
    setDrawerLayoutProperty(
        drawerLayout,
        contentLayout
    )
    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 设置 DrawerLayout 属性
 *
 * @param drawerLayout              DrawerLayout
 * @param drawerLayoutContentLayout DrawerLayout 的内容布局
 */
fun setDrawerLayoutProperty(
    drawerLayout: DrawerLayout,
    drawerLayoutContentLayout: ViewGroup
) {
    val drawer = drawerLayout.getChildAt(1) as ViewGroup
    drawerLayout.fitsSystemWindows = false
    drawerLayoutContentLayout.fitsSystemWindows = false
    drawerLayoutContentLayout.clipToPadding = true
    drawer.fitsSystemWindows = false
}

/**
 * 为DrawerLayout 布局设置状态栏变色(5.0以下无半透明效果,不建议使用)
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 * @param color        状态栏颜色值
 */
@Deprecated("")
fun setColorForDrawerLayoutDiff(
    activity: Activity,
    drawerLayout: DrawerLayout, @ColorInt color: Int
) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 生成一个状态栏大小的矩形
        val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
        val fakeStatusBarView: View = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID)
        if (fakeStatusBarView.visibility == GONE) {
            fakeStatusBarView.visibility = VISIBLE
        }
        fakeStatusBarView.setBackgroundColor(calculateStatusColor(color, DEFAULT_STATUS_BAR_ALPHA))
        // 内容布局不是 LinearLayout 时,设置padding top
        if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, activity.mStatusBarHeight, 0, 0)
        }
        // 设置属性
        setDrawerLayoutProperty(
            drawerLayout,
            contentLayout
        )
}

/**
 * 为 DrawerLayout 布局设置状态栏透明
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 */
fun setTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
    setTranslucentForDrawerLayout(
        activity,
        drawerLayout,
        DEFAULT_STATUS_BAR_ALPHA
    )
}

/**
 * 为 DrawerLayout 布局设置状态栏透明
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 */
fun setTranslucentForDrawerLayout(
    activity: Activity, drawerLayout: DrawerLayout,
    @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    setTransparentForDrawerLayout(activity, drawerLayout)
    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 为 DrawerLayout 布局设置状态栏透明
 *
 * @param activity     需要设置的activity
 * @param drawerLayout DrawerLayout
 */
fun setTransparentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = Color.TRANSPARENT
    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    // 内容布局不是 LinearLayout 时,设置padding top
    if (contentLayout !is LinearLayout && contentLayout.getChildAt(1) != null) {
        contentLayout.getChildAt(1).setPadding(0, activity.mStatusBarHeight, 0, 0)
    }
    // 设置属性
    setDrawerLayoutProperty(drawerLayout, contentLayout)
}

/**
 * 为头部是 ImageView 的界面设置状态栏全透明
 *
 * @param activity       需要设置的activity
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTransparentForImageView(activity: Activity, needOffsetView: View?) {
    setTranslucentForImageView(
        activity,
        0,
        needOffsetView
    )
}

/**
 * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
 *
 * @param activity       需要设置的activity
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTranslucentForImageView(activity: Activity, needOffsetView: View?) {
    setTranslucentForImageView(
        activity,
        DEFAULT_STATUS_BAR_ALPHA,
        needOffsetView
    )
}

/**
 * 为头部是 ImageView 的界面设置状态栏透明
 *
 * @param activity       需要设置的activity
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTranslucentForImageView(activity: Activity, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int, needOffsetView: View?) {
    setTransparentForWindow(activity)
    addTranslucentView(activity, statusBarAlpha)
    if (needOffsetView != null) {
        val haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) {
            return
        }
        val layoutParams = needOffsetView.layoutParams as MarginLayoutParams
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + activity.mStatusBarHeight,
            layoutParams.rightMargin, layoutParams.bottomMargin
        )
        needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true)
    }
}

/**
 * 为 fragment 头部是 ImageView 的设置状态栏透明
 *
 * @param activity       fragment 对应的 activity
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTranslucentForImageViewInFragment(activity: Activity, needOffsetView: View?) {
    setTranslucentForImageViewInFragment(activity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView)
}

/**
 * 为 fragment 头部是 ImageView 的设置状态栏透明
 *
 * @param activity       fragment 对应的 activity
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTransparentForImageViewInFragment(activity: Activity, needOffsetView: View?) {
    setTranslucentForImageViewInFragment(activity, 0, needOffsetView)
}

/**
 * 为 fragment 头部是 ImageView 的设置状态栏透明
 *
 * @param activity       fragment 对应的 activity
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下偏移的 View
 */
fun setTranslucentForImageViewInFragment(
    activity: Activity, @androidx.annotation.IntRange(
        from = 0,
        to = 255
    ) statusBarAlpha: Int, needOffsetView: View?
) = setTranslucentForImageView(activity, statusBarAlpha, needOffsetView)

/**
 * 隐藏伪状态栏 View
 *
 * @param activity 调用的 Activity
 */
fun hideFakeStatusBarView(activity: Activity) {
    val decorView = activity.window.decorView as ViewGroup
    val fakeStatusBarView: View = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID)
    fakeStatusBarView.visibility = GONE
    val fakeTranslucentView: View = decorView.findViewById(FAKE_TRANSLUCENT_VIEW_ID)
    fakeTranslucentView.visibility = GONE
}

@TargetApi(Build.VERSION_CODES.M)
fun setLightMode(activity: Activity) {
    setMIUIStatusBarDarkIcon(activity, true)
    setMeiZuStatusBarDarkIcon(activity, true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

@TargetApi(Build.VERSION_CODES.M)
fun setDarkMode(activity: Activity) {
    setMIUIStatusBarDarkIcon(activity, false)
    setMeiZuStatusBarDarkIcon(activity, false)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

/**
 * 修改 MIUI V6  以上状态栏颜色
 */
@SuppressLint("PrivateApi")
fun setMIUIStatusBarDarkIcon(activity: Activity, darkIcon: Boolean) {
    val clazz: Class<out Window?> = activity.window.javaClass
    try {
        val layoutParams =
            Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field: Field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val darkModeFlag: Int = field.getInt(layoutParams)
        val extraFlagField: Method = clazz.getMethod(
            "setExtraFlags",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(activity.window, if (darkIcon) darkModeFlag else 0, darkModeFlag)
    } catch (e: Exception) { //e.printStackTrace();
    }
}

/**
 * 修改魅族状态栏字体颜色 Flyme 4.0
 */
@SuppressLint("All")
fun setMeiZuStatusBarDarkIcon(activity: Activity, darkIcon: Boolean) {
    try {
        val lp = activity.window.attributes
        val darkFlag: Field =
            WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
        val meiZuFlags: Field =
            WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
        darkFlag.isAccessible = true
        meiZuFlags.isAccessible = true
        val bit: Int = darkFlag.getInt(null)
        var value: Int = meiZuFlags.getInt(lp)
        value = if (darkIcon) {
            value or bit
        } else {
            value and bit.inv()
        }
        meiZuFlags.setInt(lp, value)
        activity.window.attributes = lp
    } catch (e: Exception) { //e.printStackTrace();
    }
}

///////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////
@TargetApi(Build.VERSION_CODES.KITKAT)
fun clearPreviousSetting(activity: Activity) {
    val decorView = activity.window.decorView as ViewGroup
    val fakeStatusBarView = decorView.findViewById<View>(FAKE_STATUS_BAR_VIEW_ID)
    if (fakeStatusBarView != null) {
        decorView.removeView(fakeStatusBarView)
        val rootView = (activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.setPadding(0, 0, 0, 0)
    }
}

/**
 * 添加半透明矩形条
 *
 * @param activity       需要设置的 activity
 * @param statusBarAlpha 透明值
 */
fun addTranslucentView(activity: Activity, @androidx.annotation.IntRange(from = 0, to = 255) statusBarAlpha: Int) {
    val contentView = activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
    val fakeTranslucentView = contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
    if (fakeTranslucentView != null) {
        if (fakeTranslucentView.visibility == GONE) {
            fakeTranslucentView.visibility = VISIBLE
        }
        fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
    } else {
        contentView.addView(
         createTranslucentStatusBarView(activity, statusBarAlpha)
        )
    }
}

/**
 * 生成一个和状态栏大小相同的彩色矩形条
 *
 * @param activity 需要设置的 activity
 * @param color    状态栏颜色值
 * @return 状态栏矩形条
 */
fun createStatusBarView(activity: Activity, @ColorInt color: Int): View? {
    return createStatusBarView(activity, color, 0)
}

/**
 * 生成一个和状态栏大小相同的半透明矩形条
 *
 * @param activity 需要设置的activity
 * @param color    状态栏颜色值
 * @param alpha    透明值
 * @return 状态栏矩形条
 */
fun createStatusBarView(activity: Activity, @ColorInt color: Int, alpha: Int): View? { // 绘制一个和状态栏一样高的矩形
    val statusBarView = View(activity)
    val params =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.mStatusBarHeight)
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(calculateStatusColor(color, alpha))
    statusBarView.id = FAKE_STATUS_BAR_VIEW_ID
    return statusBarView
}

/**
 * 设置根布局参数
 */
fun setRootView(activity: Activity) {
    val parent = activity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
    var i = 0
    val count = parent.childCount
    while (i < count) {
        val childView = parent.getChildAt(i)
        if (childView is ViewGroup) {
            childView.setFitsSystemWindows(true)
            childView.clipToPadding = true
        }
        i++
    }
}

/**
 * 设置透明
 */
fun setTransparentForWindow(activity: Activity) {
    activity.window.statusBarColor = Color.TRANSPARENT
    activity.window.decorView.systemUiVisibility =
        SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

}

/**
 * 使状态栏透明
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
fun transparentStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window.statusBarColor = Color.TRANSPARENT
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/**
 * 创建半透明矩形 View
 *
 * @param alpha 透明值
 * @return 半透明 View
 */
fun createTranslucentStatusBarView(activity: Activity, alpha: Int): View? { // 绘制一个和状态栏一样高的矩形
    val statusBarView = View(activity)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.mStatusBarHeight)
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
    statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
    return statusBarView
}

/**
 * 计算状态栏颜色
 *
 * @param color color值
 * @param alpha alpha值
 * @return 最终的状态栏颜色
 */
fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
    if (alpha == 0) {
        return color
    }
    val a = 1 - alpha / 255f
    var red = color shr 16 and 0xff
    var green = color shr 8 and 0xff
    var blue = color and 0xff
    red = (red * a + 0.5).toInt()
    green = (green * a + 0.5).toInt()
    blue = (blue * a + 0.5).toInt()
    return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
}