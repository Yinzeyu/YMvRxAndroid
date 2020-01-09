package com.yzy.example.widget.imagewatcher.immersionbar

import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import com.blankj.utilcode.util.BarUtils
import kotlin.math.min

/**
 * The type Bar config.
 *
 * @author geyifeng
 * @date 2017 /5/11
 */
internal class BarConfig(activity: Activity?) {

    val statusBarHeight: Int=BarUtils.getStatusBarHeight()

    val actionBarHeight: Int=BarUtils.getActionBarHeight()
    private val mHasNavigationBar: Boolean

    val navigationBarHeight: Int=BarUtils.getNavBarHeight()

    val navigationBarWidth: Int=0
    private var mInPortrait: Boolean=true
    private var mSmallestWidthDp: Float=0F

    @SuppressLint("NewApi")
    private fun getSmallestWidthDp(activity: Activity?): Float {
        val metrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getRealMetrics(metrics)
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        return min(widthDp, heightDp)
    }

    val isNavigationAtBottom: Boolean = mSmallestWidthDp >= 600 || mInPortrait

    fun hasNavigationBar(): Boolean {
        return mHasNavigationBar
    }
    init {
        mSmallestWidthDp = getSmallestWidthDp(activity)
        mHasNavigationBar = navigationBarHeight > 0

    }
}