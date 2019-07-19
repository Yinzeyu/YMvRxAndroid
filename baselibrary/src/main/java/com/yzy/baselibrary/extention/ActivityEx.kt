package com.yzy.baselibrary.extention

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.yzy.baselibrary.utils.NavigationUtils
import com.yzy.baselibrary.utils.NavigationUtils.Companion.getNavigationBarHeight
import com.yzy.baselibrary.utils.SchedulersUtil
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * description :Activity相关扩展：如屏幕宽高、状态栏高度、虚拟导航键高度获取、键盘高度获取、键盘监听
 * 额外增加：设置状态栏颜色、设置是否填充布局到状态栏、设置虚拟导航键是否隐藏、设置是否全屏显示等
 *
 *@date 2019/7/15
 *@author: yzy.
 */
//打印的Tag
private val TAG = "ActivityEx"
//需要保存键盘高度
private var saveKeyboardHeight = 0
//方便判断改变的临时变量
private var tempKeyboardHeight = 0
//方便判断改变的临时变量
private var tempNavigationBarHeight = 0
//为了解决一直重复设置高度的问题
private var lastSetSetKeyboardHeight = 0
//防止键盘高度改变太快
private var disposable: Disposable? = null

/** 上下文(方便使用)*/
val Activity.mContext: Context
    get() {
        return this
    }

/**Activity本身(弹窗,权限请求等需要用到Activity)*/
val Activity.mActivity: Activity
    get() {
        return this
    }

/**屏幕宽度*/
val Activity.mScreenWidth: Int
    get() {
        return resources.displayMetrics.widthPixels
    }

/**屏幕高度(包含状态栏高度但不包含底部虚拟按键高度)*/
val Activity.mScreenHeight: Int
    get() {
        return resources.displayMetrics.heightPixels
    }

/**状态栏高度*/
val Activity.mStatusBarHeight: Int
    get() {
        return Resources.getSystem()
            .getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

/**ContentView*/
val Activity.mContentView: FrameLayout
    get() {
        return this.findViewById(android.R.id.content)
    }

/**状态栏操作简化*/
var Activity.mUiSystem: Int
    get() = window.decorView.systemUiVisibility
    set(value) {
        window.decorView.systemUiVisibility = value
    }

/**获取虚拟导航高度(部分手机可能不准)*/
fun Activity.getHeightNavigationBar(): Int {
    return NavigationUtils.getNavigationBarHeight(this)
}

/**获取当前键盘高度*/
fun Activity.getHeightKeyboard(): Int {
    val rect = Rect()
    //使用最外层布局填充，进行测算计算
    window.decorView.getWindowVisibleDisplayFrame(rect)
    val heightDiff = window.decorView.height - (rect.bottom - rect.top)
    return Math.max(0, heightDiff - mStatusBarHeight - getHeightNavigationBar())
}

/**添加键盘监听*/
fun Activity.addListerKeyboard(
    naHeight: ((naHeight: Int) -> Unit)? = null,
    keyboardHeight: ((keyboardHeight: Int) -> Unit)? = null
) {
    val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val realKeyboardHeight = getHeightKeyboard()
        if (realKeyboardHeight != lastSetSetKeyboardHeight && window.decorView.height > 0
            && (realKeyboardHeight > 300 || realKeyboardHeight == 0)
        ) {
            lastSetSetKeyboardHeight = realKeyboardHeight
            disposable?.dispose()
            disposable = Flowable.timer(100, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .compose(SchedulersUtil.applyFlowableSchedulers())
                .subscribe {
                    val navigationHeight = getNavigationBarHeight(this)
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
    }
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
}

/**设置状态栏颜色*/
fun Activity.setStatusColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

/**
MODE1可以获取到虚拟导航键的高度，MODE2对虚拟导航键的操作有影响
SYSTEM_UI_FLAG_LOW_PROFILE	弱化状态栏和导航栏的图标
SYSTEM_UI_FLAG_HIDE_NAVIGATION	隐藏导航栏，用户点击屏幕会显示导航栏
SYSTEM_UI_FLAG_FULLSCREEN	隐藏状态栏
SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION	拓展布局到导航栏后面
SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN	拓展布局到状态栏后面
SYSTEM_UI_FLAG_LAYOUT_STABLE	稳定的布局，不会随系统栏的隐藏、显示而变化
SYSTEM_UI_FLAG_IMMERSIVE	沉浸模式，用户可以交互的界面（配合View.SYSTEM_UI_FLAG_FULLSCREEN和View.SYSTEM_UI_FLAG_HIDE_NAVIGATION）
SYSTEM_UI_FLAG_IMMERSIVE_STICKY	沉浸模式，用户可以交互的界面。同时，用户上下拉系统栏时，会自动隐藏系统栏（配合View.SYSTEM_UI_FLAG_FULLSCREEN和View.SYSTEM_UI_FLAG_HIDE_NAVIGATION）
 */

/**填充顶部不填充底部*/
fun Activity.uiMode1Normal() {
    mUiSystem = UI_MERGE_NORMAL_TOP
    mContentView.setPadding(0, mStatusBarHeight, 0, 0)
}

/**填充顶部*/
fun Activity.uiMode1FillStatus(needFill: Boolean) {
    mContentView.setPadding(0, if (needFill) 0 else mStatusBarHeight, 0, 0)
    if (needFill) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    } else {
        when (mUiSystem) {
            mUiSystem or UI_MERGE_HIDE_NAV_FILL_TOP -> mUiSystem = UI_MERGE_HIDE_NAV_FILL_TOP
            mUiSystem or UI_MERGE_SHOW_NAV_FILL_TOP -> mUiSystem = UI_MERGE_SHOW_NAV_FILL_TOP
            mUiSystem or UI_MERGE_FULL_SCREEN_TOP -> mUiSystem = UI_MERGE_FULL_SCREEN_TOP
            mUiSystem or UI_MERGE_NO_FULL_SCREEN_TOP -> mUiSystem = UI_MERGE_NO_FULL_SCREEN_TOP
        }
    }
}

/**隐藏底部不填充*/
fun Activity.uiMode1HideNav(needHide: Boolean) {
    mUiSystem = if (needHide) UI_MERGE_HIDE_NAV_FILL_TOP else UI_MERGE_SHOW_NAV_FILL_TOP
    if (mContentView.paddingTop == 0) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    }
}

/**全屏保留底部*/
fun Activity.uiMode1FullScreen(needFull: Boolean) {
    mUiSystem = if (needFull) UI_MERGE_FULL_SCREEN_TOP else UI_MERGE_NO_FULL_SCREEN_TOP
    if (!needFull && mContentView.paddingTop == 0) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    }
}

