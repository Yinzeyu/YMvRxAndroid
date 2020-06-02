package com.yzy.example.component.splash

import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity(){
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
            when {
                //是否引导
//                MMkvUtils.instance.getNeedGuide() -> GuideActivity.startActivity(mContext)
                //是否登录
//                UserRepository.instance.isLogin() -> MainActivity.startActivity(mContext)
                //没有其他需要，进入主页
                else -> {
//                    if (!(requireActivity() as BaseActivity).getIsNavigate()){
                    nav(clRootView).navigate(R.id.action_rootFragment_to_middleFragment)
//                    }
                }
            }
        }

    }
    override fun initData() {
    }
}
