package com.yzy.example.widget.imagewatcher.immersionbar

/**
 * Bar相关信息
 *
 * @author geyifeng
 * @date 2019-05-10 18:43
 */
class BarProperties {
    /**
     * 是否是竖屏
     */
    var isPortrait = false
    /**
     * 是否是左横屏
     */
    var isLandscapeLeft = false
    /**
     * 是否是右横屏
     */
    var isLandscapeRight = false
    /**
     * 是否是刘海屏
     */
    var isNotchScreen = false
    /**
     * 是否有导航栏
     */
    private var hasNavigationBar = false
    /**
     * 状态栏高度，刘海屏横竖屏有可能状态栏高度不一样
     */
    var statusBarHeight = 0
    /**
     * 导航栏高度
     */
    var navigationBarHeight = 0
    /**
     * 导航栏宽度
     */
    var navigationBarWidth = 0
    /**
     * 刘海屏高度
     */
    var notchHeight = 0
    /**
     * ActionBar高度
     */
    var actionBarHeight = 0

    fun hasNavigationBar(): Boolean {
        return hasNavigationBar
    }

    fun setNavigationBar(hasNavigationBar: Boolean) {
        this.hasNavigationBar = hasNavigationBar
    }

}