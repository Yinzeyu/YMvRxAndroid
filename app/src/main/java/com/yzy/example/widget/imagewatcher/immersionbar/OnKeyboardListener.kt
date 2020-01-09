package com.yzy.example.widget.imagewatcher.immersionbar

/**
 * 软键盘监听
 *
 * @author geyifeng
 * @date 2017/8/28
 */
interface OnKeyboardListener {
    /**
     * On keyboard change.
     *
     * @param isPopup        the is popup  是否弹出
     * @param keyboardHeight the keyboard height  软键盘高度
     */
    fun onKeyboardChange(isPopup: Boolean, keyboardHeight: Int)
}