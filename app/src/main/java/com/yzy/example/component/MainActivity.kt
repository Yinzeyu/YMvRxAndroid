package com.yzy.example.component

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.repository.TokenStateManager


class MainActivity : BaseActivity() {

    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    override fun layoutResId(): Int = R.layout.activity_main

    //是否需要关闭页面
    private var hasFinish = false
    override fun initView() {
        hasFinish = checkReOpenHome()
        if (hasFinish) return
        ActivityUtils.finishOtherActivities(MainActivity::class.java)
    }

    //    https://www.cnblogs.com/xqz0618/p/thistaskroot.html
    private fun checkReOpenHome(): Boolean {
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot && intent != null // 判断当前activity是不是所在任务栈的根
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && Intent.ACTION_MAIN == intent.action
        ) {
            finish()
            return true
        }
        return false
    }

    override fun initData() {
        ActivityUtils.finishOtherActivities(javaClass)
        TokenStateManager.instance.mNetworkStateCallback.observe(this, Observer {
        })
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
                    finish()
                }
                return
            } else {
                super.onBackPressed()
            }
        }
    }
}