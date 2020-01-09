package com.yzy.example.component.main

import android.content.Context
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CommActivity() {
    var hasNineImgOrVideo = false

    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    //不设置状态栏填充，即显示全屏
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
            supportFragmentManager.fragments.first().childFragmentManager.fragments.last().let {
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
                    Navigation.findNavController(nav_fragment).navigateUp()
                }
            }
        }
}