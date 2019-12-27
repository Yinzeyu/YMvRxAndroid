package com.yzy.example.component.main.item

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import com.airbnb.epoxy.EpoxyAdapter
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectBgColor
import com.yzy.example.R
import com.yzy.example.component.main.BannerPagerAdapter
import com.yzy.example.repository.bean.BannerBean
import com.yzy.example.widget.cycleviewpager2.transformer.ScaleInTransformer
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
        val vertical = System.currentTimeMillis() % 2 == 0L
        dataList?.let { data ->
            //防止每次滑出屏幕再滑入后重新创建
            val tag = data.hashCode() + data.size
            if (itemView.tag == tag) return@let
            itemView.tag = tag
            val itemBanner = itemView.itemBanner
            val bannerAdapter = BannerPagerAdapter((itemView.context as BaseActivity), data)
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(ScaleInTransformer())
            compositePageTransformer.addTransformer(MarginPageTransformer(SizeUtils.dp2px(10f)))
            itemBanner.setPageTransformer(compositePageTransformer)
            itemBanner.adapter = bannerAdapter
            itemBanner.apply {
                offscreenPageLimit = 1
                val recyclerView = getChildAt(0) as RecyclerView
                recyclerView.apply {
                    val padding = SizeUtils.dp2px(20f)
                    setPadding(padding, 0, padding, 0)
                    clipToPadding = false
                }
            }
        }
    }
}