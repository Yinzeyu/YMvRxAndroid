package com.yzy.example.widget.imagewatcher.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Created by yzy on
 * 解决多点触摸和ViewPager焦点冲突
 * 报出下面的错误：java.lang.IllegalArgumentException: pointerIndex out of range
 */
class MNViewPager constructor(context: Context, attrs: AttributeSet?) : ViewPager(context,attrs) {


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }
}