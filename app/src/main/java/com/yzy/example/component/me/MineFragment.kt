package com.yzy.example.component.me

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.parseState
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentMineBinding
import com.yzy.example.extention.init
import com.yzy.example.extention.joinQQGroup
import com.yzy.example.repository.model.MeViewModel
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : CommFragment<MeViewModel, FragmentMineBinding>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun initContentView() {
        binding.vm = viewModel
        binding.click = ProxyClick()
        viewModel.getIntegral()
        me_swipe.init {
            viewModel.getIntegral()
        }
        MMkvUtils.instance.getPersonalBean()?.let {
            viewModel.name.postValue(if (it.nickname.isEmpty()) it.username else it.nickname)
        }
        viewModel.meData.observe(viewLifecycleOwner, Observer { resultState ->
            me_swipe.isRefreshing = false
            parseState(resultState, {
                viewModel.info.postValue("id：${it?.userId}　排名：${it?.rank}")
                viewModel.integral.postValue(it?.coinCount)
            }, {
                ToastUtils.showShort(it.errMsg)
            })
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_mine

    inner class ProxyClick {
        /** 登录 */
        fun login() {
//            nav().jumpByLogin {}
        }

        /** 收藏 */
        fun collect() {
//            nav().jumpByLogin {
//                it.navigate(R.id.action_mainfragment_to_collectFragment)
//            }
        }

        /** 积分 */
        fun integral() {
//            nav().jumpByLogin {
//                it.navigate(R.id.action_mainfragment_to_integralFragment,
//                    Bundle().apply {
//                        putParcelable("rank", rank)
//                    }
//                )
//            }
        }

        /** 文章 */
        fun ariticle() {
//            nav().jumpByLogin {
//                it.navigate(R.id.action_mainfragment_to_ariticleFragment)
//            }
        }

        /** Todo */
        fun todo() {
//            nav().jumpByLogin {
//                it.navigate(R.id.action_mainfragment_to_todoListFragment)
//            }
        }

        /** 玩Android开源网站 */
        fun about() {
//            nav().navigate(R.id.action_mainfragment_to_webFragment, Bundle().apply {
//                putParcelable(
//                    "bannerdata",
//                    BannerResponse(
//                        title = "玩Android网站",
//                        url = "https://www.wanandroid.com/"
//                    )
//                )
//            })
        }

        /** 加入我们 */
        fun join() {
            joinQQGroup("1nLU15GhxIe9MT3cM6djdKEDNIjwqUK6")
        }

        /** 设置 */
        fun setting() {
//            nav().navigate(R.id.action_mainfragment_to_settingFragment)
        }

        /**demo*/
        fun demo() {
//            nav().navigate(R.id.action_mainfragment_to_demoFragment)
        }
    }
}