package com.yzy.example.component.collect.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.CollectUrlBean
import com.yzy.example.widget.CollectView


class CollectUrlAdapter(data: ArrayList<CollectUrlBean>) :
    BaseQuickAdapter<CollectUrlBean, BaseViewHolder>(
        R.layout.item_collecturl, data
    ) {

    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null

//    init {
//        setAdapterAnimion(SettingUtil.getListMode())
//    }
    override fun convert(holder: BaseViewHolder, item: CollectUrlBean) {
        //赋值
        item.run {
            holder.setText(R.id.item_collecturl_name, name.toHtml())
            holder.setText(R.id.item_collecturl_link, link)
            holder.getView<CollectView>(R.id.item_collecturl_collect).isChecked = true
        }
        holder.getView<CollectView>(R.id.item_collecturl_collect)
            .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                override fun onClick(v: CollectView) {
                    mOnCollectViewClickListener?.onClick(item, v, holder.adapterPosition)
                }
            })
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(item: CollectUrlBean, v: CollectView, position: Int)
    }
}


