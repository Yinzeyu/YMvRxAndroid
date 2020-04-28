package com.yzy.example.component.main

import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.dialog.updateDialog
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : CommFragment<NoViewModel>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun fillStatus(): Boolean = false
    override val contentLayout: Int =R.layout.fragment_mine

    override fun initContentView() {
        button.click {
            updateDialog(mActivity.supportFragmentManager){

            }
        }
    }

    override fun initData() {
    }

}