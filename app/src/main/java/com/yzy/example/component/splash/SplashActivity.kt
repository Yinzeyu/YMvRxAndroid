package com.yzy.example.component.splash

import android.content.Context
import android.content.Intent
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.startActivity
import com.yzy.example.R
import com.yzy.example.component.MainActivity

class SplashActivity : BaseActivity(){
    companion object {
        fun starSplashActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }
    //倒计时是否结束
    private var countDownFinish: Boolean=false
    override fun layoutResId(): Int=R.layout.fragment_splash
    private var hasFinish = false
    override fun initView() {
//        hasFinish = checkReOpenHome()
//        if (hasFinish) return
//        launch(Dispatchers.Main) {
//            for (i in 5 downTo 1) {
//                splashTime.text = String.format("%d", i)
//                delay(1000)
//            }
//            countDownFinish = true
//            goNextPage()
//            splashTime.text = "0"
//        }
    }
    //打开下个页面
    private fun goNextPage() {
        if (countDownFinish) {
            finish()
            MainActivity.starMainActivity(this)
        }

    }
    override fun initData() {
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

}
