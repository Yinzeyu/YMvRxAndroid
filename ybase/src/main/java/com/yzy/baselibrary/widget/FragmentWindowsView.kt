package com.yzy.baselibrary.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout

/**
 * 解决fitsSystemWindows 无效问题
 */
class FragmentWindowsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : FrameLayout(context, attributeSet, defAttrStyle) {

    init {
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                requestApplyInsets()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {}
        })

    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets? {
        val childCount = childCount
        for (index in 0 until childCount) getChildAt(index).dispatchApplyWindowInsets(insets)
        return insets
    }

}