package com.yzy.baselibrary.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
abstract class BaseFragment<VM : BaseViewModel<*>, DB : ViewDataBinding>  : Fragment(),
    CoroutineScope by MainScope() {
    lateinit var viewModel: VM
     var mBinding: DB? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //第一次的时候加载xml
        Log.e("fragment", this.javaClass.name)
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            return mBinding?.root
        }
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isBack()) {
            StatusBarHelper.setStatusBarLightMode(requireActivity())
        } else {
            StatusBarHelper.setStatusBarDarkMode(requireActivity())
        }
        createViewModel()
        lifecycle.addObserver(viewModel)
        initView(savedInstanceState)
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
     * 是否屏蔽返回键
     */
    fun onBack(enabled:Boolean,onBackPressed:()->Unit){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBackPressed.invoke()
            }
        })
    }
    @LayoutRes
    abstract fun getLayoutId(): Int
    /**
     * 初始化
     */
    abstract fun initView(savedSate: Bundle?)

    /**
     * activityResult
     */
    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    /**
     * 锁屏之后从起会调用此方法
     */
    open fun onRestartNavigate() {

    }

    /**
     * 默认状态栏黑色字体图标
     */
    protected open fun isBack(): Boolean {
        return true
    }

    override fun onDestroyView() {
        cancel()
        super.onDestroyView()
    }


    open fun onBackPressed() {
       requireActivity().onBackPressed()
    }
}