package com.yzy.example.component.main.item

import android.graphics.Color
import android.view.View
import androidx.viewpager2.widget.CompositePageTransformer
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.example.R
import com.yzy.example.component.main.BannerPagerAdapter
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.widget.cycleviewpager2.CycleViewPager2Helper
import com.yzy.example.widget.cycleviewpager2.indicator.DotsIndicator
import kotlinx.android.synthetic.main.item_banner.view.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/13 19:11
 */
@EpoxyModelClass(layout = R.layout.item_banner)
abstract class BannerItem : BaseEpoxyModel<BaseEpoxyHolder>() {
    //数据源
    @EpoxyAttribute
    var dataList: MutableList<BannerBean>? = null

    override fun onBind(itemView: View) {
        //随机横竖屏切换
        dataList?.let { data ->
            //防止每次滑出屏幕再滑入后重新创建
            val tag = data.hashCode() + data.size
            if (itemView.tag == tag) return@let
            itemView.tag = tag
            val itemBanner = itemView.itemBanner
            val dotsIndicator = DotsIndicator(itemView.context)
            dotsIndicator.setRadius(SizeUtils.dp2px(4f))
            dotsIndicator.setSelectedColor(Color.RED)
            dotsIndicator.setUnSelectedColor( Color.WHITE)
            dotsIndicator.setDotsPadding(SizeUtils.dp2px(4f))
            dotsIndicator.setLeftMargin(0)
            dotsIndicator.setBottomMargin(SizeUtils.dp2px(5f))
            dotsIndicator.setRightMargin(0)
            dotsIndicator.setDirection( DotsIndicator.Direction.CENTER)
            val cycleViewPager2Helper = CycleViewPager2Helper(itemBanner)
            cycleViewPager2Helper.adapter= BannerPagerAdapter((itemView.context as BaseActivity), data)
            cycleViewPager2Helper .addPageTransformer( CompositePageTransformer())
            cycleViewPager2Helper .listSize=data.size
            cycleViewPager2Helper.addindicator(dotsIndicator)
            cycleViewPager2Helper.autoTurningTime=3000L

        }
    }
}