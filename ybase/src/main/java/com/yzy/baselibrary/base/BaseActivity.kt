package com.yzy.baselibrary.base

import android.content.Intent
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

abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        translucent(this)
        this.onCreateBefore()
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())

        initView()
        initData()
    }

    override fun onRestart() {
        super.onRestart()
        acEventType("restart")
    }

    override fun onStop() {
        super.onStop()
        acEventType("stop")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        acEventType("activityResult", requestCode, resultCode, data)
    }

    private fun acEventType(
        type: String,
        requestCode: Int = -1,
        resultCode: Int = -1,
        data: Intent? = null
    ) {
        try {
            getFragmentListLast().let {
                if (it is BaseFragment<*, *>) {
                    when (type) {
                        "stop" -> {
                            it.onFragmentStop()
                        }
                        "restart" -> {
                            it.onFragmentRestart()
                        }
                        "activityResult" -> {
                            it.onFragmentResult(requestCode, resultCode, data)
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
