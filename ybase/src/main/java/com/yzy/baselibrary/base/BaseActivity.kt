package com.yzy.baselibrary.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.baselibrary.toast.YToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope(){
    override fun onCreate(savedInstanceState: Bundle?) {
        this.onCreateBefore()
        this.initStatus()
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        initView()
        initData()
    }
    /** 获取 ViewModel */
    fun <T : ViewModel> getViewModel(clazz: Class<T>): T = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(clazz)
    /**
     * 页面内容布局resId
     */
    protected abstract fun layoutResId(): Int
    abstract fun initView()
    abstract fun initData()

//    /** 适配状态栏  */
    protected open fun initStatus() {
//        immersionBar {
//            transparentStatusBar()
//            statusBarDarkFont(true)
//            fitsSystemWindows(true)
//            statusImmersionBar(this)
//        }
    }

    protected open fun statusImmersionBar(immersionBar: ImmersionBar) {
    }

    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {}


    override fun onDestroy() {
        super.onDestroy()
        cancel()
        //移除所有Activity类型的Toast防止内存泄漏
        YToast.cancelActivityToast(this)
    }

}
