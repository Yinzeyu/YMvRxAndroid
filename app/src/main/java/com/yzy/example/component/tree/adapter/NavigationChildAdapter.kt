package com.yzy.example.component.tree.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.extention.randomColor
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.ArticleDataBean


class NavigationChildAdapter(data: ArrayList<ArticleDataBean>) :
    BaseQuickAdapter<ArticleDataBean, BaseViewHolder>(R.layout.flow_layout, data) {

//    init {
//        setAdapterAnimion(SettingUtil.getListMode())
//    }

    override fun convert(holder: BaseViewHolder, item: ArticleDataBean) {
        holder.setText(R.id.flow_tag,item.title.toHtml())
        holder.setTextColor(R.id.flow_tag, randomColor())
    }

}