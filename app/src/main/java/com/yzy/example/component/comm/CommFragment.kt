package com.yzy.example.component.comm

import android.view.*
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.base.ThrowableBean
import com.yzy.example.component.comm.view.ViewController
import com.yzy.example.component.dialog.ActionDialog


abstract class CommFragment<VM : BaseViewModel<*>> : BaseFragment<VM>() {
    /**
     * 填充布局 空布局 loading 网络异常等
     */
   private lateinit var viewController: ViewController
    override fun initView(root: View?) {
        if (rootView != null){
            viewController = ViewController(rootView!!)
        }

        //注册 UI事件
        registorDefUIChange()
        initContentView()
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
        viewModel.defUI.errorEvent.observe(viewLifecycleOwner, Observer {
            handleEvent(it)
        })
    }
    open fun handleEvent(msg: ThrowableBean) {}

    abstract fun initContentView()

    //是否需要默认填充状态栏,默认填充为白色view
    protected open fun layoutTitleContentId(): Int {
        return 0
    }

    protected open fun titleHeight(): Int {
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