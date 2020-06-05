package com.yzy.example.component.comm

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yzy.baselibrary.app.ExceptionTokenViewModel
import com.yzy.baselibrary.base.*
import com.yzy.example.component.dialog.ActionDialog
import com.yzy.example.repository.TokenStateManager


abstract class CommFragment<VM : BaseViewModel<*>,DB :ViewDataBinding> : BaseFragment<VM,DB>() {
    /**
     * 填充布局 空布局 loading 网络异常等
     */
//   private lateinit var viewController: ViewController
    override fun initView(savedSate: Bundle?) {
//        if (rootView != null){
//            viewController = ViewController(rootView!!)
//        }

        //注册 UI事件
        registorDefUIChange()
        initContentView()
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.loadingChange.showDialog.observe(viewLifecycleOwner, Observer {
            showLoadingView()
        })
        viewModel.loadingChange.dismissDialog.observe(viewLifecycleOwner, Observer {
            dismissLoadingView()
        })
        viewModel.loadingChange.tokenError.observe(viewLifecycleOwner, Observer {
            TokenStateManager.instance.mNetworkStateCallback.postValue(true)
        })
    }
    open fun handleEvent(i:Int) {}

    abstract fun initContentView()


    //#################################镶嵌在页面中的loading->Start#################################//
    //显示json动画的loading
    fun showLoadingView() {
      showActionLoading()
    }

    //关闭loadingView
    fun dismissLoadingView() {
       dismissActionLoading()
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
            it.show(requireActivity().supportFragmentManager)
        }
    }

    fun dismissActionLoading() {
        mActionDialog?.dismissAllowingStateLoss()
    }
}