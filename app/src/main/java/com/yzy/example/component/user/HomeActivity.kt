package com.yzy.example.component.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.just.agentweb.LogUtils
import com.yzy.example.R
import com.yzy.example.constants.RouterConstants

@Route(path ="/user/home")
class HomeActivity : AppCompatActivity() {
    @Autowired(name = RouterConstants.User.KEY_MAP_LAT)
    @JvmField
    var id: Long = 0L

    @Autowired(name = RouterConstants.User.KEY_MAP_LAT)
    @JvmField
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity_main)
        LogUtils.e("zhangsan", "$id   $name")
    }
}
