package com.yzy.example.component.main

import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.dialog.updateDialog
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : CommFragment<NoViewModel,ViewDataBinding>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun initContentView() {
        button.click {
            updateDialog(requireActivity().supportFragmentManager){

            }
        }
    }
    override fun getLayoutId(): Int  =R.layout.fragment_mine

}