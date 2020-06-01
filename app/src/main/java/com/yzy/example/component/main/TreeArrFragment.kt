package com.yzy.example.component.main

import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R

class TreeArrFragment :CommFragment<NoViewModel,ViewDataBinding>() {
    override fun initContentView() {

    }

    override fun getLayoutId(): Int  =R.layout.fragment_porject
}