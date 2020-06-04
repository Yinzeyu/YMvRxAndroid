package com.yzy.example.component.main

import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.gone
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R
import kotlinx.android.synthetic.main.layout_comm_title.*

class PublicNumberFragment :CommFragment<NoViewModel,ViewDataBinding>() {
    override fun initContentView() {
        commTitleText.text="公众号"
        commTitleBack.gone()
    }

    override fun getLayoutId(): Int  =R.layout.fragment_public_number
}