package com.yzy.pj.ui

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.pj.R
import kotlinx.android.synthetic.main.item_comm_loadmore.view.*

/**
 *description: 加载更多的Item.
 *@date 2019/5/10 12:05.
 *@author: YangYang.
 */
@EpoxyModelClass(layout = R.layout.item_comm_loadmore)
abstract class LoadMoreItem : BaseEpoxyModel<BaseEpoxyHolder>() {

    //提示的文字，没有默认提示 默认为："数据加载中..."
    @EpoxyAttribute
    var tipsText: CharSequence? = null

    //提示TextView的样式
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var textStyle: (TextView.() -> Unit)? = null

    //提示ProgressBar的样式
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var progressBarStyle: (ProgressBar.() -> Unit)? = null

    //加载更多的回调
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onLoadMore: (() -> Unit)? = null

    override fun onBind(itemView: View) {
        itemView.loadMoreTv.text = tipsText
        textStyle?.let {
            itemView.loadMoreTv.apply(it)
        }
        progressBarStyle?.let {
            itemView.loadMorePb.apply(it)
        }
    }

    override fun onVisibilityChanged(
            percentVisibleHeight: Float,
            percentVisibleWidth: Float,
            visibleHeight: Int,
            visibleWidth: Int,
            view: BaseEpoxyHolder
    ) {
        super.onVisibilityChanged(
                percentVisibleHeight,
                percentVisibleWidth,
                visibleHeight,
                visibleWidth,
                view
        )
        if (percentVisibleHeight == 100F) {
            //加载更多完全显示出来
            onLoadMore?.invoke()
        }
    }

}