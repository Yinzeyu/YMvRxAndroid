package com.yzy.example.component.main

import android.content.Context
import android.view.MotionEvent
import com.blankj.utilcode.util.ActivityUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R


class MainActivity : BaseActivity() {

    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }



    //不设置状态栏填充，即显示全屏
    override fun layoutResId(): Int = R.layout.activity_main

    override fun initView() {
//        val navController = Navigation.findNavController(this, R.id.nav_fragment)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment?
//        val navigator = KeepStateNavigator(this, navHostFragment!!.childFragmentManager, R.id.nav_fragment)
//        navController.navigatorProvider.addNavigator(navigator)
//        navController.setGraph(R.navigation.wan_navigation)
    }

    override fun initData() {
        //关闭其他所有页面
        ActivityUtils.finishOtherActivities(javaClass)
    }

    interface MyTouchListener {
        fun onTouchEvent(event: MotionEvent?)
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



    private var touchTime = 0L
    private val waitTime = 2000L
    override fun onBackPressed() {
        getFragmentListLast().let {
            if (it is MainFragment) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - touchTime >= waitTime) {
                    //让Toast的显示时间和等待时间相同
                    toast(R.string.double_exit)
                    touchTime = currentTime
                } else {
                    super.onBackPressed()
                }
                return
            } else {
                super.onBackPressed()
            }
        }
    }
}