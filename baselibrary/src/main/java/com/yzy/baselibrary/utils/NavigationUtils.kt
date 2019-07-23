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