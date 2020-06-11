package com.yzy.example.component.tree

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.example.R
import com.yzy.example.component.web.WebsiteDetailFragmentArgs
import com.yzy.example.databinding.FragmentSystemArrBinding
import com.yzy.example.extention.bindViewPager2
import com.yzy.example.extention.init
import com.yzy.example.repository.bean.SystemBean
import com.yzy.example.repository.model.TreeViewModel
import kotlinx.android.synthetic.main.fragment_system_arr.*

class SystemArrFragment : BaseFragment<TreeViewModel, FragmentSystemArrBinding>() {
    private val systemArrFragmentArgs: SystemArrFragmentArgs by navArgs()

    var index = 0

    private var fragments: ArrayList<Fragment> = arrayListOf()


    override fun initView(savedSate: Bundle?)  {
//
//        arguments?.let {
//            data = it.getParcelable("data")!!
//            index = it.getInt("index")
//        }

        systemArrFragmentArgs.dataBean.children.forEach {
            fragments.add(SystemChildFragment.newInstance(it.id))
        }
        //初始化viewpager2
        systemArrViewPager.init(this, fragments)
        //初始化 magic_indicator
        systemArrMagicIndicator.bindViewPager2(systemArrViewPager,  systemArrFragmentArgs.dataBean.children)
        systemArrViewPager.currentItem = systemArrFragmentArgs.index
    }

    override fun getLayoutId(): Int  = R.layout.fragment_system_arr
}