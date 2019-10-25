package com.yzy.commonlibrary.comm

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import com.airbnb.lottie.*
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseFragmentDialog
import com.yzy.baselibrary.extention.mScreenWidth
import com.yzy.baselibrary.extention.removeParent
import com.yzy.commonlibrary.repository.dialog.ActionDialog

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/8 10:03
 */
abstract class CommDialogFragment : BaseFragmentDialog() {
    //lottie的加载动画
    lateinit var loadingView: LottieAnimationView

    override fun initBeforeCreateView(savedInstanceState: Bundle?) {
        initLottie()
        super.initBeforeCreateView(savedInstanceState)
    }

    //#################################镶嵌在页面中的loading->Start#################################//
    //初始化loading
    private fun initLottie() {
        loadingView = LottieAnimationView(mContext)
        loadingView.setAnimation("loading.json")
        loadingView.imageAssetsFolder = "images/"
        loadingView.setRenderMode(RenderMode.HARDWARE)
        loadingView.repeatCount = LottieDrawable.INFINITE
        loadingView.repeatMode = LottieDrawable.RESTART
        loadingView.setOnClickListener { }
    }

    //显示json动画的loading
    fun showLoadingView() {
        view?.post { startShowLoadingView() }
        //防止获取不到高度

    }

    //显示loadingView
    private fun startShowLoadingView(
        transY: Float = getLoadingViewTransY(),
        height: Int = getLoadingViewHeight(),
        gravity: Int = getLoadingViewGravity(),
        bgColor: Int = getLoadingViewBgColor()
    ) {
        if (loadingView.parent == null) {
            val parent = view as FrameLayout
            val prams = FrameLayout.LayoutParams(-1, height)
            prams.gravity = gravity
            loadingView.translationY = transY
            loadingView.setBackgroundColor(bgColor)
            parent.addView(loadingView, prams)
        }
        loadingView.playAnimation()
    }

    //关闭loadingView
    fun dismissLoadingView() {
        loadingView.pauseAnimation()
        loadingView.cancelAnimation()
        loadingView.removeParent()
    }

    //设置偏移量
    open fun getLoadingViewTransY(): Float {
        return 0f
    }

    //设置动画高度
    open fun getLoadingViewHeight(): Int {
        return mActivity.mScreenWidth
    }

    //设置动画的位置，默认居中
    open fun getLoadingViewGravity(): Int {
        return Gravity.CENTER
    }

    //设置动画背景
    open fun getLoadingViewBgColor(): Int {
        return Color.TRANSPARENT
    }
    //#################################镶嵌在页面中的loading<-END#################################//

    private var mActionDialog: ActionDialog? = null

    fun showActionLoading(text: String? = null) {
        if (mActionDialog == null) {
            mActionDialog = ActionDialog.newInstance(true)
        }
        mActionDialog?.onDismiss {
            mActionDialog = null
        }
        mActionDialog?.let {
            if (!text.isNullOrBlank()) it.hintText = text
            it.show((mActivity as BaseActivity).supportFragmentManager)
        }
    }

    fun dismissActionLoading() {
        mActionDialog?.dismissAllowingStateLoss()
    }
}