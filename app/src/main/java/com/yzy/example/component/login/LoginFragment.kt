package com.yzy.example.component.login

import android.widget.CompoundButton
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.yzy.baselibrary.extention.nav
import com.yzy.baselibrary.extention.parseState
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentLoginBinding
import com.yzy.example.repository.model.LoginViewModel
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.layout_comm_title.*

class LoginFragment : CommFragment<LoginViewModel, FragmentLoginBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_login
    override fun initContentView() {
        binding.vm = viewModel
        binding.click = ProxyClick()
        commTitleText.text="登录"
        viewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    //登录成功
                    it?.let { it1 ->
                        MMkvUtils.instance.setPersonalBean(it1)
                    }
                    nav().navigate(R.id.action_loginFragment_to_mainFragment)
                }, {

                },{
                    //登录失败
                    ToastUtils.showLong(it.message)
                })
            })
        //设置颜色跟主题颜色一致
//        shareViewModel.appColor.value.let {
//            SettingUtil.setShapColor(loginSub, it)
//            loginGoregister.setTextColor(it)
//            toolbar.setBackgroundColor(it)
//        }
    }


    inner class ProxyClick {

        fun clear() {
            viewModel.username.postValue("")
        }

        fun login() {
            when {
                viewModel.username.value.isEmpty() -> ToastUtils.showLong("请填写账号")
                viewModel.password.value.isEmpty() -> ToastUtils.showLong("请填写密码")
                else -> viewModel.loginReq(
                    viewModel.username.value,
                    viewModel.password.value
                )
            }
        }

        fun goRegister() {
            KeyboardUtils.hideSoftInput(requireActivity())
            nav().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                viewModel.isShowPwd.postValue(isChecked)
            }

    }





}