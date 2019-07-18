package com.yzy.baselibrary.base

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.yzy.baselibrary.extention.dp2px


/**
 *description: 可以设置Item的Size,Margin,背景的Model基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseStyleModel<T : View> : EpoxyModel<T>() {
    @EpoxyAttribute
    var topMarginDp: Int? = null
    @EpoxyAttribute
    var leftMarginDp: Int? = null
    @EpoxyAttribute
    var rightMarginDp: Int? = null
    @EpoxyAttribute
    var bottomMarginDp: Int? = null

    @EpoxyAttribute
    var heightDp: Int? = null
    @EpoxyAttribute
    var widthDp: Int? = null

    @EpoxyAttribute
    var bgColor: Int? = null
    @EpoxyAttribute
    var bgDrawable: Drawable? = null

    override fun bind(view: T) {
        super.bind(view)
        setSize(view)
        setBgColor(view)
        setBgDrawable(view)
        setMargin(view)
    }

    private fun setSize(view: T) {
        if (heightDp == null && widthDp == null) {
            return
        }
        val layoutParams = view.layoutParams
        heightDp?.let {
            layoutParams.height = view.context.dp2px(it)
        }
        widthDp?.let {
            layoutParams.width = view.context.dp2px(it)
        }
        view.layoutParams = layoutParams
    }

    private fun setBgColor(view: T) {
        bgColor?.let {
            view.setBackgroundColor(it)
        }
    }

    private fun setBgDrawable(view: T) {
        bgDrawable?.let {
            view.background = it
        }
    }

    private fun setMargin(view: T) {
        if (leftMarginDp == null
            && topMarginDp == null
            && rightMarginDp == null
            && bottomMarginDp == null
        ) {
            return
        }
        val left = if (leftMarginDp == null) {
            0
        } else {
            view.context.dp2px(leftMarginDp!!)
        }
        val top = if (topMarginDp == null) {
            0
        } else {
            view.context.dp2px(topMarginDp!!)
        }
        val right = if (rightMarginDp == null) {
            0
        } else {
            view.context.dp2px(rightMarginDp!!)
        }
        val bottom = if (bottomMarginDp == null) {
            0
        } else {
            view.context.dp2px(bottomMarginDp!!)
        }
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val marginLayoutParams = (view.layoutParams as ViewGroup.MarginLayoutParams)
            marginLayoutParams.setMargins(
                left,
                top,
                right,
                bottom
            )
            view.layoutParams = marginLayoutParams
        }
    }
}