package com.yzy.example.comm.viewPager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerFragmentStateAdapter(fragment: Fragment, var list: MutableList<Fragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size
    override fun createFragment(position: Int): Fragment = list[position]
}