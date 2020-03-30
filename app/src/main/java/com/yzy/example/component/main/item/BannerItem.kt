package com.yzy.example.component.main.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.BannerBean
import kotlinx.android.synthetic.main.item_banner.view.*


/**
 * Description:
 * @author: YZY
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
            itemBanner.listSize = data.size
            val bannerAdapter = ViewPagerAdapter(data)
            itemBanner.setAdapter(bannerAdapter)
            itemBanner.mViewPager2?.setPageTransformer(CompositePageTransformer())
            itemBanner.setAutoTurning(3000L)
        }
    }

    private class ViewPagerAdapter(var list: MutableList<BannerBean>) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

        internal inner class ViewPagerViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var mContainer: ImageView = itemView.findViewById(R.id.itemBannerIV)

        }

        override fun getItemCount(): Int {
            return if (list.size <= 1) 1 else Integer.MAX_VALUE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
            return ViewPagerViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_banner_child, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
            holder.mContainer.tag = list[position % list.size]
            holder.mContainer.load(list[position % list.size].imagePath)

        }
    }
}


