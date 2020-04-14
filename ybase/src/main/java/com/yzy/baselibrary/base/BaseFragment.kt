package com.yzy.baselibrary.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.yzy.baselibrary.extention.StatusBarHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

/**
 *description: BaseFragment.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseFragment<VM : BaseViewModel<*>> : Fragment(),
    CoroutineScope by MainScope() {
    lateinit var viewModel: VM

    //是否第一次加载
    private var isFirst: Boolean = true
    var isNavigate: Boolean = false
    //页面基础信息
    lateinit var mActivity:BaseActivity
    lateinit var mContext:Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = requireActivity() as BaseActivity
        mContext=context
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
        return inflater.inflate(contentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isBack()) {
            StatusBarHelper.setStatusBarLightMode(mActivity)
        } else {
            StatusBarHelper.setStatusBarDarkMode(mActivity)
        }
        onVisible()
        createViewModel()
        lifecycle.addObserver(viewModel)
        initView(view)
        initData()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
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

    override fun onDestroyView() {
        cancel()
        super.onDestroyView()
    }


    open fun onBackPressed() {
        (mContext as BaseActivity).onBackPressed()
    }
}