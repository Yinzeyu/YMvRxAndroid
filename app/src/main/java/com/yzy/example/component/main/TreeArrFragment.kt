package com.yzy.example.component.main

import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.gone
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R
import kotlinx.android.synthetic.main.layout_comm_title.*

class TreeArrFragment :CommFragment<NoViewModel,ViewDataBinding>() {

    override fun initContentView() {
        commTitleText.text="广场"
        commTitleBack.gone()
    }

    override fun getLayoutId(): Int  =R.layout.fragment_tree_arr
}