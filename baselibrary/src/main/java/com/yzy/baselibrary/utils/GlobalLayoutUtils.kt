package com.yzy.baselibrary.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout

/**
 * @author: liuzhenfeng
 * @function: 解决全屏模式下，输入框高度不能自适应，兼容导航栏变化、屏幕旋转
 * @date: 2019-06-19  09:12
 * For more information, see https://code.google.com/p/android/issues/detail?id=5497
 * To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
 */
class GlobalLayoutUtils(activity: Activity, private var isImmersed: Boolean = true) {

    // 当前界面根布局，就是我们设置的 setContentView()
    private var mChildOfContent: View
    private var frameLayoutParams: FrameLayout.LayoutParams
    // 变化前的试图高度
    private var usableHeightPrevious = 0

    init {
        val content: FrameLayout = activity.findViewById(android.R.id.content)
        mChildOfContent = content.getChildAt(0)

        // 添加布局变化监听
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(activity)
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    private fun possiblyResizeChildOfContent(activity: Activity) {

        // 当前可视区域的高度
        val usableHeightNow = computeUsableHeight()
        // 当前高度值和之前的进行对比，变化将进行重绘
        if (usableHeightNow != usableHeightPrevious) {
            // 获取当前屏幕高度
            // Ps：并不是真正的屏幕高度，是当前app的窗口高度，分屏时的高度为分屏窗口高度
            var usableHeightSansKeyboard = mChildOfContent.rootView.height
            // 高度差值：屏幕高度 - 可视内容高度
            var heightDifference = usableHeightSansKeyboard - usableHeightNow
            // 差值为负，说明获取屏幕高度时出错，宽高状态值反了，重新计算
            if (heightDifference < 0) {
                usableHeightSansKeyboard = mChildOfContent.rootView.width
                heightDifference = usableHeightSansKeyboard - usableHeightNow
            }

            when {
                // keyboard probably just became visible
                // 如果差值大于屏幕高度的 1/4，则认为输入软键盘为弹出状态
                heightDifference > usableHeightSansKeyboard / 4 ->
                    // 设置布局高度为：屏幕高度 - 高度差
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference

                // keyboard probably just became hidden
                // 虚拟导航栏显示
                heightDifference >= getNavigationBarHeight(activity) ->
                    frameLayoutParams.height = usableHeightSansKeyboard - getNavigationBarHeight(activity)

                // 其他情况直接设置为可视高度即可
                else -> frameLayoutParams.height = usableHeightNow
            }
        }

        // 刷新布局，会重新测量、绘制
        mChildOfContent.requestLayout()
        // 保存高度信息
        usableHeightPrevious = usableHeightNow

    }

    /**
     * 获取可视内容区域的高度
     */
    private fun computeUsableHeight(): Int {
        val r = Rect()
        // 当前窗口可视区域，不包括通知栏、导航栏、输入键盘区域
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return if (isImmersed) {
            // 沉浸模式下，底部坐标就是内容有效高度
            r.bottom
        } else {
            // 非沉浸模式下，去掉通知栏的高度 r.top（可用于通知栏高度的计算）
            r.bottom - r.top
        }
    }

    // 获取导航栏真实的高度（可能未显示）
    private fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resources = context.resources
        val resourceId =
                resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}