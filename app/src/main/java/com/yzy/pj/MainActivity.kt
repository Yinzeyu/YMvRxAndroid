package com.yzy.pj

import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.ActivityUtils
import com.yzy.baselibrary.base.activity.BaseActivity
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.setStatusBarBlackText
import com.yzy.baselibrary.extention.toast
import com.yzy.pj.ui.IndexFragment

class MainActivity : BaseActivity() {

    /** 点击退出的时间  */
    private var mExitTime: Long = 0
    private lateinit var indexFragment: IndexFragment
    override fun layoutResId(): Int = R.layout.activity_main;
    override fun initView() {
        finishAllActivity()
    }

    override fun initStatus() {
        setStatusBarBlackText()
    }


    override fun initDate() {
        indexFragment = IndexFragment.newInstance()

        //添加到fm
        if (!indexFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .add(R.id.itemLayoutView, indexFragment)
                .commitAllowingStateLoss()
        }
        supportFragmentManager.beginTransaction()
            .hide(indexFragment)
            .show(indexFragment)
            .commitAllowingStateLoss()
    }

    private fun finishAllActivity() {
        // 先关闭其他activity
        ActivityUtils.finishOtherActivities(this@MainActivity::class.java, false)
        //清理奔溃前的fragment
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2 * TimeConstants.SEC) {
            mContext.toast(R.string.exit_app)
            mExitTime = System.currentTimeMillis()
        } else {
            ActivityUtils.finishAllActivities()
            finish()
        }
    }
}
