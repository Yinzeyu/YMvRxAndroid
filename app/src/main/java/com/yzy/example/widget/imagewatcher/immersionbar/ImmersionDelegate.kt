package com.yzy.example.widget.imagewatcher.immersionbar

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.view.Surface
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.yzy.example.widget.imagewatcher.immersionbar.ImmersionBar

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
internal class ImmersionDelegate : Runnable {
    private var mImmersionBar: ImmersionBar? = null
    private var mStatusBarHeight = BarUtils.getStatusBarHeight()
    private var mBarProperties: BarProperties? = null
    private var mOnBarListener: OnBarListener? = null
    private var mNotchHeight = 0

    constructor(o: Any?) {
        if (o is Activity) {
            if (mImmersionBar == null) {
                mImmersionBar = ImmersionBar(o)
            }
        } else if (o is Fragment) {
            if (mImmersionBar == null) {
                mImmersionBar = if (o is DialogFragment) {
                    ImmersionBar(o)
                } else {
                    ImmersionBar(o)
                }
            }
        } else if (o is android.app.Fragment) {
            if (mImmersionBar == null) {
                mImmersionBar = if (o is android.app.DialogFragment) {
                    ImmersionBar(o)
                } else {
                    ImmersionBar(o)
                }
            }
        }
    }

    constructor(activity: Activity, dialog: Dialog?) {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar(activity, dialog)
            mStatusBarHeight = ImmersionBar.Companion.getStatusBarHeight(activity)
        }
    }

    fun get(): ImmersionBar? {
        return mImmersionBar
    }

    fun onActivityCreated(configuration: Configuration) {
        barChanged(configuration)
    }

    fun onResume() {
        mImmersionBar?.let {
            if (!it.isFragment && it.initialized()) {
                if (OSUtils.isEMUI3_x && (it.barParams?.navigationBarWithEMUI3Enable?:false)) {
                    reinitialize()
                } else {
                       it.setBar()
                }
            }
        }

    }

    fun onDestroy() {
        mBarProperties = null
        if (mImmersionBar != null) {
            mImmersionBar!!.destroy()
            mImmersionBar = null
        }
    }

    fun onConfigurationChanged(newConfig: Configuration) {
        if (mImmersionBar != null) {
            if (OSUtils.isEMUI3_x ) {
                mImmersionBar?.let {
                    if (it.initialized() && !mImmersionBar!!.isFragment && (it.barParams?.navigationBarWithEMUI3Enable?:false)) {
                        reinitialize()
                    } else {
                        fitsWindows()
                    }
                }

            } else {
                fitsWindows()
            }
            barChanged(newConfig)
        }
    }

    /**
     * 重新初始化，适配一些特殊机型
     */
    private fun reinitialize() {
        if (mImmersionBar != null) {
            mImmersionBar!!.init()
        }
    }

    /**
     * 状态栏高度改变的时候重新适配布局重叠问题
     */
    private fun fitsWindows() {
            mImmersionBar?.fitsWindows()
            mStatusBarHeight = BarUtils.getStatusBarHeight()
    }

    /**
     * 横竖屏切换监听
     * Orientation change.
     *
     * @param configuration the configuration
     */
    private fun barChanged(configuration: Configuration) {
        mImmersionBar?.let {
            if (it.initialized() ) {
                mOnBarListener = it.barParams?.onBarListener
                if (mOnBarListener != null) {
                    val activity = it.activity
                    if (mBarProperties == null) {
                        mBarProperties = BarProperties()
                    }
                    mBarProperties?.isPortrait=configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                    val rotation = activity.windowManager.defaultDisplay.rotation
                    if (rotation == Surface.ROTATION_90) {
                        mBarProperties?.isLandscapeLeft=true
                        mBarProperties?.isLandscapeRight=false
                    } else if (rotation == Surface.ROTATION_270) {
                        mBarProperties?.isLandscapeLeft=false
                        mBarProperties?.isLandscapeRight=true
                    } else {
                        mBarProperties?.isLandscapeLeft=false
                        mBarProperties?.isLandscapeRight=false
                    }
                    activity.window.decorView.post(this)
                }
            }
        }

    }

    override fun run() {
        mImmersionBar?.let {
            val activity = it.activity
            val barConfig = BarConfig(activity)
            mBarProperties?.apply {
                statusBarHeight=barConfig.statusBarHeight
                setNavigationBar(barConfig.hasNavigationBar())
                actionBarHeight=barConfig.actionBarHeight
                val screen = NotchUtils.hasNotchScreen(activity)
                isNotchScreen=screen
                if (screen && mNotchHeight == 0) {
                    mNotchHeight = NotchUtils.getNotchHeight(activity)
                    notchHeight=mNotchHeight
                }
            }
            mOnBarListener?.onBarChange(mBarProperties)
        }

    }
}