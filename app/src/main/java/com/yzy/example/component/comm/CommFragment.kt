package com.yzy.example.component.comm

import android.graphics.Color
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.example.component.comm.view.ViewController
import com.yzy.example.component.dialog.ActionDialog


abstract class CommFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : BaseFragment<VM, DB>() {
//    override val contentLayout: Int = R.layout.base_fragment

    /**
     * 替换view
     */
    var viewController: ViewController? = null
//    abstract fun layoutResContentId(): Int

//    override fun initView(root: View?) {
//
//        titleView = baseStatusView
//
//
//        if (layoutTitleContentId() != 0) {
//            val titleView = View.inflate(mContext, layoutTitleContentId(), null)
//            baseStatusView.addView(
//                titleView,
//                ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    titleView.height + statusBarHeight
//                )
//            )
//        } else {
//
//        }
//
//        contentView.addView(
//            View.inflate(mContext, layoutResContentId(), null),
//            ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        )
//
//        initContentView()
//    }
    fun  setRootView(rootView: FrameLayout){
        viewController = ViewController(rootView)
    }
    fun setTitleBarHeight(titleView: FrameLayout){
        val statusBarHeight = if (fillStatus()) StatusBarHelper.getStatusBarHeight(mContext) else 0
        titleView.layoutParams.height = titleView.height + statusBarHeight
    }

//    abstract fun initContentView()
//
//    //是否需要默认填充状态栏,默认填充为白色view
//    protected open fun layoutTitleContentId(): Int {
//        return 0
//    }

    //#################################镶嵌在页面中的loading->Start#################################//
    //显示json动画的loading
    fun showLoadingView() {
       viewController?.showLoading()
    }

    //关闭loadingView
    fun dismissLoadingView() {
        viewController?.restore()
    }

    //设置偏移量
    open fun getLoadingViewTransY(): Float {
        return 0f
    }

//    //设置动画高度
//    open fun getLoadingViewHeight(): Int {
//        return rootView.width
//    }

    //设置动画的位置，默认居中
    open fun getLoadingViewGravity(): Int {
        return Gravity.CENTER
    }

    //设置动画背景
    open fun getLoadingViewBgColor(): Int {
        return Color.WHITE
    }
    //#################################镶嵌在页面中的loading<-END#################################//
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
            it.show((mContext as BaseActivity<*, *>).supportFragmentManager)
        }
    }

    fun dismissActionLoading() {
        mActionDialog?.dismissAllowingStateLoss()
    }
}