package com.yzy.example.component

import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.home.HomeFragment
import com.yzy.example.component.project.ProjectFragment
import com.yzy.example.component.publicNumber.PublicNumberFragment
import com.yzy.example.component.tree.NavigationFragment
import com.yzy.example.component.tree.SystemFragment
import com.yzy.example.databinding.FragmentMainBinding
import com.yzy.example.repository.model.MainViewModel

class MainFragment : CommFragment<MainViewModel, FragmentMainBinding>() {
    var fragments = arrayListOf<Fragment>()
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val projectFragment: ProjectFragment by lazy { ProjectFragment() }
    private val systemFragment: SystemFragment by lazy { SystemFragment() }
    private val navigationFragment: NavigationFragment by lazy { NavigationFragment() }
    private val publicNumberFragment: PublicNumberFragment by lazy { PublicNumberFragment() }

    init {
        fragments.apply {
            add(homeFragment)
            add(projectFragment)
            add(systemFragment)
            add(navigationFragment)
            add(publicNumberFragment)
        }
    }

    override fun initContentView() {
        if (viewModel.loadPosition() == -1){
            selectFragment(0)
        }else{
            selectFragment(viewModel.loadPosition())
        }
//      val view=  navigationDraw.getHeaderView(0)
//        MMkvUtils.instance.getPersonalBean()?.let {
//            viewModel.name.postValue(if (it.nickname.isEmpty()) it.username else it.nickname)
//        }
//        viewModel.meData.observe(viewLifecycleOwner, Observer {
//            if (it.isSuccess) {
//                viewModel.info.postValue("id：${it?.data?.userId}　排名：${it?.data?.rank}")
//                viewModel.integral.postValue(it?.data?.coinCount)
//            } else {
//                if (it.errCode == -1001){
//                    initLoginDialog(childFragmentManager){
//                        mainToLogin ={
//                            nav().navigate(MainFragmentDirections.actionMainFragmentToLoginFragment())
//                        }
//                    }
//                }
//            }
//        })
//        viewModel.getIntegral()
//        viewModel.name.observe(viewLifecycleOwner, Observer {
//            view.me_name.text=it
//        })
//
//        viewModel.info.observe(viewLifecycleOwner, Observer {
//            view.me_info.text=it
//        })
//        mainNavigation.run {
//            setOnNavigationItemSelectedListener {
//                when (it.itemId) {
//                    R.id.menu_main -> selectFragment(0)
//                    R.id.menu_project -> selectFragment(1)
//                    R.id.menu_system -> selectFragment(2)
//                    R.id.menu_navigation -> selectFragment(3)
//                    R.id.menu_public -> selectFragment(4)
//
//                }
//                true
//            }
//        }
//        navigationDraw.itemIconTintList = null
//
//        navigationDraw.setNavigationItemSelectedListener {
//            // 关闭侧边栏
//            drawer.close()
//            when (it.itemId) {
//                R.id.nav_menu_rank -> {
//                }
//                R.id.nav_menu_square -> {
//                    nav().navigate(MainFragmentDirections.actionMainFragmentToPlazaFragment())
//                }
//                R.id.nav_menu_collect -> {
//                    nav().navigate(MainFragmentDirections.actionMainFragmentToCollectFragment())
//                }
//
//                R.id.nav_menu_question -> {
//                    nav().navigate(MainFragmentDirections.actionMainFragmentToAskFragment())
//                }
//
//                R.id.nav_menu_theme -> {
//
//                    MaterialDialog(requireContext()).show {
//                        title(R.string.theme_color)
//                        cornerRadius(16.0f)
//                        colorChooser(
//                            ColorUtil.ACCENT_COLORS,
//                            initialSelection = ColorUtil.getColor(requireContext()),
//                            subColors = ColorUtil.PRIMARY_COLORS_SUB
//                        ) { dialog, color ->
//                            ColorUtil.setColor(color)
////                            ChangeThemeEvent().post()
//                        }
//                        positiveButton(R.string.done)
//                        negativeButton(R.string.cancel)
//                    }
//                }
//                R.id.nav_menu_add -> {
//                    joinQQGroup("1nLU15GhxIe9MT3cM6djdKEDNIjwqUK6")
//                }
//                R.id.nav_menu_setting -> {
//                    nav().navigate(MainFragmentDirections.actionMainFragmentToSettingFragment())
//                }
//                R.id.nav_menu_logout -> {
//                }
//            }
//            true
//        }
    }

    //当前页面
    private var currentFragment: Fragment? = null
    private var currentPosition: Int = 0

    //设置选中的fragment
    private fun selectFragment(@IntRange(from = 0, to = 4) index: Int) {
//        //需要显示的fragment
//        val fragment = fragments[index]
//        //和当前选中的一样，则不再处理
//        if (currentFragment == fragment) return
//        currentPosition=index
//        drawer.setDrawerLockMode( if ( index == 0) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//        //先关闭之前显示的
//        currentFragment?.let { FragmentUtils.hide(it) }
//        //设置现在需要显示的
//        currentFragment = fragment
//        if (!fragment.isAdded) { //没有添加，则添加并显示
//            val tag = fragment::class.java.simpleName
//            FragmentUtils.add(childFragmentManager, fragment, mainContainer.id, tag, false)
//        } else { //添加了就直接显示
//            FragmentUtils.show(fragment)
//        }
    }


    override fun getLayoutId(): Int = R.layout.fragment_main
    override fun onDestroyView() {
        viewModel.setValue(currentPosition)
        super.onDestroyView()
    }
}