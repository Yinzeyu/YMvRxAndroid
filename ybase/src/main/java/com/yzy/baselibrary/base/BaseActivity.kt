package com.yzy.baselibrary.base

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yzy.baselibrary.extention.StatusBarHelper.translucent
import com.yzy.baselibrary.extention.fixInputMethodManagerLeak
import com.yzy.baselibrary.toast.YToast
import com.yzy.baselibrary.utils.MyTouchListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

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
                if (it is BaseFragment<*>) {
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
    // 保存MyTouchListener接口的列表
    private val myTouchListeners = mutableListOf<MyTouchListener>()

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     * @param listener
     */
    fun registerMyTouchListener(listener: MyTouchListener) {
        myTouchListeners.add(listener)
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     * @param listener
     */
    fun unRegisterMyTouchListener(listener: MyTouchListener?) {
        myTouchListeners.remove(listener)
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (listener in myTouchListeners) {
            listener.onTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
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
        fixInputMethodManagerLeak(this)
        super.onDestroy()
    }

    fun getFragmentListLast(): Fragment = supportFragmentManager.fragments.first().childFragmentManager.fragments.last()

    fun getFragmentLists(): List<Fragment> = supportFragmentManager.fragments.first().childFragmentManager.fragments
}
