package com.yzy.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import androidx.appcompat.widget.AppCompatTextView

import com.yzy.baselibrary.utils.span.LinkTouchMovementMethod


/**
 * description: 解决有SpanClick点击事件和TextView点击事件同时出发的TextView.
 *
 * @date 2019/7/26 11:54.
 * @author: yzy.
 */
class SpanClickTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), View.OnClickListener {
    private var preventClick: Boolean = false
    private var clickListener: View.OnClickListener? = null

    init {
        highlightColor = Color.TRANSPARENT
        movementMethod = LinkTouchMovementMethod(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (movementMethod != null) {
            movementMethod.onTouchEvent(this, text as Spannable, event)
        }
        return super.onTouchEvent(event)
    }

    /**
     * ClickSpan点击事件出发后调用
     */
    fun preventNextClick() {
        preventClick = true
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        this.clickListener = listener
        super.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (preventClick) {
            preventClick = false
        } else if (clickListener != null) {
            clickListener!!.onClick(v)
        }
    }
}
