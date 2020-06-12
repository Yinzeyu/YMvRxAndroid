package com.yzy.example.component.dialog

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.repository.bean.ColorBean
import com.yzy.example.widget.ColorCircleView


class ColorGridAdapter : BaseMultiItemQuickAdapter<ColorBean< Int>, BaseViewHolder>() {

    override fun convert(holder: BaseViewHolder, item: ColorBean< Int>) {
        when (holder.itemViewType) {
            ColorBean.COLOR_BACK->{

            }

            ColorBean.COLOR_LIST->{
                val bean = item.bean
                holder.getView<ColorCircleView>(R.id.color_view).color =bean
            }
        }
    }

    init {
        addItemType(ColorBean.COLOR_BACK, R.layout.md_color_grid_item_go_up)
        addItemType(ColorBean.COLOR_LIST, R.layout.md_color_grid_item)
    }
}