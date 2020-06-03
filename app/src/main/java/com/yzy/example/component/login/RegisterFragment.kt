package com.yzy.example.component.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.nav
import com.yzy.baselibrary.extention.parseState
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.example.R
import com.yzy.example.databinding.FragmentRegisterBinding
import com.yzy.example.extention.initClose
import com.yzy.example.repository.model.RegisterViewModel
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_register
    override fun initView(savedSate: Bundle?) {
        binding.vm = viewModel
        binding.click = ProxyClick()
        commTitleText.text="注册"
        commTitleBack.pressEffectAlpha()
        commTitleBack.click {
            nav().navigateUp()
        }
        viewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    it?.let { it1 ->
                        MMkvUtils.instance.setPersonalBean(it1)
                    }
//                    shareViewModel.isLogin.postValue(true)
//                    CacheUtil.setUser(it)
//                    shareViewModel.userinfo.postValue(it)
                    nav().navigate(R.id.action_registerFragment_to_mainFragment)
                }, {
                    ToastUtils.showLong(it.errMsg)
                })
            })
    }

    inner class ProxyClick {
        /**清空*/
        fun clear() {
            viewModel.username.postValue("")
        }

        /**注册*/
        fun register() {
            when {
                viewModel.username.value.isEmpty() -> ToastUtils.showLong("请填写账号")
                viewModel.password.value.isEmpty() -> ToastUtils.showLong("请填写密码")
                viewModel.password2.value.isEmpty() ->ToastUtils.showLong("请填写确认密码")
                viewModel.password.value.length < 6 -> ToastUtils.showLong("密码最少6位")
                viewModel.password.value != viewModel.password2.value ->  ToastUtils.showLong("密码不一致")
                else -> viewModel.registerAndlogin(
                    viewModel.username.value,
                    viewModel.password.value
                )
            }
        }

        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.isShowPwd.postValue(isChecked)
        }
        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            viewModel.isShowPwd2.postValue(isChecked)
        }

    }


}