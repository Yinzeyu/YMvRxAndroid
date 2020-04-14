package com.yzy.example.component.main

import android.content.Context
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
    override fun layoutResId(): Int = R.layout.activity_main

    override fun initView() {
    }
    override fun initData() {
        ActivityUtils.finishOtherActivities(javaClass)
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