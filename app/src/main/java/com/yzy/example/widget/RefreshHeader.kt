package com.yzy.example.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.yzy.baselibrary.widget.refresh.api.RefreshHeader
import com.yzy.baselibrary.widget.refresh.api.RefreshKernel
import com.yzy.baselibrary.widget.refresh.api.RefreshLayout
import com.yzy.baselibrary.widget.refresh.constant.RefreshState
import com.yzy.baselibrary.widget.refresh.constant.SpinnerStyle
import com.yzy.example.R

/**
 *description: 小黑人动画的下拉刷新的头.
 *@date 2019/6/13 18:17.
 *@author: YangYang.
 */
class RefreshHeader(context: Context) : RefreshHeader {

    private val rootView: View =
            LayoutInflater.from(context)
                    .inflate(R.layout.layout_refresh_header, null)

    private var headerLav: LottieAnimationView? = null
    private var headerTv: TextView? = null

    init {
        headerLav = rootView.findViewById(R.id.headerLav)
        headerTv = rootView.findViewById(R.id.headerTv)
    }

    override fun getSpinnerStyle() = SpinnerStyle.Translate

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        headerLav?.cancelAnimation()
        headerLav?.progress = 0f
        return 200
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View {
        return rootView
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        headerLav?.playAnimation()
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.None -> {
                headerTv?.text = "下拉刷新"
            }
            RefreshState.PullDownToRefresh -> {
                headerTv?.text = "下拉刷新"
            }
            RefreshState.ReleaseToRefresh -> {
                headerTv?.text = "释放刷新"
            }
            RefreshState.Refreshing -> {
                headerTv?.text = ""
            }
            else -> {

            }
        }
    }

    override fun onMoving(
            isDragging: Boolean,
            percent: Float,
            offset: Int,
            height: Int,
            maxDragHeight: Int
    ) {
    }

    override fun isSupportHorizontalDrag() = false

}