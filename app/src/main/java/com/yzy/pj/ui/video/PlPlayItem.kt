package com.yzy.pj.ui.video

import android.view.View
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.pj.R

@EpoxyModelClass(layout = R.layout.pl_item)
abstract class PlPlayItem : BaseEpoxyModel<BaseEpoxyHolder>() {
    override fun onBind(itemView: View) {
        
    }
}