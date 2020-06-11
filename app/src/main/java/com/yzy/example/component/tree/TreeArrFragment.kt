package com.yzy.example.component.tree

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.visible
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R
import com.yzy.example.extention.bindViewPager2
import com.yzy.example.extention.init
import kotlinx.android.synthetic.main.fragment_tree_arr.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class TreeArrFragment :CommFragment<NoViewModel,ViewDataBinding>() {
    var titleData = arrayListOf("广场", "每日一问", "体系", "导航")
    override fun initContentView() {
        commTitleText.text="广场"
        commTitleBack.gone()
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())

        //初始化viewpager2
        viewPager.init(this, fragments).offscreenPageLimit = fragments.size
        magicIndicator.bindViewPager2(viewPager, mStringList = titleData) {
            if (it != 0) {
                todoAdd.gone()
            } else {
                todoAdd.visible()
            }
        }
        todoAdd.click {

        }
    }
    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun getLayoutId(): Int  =R.layout.fragment_tree_arr
}