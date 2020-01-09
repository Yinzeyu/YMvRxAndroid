package com.yzy.example.widget.imagewatcher.immersionbar

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.util.*

/**
 * 沉浸式参数信息
 *
 * @author geyifeng
 * @date 2017 /5/9
 */
class BarParams : Cloneable {
    /**
     * 状态栏颜色
     * The Status bar color.
     */
    @ColorInt
    var statusBarColor = Color.TRANSPARENT
    /**
     * 导航栏颜色
     * The Navigation bar color.
     */
    @ColorInt
    var navigationBarColor = Color.BLACK
    /**
     * The Default navigation bar color.
     */
    var defaultNavigationBarColor = Color.BLACK
    /**
     * 状态栏透明度
     * The Status bar alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var statusBarAlpha = 0.0f
    /**
     * 导航栏透明度
     * The Navigation bar alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var navigationBarAlpha = 0.0f
    /**
     * 有导航栏的情况，全屏显示
     * The Full screen.
     */
    var fullScreen :Boolean= false
    /**
     * 是否隐藏了导航栏
     * The Hide navigation bar.
     */
    var hideNavigationBar = false
    /**
     * 隐藏Bar
     * The Bar hide.
     */
    var barHide = BarHide.FLAG_SHOW_BAR
    /**
     * 状态栏字体深色与亮色标志位
     * The Dark font.
     */
    var statusBarDarkFont = false
    /**
     * 导航栏图标深色与亮色标志位
     * The Navigation bar dark icon.
     */
    var navigationBarDarkIcon = false
    /**
     * 是否启用 自动根据StatusBar颜色调整深色模式与亮色模式
     * The Auto status bar dark mode enable.
     */
    var autoStatusBarDarkModeEnable = false
    /**
     * 是否启用 自动根据NavigationBar颜色调整深色模式与亮色模式
     * The Auto navigation bar dark mode enable.
     */
    var autoNavigationBarDarkModeEnable = false
    /**
     * The Auto status bar dark mode alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var autoStatusBarDarkModeAlpha = 0.0f
    /**
     * The Auto navigation bar dark mode alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var autoNavigationBarDarkModeAlpha = 0.0f
    /**
     * 是否可以修改状态栏颜色
     * The Status bar flag.
     */
    var statusBarColorEnabled = true
    /**
     * 状态栏变换后的颜色
     * The Status bar color transform.
     */
    @ColorInt
    var statusBarColorTransform = Color.BLACK
    /**
     * 导航栏变换后的颜色
     * The Navigation bar color transform.
     */
    @ColorInt
    var navigationBarColorTransform = Color.BLACK
    /**
     * 支持view变色
     * The View map.
     */
    var viewMap: MutableMap<View?, Map<Int, Int>> =
        HashMap()
    /**
     * The View alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var viewAlpha = 0.0f
    /**
     * The Status bar color content view.
     */
    @ColorInt
    var contentColor = Color.TRANSPARENT
    /**
     * The Status bar color content view transform.
     */
    @ColorInt
    var contentColorTransform = Color.BLACK
    /**
     * The Status bar content view alpha.
     */
    @FloatRange(from = 0.0, to = 1.0)
    var contentAlpha = 0.0f
    /**
     * 解决标题栏与状态栏重叠问题
     * The Fits.
     */
    var fits = false
    /**
     * 解决标题栏与状态栏重叠问题
     * The Title bar view.
     */
    var titleBarView: View? = null
    /**
     * 解决标题栏与状态栏重叠问题
     * The Status bar view by height.
     */
    var statusBarView: View? = null
    /**
     * flymeOS状态栏字体变色
     * The Flyme os status bar font color.
     */
    @ColorInt
    var flymeOSStatusBarFontColor = 0
    /**
     * 结合actionBar使用
     * The Is support action bar.
     */
    var isSupportActionBar = false
    /**
     * 解决软键盘与输入框冲突问题
     * The Keyboard enable.
     */
    var keyboardEnable = false
    /**
     * 软键盘属性
     * The Keyboard mode.
     */
    var keyboardMode = (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
            or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    /**
     * 是否能修改导航栏颜色
     * The Navigation bar enable.
     */
    var navigationBarEnable = true
    /**
     * 是否能修改4.4手机以及华为emui3.1导航栏颜色
     * The Navigation bar with kitkat enable.
     */
    var navigationBarWithKitkatEnable = true
    /**
     * 是否可以修改emui3系列手机导航栏
     * The Navigation bar with emui 3 enable.
     */
    var navigationBarWithEMUI3Enable = true
    /**
     * 是否可以沉浸式
     * The Init enable.
     */
    var barEnable = true
    /**
     * 软键盘监听类
     * The On keyboard listener.
     */
    var onKeyboardListener: OnKeyboardListener? = null
    /**
     * 导航栏显示隐藏监听
     */
    var onNavigationBarListener: OnNavigationBarListener? = null
    /**
     * 横竖屏监听
     */
    var onBarListener: OnBarListener? = null

    public override fun clone(): BarParams {
        var barParams = BarParams()
        try {
            barParams = super.clone() as BarParams
        } catch (ignored: CloneNotSupportedException) {
        }
        return barParams
    }
}