package com.yzy.example.component.main

import androidx.fragment.app.Fragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.me.MineFragment
import com.yzy.example.databinding.FragmentMainBinding
import com.yzy.example.extention.init
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : CommFragment<NoViewModel,FragmentMainBinding>() {

    var fragments = arrayListOf<Fragment>()
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val projectFragment: ProjectFragment by lazy { ProjectFragment() }
    private val treeArrFragment: TreeArrFragment by lazy { TreeArrFragment() }
    private val publicNumberFragment: PublicNumberFragment by lazy { PublicNumberFragment() }
    private val meFragment: MineFragment by lazy { MineFragment() }

    init {
        fragments.apply {
            add(homeFragment)
            add(projectFragment)
            add(treeArrFragment)
            add(publicNumberFragment)
            add(meFragment)
        }
    }
    override fun initContentView() {
 //初始化viewpager2
        mainViewpager.init(this,fragments,false).run {
//            offscreenPageLimit = fragments.size
        }
        //初始化 bottombar
        mainNavigation.run {
//            enableAnimation(false)
//            enableShiftingMode(false)
//            enableItemShiftingMode(false)
//            shareViewModel.appColor.value.let {
//                itemIconTintList = SettingUtil.getColorStateList(it)
//                itemTextColor = SettingUtil.getColorStateList(it)
//            }
//            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                    R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
                    R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
                    R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
                    R.id.menu_me -> mainViewpager.setCurrentItem(4, false)
                }
                true
            }
        }
    }



    override fun getLayoutId(): Int =R.layout.fragment_main

}