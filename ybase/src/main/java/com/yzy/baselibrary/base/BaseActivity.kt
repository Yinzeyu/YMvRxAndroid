package com.yzy.baselibrary.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.baselibrary.extention.StatusBarHelper.translucent
import com.yzy.baselibrary.toast.YToast
import com.yzy.baselibrary.utils.CleanLeakUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>  : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var viewModel: VM
    private var mBinding: DB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        translucent(this)
        this.onCreateBefore()
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        lifecycle.addObserver(viewModel)

        initView()
        initData()
    }
    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.setContentView(this, layoutResId())
            mBinding?.lifecycleOwner = this
        } else setContentView(layoutResId())
        createViewModel()
    }

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
     * 页面内容布局resId
     */
    protected abstract fun layoutResId(): Int

    abstract fun initView()
    abstract fun initData()


    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {}


    override fun onDestroy() {
        cancel()
        //移除所有Activity类型的Toast防止内存泄漏
        YToast.cancelActivityToast(this)
        CleanLeakUtils.instance.fixInputMethodManagerLeak(this)
        super.onDestroy()
    }
    
    fun getFragmentListLast(): Fragment =
        supportFragmentManager.fragments.first().childFragmentManager.fragments.last()

    fun getFragmentLists(): List<Fragment> =
        supportFragmentManager.fragments.first().childFragmentManager.fragments
}
