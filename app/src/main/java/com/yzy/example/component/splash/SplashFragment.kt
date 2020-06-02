package com.yzy.example.component.splash


import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentSplashBinding
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : CommFragment<NoViewModel, FragmentSplashBinding>() {


    //倒计时是否结束
    private var countDownFinish: Boolean = false


    override fun initContentView() {
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
                !MMkvUtils.instance.isLogin() -> nav(clRootView).navigate(R.id.action_splashFragment_to_loginFragment)
                //没有其他需要，进入主页
                else -> {
                    nav(clRootView).navigate(R.id.action_rootFragment_to_middleFragment)
                }
            }
        }

    }

    override fun onDestroyView() {
        requireActivity().setTheme(R.style.AppTheme)
        super.onDestroyView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_splash
}