/**填充顶部和底部模式*/
fun Activity.uiMode2Normal() {
    mUiSystem = UI_MERGE_NORMAL_ALL
    mContentView.setPadding(0, mStatusBarHeight, 0, 0)
}

/**填充顶部*/
fun Activity.uiMode2FillStatus(needFill: Boolean) {
    mContentView.setPadding(0, if (needFill) 0 else mStatusBarHeight, 0, 0)
    if (needFill) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    } else {
        when (mUiSystem) {
            mUiSystem or UI_MERGE_HIDE_NAV_FILL_ALL -> mUiSystem = UI_MERGE_HIDE_NAV_FILL_ALL
            mUiSystem or UI_MERGE_SHOW_NAV_FILL_ALL -> mUiSystem = UI_MERGE_SHOW_NAV_FILL_ALL
            mUiSystem or UI_MERGE_FULL_SCREEN_ALL -> mUiSystem = UI_MERGE_FULL_SCREEN_ALL
            mUiSystem or UI_MERGE_NO_FULL_SCREEN_ALL -> mUiSystem = UI_MERGE_NO_FULL_SCREEN_ALL
        }
    }
}

/**隐藏底部填充*/
fun Activity.uiMode2HideNav(needHide: Boolean) {
    mUiSystem = if (needHide) UI_MERGE_HIDE_NAV_FILL_ALL else UI_MERGE_SHOW_NAV_FILL_ALL
    if (mContentView.paddingTop == 0) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    }
}

/**全屏*/
fun Activity.uiMode2FullScreen(needFull: Boolean) {
    mUiSystem = if (needFull) UI_MERGE_FULL_SCREEN_ALL else UI_MERGE_NO_FULL_SCREEN_ALL
    if (!needFull && mContentView.paddingTop == 0) {
        mUiSystem = mUiSystem or UI_LOW_POWER
    }
}

