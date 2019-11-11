package com.yzy.example.component.user

import com.just.agentweb.LogUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.example.R

//@Route(path = "/user/home")
class HomeActivity : BaseActivity() {
    @JvmField
    var id: Long = 0L

    @JvmField
    var name: String = ""
    override fun layoutResId(): Int = R.layout.user_activity_main

    override fun initView() {

        LogUtils.e("zhangsan", "011  $id   $name")
    }

    override fun initData() {
        LogUtils.e("zhangsan", "011  $id   $name")
    }
}
