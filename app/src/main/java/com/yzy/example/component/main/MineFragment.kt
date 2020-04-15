package com.yzy.example.component.main

import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment

class MineFragment : CommFragment<NoViewModel>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun fillStatus(): Boolean = false
    override val contentLayout: Int =R.layout.fragment_mine

    override fun initContentView() {
    }

    override fun initData() {
    }

}