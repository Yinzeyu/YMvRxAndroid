package com.yzy.example.component.main

import android.content.Context
import android.content.Intent
import androidx.annotation.IntRange
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommActivity
import com.yzy.example.component.comm.CommFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommActivity() {


    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }
    override fun initStatus() {
        immersionBar { statusBarDarkFont(true) }
    }
    override fun layoutResId(): Int = R.layout.activity_main
    override fun initView() {
    }
    override fun initData() {
        //关闭其他所有页面
        ActivityUtils.finishOtherActivities(javaClass)
    }

    private var touchTime = 0L
    private val waitTime = 2000L
    override fun onBackPressed() {
        supportFragmentManager.fragments.first()
            .childFragmentManager.fragments.last().let {
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
            }
        }
        super.onBackPressed()
    }
}