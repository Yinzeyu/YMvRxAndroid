package com.yzy.example.component.main

import android.content.Context
import android.widget.ImageView
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.repository.bean.GankAndroidBean
import com.yzy.example.repository.bean.PicBean
import com.yzy.example.widget.ninegridview.ItemClickListener
import com.yzy.example.widget.ninegridview.NineGridAdapter
import com.yzy.example.widget.ninegridview.NineGridView

class DynAdapter : BaseQuickAdapter<GankAndroidBean, BaseViewHolder>(R.layout.item_gank_android) ,
    LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: GankAndroidBean) {
        //发布者
        holder.setText(R.id.itemGankAndroidUser, item.who)
        //发布时间
        holder.setText(R.id.itemGankAndroidTime, item.publishTime)
        //内容
        holder.setText(R.id.itemGankAndroidDes, item.desc)
        //图片
        val nieView = holder.getView<NineGridView<PicBean>>(R.id.itemGankAndroidNine)
        nieView.isVisible=!item.images.isNullOrEmpty()
        if (!item.images.isNullOrEmpty()) {
            setMultiImages(item.urlImgs, nieView)
        }

        //item点击
//        itemView.click { onItemClick?.invoke(dataBean) }
        //item按压效果
//        itemView.pressEffectBgColor()
    }
    @Suppress("UNCHECKED_CAST")
    private fun setMultiImages(
        picBeans: MutableList<PicBean>,
        nineGridView: NineGridView<PicBean>
    ) {
        nineGridView.mAdapter=NineGridAdapter()
        nineGridView.setImagesData(picBeans)
        nineGridView.mItemClickListener=object :ItemClickListener<PicBean> {
            override fun onItemClick(
                context: Context,
                imageView: ImageView,
                index: Int,
                list: List<PicBean>
            ) {
            }

        }
//        nineGridView.setImageClickListener { _, imageView, index, list ->
//            BrowserUtils.instance.show(picStr as ArrayList<String>, index)
//        }
    }
}