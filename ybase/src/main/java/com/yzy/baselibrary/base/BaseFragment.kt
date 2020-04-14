package com.yzy.baselibrary.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.R
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.baselibrary.extention.backgroundColor
import com.yzy.baselibrary.extention.inflate
import com.yzy.baselibrary.extention.removeParent
import kotlinx.android.synthetic.main.base_fragment.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 *description: BaseFragment.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment(),
    CoroutineScope by MainScope() {
    lateinit var viewModel: VM
    private var mBinding: DB? = null

    //是否第一次加载
    private var isFirst: Boolean = true
     var isNavigate: Boolean = false


    //页面基础信息
    lateinit var mContext: Activity
    protected var rootView: FrameLayout? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    /**
     * 内容布局的ResId
     */
    protected abstract val contentLayout: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        var view = inflater.inflate(R.layout.base_fragment, container, false)
        rootView = view.contentView
        if (contentLayout > 0) {
            rootView?.addView(mContext.inflate(contentLayout, container, false))
        } else {
            rootView?.removeParent()
        }
        view.baseStatusView?.let {
            it.layoutParams.height =
                if (fillStatus()) StatusBarHelper.getStatusBarHeight(mContext) else 0
            it.backgroundColor = statusColor()
        }
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.bind(view)
            mBinding?.lifecycleOwner = this
            view = mBinding?.root
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isBack()) {
            StatusBarHelper.setStatusBarLightMode(mContext)
        } else {
            StatusBarHelper.setStatusBarDarkMode(mContext)
        }
        onVisible()
        createViewModel()
        lifecycle.addObserver(viewModel)
        initView(view)
        initData()
    }

    private var isShow: Boolean = false
    override fun onResume() {
        super.onResume()
        onVisible()
        if (isShow) {
            if (NavigateManager.instance.getDestination() != null) {
                NavigateManager.instance.getDestination()?.let {
                    rootView?.let { it1 ->
                        Navigation.findNavController(it1).navigate(
                            it.id
                        )
                    }
                    NavigateManager.instance.setDestination(null)
                    isShow = false
                }
            }
        }
    }


    //    override fun onSaveInstanceState(outState: Bundle) {
//
//        if (this.isAdded){
//            LogUtils.e("mFragment",this.javaClass.name)
//            parentFragmentManager.putFragment(outState, "mFragment", this);
//        }
//        super.onSaveInstanceState(outState)
//    }
    fun setVis() {
        isShow = true
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
    }

    /**
     * 初始化View
     */
    protected abstract fun initView(root: View?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    open fun onFragmentRestart() {
        if (isNavigate) {
            onRestartNavigate()
            isNavigate = false
        }
    }

    open fun onRestartNavigate() {

    }

    open fun onFragmentStop() {
        isNavigate = true
    }

    //是否需要默认填充状态栏,默认填充为白色view
    protected open fun fillStatus(): Boolean {
        return true
    }

    protected open fun statusColor(): Int {
        return Color.TRANSPARENT
    }

    protected open fun isBack(): Boolean {
        return true
    }

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(clazz)

    override fun onDestroyView() {
        cancel()
        super.onDestroyView()
    }


    open fun onBackPressed() {
        (mContext as BaseActivity<*, *>).onBackPressed()
    }
}