package com.yzy.example.imModel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class KeyboardHeightFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : FrameLayout(context, attributeSet, defAttrStyle), InputAwareLayout.InputView {

    override fun show(height: Int, immediate: Boolean) {
        val layoutParams = layoutParams
        layoutParams.height = height
        getChildAt(0).visibility = View.VISIBLE
        visibility = View.VISIBLE
    }

    override fun hide(immediate: Boolean) {
        visibility = View.GONE
    }

    override fun isShowing(): Boolean {
        return visibility == View.VISIBLE
    }
}