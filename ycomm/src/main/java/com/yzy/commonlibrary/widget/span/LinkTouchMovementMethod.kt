package com.yzy.commonlibrary.widget.span

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * 用于点击ClickableSpan时多个按钮分别变色
 * Author:yzy
 * Date:2019/7/26
 * Time:14:09
 */
class LinkTouchMovementMethod constructor(var mPressedSpan: TouchableSpan?) : LinkMovementMethod() {
    override fun onTouchEvent(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mPressedSpan = getPressedSpan(textView, spannable, event)
            if (mPressedSpan != null) {
                mPressedSpan?.mIsPressed = true
                Selection.setSelection(
                    spannable, spannable.getSpanStart(mPressedSpan),
                    spannable.getSpanEnd(mPressedSpan)
                )
            }
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            val touchedSpan = getPressedSpan(textView, spannable, event)
            if (mPressedSpan != null && touchedSpan !== mPressedSpan) {
                mPressedSpan?.mIsPressed = false;
                mPressedSpan = null
                Selection.removeSelection(spannable)
            }
        } else {
            if (mPressedSpan != null) {
                mPressedSpan?.mIsPressed = false;
                super.onTouchEvent(textView, spannable, event)
            }
            mPressedSpan = null
            Selection.removeSelection(spannable)
        }
        return true
    }

    private fun getPressedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): TouchableSpan? {

        var x = event.x.toInt()
        var y = event.y.toInt()

        x -= textView.totalPaddingLeft
        y -= textView.totalPaddingTop

        x += textView.scrollX
        y += textView.scrollY

        val layout = textView.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val link = spannable.getSpans(off, off, TouchableSpan::class.java)
        var touchedSpan: TouchableSpan? = null
        if (link.isNotEmpty()) {
            touchedSpan = link[0]
        }
        return touchedSpan
    }
}
