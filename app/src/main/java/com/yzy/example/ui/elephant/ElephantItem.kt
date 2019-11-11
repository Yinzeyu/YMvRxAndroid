package com.yzy.example.ui.elephant

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.load
import com.yzy.example.repository.bean.FuliBean
import com.yzy.example.R
import kotlinx.android.synthetic.main.main_item.view.*

@EpoxyModelClass(layout = R.layout.item)
abstract class ElephantItem : BaseEpoxyModel<BaseEpoxyHolder>() {

    @EpoxyAttribute
    lateinit var messageBean: FuliBean

    override fun onBind(itemView: View) {
        super.onBind(itemView)
        itemView.iv_item_bg.load(messageBean.url, 0)
    }
}