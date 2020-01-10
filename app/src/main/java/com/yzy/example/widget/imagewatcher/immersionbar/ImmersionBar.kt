package com.yzy.example.widget.imagewatcher.immersionbar

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import java.util.*

/**
 * android 4.4以上沉浸式以及bar的管理
 *
 * @author gyf
 * @date 2017 /05/09
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class ImmersionBar : ImmersionCallback {
    var activity: Activity
    private var mSupportFragment: Fragment? = null
    private var mFragment: android.app.Fragment? = null
    private var mDialog: Dialog? = null
    private var mWindow: Window? = null
    private var mDecorView: ViewGroup? = null
    private var mContentView: ViewGroup? = null
    /**
     * 是否是在Activity里使用
     */
    private var mIsActivity = false
    /**
     * 是否是在Fragment的使用的
     *
     * @return the boolean
     */
    /**
     * 是否是在Fragment里使用
     */
    var isFragment = false
    /**
     * 是否是在Dialog里使用
     */
    private var mIsDialog = false
    /**
     * Gets bar params.
     *
     * @return the bar params
     */
    /**
     * 用户配置的bar参数
     */
    var barParams: BarParams? = null

    /**
     * 系统bar相关信息
     */
    private var mBarConfig: BarConfig? = null
    /**
     * 导航栏的高度，适配Emui3系统有用
     */
    private var mNavigationBarHeight = 0
    /**
     * 导航栏的宽度，适配Emui3系统有用
     */
    private var mNavigationBarWidth = 0
    /**
     * ActionBar的高度
     */
    private var mActionBarHeight = 0
    /**
     * 软键盘监听相关
     */
    private var mFitsKeyboard: FitsKeyboard? = null
    /**
     * 用户使用tag增加的bar参数的集合
     */
    private val mTagMap: MutableMap<String, BarParams?> =
        HashMap()
    /**
     * 当前顶部布局和状态栏重叠是以哪种方式适配的
     */
    private var mFitsStatusBarType =
        Constants.FLAG_FITS_DEFAULT
    /**
     * 是否已经调用过init()方法了
     */
    private var mInitialized = false
    /**
     * ActionBar是否是在LOLLIPOP下设备使用
     *
     * @return boolean
     */
    /**
     * ActionBar是否是在LOLLIPOP下设备使用
     */
    var isActionBarBelowLOLLIPOP = false
    private var mKeyboardTempEnable = false
    /**
     * Gets padding left.
     *
     * @return the padding left
     */
    var paddingLeft = 0
    /**
     * Gets padding top.
     *
     * @return the padding top
     */
    var paddingTop = 0
    /**
     * Gets padding right.
     *
     * @return the padding right
     */
    var paddingRight = 0
    /**
     * Gets padding bottom.
     *
     * @return the padding bottom
     */
    var paddingBottom = 0

    /**
     * 在Activity里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     */
    internal constructor(activity: Activity) {
        mIsActivity = true
        this.activity = activity
        initCommonParameter(activity.window)
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    internal constructor(fragment: Fragment) {
        isFragment = true
        activity = fragment.activity!!
        mSupportFragment = fragment
        checkInitWithActivity()
        initCommonParameter(activity?.window)
    }

    /**
     * 在Fragment里初始化
     * Instantiates a new Immersion bar.
     *
     * @param fragment the fragment
     */
    internal constructor(fragment: android.app.Fragment) {
        isFragment = true
        activity = fragment.activity
        mFragment = fragment
        checkInitWithActivity()
        initCommonParameter(activity.getWindow())
    }

    /**
     * 在dialogFragment里使用
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    internal constructor(dialogFragment: DialogFragment) {
        mIsDialog = true
        activity = dialogFragment.activity!!
        mSupportFragment = dialogFragment
        mDialog = dialogFragment.dialog
        checkInitWithActivity()
        initCommonParameter(mDialog!!.window)
    }

    /**
     * 在dialogFragment里使用
     * Instantiates a new Immersion bar.
     *
     * @param dialogFragment the dialog fragment
     */
    internal constructor(dialogFragment: android.app.DialogFragment) {
        mIsDialog = true
        activity = dialogFragment.activity
        mFragment = dialogFragment
        mDialog = dialogFragment.dialog
        checkInitWithActivity()
        initCommonParameter(mDialog?.window)
    }

    /**
     * 在Dialog里初始化
     * Instantiates a new Immersion bar.
     *
     * @param activity the activity
     * @param dialog   the dialog
     */
    internal constructor(activity: Activity, dialog: Dialog?) {
        mIsDialog = true
        this.activity = activity
        mDialog = dialog
        checkInitWithActivity()
        initCommonParameter(mDialog?.window)
    }

    /**
     * 检查是否已经在Activity中初始化了，未初始化则自动初始化
     */
    private fun checkInitWithActivity() {
        if (!with(activity!!)!!.initialized()) {
            with(activity!!)!!.init()
        }
    }

    /**
     * 初始化共同参数
     *
     * @param window window
     */
    private fun initCommonParameter(window: Window?) {
        mWindow = window
        barParams = BarParams()
        mDecorView = mWindow!!.decorView as ViewGroup
        mContentView = mDecorView!!.findViewById(android.R.id.content)
    }

    /**
     * 透明状态栏，默认透明
     *
     * @return the immersion bar
     */
    fun transparentStatusBar(): ImmersionBar {
        barParams!!.statusBarColor = Color.TRANSPARENT
        return this
    }

    /**
     * 透明导航栏，默认黑色
     *
     * @return the immersion bar
     */
    fun transparentNavigationBar(): ImmersionBar {
        barParams?.navigationBarColor = Color.TRANSPARENT
        barParams?.fullScreen = true
        return this
    }

    /**
     * 透明状态栏和导航栏
     *
     * @return the immersion bar
     */
    fun transparentBar(): ImmersionBar {
        barParams!!.statusBarColor = Color.TRANSPARENT
        barParams!!.navigationBarColor = Color.TRANSPARENT
        barParams!!.fullScreen = true
        return this
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    fun statusBarColor(@ColorRes statusBarColor: Int): ImmersionBar {
        return this.statusBarColorInt(ContextCompat.getColor(activity!!, statusBarColor))
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColor(
        @ColorRes statusBarColor: Int,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        return this.statusBarColorInt(ContextCompat.getColor(activity!!, statusBarColor), alpha)
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色，资源文件（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColor(
        @ColorRes statusBarColor: Int,
        @ColorRes statusBarColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        return this.statusBarColorInt(
            ContextCompat.getColor(activity!!, statusBarColor),
            ContextCompat.getColor(activity!!, statusBarColorTransform),
            alpha
        )
    }

    /**
     * 状态栏颜色
     * Status bar color int immersion bar.
     *
     * @param statusBarColor the status bar color
     * @return the immersion bar
     */
    fun statusBarColor(statusBarColor: String?): ImmersionBar {
        return this.statusBarColorInt(Color.parseColor(statusBarColor))
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColor(
        statusBarColor: String?,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        return this.statusBarColorInt(Color.parseColor(statusBarColor), alpha)
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColor(
        statusBarColor: String?,
        statusBarColorTransform: String?,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        return this.statusBarColorInt(
            Color.parseColor(statusBarColor),
            Color.parseColor(statusBarColorTransform),
            alpha
        )
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @return the immersion bar
     */
    fun statusBarColorInt(@ColorInt statusBarColor: Int): ImmersionBar {
        barParams!!.statusBarColor = statusBarColor
        return this
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor 状态栏颜色，资源文件（R.color.xxx）
     * @param alpha          the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColorInt(
        @ColorInt statusBarColor: Int,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        barParams!!.statusBarColor = statusBarColor
        barParams!!.statusBarAlpha = alpha
        return this
    }

    /**
     * 状态栏颜色
     *
     * @param statusBarColor          状态栏颜色，资源文件（R.color.xxx）
     * @param statusBarColorTransform the status bar color transform 状态栏变换后的颜色
     * @param alpha                   the alpha  透明度
     * @return the immersion bar
     */
    fun statusBarColorInt(
        @ColorInt statusBarColor: Int,
        @ColorInt statusBarColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) alpha: Float
    ): ImmersionBar {
        barParams!!.statusBarColor = statusBarColor
        barParams!!.statusBarColorTransform = statusBarColorTransform
        barParams!!.statusBarAlpha = alpha
        return this
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    fun navigationBarColor(@ColorRes navigationBarColor: Int): ImmersionBar {
        return this.navigationBarColorInt(ContextCompat.getColor(activity!!, navigationBarColor))
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    fun navigationBarColor(
        @ColorRes navigationBarColor: Int,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(
            ContextCompat.getColor(activity!!, navigationBarColor),
            navigationAlpha
        )
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    fun navigationBarColor(
        @ColorRes navigationBarColor: Int,
        @ColorRes navigationBarColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(
            ContextCompat.getColor(activity!!, navigationBarColor),
            ContextCompat.getColor(activity!!, navigationBarColorTransform), navigationAlpha
        )
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    fun navigationBarColor(navigationBarColor: String?): ImmersionBar {
        return this.navigationBarColorInt(Color.parseColor(navigationBarColor))
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    fun navigationBarColor(
        navigationBarColor: String?,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(
            Color.parseColor(navigationBarColor),
            navigationAlpha
        )
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    fun navigationBarColor(
        navigationBarColor: String?,
        navigationBarColorTransform: String?,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        return this.navigationBarColorInt(
            Color.parseColor(navigationBarColor),
            Color.parseColor(navigationBarColorTransform), navigationAlpha
        )
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @return the immersion bar
     */
    fun navigationBarColorInt(@ColorInt navigationBarColor: Int): ImmersionBar {
        barParams!!.navigationBarColor = navigationBarColor
        return this
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor the navigation bar color 导航栏颜色
     * @param navigationAlpha    the navigation alpha 透明度
     * @return the immersion bar
     */
    fun navigationBarColorInt(
        @ColorInt navigationBarColor: Int,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        barParams!!.navigationBarColor = navigationBarColor
        barParams!!.navigationBarAlpha = navigationAlpha
        return this
    }

    /**
     * 导航栏颜色
     *
     * @param navigationBarColor          the navigation bar color 导航栏颜色
     * @param navigationBarColorTransform the navigation bar color transform  导航栏变色后的颜色
     * @param navigationAlpha             the navigation alpha  透明度
     * @return the immersion bar
     */
    fun navigationBarColorInt(
        @ColorInt navigationBarColor: Int,
        @ColorInt navigationBarColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) navigationAlpha: Float
    ): ImmersionBar {
        barParams!!.navigationBarColor = navigationBarColor
        barParams!!.navigationBarColorTransform = navigationBarColorTransform
        barParams!!.navigationBarAlpha = navigationAlpha
        return this
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColor(@ColorRes barColor: Int): ImmersionBar {
        return this.barColorInt(ContextCompat.getColor(activity!!, barColor))
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColor(
        @ColorRes barColor: Int, @FloatRange(from = 0.0,to = 1.0
        ) barAlpha: Float
    ): ImmersionBar {
        return this.barColorInt(ContextCompat.getColor(activity!!, barColor), barColor.toFloat())
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColor(
        @ColorRes barColor: Int,
        @ColorRes barColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) barAlpha: Float
    ): ImmersionBar {
        return this.barColorInt(
            ContextCompat.getColor(activity!!, barColor),
            ContextCompat.getColor(activity!!, barColorTransform), barAlpha
        )
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColor(barColor: String?): ImmersionBar {
        return this.barColorInt(Color.parseColor(barColor))
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColor(
        barColor: String?, @FloatRange(from = 0.0,to = 1.0
        ) barAlpha: Float
    ): ImmersionBar {
        return this.barColorInt(Color.parseColor(barColor), barAlpha)
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColor(
        barColor: String?,
        barColorTransform: String?,
        @FloatRange(from = 0.0,to = 1.0) barAlpha: Float
    ): ImmersionBar {
        return this.barColorInt(
            Color.parseColor(barColor),
            Color.parseColor(barColorTransform),
            barAlpha
        )
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @return the immersion bar
     */
    fun barColorInt(@ColorInt barColor: Int): ImmersionBar {
        barParams!!.statusBarColor = barColor
        barParams!!.navigationBarColor = barColor
        return this
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor the bar color
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barColorInt(
        @ColorInt barColor: Int, @FloatRange(from = 0.0,to = 1.0
        ) barAlpha: Float
    ): ImmersionBar {
        barParams!!.statusBarColor = barColor
        barParams!!.navigationBarColor = barColor
        barParams!!.statusBarAlpha = barAlpha
        barParams!!.navigationBarAlpha = barAlpha
        return this
    }

    /**
     * 状态栏和导航栏颜色
     *
     * @param barColor          the bar color
     * @param barColorTransform the bar color transform
     * @param barAlpha          the bar alpha
     * @return the immersion bar
     */
    fun barColorInt(
        @ColorInt barColor: Int,
        @ColorInt barColorTransform: Int,
        @FloatRange(from = 0.0,to = 1.0) barAlpha: Float
    ): ImmersionBar {
        barParams!!.statusBarColor = barColor
        barParams!!.navigationBarColor = barColor
        barParams!!.statusBarColorTransform = barColorTransform
        barParams!!.navigationBarColorTransform = barColorTransform
        barParams!!.statusBarAlpha = barAlpha
        barParams!!.navigationBarAlpha = barAlpha
        return this
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransform(@ColorRes statusBarColorTransform: Int): ImmersionBar {
        return statusBarColorTransformInt(
            ContextCompat.getColor(
                activity!!,
                statusBarColorTransform
            )
        )
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransform(statusBarColorTransform: String?): ImmersionBar {
        return statusBarColorTransformInt(
            Color.parseColor(
                statusBarColorTransform
            )
        )
    }

    /**
     * 状态栏根据透明度最后变换成的颜色
     *
     * @param statusBarColorTransform the status bar color transform
     * @return the immersion bar
     */
    fun statusBarColorTransformInt(@ColorInt statusBarColorTransform: Int): ImmersionBar {
        barParams!!.statusBarColorTransform = statusBarColorTransform
        return this
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(@ColorRes navigationBarColorTransform: Int): ImmersionBar {
        return navigationBarColorTransformInt(
            ContextCompat.getColor(
                activity!!,
                navigationBarColorTransform
            )
        )
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransform(navigationBarColorTransform: String?): ImmersionBar {
        return navigationBarColorTransformInt(
            Color.parseColor(
                navigationBarColorTransform
            )
        )
    }

    /**
     * 导航栏根据透明度最后变换成的颜色
     *
     * @param navigationBarColorTransform the m navigation bar color transform
     * @return the immersion bar
     */
    fun navigationBarColorTransformInt(@ColorInt navigationBarColorTransform: Int): ImmersionBar {
        barParams!!.navigationBarColorTransform = navigationBarColorTransform
        return this
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransform(@ColorRes barColorTransform: Int): ImmersionBar {
        return barColorTransformInt(ContextCompat.getColor(activity!!, barColorTransform))
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransform(barColorTransform: String?): ImmersionBar {
        return barColorTransformInt(Color.parseColor(barColorTransform))
    }

    /**
     * 状态栏和导航栏根据透明度最后变换成的颜色
     *
     * @param barColorTransform the bar color transform
     * @return the immersion bar
     */
    fun barColorTransformInt(@ColorInt barColorTransform: Int): ImmersionBar {
        barParams!!.statusBarColorTransform = barColorTransform
        barParams!!.navigationBarColorTransform = barColorTransform
        return this
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view the view
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view: View?): ImmersionBar {
        return this.addViewSupportTransformColorInt(view, barParams!!.statusBarColorTransform)
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(view: View?, @ColorRes viewColorAfterTransform: Int): ImmersionBar {
        return this.addViewSupportTransformColorInt(
            view,
            ContextCompat.getColor(activity!!, viewColorAfterTransform)
        )
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(
        view: View?, @ColorRes viewColorBeforeTransform: Int,
        @ColorRes viewColorAfterTransform: Int
    ): ImmersionBar {
        return this.addViewSupportTransformColorInt(
            view,
            ContextCompat.getColor(activity!!, viewColorBeforeTransform),
            ContextCompat.getColor(activity!!, viewColorAfterTransform)
        )
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(
        view: View?,
        viewColorAfterTransform: String?
    ): ImmersionBar {
        return this.addViewSupportTransformColorInt(
            view,
            Color.parseColor(viewColorAfterTransform)
        )
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColor(
        view: View?, viewColorBeforeTransform: String?,
        viewColorAfterTransform: String?
    ): ImmersionBar {
        return this.addViewSupportTransformColorInt(
            view,
            Color.parseColor(viewColorBeforeTransform),
            Color.parseColor(viewColorAfterTransform)
        )
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                    the view
     * @param viewColorAfterTransform the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColorInt(view: View?, @ColorInt viewColorAfterTransform: Int): ImmersionBar {
        requireNotNull(view) { "View参数不能为空" }
        val map: MutableMap<Int?, Int?> = HashMap()
        map[barParams!!.statusBarColor] = viewColorAfterTransform
        barParams!!.viewMap[view] = map as Map<Int, Int>
        return this
    }

    /**
     * Add 颜色变换支持View
     *
     * @param view                     the view
     * @param viewColorBeforeTransform the view color before transform
     * @param viewColorAfterTransform  the view color after transform
     * @return the immersion bar
     */
    fun addViewSupportTransformColorInt(
        view: View?, @ColorInt viewColorBeforeTransform: Int,
        @ColorInt viewColorAfterTransform: Int
    ): ImmersionBar {
        requireNotNull(view) { "View参数不能为空" }
        val map: MutableMap<Int?, Int?> = HashMap()
        map[viewColorBeforeTransform] = viewColorAfterTransform
        barParams!!.viewMap[view] = map as Map<Int, Int>
        return this
    }

    /**
     * view透明度
     * View alpha immersion bar.
     *
     * @param viewAlpha the view alpha
     * @return the immersion bar
     */
    fun viewAlpha(@FloatRange(from = 0.0,to = 1.0) viewAlpha: Float): ImmersionBar {
        barParams!!.viewAlpha = viewAlpha
        return this
    }

    /**
     * Remove support view immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    fun removeSupportView(view: View?): ImmersionBar {
        requireNotNull(view) { "View参数不能为空" }
        val map = barParams!!.viewMap[view]
        if (map != null && map.size != 0) {
            barParams!!.viewMap.remove(view)
        }
        return this
    }

    /**
     * Remove support all view immersion bar.
     *
     * @return the immersion bar
     */
    fun removeSupportAllView(): ImmersionBar {
        if (barParams!!.viewMap.size != 0) {
            barParams!!.viewMap.clear()
        }
        return this
    }

    /**
     * 有导航栏的情况下，Activity是否全屏显示
     *
     * @param isFullScreen the is full screen
     * @return the immersion bar
     */
    fun fullScreen(isFullScreen: Boolean): ImmersionBar {
        barParams!!.fullScreen = isFullScreen
        return this
    }

    /**
     * 状态栏透明度
     *
     * @param statusAlpha the status alpha
     * @return the immersion bar
     */
    fun statusBarAlpha(@FloatRange(from = 0.0,to = 1.0) statusAlpha: Float): ImmersionBar {
        barParams!!.statusBarAlpha = statusAlpha
        return this
    }

    /**
     * 导航栏透明度
     *
     * @param navigationAlpha the navigation alpha
     * @return the immersion bar
     */
    fun navigationBarAlpha(
        @FloatRange(from = 0.0,to = 1.0
        ) navigationAlpha: Float
    ): ImmersionBar {
        barParams!!.navigationBarAlpha = navigationAlpha
        return this
    }

    /**
     * 状态栏和导航栏透明度
     *
     * @param barAlpha the bar alpha
     * @return the immersion bar
     */
    fun barAlpha(@FloatRange(from = 0.0,to = 1.0) barAlpha: Float): ImmersionBar {
        barParams!!.statusBarAlpha = barAlpha
        barParams!!.navigationBarAlpha = barAlpha
        return this
    }
    /**
     * 是否启用自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     * Auto dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    /**
     * 是否启用 自动根据StatusBar和NavigationBar颜色调整深色模式与亮色模式
     *
     * @param isEnable true启用 默认false
     * @return the immersion bar
     */
    @JvmOverloads
    fun autoDarkModeEnable(
        isEnable: Boolean, @FloatRange(from = 0.0,to = 1.0
        ) autoDarkModeAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.autoStatusBarDarkModeEnable = isEnable
        barParams!!.autoStatusBarDarkModeAlpha = autoDarkModeAlpha
        barParams!!.autoNavigationBarDarkModeEnable = isEnable
        barParams!!.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }
    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto status bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    @JvmOverloads
    fun autoStatusBarDarkModeEnable(
        isEnable: Boolean, @FloatRange(from = 0.0,to = 1.0
        ) autoDarkModeAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.autoStatusBarDarkModeEnable = isEnable
        barParams!!.autoStatusBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }
    /**
     * 是否启用自动根据NavigationBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable          the is enable
     * @param autoDarkModeAlpha the auto dark mode alpha
     * @return the immersion bar
     */
    /**
     * 是否启用自动根据StatusBar颜色调整深色模式与亮色模式
     * Auto navigation bar dark mode enable immersion bar.
     *
     * @param isEnable the is enable
     * @return the immersion bar
     */
    @JvmOverloads
    fun autoNavigationBarDarkModeEnable(
        isEnable: Boolean, @FloatRange(from = 0.0,to = 1.0
        ) autoDarkModeAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.autoNavigationBarDarkModeEnable = isEnable
        barParams!!.autoNavigationBarDarkModeAlpha = autoDarkModeAlpha
        return this
    }
    /**
     * 状态栏字体深色或亮色，判断设备支不支持状态栏变色来设置状态栏透明度
     * Status bar dark font immersion bar.
     *
     * @param isDarkFont  the is dark font
     * @param statusAlpha the status alpha 如果不支持状态栏字体变色可以使用statusAlpha来指定状态栏透明度，比如白色状态栏的时候可以用到
     * @return the immersion bar
     */
    /**
     * 状态栏字体深色或亮色
     *
     * @param isDarkFont true 深色
     * @return the immersion bar
     */
    @JvmOverloads
    fun statusBarDarkFont(
        isDarkFont: Boolean, @FloatRange(from = 0.0,to = 1.0
        ) statusAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.statusBarDarkFont = isDarkFont
        if (isDarkFont && !isSupportStatusBarDarkFont) {
            barParams!!.statusBarAlpha = statusAlpha
        } else {
            barParams!!.flymeOSStatusBarFontColor = 0
            barParams!!.statusBarAlpha = 0f
        }
        return this
    }
    /**
     * 导航栏图标深色或亮色，只支持android o以上版本，判断设备支不支持导航栏图标变色来设置导航栏透明度
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon      the is dark icon
     * @param navigationAlpha the navigation alpha 如果不支持导航栏图标变色可以使用navigationAlpha来指定导航栏透明度，比如白色导航栏的时候可以用到
     * @return the immersion bar
     */
    /**
     * 导航栏图标深色或亮色，只支持android o以上版本
     * Navigation bar dark icon immersion bar.
     *
     * @param isDarkIcon the is dark icon
     * @return the immersion bar
     */
    @JvmOverloads
    fun navigationBarDarkIcon(
        isDarkIcon: Boolean, @FloatRange(from = 0.0,to = 1.0
        ) navigationAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.navigationBarDarkIcon = isDarkIcon
        if (isDarkIcon && !isSupportNavigationIconDark) {
            barParams!!.navigationBarAlpha = navigationAlpha
        } else {
            barParams!!.navigationBarAlpha = 0f
        }
        return this
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColor(@ColorRes flymeOSStatusBarFontColor: Int): ImmersionBar {
        barParams!!.flymeOSStatusBarFontColor =
            ContextCompat.getColor(activity!!, flymeOSStatusBarFontColor)
        return this
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColor(flymeOSStatusBarFontColor: String?): ImmersionBar {
        barParams!!.flymeOSStatusBarFontColor =
            Color.parseColor(flymeOSStatusBarFontColor)
        return this
    }

    /**
     * 修改 Flyme OS系统手机状态栏字体颜色，优先级高于statusBarDarkFont(boolean isDarkFont)方法
     * Flyme os status bar font color immersion bar.
     *
     * @param flymeOSStatusBarFontColor the flyme os status bar font color
     * @return the immersion bar
     */
    fun flymeOSStatusBarFontColorInt(@ColorInt flymeOSStatusBarFontColor: Int): ImmersionBar {
        barParams!!.flymeOSStatusBarFontColor = flymeOSStatusBarFontColor
        return this
    }

    /**
     * 隐藏导航栏或状态栏
     *
     * @param barHide the bar hide
     * @return the immersion bar
     */
    fun hideBar(barHide: BarHide?): ImmersionBar {
        barParams!!.barHide = barHide!!
        if (OSUtils.isEMUI3_x) {
            if (barParams!!.barHide == BarHide.FLAG_HIDE_NAVIGATION_BAR ||
                barParams!!.barHide == BarHide.FLAG_HIDE_BAR
            ) {
                barParams!!.hideNavigationBar = true
            } else {
                barParams!!.hideNavigationBar = false
            }
        }
        return this
    }

    /**
     * 解决布局与状态栏重叠问题
     *
     * @param fits the fits
     * @return the immersion bar
     */
    fun fitsSystemWindows(fits: Boolean): ImmersionBar {
        barParams!!.fits = fits
        if (barParams!!.fits) {
            if (mFitsStatusBarType == Constants.FLAG_FITS_DEFAULT) {
                mFitsStatusBarType =
                    Constants.FLAG_FITS_SYSTEM_WINDOWS
            }
        } else {
            mFitsStatusBarType =
                Constants.FLAG_FITS_DEFAULT
        }
        return this
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    fun fitsSystemWindows(fits: Boolean, @ColorRes contentColor: Int): ImmersionBar {
        return fitsSystemWindowsInt(fits, ContextCompat.getColor(activity!!, contentColor))
    }

    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color 整体界面背景色
     * @param contentColorTransform the content color transform  整体界面变换后的背景色
     * @param contentAlpha          the content alpha 整体界面透明度
     * @return the immersion bar
     */
    fun fitsSystemWindows(
        fits: Boolean, @ColorRes contentColor: Int
        , @ColorRes contentColorTransform: Int, @FloatRange(from = 0.0,to = 1.0
        ) contentAlpha: Float
    ): ImmersionBar {
        return fitsSystemWindowsInt(
            fits, ContextCompat.getColor(activity!!, contentColor),
            ContextCompat.getColor(activity!!, contentColorTransform), contentAlpha
        )
    }
    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows int immersion bar.
     *
     * @param fits                  the fits
     * @param contentColor          the content color 整体界面背景色
     * @param contentColorTransform the content color transform 整体界面变换后的背景色
     * @param contentAlpha          the content alpha 整体界面透明度
     * @return the immersion bar
     */
    /**
     * 解决布局与状态栏重叠问题，支持侧滑返回
     * Fits system windows int immersion bar.
     *
     * @param fits         the fits
     * @param contentColor the content color 整体界面背景色
     * @return the immersion bar
     */
    @JvmOverloads
    fun fitsSystemWindowsInt(
        fits: Boolean, @ColorInt contentColor: Int
        , @ColorInt contentColorTransform: Int = Color.BLACK, @FloatRange(from = 0.0,to = 1.0) contentAlpha: Float = 0f
    ): ImmersionBar {
        barParams!!.fits = fits
        barParams!!.contentColor = contentColor
        barParams!!.contentColorTransform = contentColorTransform
        barParams!!.contentAlpha = contentAlpha
        if (barParams!!.fits) {
            if (mFitsStatusBarType == Constants.FLAG_FITS_DEFAULT) {
                mFitsStatusBarType =
                    Constants.FLAG_FITS_SYSTEM_WINDOWS
            }
        } else {
            mFitsStatusBarType =
                Constants.FLAG_FITS_DEFAULT
        }
        mContentView!!.setBackgroundColor(
            ColorUtils.blendARGB(
                barParams!!.contentColor,
                barParams!!.contentColorTransform, barParams!!.contentAlpha
            )
        )
        return this
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     *
     * @param view the view
     * @return the immersion bar
     */
    fun statusBarView(view: View?): ImmersionBar {
        if (view == null) {
            return this
        }
        barParams!!.statusBarView = view
        if (mFitsStatusBarType == Constants.FLAG_FITS_DEFAULT) {
            mFitsStatusBarType =
                Constants.FLAG_FITS_STATUS
        }
        return this
    }

    /**
     * 通过状态栏高度动态设置状态栏布局,只能在Activity中使用
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    fun statusBarView(@IdRes viewId: Int): ImmersionBar {
        return statusBarView(activity!!.findViewById<View>(viewId))
    }

    /**
     * 通过状态栏高度动态设置状态栏布局
     * Status bar view immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    fun statusBarView(@IdRes viewId: Int, rootView: View): ImmersionBar {
        return statusBarView(rootView.findViewById<View>(viewId))
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view the view
     * @return the immersion bar
     */
    fun titleBar(view: View?): ImmersionBar {
        return if (view == null) {
            this
        } else titleBar(view, true)
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法
     * Title bar immersion bar.
     *
     * @param view                          the view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    fun titleBar(
        view: View?,
        statusBarColorTransformEnable: Boolean
    ): ImmersionBar {
        if (view == null) {
            return this
        }
        if (mFitsStatusBarType == Constants.FLAG_FITS_DEFAULT) {
            mFitsStatusBarType =
                Constants.FLAG_FITS_TITLE
        }
        barParams!!.titleBarView = view
        barParams!!.statusBarColorEnabled = statusBarColorTransformEnable
        return this
    }
    /**
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    /**
     * 解决状态栏与布局顶部重叠又多了种方法，只支持Activity
     * Title bar immersion bar.
     *
     * @param viewId the view id
     * @return the immersion bar
     */
    @JvmOverloads
    fun titleBar(@IdRes viewId: Int, statusBarColorTransformEnable: Boolean = true): ImmersionBar {
        return if (mSupportFragment != null && mSupportFragment!!.view != null) {
            titleBar(
                mSupportFragment!!.view!!.findViewById<View>(viewId),
                statusBarColorTransformEnable
            )
        } else if (mFragment != null && mFragment!!.view != null) {
            titleBar(
                mFragment!!.view!!.findViewById<View>(viewId),
                statusBarColorTransformEnable
            )
        } else {
            titleBar(
                activity!!.findViewById<View>(viewId),
                statusBarColorTransformEnable
            )
        }
    }

    /**
     * Title bar immersion bar.
     *
     * @param viewId   the view id
     * @param rootView the root view
     * @return the immersion bar
     */
    fun titleBar(@IdRes viewId: Int, rootView: View): ImmersionBar {
        return titleBar(rootView.findViewById<View>(viewId), true)
    }

    /**
     * 解决状态栏与布局顶部重叠又多了种方法，支持任何view
     * Title bar immersion bar.
     *
     * @param viewId                        the view id
     * @param rootView                      the root view
     * @param statusBarColorTransformEnable the status bar flag 默认为true false表示状态栏不支持变色，true表示状态栏支持变色
     * @return the immersion bar
     */
    fun titleBar(
        @IdRes viewId: Int, rootView: View,
        statusBarColorTransformEnable: Boolean
    ): ImmersionBar {
        return titleBar(
            rootView.findViewById<View>(viewId),
            statusBarColorTransformEnable
        )
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId the view id   标题栏资源id
     * @return the immersion bar
     */
    fun titleBarMarginTop(@IdRes viewId: Int): ImmersionBar {
        return if (mSupportFragment != null && mSupportFragment!!.view != null) {
            titleBarMarginTop(
                mSupportFragment!!.view!!.findViewById<View>(
                    viewId
                )
            )
        } else if (mFragment != null && mFragment!!.view != null) {
            titleBarMarginTop(mFragment!!.view!!.findViewById<View>(viewId))
        } else {
            titleBarMarginTop(activity!!.findViewById<View>(viewId))
        }
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param viewId   the view id  标题栏资源id
     * @param rootView the root view  布局view
     * @return the immersion bar
     */
    fun titleBarMarginTop(@IdRes viewId: Int, rootView: View): ImmersionBar {
        return titleBarMarginTop(rootView.findViewById<View>(viewId))
    }

    /**
     * 绘制标题栏距离顶部的高度为状态栏的高度
     * Title bar margin top immersion bar.
     *
     * @param view the view  要改变的标题栏view
     * @return the immersion bar
     */
    fun titleBarMarginTop(view: View?): ImmersionBar {
        if (view == null) {
            return this
        }
        if (mFitsStatusBarType == Constants.FLAG_FITS_DEFAULT) {
            mFitsStatusBarType =
                Constants.FLAG_FITS_TITLE_MARGIN_TOP
        }
        barParams!!.titleBarView = view
        return this
    }

    /**
     * 支持有actionBar的界面,调用该方法，布局讲从actionBar下面开始绘制
     * Support action bar immersion bar.
     *
     * @param isSupportActionBar the is support action bar
     * @return the immersion bar
     */
    fun supportActionBar(isSupportActionBar: Boolean): ImmersionBar {
        barParams!!.isSupportActionBar = isSupportActionBar
        return this
    }

    /**
     * Status bar color transform enable immersion bar.
     *
     * @param statusBarColorTransformEnable the status bar flag
     * @return the immersion bar
     */
    fun statusBarColorTransformEnable(statusBarColorTransformEnable: Boolean): ImmersionBar {
        barParams!!.statusBarColorEnabled = statusBarColorTransformEnable
        return this
    }

    /**
     * 一键重置所有参数
     * Reset immersion bar.
     *
     * @return the immersion bar
     */
    fun reset(): ImmersionBar {
        barParams = BarParams()
        mFitsStatusBarType =
            Constants.FLAG_FITS_DEFAULT
        return this
    }

    /**
     * 给某个页面设置tag来标识这页bar的属性.
     * Add tag bar tag.
     *
     * @param tag the tag
     * @return the bar tag
     */
    fun addTag(tag: String): ImmersionBar {
        require(!isEmpty(tag)) { "tag不能为空" }
        val barParams = barParams!!.clone()
        mTagMap[tag] = barParams
        return this
    }

    /**
     * 根据tag恢复到某次调用时的参数
     * Recover immersion bar.
     *
     * @param tag the tag
     * @return the immersion bar
     */
    fun getTag(tag: String?): ImmersionBar {
        require(!isEmpty(tag)) { "tag不能为空" }
        val barParams = mTagMap[tag]
        if (barParams != null) {
            this.barParams = barParams.clone()
        }
        return this
    }
    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     *
     * @param enable       the enable
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    /**
     * 解决软键盘与底部输入框冲突问题 ，默认是false
     * Keyboard enable immersion bar.
     *
     * @param enable the enable
     * @return the immersion bar
     */
    @JvmOverloads
    fun keyboardEnable(
        enable: Boolean,
        keyboardMode: Int = barParams!!.keyboardMode
    ): ImmersionBar {
        barParams!!.keyboardEnable = enable
        barParams!!.keyboardMode = keyboardMode
        mKeyboardTempEnable = enable
        return this
    }

    /**
     * 修改键盘模式
     * Keyboard mode immersion bar.
     *
     * @param keyboardMode the keyboard mode
     * @return the immersion bar
     */
    fun keyboardMode(keyboardMode: Int): ImmersionBar {
        barParams!!.keyboardMode = keyboardMode
        return this
    }

    /**
     * 软键盘弹出关闭的回调监听
     * Sets on keyboard listener.
     *
     * @param onKeyboardListener the on keyboard listener
     * @return the on keyboard listener
     */
    fun setOnKeyboardListener(onKeyboardListener: OnKeyboardListener?): ImmersionBar {
        if (barParams!!.onKeyboardListener == null) {
            barParams!!.onKeyboardListener = onKeyboardListener
        }
        return this
    }

    /**
     * 导航栏显示隐藏监听器
     * Sets on navigation bar listener.
     *
     * @param onNavigationBarListener the on navigation bar listener
     * @return the on navigation bar listener
     */
    fun setOnNavigationBarListener(onNavigationBarListener: OnNavigationBarListener?): ImmersionBar {
        if (onNavigationBarListener != null) {
            if (barParams!!.onNavigationBarListener == null) {
                barParams!!.onNavigationBarListener = onNavigationBarListener
                NavigationBarObserver.instance
                    .addOnNavigationBarListener(barParams!!.onNavigationBarListener)
            }
        } else {
            if (barParams!!.onNavigationBarListener != null) {
                NavigationBarObserver.instance
                    .removeOnNavigationBarListener(barParams!!.onNavigationBarListener)
                barParams!!.onNavigationBarListener = null
            }
        }
        return this
    }

    /**
     * Bar监听，第一次调用和横竖屏切换都会触发此方法，比如可以解决横竖屏切换，横屏情况下，刘海屏遮挡布局的问题
     * Sets on bar listener.
     *
     * @param onBarListener the on bar listener
     * @return the on bar listener
     */
    fun setOnBarListener(onBarListener: OnBarListener?): ImmersionBar {
        if (onBarListener != null) {
            if (barParams!!.onBarListener == null) {
                barParams!!.onBarListener = onBarListener
            }
        } else {
            if (barParams!!.onBarListener != null) {
                barParams!!.onBarListener = null
            }
        }
        return this
    }

    /**
     * 是否可以修改导航栏颜色，默认为true
     * 优先级 navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     * Navigation bar enable immersion bar.
     *
     * @param navigationBarEnable the enable
     * @return the immersion bar
     */
    fun navigationBarEnable(navigationBarEnable: Boolean): ImmersionBar {
        barParams!!.navigationBarEnable = navigationBarEnable
        return this
    }

    /**
     * 是否可以修改4.4设备导航栏颜色，默认为true
     * 优先级 navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     *
     * @param navigationBarWithKitkatEnable the navigation bar with kitkat enable
     * @return the immersion bar
     */
    fun navigationBarWithKitkatEnable(navigationBarWithKitkatEnable: Boolean): ImmersionBar {
        barParams!!.navigationBarWithKitkatEnable = navigationBarWithKitkatEnable
        if (OSUtils.isEMUI3_x) {
            if (barParams!!.navigationBarWithEMUI3Enable) {
                barParams!!.navigationBarWithKitkatEnable = true
            } else {
                if (barParams!!.navigationBarWithKitkatEnable) {
                    barParams!!.navigationBarWithKitkatEnable = false
                }
            }
        }
        return this
    }

    /**
     * 是否能修改华为emui3.1导航栏颜色，默认为true，
     * 优先级 navigationBarEnable > navigationBarWithEMUI3Enable > navigationBarWithKitkatEnable
     * Navigation bar with emui 3 enable immersion bar.
     *
     * @param navigationBarWithEMUI3Enable the navigation bar with emui 3 1 enable
     * @return the immersion bar
     */
    fun navigationBarWithEMUI3Enable(navigationBarWithEMUI3Enable: Boolean): ImmersionBar { //是否可以修改emui3系列手机导航栏
        if (OSUtils.isEMUI3_x) {
            barParams!!.navigationBarWithEMUI3Enable = navigationBarWithEMUI3Enable
            barParams!!.navigationBarWithKitkatEnable = navigationBarWithEMUI3Enable
        }
        return this
    }

    /**
     * 是否可以使用沉浸式，如果已经是true了，在改为false，之前沉浸式效果不会消失，之后设置的沉浸式效果也不会生效
     * Bar enable immersion bar.
     *
     * @param barEnable the bar enable
     * @return the immersion bar
     */
    fun barEnable(barEnable: Boolean): ImmersionBar {
        barParams!!.barEnable = barEnable
        return this
    }

    /**
     * 通过上面配置后初始化后方可成功调用
     */
    fun init() {
        if (barParams!!.barEnable) { //更新Bar的参数
            updateBarParams()
            //设置沉浸式
            setBar()
            //修正界面显示
            fitsWindows()
            //适配软键盘与底部输入框冲突问题
            fitsKeyboard()
            //变色view
            transformView()
            mInitialized = true
        }
    }

    /**
     * 内部方法无需调用
     */
    fun destroy() { //取消监听
        cancelListener()
        if (mIsDialog) {
            val immersionBar = with(activity!!)
            if (immersionBar != null) {
                immersionBar.barParams!!.keyboardEnable = immersionBar.mKeyboardTempEnable
                if (immersionBar.barParams!!.barHide != BarHide.FLAG_SHOW_BAR) {
                    immersionBar.setBar()
                }
            }
        }
        mInitialized = false
    }

    /**
     * 更新Bar的参数
     * Update bar params.
     */
    private fun updateBarParams() {
        adjustDarkModeParams()
        //获得Bar相关信息
        updateBarConfig()
        //如果在Fragment中使用，让Activity同步Fragment的BarParams参数
        if (isFragment) {
            val immersionBar = with(activity!!)
            if (immersionBar != null) {
                immersionBar.barParams = barParams
            }
        }
        //如果dialog里设置了keyboardEnable为true，则Activity中所设置的keyboardEnable为false
        if (mIsDialog) {
            val immersionBar = with(activity!!)
            if (immersionBar != null) {
                if (immersionBar.mKeyboardTempEnable) {
                    immersionBar.barParams!!.keyboardEnable = false
                }
            }
        }
    }

    /**
     * 初始化状态栏和导航栏
     */
    fun setBar() { //防止系统栏隐藏时内容区域大小发生变化
        var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x) { //适配刘海屏
            fitsNotchScreen()
            //初始化5.0以上，包含5.0
            uiFlags = initBarAboveLOLLIPOP(uiFlags)
            //android 6.0以上设置状态栏字体为暗色
            uiFlags = setStatusBarDarkFont(uiFlags)
            //android 8.0以上设置导航栏图标为暗色
            uiFlags = setNavigationIconDark(uiFlags)
        } else { //初始化5.0以下，4.4以上沉浸式
            initBarBelowLOLLIPOP()
        }
        //隐藏状态栏或者导航栏
        uiFlags = hideBar(uiFlags)
        mDecorView!!.systemUiVisibility = uiFlags
        if (OSUtils.isMIUI6Later) { //修改miui状态栏字体颜色
            setMIUIBarDark(
                mWindow,
                Constants.IMMERSION_MIUI_STATUS_BAR_DARK,
                barParams!!.statusBarDarkFont
            )
            //修改miui导航栏图标为黑色
            if (barParams!!.navigationBarEnable) {
                setMIUIBarDark(
                    mWindow,
                    Constants.IMMERSION_MIUI_NAVIGATION_BAR_DARK,
                    barParams!!.navigationBarDarkIcon
                )
            }
        }
        // 修改Flyme OS状态栏字体颜色
        if (OSUtils.isFlymeOS4Later) {
            if (barParams!!.flymeOSStatusBarFontColor != 0) {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(
                    activity,
                    barParams!!.flymeOSStatusBarFontColor
                )
            } else {
                FlymeOSStatusBarFontUtils.setStatusBarDarkIcon(
                    activity,
                    barParams!!.statusBarDarkFont
                )
            }
        }
        //导航栏显示隐藏监听，目前只支持带有导航栏的华为和小米手机
        if (barParams!!.onNavigationBarListener != null) {
            NavigationBarObserver.instance.register(activity!!.application)
        }
    }

    /**
     * 适配刘海屏
     * Fits notch screen.
     */
    private fun fitsNotchScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !initialized()) {
            val lp = mWindow!!.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            mWindow!!.attributes = lp
        }
    }

    /**
     * 初始化android 5.0以上状态栏和导航栏
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initBarAboveLOLLIPOP(uiFlags: Int): Int { //获得默认导航栏颜色
        var uiFlags = uiFlags
        if (!initialized()) {
            barParams!!.defaultNavigationBarColor = mWindow!!.navigationBarColor
        }
        //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态栏遮住。
        uiFlags = uiFlags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (barParams!!.fullScreen && barParams!!.navigationBarEnable) { //Activity全屏显示，但导航栏不会被隐藏覆盖，导航栏依然可见，Activity底部布局部分会被导航栏遮住。
            uiFlags = uiFlags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        mWindow!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //判断是否存在导航栏
        if (mBarConfig!!.hasNavigationBar()) {
            mWindow!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        //需要设置这个才能设置状态栏和导航栏颜色
        mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        if (barParams!!.statusBarColorEnabled) {
            mWindow!!.statusBarColor = ColorUtils.blendARGB(
                barParams!!.statusBarColor,
                barParams!!.statusBarColorTransform, barParams!!.statusBarAlpha
            )
        } else {
            mWindow!!.statusBarColor = ColorUtils.blendARGB(
                barParams!!.statusBarColor,
                Color.TRANSPARENT, barParams!!.statusBarAlpha
            )
        }
        //设置导航栏颜色
        if (barParams!!.navigationBarEnable) {
            mWindow!!.navigationBarColor = ColorUtils.blendARGB(
                barParams!!.navigationBarColor,
                barParams!!.navigationBarColorTransform, barParams!!.navigationBarAlpha
            )
        } else {
            mWindow!!.navigationBarColor = barParams!!.defaultNavigationBarColor
        }
        return uiFlags
    }

    /**
     * 初始化android 4.4和emui3.1状态栏和导航栏
     */
    private fun initBarBelowLOLLIPOP() { //透明状态栏
        mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //创建一个假的状态栏
        setupStatusBarView()
        //判断是否存在导航栏，是否禁止设置导航栏
        if (mBarConfig!!.hasNavigationBar() || OSUtils.isEMUI3_x) {
            if (barParams!!.navigationBarEnable && barParams!!.navigationBarWithKitkatEnable) { //透明导航栏，设置这个，如果有导航栏，底部布局会被导航栏遮住
                mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            } else {
                mWindow!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            if (mNavigationBarHeight == 0) {
                mNavigationBarHeight = BarUtils.getNavBarHeight()
            }
            if (mNavigationBarWidth == 0) {
                mNavigationBarWidth = mBarConfig!!.navigationBarWidth
            }
            //创建一个假的导航栏
            setupNavBarView()
        }
    }

    /**
     * 设置一个可以自定义颜色的状态栏
     */
    private fun setupStatusBarView() {
        var statusBarView =
            mDecorView!!.findViewById<View>(Constants.IMMERSION_ID_STATUS_BAR_VIEW)
        if (statusBarView == null) {
            statusBarView = View(activity)
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                BarUtils.getStatusBarHeight()
            )
            params.gravity = Gravity.TOP
            statusBarView.layoutParams = params
            statusBarView.visibility = View.VISIBLE
            statusBarView.id = Constants.IMMERSION_ID_STATUS_BAR_VIEW
            mDecorView!!.addView(statusBarView)
        }
        if (barParams!!.statusBarColorEnabled) {
            statusBarView.setBackgroundColor(
                ColorUtils.blendARGB(
                    barParams!!.statusBarColor,
                    barParams!!.statusBarColorTransform, barParams!!.statusBarAlpha
                )
            )
        } else {
            statusBarView.setBackgroundColor(
                ColorUtils.blendARGB(
                    barParams!!.statusBarColor,
                    Color.TRANSPARENT, barParams!!.statusBarAlpha
                )
            )
        }
    }

    /**
     * 设置一个可以自定义颜色的导航栏
     */
    private fun setupNavBarView() {
        var navigationBarView =
            mDecorView!!.findViewById<View>(Constants.IMMERSION_ID_NAVIGATION_BAR_VIEW)
        if (navigationBarView == null) {
            navigationBarView = View(activity)
            navigationBarView.id = Constants.IMMERSION_ID_NAVIGATION_BAR_VIEW
            mDecorView!!.addView(navigationBarView)
        }
        val params: FrameLayout.LayoutParams
        if (mBarConfig!!.isNavigationAtBottom) {
            params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                mBarConfig!!.navigationBarHeight
            )
            params.gravity = Gravity.BOTTOM
        } else {
            params = FrameLayout.LayoutParams(
                mBarConfig!!.navigationBarWidth,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            params.gravity = Gravity.END
        }
        navigationBarView.layoutParams = params
        navigationBarView.setBackgroundColor(
            ColorUtils.blendARGB(
                barParams!!.navigationBarColor,
                barParams!!.navigationBarColorTransform, barParams!!.navigationBarAlpha
            )
        )
        if (barParams!!.navigationBarEnable && barParams!!.navigationBarWithKitkatEnable && !barParams!!.hideNavigationBar) {
            navigationBarView.visibility = View.VISIBLE
        } else {
            navigationBarView.visibility = View.GONE
        }
    }

    /**
     * 调整深色亮色模式参数
     */
    private fun adjustDarkModeParams() {
        if (barParams!!.autoStatusBarDarkModeEnable && barParams!!.statusBarColor != Color.TRANSPARENT) {
            val statusBarDarkFont =
                barParams!!.statusBarColor > Constants.IMMERSION_BOUNDARY_COLOR
            statusBarDarkFont(statusBarDarkFont, barParams!!.autoStatusBarDarkModeAlpha)
        }
        if (barParams!!.autoNavigationBarDarkModeEnable && barParams!!.navigationBarColor != Color.TRANSPARENT) {
            val navigationBarDarkIcon =
                barParams!!.navigationBarColor > Constants.IMMERSION_BOUNDARY_COLOR
            navigationBarDarkIcon(navigationBarDarkIcon, barParams!!.autoNavigationBarDarkModeAlpha)
        }
    }

    /**
     * Hide bar.
     * 隐藏或显示状态栏和导航栏。
     *
     * @param uiFlags the ui flags
     * @return the int
     */
    private fun hideBar(uiFlags: Int): Int {
        var uiFlags = uiFlags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            when (barParams!!.barHide) {
                BarHide.FLAG_HIDE_BAR -> uiFlags =
                    uiFlags or (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.INVISIBLE)
                BarHide.FLAG_HIDE_STATUS_BAR -> uiFlags =
                    uiFlags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.INVISIBLE
                BarHide.FLAG_HIDE_NAVIGATION_BAR -> uiFlags =
                    uiFlags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View . SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        BarHide.FLAG_SHOW_BAR -> uiFlags =
                    uiFlags or View.SYSTEM_UI_FLAG_VISIBLE
                else -> {
                }
            }
        }
        return uiFlags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    /**
     * 修正界面显示
     */
    fun fitsWindows() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !OSUtils.isEMUI3_x) { //android 5.0以上解决状态栏和布局重叠问题
                fitsWindowsAboveLOLLIPOP()
            //适配状态栏与布局重叠问题
            fitsLayoutOverlap()
        }
    }

    /**
     * android 5.0以下解决状态栏和布局重叠问题
     */
    private fun fitsWindowsBelowLOLLIPOP() {
        if (barParams!!.isSupportActionBar) {
            isActionBarBelowLOLLIPOP = true
            mContentView!!.post(this)
        } else {
            isActionBarBelowLOLLIPOP = false
            postFitsWindowsBelowLOLLIPOP()
        }
    }

    override fun run() {
        postFitsWindowsBelowLOLLIPOP()
    }

    private fun postFitsWindowsBelowLOLLIPOP() {
        updateBarConfig()
        //解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
        fitsWindowsKITKAT()
        //解决华为emui3.1或者3.0导航栏手动隐藏的问题
        if (!isFragment && OSUtils.isEMUI3_x) {
            fitsWindowsEMUI()
        }
    }

    /**
     * android 5.0以上解决状态栏和布局重叠问题
     * Fits windows above lollipop.
     */
    private fun fitsWindowsAboveLOLLIPOP() {
        updateBarConfig()
        if (checkFitsSystemWindows(
                mDecorView!!.findViewById(
                    android.R.id.content
                )
            )
        ) {
            if (barParams!!.isSupportActionBar) {
                setPadding(0, mActionBarHeight, 0, 0)
            }
            return
        }
        var top = 0
        if (barParams!!.fits && mFitsStatusBarType == Constants.FLAG_FITS_SYSTEM_WINDOWS) {
            top = BarUtils.getStatusBarHeight()
        }
        if (barParams!!.isSupportActionBar) {
            top = BarUtils.getStatusBarHeight() + mActionBarHeight
        }
        setPadding(0, top, 0, 0)
    }

    /**
     * 解决android4.4有导航栏的情况下，activity底部被导航栏遮挡的问题和android 5.0以下解决状态栏和布局重叠问题
     * Fits windows below lollipop.
     */
    private fun fitsWindowsKITKAT() {
        if (checkFitsSystemWindows(
                mDecorView!!.findViewById(
                    android.R.id.content
                )
            )
        ) {
            if (barParams!!.isSupportActionBar) {
                setPadding(0, mActionBarHeight, 0, 0)
            }
            return
        }
        var top = 0
        var right = 0
        var bottom = 0
        if (barParams!!.fits && mFitsStatusBarType == Constants.FLAG_FITS_SYSTEM_WINDOWS) {
            top = BarUtils.getStatusBarHeight()
        }
        if (barParams!!.isSupportActionBar) {
            top = BarUtils.getStatusBarHeight() + mActionBarHeight
        }
        if (mBarConfig!!.hasNavigationBar() && barParams!!.navigationBarEnable && barParams!!.navigationBarWithKitkatEnable) {
            if (!barParams!!.fullScreen) {
                if (mBarConfig!!.isNavigationAtBottom) {
                    bottom = mBarConfig!!.navigationBarHeight
                } else {
                    right = mBarConfig!!.navigationBarWidth
                }
            }
            if (barParams!!.hideNavigationBar) {
                if (mBarConfig!!.isNavigationAtBottom) {
                    bottom = 0
                } else {
                    right = 0
                }
            } else {
                if (!mBarConfig!!.isNavigationAtBottom) {
                    right = mBarConfig!!.navigationBarWidth
                }
            }
        }
        setPadding(0, top, right, bottom)
    }

    /**
     * 注册emui3.x导航栏监听函数
     * Register emui 3 x.
     */
    private fun fitsWindowsEMUI() {
        val navigationBarView =
            mDecorView!!.findViewById<View>(Constants.IMMERSION_ID_NAVIGATION_BAR_VIEW)
        if (barParams!!.navigationBarEnable && barParams!!.navigationBarWithKitkatEnable) {
            if (navigationBarView != null) {
                EMUI3NavigationBarObserver.instance.addOnNavigationBarListener(this)
                EMUI3NavigationBarObserver.instance.register()
            }
        } else {
            EMUI3NavigationBarObserver.instance.removeOnNavigationBarListener(this)
            navigationBarView.gone()
        }
    }

    /**
     * 更新BarConfig
     */
    private fun updateBarConfig() {
        mBarConfig = BarConfig(activity)
        if (!initialized() || isActionBarBelowLOLLIPOP) {
            mActionBarHeight = BarUtils.getActionBarHeight()
        }
            mFitsKeyboard?.updateBarConfig(mBarConfig!!)
    }

    override fun onNavigationBarChange(show: Boolean) {
        val navigationBarView = mDecorView?.findViewById<View>(Constants.IMMERSION_ID_NAVIGATION_BAR_VIEW)
        if (navigationBarView != null) {
            mBarConfig = BarConfig(activity)
            var bottom = mContentView?.paddingBottom?:0
            var right = mContentView?.paddingRight?:0
            if (!show) { //导航键隐藏了
                navigationBarView.gone()
                bottom = 0
                right = 0
            } else { //导航键显示了
                navigationBarView.visible()
                if (checkFitsSystemWindows(mDecorView?.findViewById(android.R.id.content))
                ) {
                    bottom = 0
                    right = 0
                } else {
                    if (mNavigationBarHeight == 0) {
                        mNavigationBarHeight = mBarConfig!!.navigationBarHeight
                    }
                    if (mNavigationBarWidth == 0) {
                        mNavigationBarWidth = mBarConfig!!.navigationBarWidth
                    }
                    if (!barParams!!.hideNavigationBar) {
                        val params =
                            navigationBarView.layoutParams as FrameLayout.LayoutParams
                        if (mBarConfig!!.isNavigationAtBottom) {
                            params.gravity = Gravity.BOTTOM
                            params.height = mNavigationBarHeight
                            bottom = if (barParams?.fullScreen != true) mNavigationBarHeight else 0
                            right = 0
                        } else {
                            params.gravity = Gravity.END
                            params.width = mNavigationBarWidth
                            bottom = 0
                            right = if (!barParams!!.fullScreen) mNavigationBarWidth else 0
                        }
                        navigationBarView.layoutParams = params
                    }
                }
            }
            setPadding(0, mContentView!!.paddingTop, right, bottom)
        }
    }

    /**
     * Sets status bar dark font.
     * 设置状态栏字体颜色，android6.0以上
     */
    private fun setStatusBarDarkFont(uiFlags: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && barParams!!.statusBarDarkFont) {
            uiFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            uiFlags
        }
    }

    /**
     * 设置导航栏图标亮色与暗色
     * Sets dark navigation icon.
     */
    private fun setNavigationIconDark(uiFlags: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && barParams!!.navigationBarDarkIcon) {
            uiFlags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            uiFlags
        }
    }

    @SuppressLint("PrivateApi")
    private fun setMIUIBarDark(
        window: Window?,
        key: String?,
        dark: Boolean
    ) {
        if (window != null) {
            val clazz: Class<out Window> = window.javaClass
            try {
                val darkModeFlag: Int
                val layoutParams =
                    Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField(key!!)
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) { //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                } else { //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag)
                }
            } catch (ignored: Exception) {
            }
        }
    }

    /**
     * 适配状态栏与布局重叠问题
     * Fits layout overlap.
     */
    private fun fitsLayoutOverlap() {
        when (mFitsStatusBarType) {
            Constants.FLAG_FITS_TITLE ->  //通过设置paddingTop重新绘制标题栏高度
                setTitleBar(activity, barParams!!.titleBarView)
            Constants.FLAG_FITS_TITLE_MARGIN_TOP ->  //通过设置marginTop重新绘制标题栏高度
                setTitleBarMarginTop(activity, barParams!!.titleBarView)
            Constants.FLAG_FITS_STATUS ->  //通过状态栏高度动态设置状态栏布局
                setStatusBarView(activity, barParams!!.statusBarView)
            else -> {
            }
        }
    }

    /**
     * 变色view
     * Transform view.
     */
    private fun transformView() {
        if (barParams!!.viewMap.size != 0) {
            val entrySet: MutableSet<MutableMap.MutableEntry<View?, Map<Int, Int>>> =
                barParams!!.viewMap.entries
            for ((view, map) in entrySet) {
                var colorBefore = barParams!!.statusBarColor
                var colorAfter = barParams!!.statusBarColorTransform
                for ((key, value) in map!!) {
                    colorBefore = key
                    colorAfter = value
                }
                if (view != null) {
                    if (Math.abs(barParams!!.viewAlpha - 0.0f) == 0f) {
                        view.setBackgroundColor(
                            ColorUtils.blendARGB(
                                colorBefore,
                                colorAfter,
                                barParams!!.statusBarAlpha
                            )
                        )
                    } else {
                        view.setBackgroundColor(
                            ColorUtils.blendARGB(
                                colorBefore,
                                colorAfter,
                                barParams!!.viewAlpha
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * 取消注册emui3.x导航栏监听函数和软键盘监听
     * Cancel listener.
     */
    private fun cancelListener() {
        if (activity != null) {
            if (mFitsKeyboard != null) {
                mFitsKeyboard!!.cancel()
                mFitsKeyboard = null
            }
            EMUI3NavigationBarObserver.instance.removeOnNavigationBarListener(this)
            NavigationBarObserver.instance
                .removeOnNavigationBarListener(barParams!!.onNavigationBarListener)
        }
    }

    /**
     * 解决底部输入框与软键盘问题
     * Keyboard enable.
     */
    private fun fitsKeyboard() {
            if (!isFragment) {
                if (barParams!!.keyboardEnable) {
                    if (mFitsKeyboard == null) {
                        mWindow?.let {
                            mFitsKeyboard = FitsKeyboard(this, it)
                        }

                    }
                    mFitsKeyboard!!.enable(barParams!!.keyboardMode)
                } else {
                    if (mFitsKeyboard != null) {
                        mFitsKeyboard!!.disable()
                    }
                }
            } else {
                val immersionBar = with(activity!!)
                if (immersionBar != null) {
                    if (immersionBar.barParams!!.keyboardEnable) {
                        if (immersionBar.mFitsKeyboard == null) {
                            immersionBar.mWindow?.let {
                                immersionBar.mFitsKeyboard = FitsKeyboard(
                                    immersionBar,
                                    it
                                )
                            }

                        }
                        immersionBar.mFitsKeyboard!!.enable(immersionBar.barParams!!.keyboardMode)
                    } else {
                        if (immersionBar.mFitsKeyboard != null) {
                            immersionBar.mFitsKeyboard!!.disable()
                        }
                    }
                }
            }
    }

    /**
     * 是否已经调用过init()方法了
     */
    fun initialized(): Boolean {
        return mInitialized
    }

    private fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        if (mContentView != null) {
            mContentView!!.setPadding(left, top, right, bottom)
        }
        paddingLeft = left
        paddingTop = top
        paddingRight = right
        paddingBottom = bottom
    }

    companion object {
        /**
         * 在Activity使用
         * With immersion bar.
         *
         * @param activity the activity
         * @return the immersion bar
         */
        fun with(activity: Activity): ImmersionBar? {
            return retriever[activity]
        }

        /**
         * 在Fragment使用
         * With immersion bar.
         *
         * @param fragment the fragment
         * @return the immersion bar
         */
        fun with(fragment: Fragment): ImmersionBar? {
            return retriever[fragment]
        }

        /**
         * 在Fragment使用
         * With immersion bar.
         *
         * @param fragment the fragment
         * @return the immersion bar
         */
        fun with(fragment: android.app.Fragment): ImmersionBar? {
            return retriever[fragment]
        }

        /**
         * 在DialogFragment使用
         * With immersion bar.
         *
         * @param dialogFragment the dialog fragment
         * @return the immersion bar
         */
        fun with(dialogFragment: DialogFragment): ImmersionBar? {
            return retriever[dialogFragment]
        }

        /**
         * 在DialogFragment使用
         * With immersion bar.
         *
         * @param dialogFragment the dialog fragment
         * @return the immersion bar
         */
        fun with(dialogFragment: android.app.DialogFragment): ImmersionBar? {
            return retriever[dialogFragment]
        }

        /**
         * 在dialog里使用
         * With immersion bar.
         *
         * @param activity the activity
         * @param dialog   the dialog
         * @return the immersion bar
         */
        fun with(activity: Activity, dialog: Dialog): ImmersionBar? {
            return retriever[activity, dialog]
        }

        /**
         * 在Dialog里销毁，不包括DialogFragment
         *
         * @param activity the activity
         * @param dialog   the dialog
         */
        fun destroy(activity: Activity, dialog: Dialog) {
            retriever.destroy(activity, dialog)
        }

        /**
         * 判断手机支不支持状态栏字体变色
         * Is support status bar dark font boolean.
         *
         * @return the boolean
         */
        val isSupportStatusBarDarkFont: Boolean
            get() = (OSUtils.isMIUI6Later || OSUtils.isFlymeOS4Later
                    || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

        /**
         * 判断导航栏图标是否支持变色
         * Is support navigation icon dark boolean.
         *
         * @return the boolean
         */
        val isSupportNavigationIconDark: Boolean
            get() = OSUtils.isMIUI6Later || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

        /**
         * 单独设置标题栏的高度
         * Sets title bar.
         *
         * @param activity the activity
         * @param view     the view
         */
        @Synchronized
        fun setTitleBar(activity: Activity?, vararg view: View?) {
            if (activity == null) {
                return
            }
            for (v in view) {
                if (v == null) {
                    continue
                }
                val statusBarHeight = getStatusBarHeight(activity)
                var fitsHeight = v.getTag(R.id.mn_ib_fits_layout_overlap) as Int
                if (fitsHeight == null) {
                    fitsHeight = 0
                }
                if (fitsHeight != statusBarHeight) {
                    v.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight)
                    var layoutParams = v.layoutParams
                    if (layoutParams == null) {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                        layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT
                    ) {
                        val finalLayoutParams = layoutParams
                        val finalFitsHeight = fitsHeight
                        v.post(Runnable {
                            finalLayoutParams.height =
                                v.height + statusBarHeight - finalFitsHeight
                            v.setPadding(
                                v.paddingLeft,
                                v.paddingTop + statusBarHeight - finalFitsHeight,
                                v.paddingRight,
                                v.paddingBottom
                            )
                            v.layoutParams = finalLayoutParams
                        })
                    } else {
                        layoutParams.height += statusBarHeight - fitsHeight
                        v.setPadding(
                            v.paddingLeft, v.paddingTop + statusBarHeight - fitsHeight,
                            v.paddingRight, v.paddingBottom
                        )
                        v.layoutParams = layoutParams
                    }
                }
            }
        }

        fun setTitleBar(
            fragment: Fragment?,
            vararg view: View?
        ) {
            if (fragment == null) {
                return
            }
            setTitleBar(fragment.activity, *view)
        }

        fun setTitleBar(
            fragment: android.app.Fragment?,
            vararg view: View?
        ) {
            if (fragment == null) {
                return
            }
            setTitleBar(fragment.activity, *view)
        }

        /**
         * 设置标题栏MarginTop值为导航栏的高度
         * Sets title bar margin top.
         *
         * @param activity the activity
         * @param view     the view
         */
        fun setTitleBarMarginTop(
            activity: Activity?,
            vararg view: View?
        ) {
            if (activity == null) {
                return
            }
            for (v in view) {
                if (v == null) {
                    continue
                }
                val statusBarHeight = getStatusBarHeight(activity)
                var fitsHeight = v.getTag(R.id.mn_ib_fits_layout_overlap) as Int
                if (fitsHeight == null) {
                    fitsHeight = 0
                }
                if (fitsHeight != statusBarHeight) {
                    v.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight)
                    var lp = v.layoutParams
                    if (lp == null) {
                        lp = MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    val layoutParams = lp as MarginLayoutParams
                    layoutParams.setMargins(
                        layoutParams.leftMargin,
                        layoutParams.topMargin + statusBarHeight - fitsHeight,
                        layoutParams.rightMargin,
                        layoutParams.bottomMargin
                    )
                    v.layoutParams = layoutParams
                }
            }
        }

        fun setTitleBarMarginTop(
            fragment: Fragment?,
            vararg view: View?
        ) {
            if (fragment == null) {
                return
            }
            setTitleBarMarginTop(fragment.activity, *view)
        }

        fun setTitleBarMarginTop(
            fragment: android.app.Fragment?,
            vararg view: View?
        ) {
            if (fragment == null) {
                return
            }
            setTitleBarMarginTop(fragment.activity, *view)
        }

        /**
         * 单独在标题栏的位置增加view，高度为状态栏的高度
         * Sets status bar view.
         *
         * @param activity the activity
         * @param view     the view
         */
        fun setStatusBarView(activity: Activity?, view: View?) {
            if (activity == null) {
                return
            }
            if (view == null) {
                return
            }
            val statusBarHeight = getStatusBarHeight(activity)
            var fitsHeight = view.getTag(R.id.mn_ib_fits_layout_overlap) as Int
            if (fitsHeight == null) {
                fitsHeight = 0
            }
            if (fitsHeight != statusBarHeight) {
                view.setTag(R.id.mn_ib_fits_layout_overlap, statusBarHeight)
                var lp = view.layoutParams
                if (lp == null) {
                    lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
                }
                lp.height = statusBarHeight
                view.layoutParams = lp
            }
        }

        fun setStatusBarView(
            fragment: Fragment?,
            view: View?
        ) {
            if (fragment == null) {
                return
            }
            setStatusBarView(fragment.activity, view)
        }

        fun setStatusBarView(
            fragment: android.app.Fragment?,
            view: View?
        ) {
            if (fragment == null) {
                return
            }
            setStatusBarView(fragment.activity, view)
        }

        /**
         * 解决顶部与布局重叠问题
         * Sets fits system windows.
         *
         * @param activity the activity
         */
        fun setFitsSystemWindows(activity: Activity?) {
            if (activity == null) {
                return
            }
            val parent = activity.findViewById<ViewGroup>(android.R.id.content)
            var i = 0
            val count = parent.childCount
            while (i < count) {
                val childView = parent.getChildAt(i)
                if (childView is ViewGroup) {
                    if (childView is DrawerLayout) {
                        i++
                        continue
                    }
                    childView.setFitsSystemWindows(true)
                    childView.clipToPadding = true
                }
                i++
            }
        }

        fun setFitsSystemWindows(fragment: Fragment?) {
            if (fragment == null) {
                return
            }
            setFitsSystemWindows(fragment.activity)
        }

        fun setFitsSystemWindows(fragment: android.app.Fragment?) {
            if (fragment == null) {
                return
            }
            setFitsSystemWindows(fragment.activity)
        }

        /**
         * 检查布局根节点是否使用了android:fitsSystemWindows="true"属性
         * Check fits system windows boolean.
         *
         * @param view the view
         * @return the boolean
         */
        fun checkFitsSystemWindows(view: View?): Boolean {
            if (view == null) {
                return false
            }
            if (view.fitsSystemWindows) {
                return true
            }
            if (view is ViewGroup) {
                val viewGroup = view
                var i = 0
                val count = viewGroup.childCount
                while (i < count) {
                    val childView = viewGroup.getChildAt(i)
                    if (childView is DrawerLayout) {
                        if (checkFitsSystemWindows(childView)) {
                            return true
                        }
                    }
                    if (childView.fitsSystemWindows) {
                        return true
                    }
                    i++
                }
            }
            return false
        }

        /**
         * Has navigtion bar boolean.
         * 判断是否存在导航栏
         *
         * @param activity the activity
         * @return the boolean
         */
        @TargetApi(14)
        fun hasNavigationBar(activity: Activity): Boolean {
            val config = BarConfig(activity)
            return config.hasNavigationBar()
        }

        @TargetApi(14)
        fun hasNavigationBar(fragment: Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else hasNavigationBar(fragment.activity!!)
        }

        @TargetApi(14)
        fun hasNavigationBar(fragment: android.app.Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else hasNavigationBar(fragment.activity)
        }

        /**
         * Gets navigation bar height.
         * 获得导航栏的高度
         *
         * @param activity the activity
         * @return the navigation bar height
         */
        @TargetApi(14)
        fun getNavigationBarHeight(activity: Activity): Int {
            val config = BarConfig(activity)
            return config.navigationBarHeight
        }

        @TargetApi(14)
        fun getNavigationBarHeight(fragment: Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNavigationBarHeight(fragment.activity!!)
        }

        @TargetApi(14)
        fun getNavigationBarHeight(fragment: android.app.Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNavigationBarHeight(fragment.activity)
        }

        /**
         * Gets navigation bar width.
         * 获得导航栏的宽度
         *
         * @param activity the activity
         * @return the navigation bar width
         */
        @TargetApi(14)
        fun getNavigationBarWidth(activity: Activity): Int {
            val config = BarConfig(activity)
            return config.navigationBarWidth
        }

        @TargetApi(14)
        fun getNavigationBarWidth(fragment: Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNavigationBarWidth(fragment.activity!!)
        }

        @TargetApi(14)
        fun getNavigationBarWidth(fragment: android.app.Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNavigationBarWidth(fragment.activity)
        }

        /**
         * Is navigation at bottom boolean.
         * 判断导航栏是否在底部
         *
         * @param activity the activity
         * @return the boolean
         */
        @TargetApi(14)
        fun isNavigationAtBottom(activity: Activity): Boolean {
            val config = BarConfig(activity)
            return config.isNavigationAtBottom
        }

        @TargetApi(14)
        fun isNavigationAtBottom(fragment: Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else isNavigationAtBottom(fragment.activity!!)
        }

        @TargetApi(14)
        fun isNavigationAtBottom(fragment: android.app.Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else isNavigationAtBottom(fragment.activity)
        }

        /**
         * Gets status bar height.
         * 或得状态栏的高度
         *
         * @param activity the activity
         * @return the status bar height
         */
        @TargetApi(14)
        fun getStatusBarHeight(activity: Activity): Int {
            val config = BarConfig(activity)
            return config.statusBarHeight
        }

        @TargetApi(14)
        fun getStatusBarHeight(fragment: Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getStatusBarHeight(fragment.activity!!)
        }

        @TargetApi(14)
        fun getStatusBarHeight(fragment: android.app.Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getStatusBarHeight(fragment.activity)
        }

        /**
         * Gets action bar height.
         * 或得ActionBar得高度
         *
         * @param activity the activity
         * @return the action bar height
         */
        @TargetApi(14)
        fun getActionBarHeight(activity: Activity): Int {
            val config = BarConfig(activity)
            return config.actionBarHeight
        }

        @TargetApi(14)
        fun getActionBarHeight(fragment: Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getActionBarHeight(fragment.activity!!)
        }

        @TargetApi(14)
        fun getActionBarHeight(fragment: android.app.Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getActionBarHeight(fragment.activity)
        }

        /**
         * 是否是刘海屏
         * Has notch screen boolean.
         * e.g:getWindow().getDecorView().post(() -> ImmersionBar.hasNotchScreen(this));
         *
         * @param activity the activity
         * @return the boolean
         */
        fun hasNotchScreen(activity: Activity): Boolean {
            return NotchUtils.hasNotchScreen(activity)
        }

        fun hasNotchScreen(fragment: Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else hasNotchScreen(fragment.activity!!)
        }

        fun hasNotchScreen(fragment: android.app.Fragment): Boolean {
            return if (fragment.activity == null) {
                false
            } else hasNotchScreen(fragment.activity)
        }

        /**
         * 是否是刘海屏
         * Has notch screen boolean.
         *
         * @param view the view
         * @return the boolean
         */
        fun hasNotchScreen(view: View): Boolean {
            return NotchUtils.hasNotchScreen(view)
        }

        /**
         * 刘海屏高度
         * Notch height int.
         * e.g: getWindow().getDecorView().post(() -> ImmersionBar.getNotchHeight(this));
         *
         * @param activity the activity
         * @return the int
         */
        fun getNotchHeight(activity: Activity): Int {
            return if (hasNotchScreen(activity)) {
                NotchUtils.getNotchHeight(activity)
            } else {
                0
            }
        }

        fun getNotchHeight(fragment: Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNotchHeight(fragment.activity!!)
        }

        fun getNotchHeight(fragment: android.app.Fragment): Int {
            return if (fragment.activity == null) {
                0
            } else getNotchHeight(fragment.activity)
        }

        /**
         * 隐藏状态栏
         * Hide status bar.
         *
         * @param window the window
         */
        fun hideStatusBar(window: Window) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        /**
         * 显示状态栏
         * Show status bar.
         *
         * @param window the window
         */
        fun showStatusBar(window: Window) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        private var retriever: RequestManagerRetriever= RequestManagerRetriever.instance

        private fun isEmpty(str: String?): Boolean {
            return str == null || str.trim { it <= ' ' }.isEmpty()
        }
    }
}