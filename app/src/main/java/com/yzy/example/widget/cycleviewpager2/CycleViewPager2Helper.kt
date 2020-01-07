package com.yzy.example.widget.cycleviewpager2

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.yzy.example.widget.CycleViewPager
import com.yzy.example.widget.cycleviewpager2.indicator.Indicator

/**
 * Created by wangpeiyuan on 2019-12-03.
 */
class CycleViewPager2Helper(
    private val cycleViewPager2: CycleViewPager) {
    var adapter: RecyclerView.Adapter<*>? = null
        set(value) {
            cycleViewPager2.adapter = value
            field = value
        }
    @ViewPager2.Orientation
    var orientation = ViewPager2.ORIENTATION_HORIZONTAL
    @OffscreenPageLimit
    var autoTurningTime: Long = 0
        set(value) {
        cycleViewPager2.setAutoTurning(value)
    }
    fun addPageTransformer(pageTransformer: ViewPager2.PageTransformer) =
        cycleViewPager2.setPageTransformer(pageTransformer)


    var listSize: Int = 0
        set(value) {
            cycleViewPager2.listSize=value
            field = value
        }
    fun addindicator(   indicator: Indicator) =
        cycleViewPager2.setIndicator(indicator)


    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) = cycleViewPager2.addItemDecoration(itemDecoration)

    fun registerOnPageChangeCallback(callback: OnPageChangeCallback) =
        cycleViewPager2.registerOnPageChangeCallback(callback)

}