package com.yzy.baselibrary.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yzy.baselibrary.extention.setLightMode
import com.yzy.baselibrary.toast.YToast
import com.yzy.baselibrary.utils.CleanLeakUtils
import com.yzy.baselibrary.utils.setStatusBarLightMode
import com.yzy.baselibrary.utils.translucent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    //    private var mStatusView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        this.onCreateBefore()
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        translucent(this)
        setStatusBarLightMode(this);
        initView()
        initData()
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

    /** 获取 ViewModel */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)

        fun getFragmentListLast(): Fragment = supportFragmentManager.fragments.first().childFragmentManager.fragments.last()

    fun getFragmentLists(): List<Fragment> = supportFragmentManager.fragments.first().childFragmentManager.fragments
}
