package com.yzy.example.component.main

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.blankj.utilcode.util.ActivityUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.KeepStateNavigator
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }
    override fun layoutResId(): Int = R.layout.activity_main

    override fun initView() {
        val navController = Navigation.findNavController(this, R.id.fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?
        val navigator = KeepStateNavigator(this, navHostFragment!!.childFragmentManager, R.id.fragment)
        navController.navigatorProvider.addNavigator(navigator)
        navController.setGraph(R.navigation.wan_navigation)
    }

    fun getRootView(): ConstraintLayout {
        return mainRootView
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
                finish()
                }
                return
            } else {
                super.onBackPressed()
            }
        }
    }
}