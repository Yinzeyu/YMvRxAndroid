package com.yzy.example.component.main

import android.view.View
import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment

class MineFragment(override val contentLayout: Int = R.layout.fragment_mine) : CommFragment<NoViewModel>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun fillStatus(): Boolean = false

    override fun initView(root: View?) {
    }

    override fun initData() {
    }

}