//----------------------------↓↓↓简化系统变量↓↓↓----------------------------//
/**状态栏低电量模式*/
private const val UI_LOW_POWER = View.SYSTEM_UI_FLAG_LOW_PROFILE
/**填充状态栏*/
private const val UI_FILL_TOP = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
/**填充底部导航栏*/
private const val UI_FILL_BOTTOM = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
/**固定操作*/
private const val UI_STICKY = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
/**常规*/
private const val UI_STABLE = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
/**隐藏底部导航栏*/
private const val UI_HIDE_NAI = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//----------------------------↓↓↓组合出状态栏和导航键的操作↓↓↓----------------------------//
/**正常显示1:填充顶部和底部*/
private const val UI_MERGE_NORMAL_ALL = UI_STABLE or
    UI_STICKY or
    UI_LOW_POWER or
    UI_FILL_TOP or
    UI_FILL_BOTTOM
/**正常显示2:只填充顶部*/
private const val UI_MERGE_NORMAL_TOP = UI_STABLE or
    UI_STICKY or
    UI_LOW_POWER or
    UI_FILL_TOP
/**全屏填充顶部和底部*/
private const val UI_MERGE_FULL_SCREEN_ALL = UI_STABLE or
    UI_STICKY or
    UI_LOW_POWER or
    UI_FILL_BOTTOM or
    UI_FILL_TOP or
    UI_HIDE_NAI or
    View.SYSTEM_UI_FLAG_FULLSCREEN
/**全屏只填充顶部*/
private const val UI_MERGE_FULL_SCREEN_TOP = UI_STABLE or
    UI_STICKY or
    UI_LOW_POWER or
    UI_FILL_TOP or
    UI_HIDE_NAI or
    View.SYSTEM_UI_FLAG_FULLSCREEN
/**非全屏填充顶部和底部*/
private const val UI_MERGE_NO_FULL_SCREEN_ALL = UI_STABLE or
    UI_FILL_TOP or
    UI_FILL_BOTTOM
/**非全屏只填充顶部*/
private const val UI_MERGE_NO_FULL_SCREEN_TOP = UI_STABLE or
    UI_FILL_TOP
/**隐藏底部并填充顶部和底部*/
private const val UI_MERGE_HIDE_NAV_FILL_ALL = UI_STABLE or
    UI_STICKY or
    UI_FILL_TOP or
    UI_FILL_BOTTOM or
    UI_HIDE_NAI
/**隐藏底部并填充底部*/
private const val UI_MERGE_HIDE_NAV_FILL_TOP = UI_STABLE or
    UI_STICKY or
    UI_FILL_TOP or
    UI_HIDE_NAI
/**显示底部并填充顶部和底部*/
private const val UI_MERGE_SHOW_NAV_FILL_ALL = UI_STABLE or
    UI_STICKY or
    UI_FILL_TOP or
    UI_FILL_BOTTOM
/**显示底部并填充底部*/
private const val UI_MERGE_SHOW_NAV_FILL_TOP = UI_STABLE or
    UI_STICKY or
    UI_FILL_TOP


/**设置状态栏字体为黑色*/
fun Activity.setStatusBarBlackText() {
    //原生
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    //小米
    setMiUIStatusBarTextColor(this, true)
    //魅族
    setFlymeStatusBarTextColor(this, true)
}

/**设置状态栏字体为白色*/
fun Activity.setStatusBarWhiteText() {
    //原生
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    //小米
    setMiUIStatusBarTextColor(this, false)
    //魅族
    setFlymeStatusBarTextColor(this, false)
}

/**
 * 小米设置状态栏字体颜色
 *
 * @param isBlack
 */
private fun setMiUIStatusBarTextColor(activity: Activity, isBlack: Boolean) {
    val clazz = activity.window::class.java
    try {
        val darkModeFlag: Int
        val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        darkModeFlag = field.getInt(layoutParams)
        val extraFlagField = clazz.getMethod(
            "setExtraFlags",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        extraFlagField.invoke(activity.window, if (isBlack) darkModeFlag else 0, darkModeFlag)
    } catch (e: Exception) {
        //            Logger.e("不是MIUI");
    }
}
/**
 * 魅族设置状态栏字体颜色
 *
 * @param isBlack
 * @return
 */
private fun setFlymeStatusBarTextColor(activity: Activity, isBlack: Boolean): Boolean {
    var result = false
    if (activity.window != null) {
        try {
            val lp = activity.window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            if (isBlack) {
                value = value or bit
            } else {
                value = value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            activity.window.attributes = lp
            result = true
        } catch (e: Exception) {
            //                Logger.e("不是Flyme");
        }
    }
    return result
}
