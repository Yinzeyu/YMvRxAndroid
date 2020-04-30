package com.yzy.example.component.splash


import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentSplashBinding
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : CommFragment<NoViewModel,FragmentSplashBinding>() {


    //倒计时是否结束
    private var countDownFinish: Boolean=false


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

    override fun onRestartNavigate() {
        if (countDownFinish){
                Navigation.findNavController(clRootView).navigate(R.id.action_rootFragment_to_middleFragment)
            Log.e("fragment", this.javaClass.name+"=onRestartNavigate")
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
                    if (!(requireActivity() as BaseActivity).getIsNavigate()){
                        Navigation.findNavController(clRootView).navigate(R.id.action_rootFragment_to_middleFragment)
                    }
                }
            }
        }

    }

    override fun getLayoutId(): Int = R.layout.fragment_splash
}
