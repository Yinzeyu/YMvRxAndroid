package com.yzy.example.ui

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.load
import com.yzy.commonlibrary.repository.bean.BannerBean
import com.yzy.example.R
import kotlinx.android.synthetic.main.main_item.view.*

@EpoxyModelClass(layout = R.layout.main_item)
abstract class AtMeMessageItem : BaseEpoxyModel<BaseEpoxyHolder>() {

    @EpoxyAttribute
    lateinit var messageBean: BannerBean

    override fun onBind(itemView: View) {
        super.onBind(itemView)
        itemView.iv_item_bg.load(messageBean.imagePath, 0)
    }
}