package com.yzy.example.component.tree.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.extention.randomColor
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.ClassifyBean


class SystemChildAdapter(data: ArrayList<ClassifyBean>) :
    BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.flow_layout, data) {

//    init {
//        setAdapterAnimion(SettingUtil.getListMode())
//    }

    override fun convert(holder: BaseViewHolder, item: ClassifyBean) {
        holder.setText(R.id.flow_tag, item.name.toHtml())
        holder.setTextColor(R.id.flow_tag,randomColor())
    }

}