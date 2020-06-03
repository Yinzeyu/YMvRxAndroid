package com.yzy.example.component.splash

import android.content.Context
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.nav
import com.yzy.baselibrary.extention.startActivity
import com.yzy.example.R
import com.yzy.example.component.main.MainActivity
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity(){
    companion object {
        fun starSplashActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }
    //倒计时是否结束
    private var countDownFinish: Boolean=false
    override fun layoutResId(): Int=R.layout.fragment_splash

    override fun initView() {
        launch(Dispatchers.Main) {
            for (i in 5 downTo 1) {
                splashTime.text = String.format("%d", i)
                delay(1000)
            }
            countDownFinish = true
            goNextPage()
            splashTime.text = "0"
        }
    }
    //打开下个页面
    private fun goNextPage() {
        if (countDownFinish) {
            MainActivity.starMainActivity(mContext)
        }

    }
    override fun initData() {
    }
}
