package com.yzy.example.component.main

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : CommFragment<NoViewModel>() {
    //页面
    private lateinit var homeFragment: Fragment
    private lateinit var dynFragment: Fragment
    private lateinit var mineFragment: Fragment
    //当前页面
    private var currentFragment: Fragment? = null
    //子列表合集，方便外部调用选中那个
    private var fragmentList = mutableListOf<Fragment>()
    override val contentLayout: Int=R.layout.fragment_main
    override fun fillStatus(): Boolean = false
    override fun initContentView() {
//        初始化
        homeFragment = HomeFragment.newInstance()
        dynFragment = DynFragment.newInstance()
        mineFragment = MineFragment.newInstance()
        //添加
        fragmentList = mutableListOf(homeFragment, dynFragment, mineFragment)
        //设置选中
        selectFragment(0)
        setSelectIndex(0)
        //切换
        mainNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_home -> selectFragment(0)
                R.id.menu_main_dyn -> selectFragment(1)
                R.id.menu_main_mine -> selectFragment(2)
            }
            true//返回true让其默认选中点击的选项
        }

    }

    override fun initData() {
    }

//    设置选中的fragment
    private fun selectFragment(@androidx.annotation.IntRange(from = 0, to = 2) index: Int) {
        //需要显示的fragment
        val fragment = fragmentList[index]
        //和当前选中的一样，则不再处理
        if (currentFragment == fragment) return
        //先关闭之前显示的
        currentFragment?.let { FragmentUtils.hide(it) }
        //设置现在需要显示的
        currentFragment = fragment
        if (!fragment.isAdded) { //没有添加，则添加并显示
            val tag = fragment::class.java.simpleName
            FragmentUtils.add(
                childFragmentManager, fragment, mainContainer.id, tag, false
            )
        } else { //添加了就直接显示
            FragmentUtils.show(fragment)
        }
    }

    //外部调用选中哪一个tab
    private fun setSelectIndex(@androidx.annotation.IntRange(from = 0, to = 2) index: Int) {
        val selectId = when (index) {
            1 -> R.id.menu_main_dyn
            2 -> R.id.menu_main_mine
            else -> R.id.menu_main_home
        }
        mainNavigation?.post {
            if (mainNavigation.selectedItemId != selectId) mainNavigation.selectedItemId = selectId
        }
    }


}