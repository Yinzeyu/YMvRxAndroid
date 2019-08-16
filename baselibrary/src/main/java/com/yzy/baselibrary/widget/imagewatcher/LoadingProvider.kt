package com.yzy.baselibrary.widget.imagewatcher

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.R

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/8 12:20
 */
internal class LoadingProvider : ImageWatcher.LoadingUIProvider {
    override fun initialView(parent: ViewGroup): View {
        val context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(R.layout.layout_imagewatcher_loading, parent, false)
        val params = FrameLayout.LayoutParams(SizeUtils.dp2px(60f), SizeUtils.dp2px(60f))
        params.gravity = Gravity.CENTER
        view.layoutParams = params
        return view
    }

    override fun start(loadView: View) {
        loadView.visibility = View.VISIBLE
    }

    override fun stop(loadView: View) {
        loadView.visibility = View.GONE
    }
}
