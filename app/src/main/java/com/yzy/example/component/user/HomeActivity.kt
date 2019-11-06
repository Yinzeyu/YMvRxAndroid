package com.yzy.example.component.user

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.just.agentweb.LogUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.example.R
import com.yzy.example.constants.RouterConstants

@Route(path = "/user/home")
class HomeActivity : BaseActivity() {
    @Autowired(name = RouterConstants.User.KEYMAPLAT)
    @JvmField
    var id: Long = 0L

    @Autowired(name = RouterConstants.User.KEYMAPLNG)
    @JvmField
    var name: String = ""
    override fun layoutResId(): Int = R.layout.user_activity_main

    override fun initView() {
        LogUtils.e("zhangsan", "$id   $name")
    }

    override fun initData() {
    }
}
