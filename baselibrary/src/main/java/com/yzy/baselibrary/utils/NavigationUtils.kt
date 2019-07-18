package com.yzy.baselibrary.utils

import android.app.Activity
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.blankj.utilcode.util.BarUtils

/**
 * description :如果遇到全屏的页面，可能不准，需要自己处理
 *
 * @author : case
 * @date : 2019/3/7 15:07
 */
class NavigationUtils private constructor() {
    companion object {
        /**获取虚拟导航键高度*/
        fun getNavigationBarHeight(activity: Activity): Int {
            return if (navigationBarIsShow(activity)) {
                activity.resources.getDimensionPixelSize(
                    activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                )
            } else {
                0
            }
        }

        /**判断虚拟导航键是否显示*/
        fun navigationBarIsShow(activity: Activity): Boolean {
            //直接判断布局
            val decorView = activity.window.decorView
            if (decorView is ViewGroup && decorView.childCount > 0) {
                val firstView = decorView.getChildAt(0)
                if (firstView.height > 0) {
                    return decorView.height == firstView.height + BarUtils.getNavBarHeight()
                }
            }
            //正常判断
            val metrics = DisplayMetrics()
            val realMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            activity.windowManager.defaultDisplay.getRealMetrics(realMetrics)
            return realMetrics.widthPixels - metrics.widthPixels > 0 || realMetrics.heightPixels - metrics.heightPixels > 0
        }

        /**因此虚拟导航键*/
        fun hideNavigation(window: Window) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
            window.navigationBarColor = Color.BLACK
        }
    }
}