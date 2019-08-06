package com.yzy.baselibrary.utils.span

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * 为多个TouchableSpan分别添加点击变色
 * Author:yzy
 * Date: 2019/7/26
 * Time:14:06
 */
abstract class TouchableSpan constructor(
    var mNormalTextColor: Int,
    var mPressedTextColor: Int,
    var mPressedBackgroundColor: Int,
    var mIsShowUnderLine: Boolean = false
) : ClickableSpan() {
    var mIsPressed: Boolean = false
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = if (mIsPressed) mPressedTextColor else mNormalTextColor
        ds.bgColor = if (mIsPressed) mPressedBackgroundColor else Color.TRANSPARENT
        ds.isUnderlineText = mIsShowUnderLine
    }

}