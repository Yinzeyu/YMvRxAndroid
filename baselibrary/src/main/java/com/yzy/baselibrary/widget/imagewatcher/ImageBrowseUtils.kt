package com.yzy.baselibrary.widget.imagewatcher

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity


object ImageBrowseUtils {
    fun newInstance(
        activity: FragmentActivity, listener: EnterExitListener,
        isShowIndex: Boolean, logClickListener: ((v: View, uri: Uri, pos: Int) -> Unit)? = null
    ): ImageBrowse {
        return ImageBrowse.newInstance(activity, listener, isShowIndex, logClickListener)
    }

    fun newInstance(
        activity: FragmentActivity,
        isShowIndex: Boolean,
        logClickListener: ((v: View, uri: Uri, pos: Int) -> Unit)? = null
    ): ImageBrowse {
        return ImageBrowse.newInstance(activity, null, isShowIndex, logClickListener)
    }

    /** 是否需要大图处理返回，不需要则返回false  */
    fun handleBackPressed(activity: FragmentActivity?): Boolean {
        if (activity != null) {
            val view = activity.window.decorView as ViewGroup
            val count = view.childCount
            for (i in count - 1 downTo 0) {
                val child = view.getChildAt(i)
                if (child is ImageWatcher) {
                    return child.handleBackPressed()
                }
            }
        }
        return false
    }
}

