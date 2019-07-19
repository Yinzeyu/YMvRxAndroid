package com.yzy.baselibrary.extention

import android.view.ViewGroup
import android.widget.TextView

/**
 *description: TextView的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

/**
 * 如果内容为null或空就gone掉TextView否则显示内容
 */
fun TextView.showTextNotNull(text: CharSequence?) {
    if (text.isNullOrBlank()) {
        gone()
    } else {
        visible()
        setText(text)
    }
}

fun TextView.topMargin(dp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        // marginlayoutParams 中，设置topmargin 需要赋值bottommargin才有效果
        params.setMargins(
            params.leftMargin,
            params.topMargin,
            params.rightMargin,
            context.dp2px(dp)
        )
    }
    layoutParams = params
}

fun TextView.leftMargin(dp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginStart = context.dp2px(dp)
    }
    layoutParams = params
}

fun TextView.rightMargin(dp: Int) {
    val params = layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginEnd = context.dp2px(dp)
    }
    layoutParams = params
}

fun TextView.Width(dp: Int) {
    val params = layoutParams
    params.width = context.dp2px(dp)
    layoutParams = params
}