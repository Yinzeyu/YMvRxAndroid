package com.yzy.baselibrary.base

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
import com.blankj.utilcode.util.LogUtils
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
    lateinit var binding: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("baseFragment",javaClass.name)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
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
     * 默认状态栏黑色字体图标
     */
    protected open fun isBack(): Boolean {
        return true
    }

    override fun onDestroyView() {
        cancel()
        Log.e("baseFragment",javaClass.name)
        super.onDestroyView()
    }


    open fun onBackPressed() {
       requireActivity().onBackPressed()
    }
}