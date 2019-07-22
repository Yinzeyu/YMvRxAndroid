package com.yzy.commonlibrary.refresh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.yzy.commonlibrary.R

/**
 *description: 黑色loading样式的下拉刷新的头.
 *@date 2019/6/13 18:17.
 *@author: YangYang.
 */
class BlackLoadingHeader(context: Context) : RefreshHeader {

  private val rootView: View =
    LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_blakloading, null)

  private var headerLav: LottieAnimationView? = null

  init {
    headerLav = rootView.findViewById(R.id.headerLav)
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