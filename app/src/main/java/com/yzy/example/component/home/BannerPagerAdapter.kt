package com.yzy.example.component.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yzy.example.component.home.BannerFragment
import com.yzy.example.repository.bean.BannerBean

class BannerPagerAdapter(fragment: FragmentActivity, var list: MutableList<BannerBean>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =  if (list.size <= 1) 1 else Integer.MAX_VALUE
    override fun createFragment(position: Int): Fragment = BannerFragment.newInstance(list[position % list.size])
}