package com.yzy.example.component.dialog

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.example.extention.load
import com.yzy.example.R
import com.yzy.example.repository.bean.BannerBean
import kotlinx.android.synthetic.main.main_item.view.*

@EpoxyModelClass(layout = R.layout.main_item)
abstract class AtMeMessageItem : BaseEpoxyModel<BaseEpoxyHolder>() {

    @EpoxyAttribute
    lateinit var messageBean: BannerBean

    override fun onBind(itemView: View) {
        super.onBind(itemView)
//        itemView.iv_item_bg.load(messageBean.imagePath, 0)
    }
}