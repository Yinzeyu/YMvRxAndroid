package com.yzy.example.component.comm

import android.view.*
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.baselibrary.extention.backgroundColor
import com.yzy.baselibrary.http.event.Message
import com.yzy.example.component.comm.view.ViewController
import com.yzy.example.component.dialog.ActionDialog
import kotlinx.android.synthetic.main.base_fragment.*


abstract class CommFragment<VM : BaseViewModel<*>> : BaseFragment<VM>() {
    override val contentLayout: Int = com.yzy.example.R.layout.base_fragment
    /**
     * 填充布局 空布局 loading 网络异常等
     */
   private lateinit var viewController: ViewController
    abstract fun layoutResContentId(): Int
    lateinit var rootView:View
    override fun initView(root: View?) {
        val statusBarHeight = if (fillStatus()) BarUtils.getStatusBarHeight() else 0
        viewController = ViewController(contentView)
        rootView =View.inflate(mContext, layoutResContentId(), null)
        contentView.addView(rootView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        //注册 UI事件
        registorDefUIChange()
        initContentView()
        baseStatusView?.let {
            it.backgroundColor =statusColor()
            it.layoutParams.height =
                if (fillStatus())  BarUtils.getStatusBarHeight() else 0
            it.backgroundColor = statusColor()
        }

//        if (layoutTitleContentId() != 0) {
//            val titleView = View.inflate(mContext, layoutTitleContentId(), null)
//            titleView.backgroundColor=statusColor()
//            titleView.post {
//                baseStatusView.addView(titleView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleView.height + statusBarHeight))
//            }
//
//        } else {
//            baseStatusView.layoutParams.height =baseStatusView.height+ statusBarHeight
//        }
    }
    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.defUI.showDialog.observe(viewLifecycleOwner, Observer {
            showLoadingView()
        })
        viewModel.defUI.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoadingView()
        })
        viewModel.defUI.toastEvent.observe(viewLifecycleOwner, Observer {
            ToastUtils.showShort(it)
        })
        viewModel.defUI.msgEvent.observe(viewLifecycleOwner, Observer {
            handleEvent(it)
        })
    }
    open fun handleEvent(msg: Message) {}

    abstract fun initContentView()

    //是否需要默认填充状态栏,默认填充为白色view
    protected open fun layoutTitleContentId(): Int {
        return 0
    }
  open  fun getViewController():ViewController{
        return viewController
    }

    //#################################镶嵌在页面中的loading->Start#################################//
    //显示json动画的loading
    fun showLoadingView() {
        viewController.showLoading()
    }

    //关闭loadingView
    fun dismissLoadingView() {
        viewController.restore()
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
            it.show((mContext as BaseActivity).supportFragmentManager)
        }
    }

    fun dismissActionLoading() {
        mActionDialog?.dismissAllowingStateLoss()
    }
}