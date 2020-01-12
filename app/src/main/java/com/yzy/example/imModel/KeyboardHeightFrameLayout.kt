package com.yzy.example.imModel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class KeyboardHeightFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : FrameLayout(context, attributeSet, defAttrStyle) {

    fun show(height: Int) {
        val layoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
        visibility = View.VISIBLE
    }

     fun hide() {
        visibility = View.GONE
    }

     fun isShowing(): Boolean {
        return visibility == View.VISIBLE
    }
}