package com.yzy.example.component.main

import android.view.View
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment

class MineFragment: CommFragment(){

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override val contentLayout: Int = R.layout.fragment_home

    override fun initView(root: View?) {
    }

    override fun initData() {
    }

}