package com.yzy.example.component.main.item

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectBgColor
import com.yzy.example.R
import com.yzy.example.repository.bean.ArticleBean
import kotlinx.android.synthetic.main.item_wan_article.view.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/13 19:38
 */
@EpoxyModelClass(layout = R.layout.item_wan_article)
abstract class WanArticleItem : BaseEpoxyModel<BaseEpoxyHolder>() {
    //数据源
    @EpoxyAttribute
    var dataBean: ArticleBean? = null
    //点击item
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onItemClick: ((bean: ArticleBean?) -> Unit)? = null

    override fun onBind(itemView: View) {
//        dataBean?.let {
//            //作者
//            itemView.itemArticleUser.text = it.showAuthor
//            //时间
//            itemView.itemArticleTime.text = it.showTime
//            //类型
//            itemView.itemArticleType.text = it.showType
//            //标题+描述
//            itemView.itemArticleDes.text = it.showInfo
//        }
        //item点击
        itemView.click { onItemClick?.invoke(dataBean) }
        //item按压效果
        itemView.pressEffectBgColor()
    }
}