package com.yzy.example.extention

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ToastUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * 获取随机rgb颜色值
 */
fun randomColor(): Int {
    Random().run {
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = nextInt(190)
        val green = nextInt(190)
        val blue = nextInt(190)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }
}

/**
 * 加入qq聊天群
 */
fun Fragment.joinQQGroup(key: String): Boolean {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
        false
    }
}

/**
 * 拦截登录操作，如果没有登录跳转登录，登录过了贼执行你的方法
 */
//fun NavController.jumpByLogin(action: (NavController) -> Unit) {
//    if (CacheUtil.isLogin()) {
//        action(this)
//    } else {
//        //注意一下，这里我是确定我所有的拦截登录都是在MainFragment中的，所以我可以写死，但是如果不在MainFragment中时跳转，你会报错,
//        //当然你也可以执行下面那个方法 自己写跳转
//        this.navigate(R.id.action_mainFragment_to_loginFragment)
//    }
//}

/**
 * 拦截登录操作，如果没有登录执行方法 actionLogin 登录过了执行 action
 */
//fun NavController.jumpByLogin(
//    actionLogin: (NavController) -> Unit,
//    action: (NavController) -> Unit
//) {
//    if (CacheUtil.isLogin()) {
//        action(this)
//    } else {
//        actionLogin(this)
//    }
//}


