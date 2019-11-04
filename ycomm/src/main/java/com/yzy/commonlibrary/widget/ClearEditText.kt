package com.yzy.commonlibrary.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.appcompat.widget.AppCompatEditText

import com.yzy.commonlibrary.R


/**
 * Author liqikun
 * Date on 2015/11/9.
 * 带清除按钮的输入框
 *
 * @author yin97
 */
open class ClearEditText constructor(context: Context, attrs: AttributeSet, defStyle: Int) : AppCompatEditText(
        context,
        attrs,
        defStyle
), TextWatcher {

    private var mClearDrawable: Drawable? = null
    private var mOnTextClearListener: onTextClearListener? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // 获取drawableRight
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            // 如果为空，即没有设置drawableRight，则使用R.mipmap.close这张图片
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)

            mClearDrawable = typedArray.getDrawable(R.styleable.ClearEditText_rightDrawable)
            typedArray.recycle()
        }
        mClearDrawable?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }

        addTextChangedListener(this)
        // 默认隐藏图标
        setDrawableVisible(false)
    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟clear点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                // 起始位置
                val start = width - totalPaddingRight + paddingRight
                // 结束位置
                val end = width
                val available = event.x > start && event.x < end
                if (available) {
                    this.setText("")
                    if (mOnTextClearListener != null) {
                        mOnTextClearListener!!.onClearClick()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        setDrawableVisible(s.length > 0)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    protected fun setDrawableVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
                compoundDrawables[0], compoundDrawables[1], right,
                compoundDrawables[3]
        )
    }

    interface onTextClearListener {
        fun onClearClick()
    }
}
