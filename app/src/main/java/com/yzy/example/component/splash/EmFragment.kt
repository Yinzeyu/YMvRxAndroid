package com.yzy.example.component.splash


import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.nav
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentEmBinding
import com.yzy.example.utils.MMkvUtils

class EmFragment : CommFragment<NoViewModel, FragmentEmBinding>() {

    //倒计时是否结束

    override fun initContentView() {
        when {
            //是否引导
//                MMkvUtils.instance.getNeedGuide() -> GuideActivity.startActivity(mContext)
            //是否登录
            !MMkvUtils.instance.isLogin() ->{
                nav().navigate(R.id.action_splashFragment_to_loginFragment)
            }


            //没有其他需要，进入主页
            else -> {
              nav().navigate(R.id.action_rootFragment_to_middleFragment)
            }
        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_em
}
