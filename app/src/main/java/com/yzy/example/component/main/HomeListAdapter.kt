package com.yzy.example.component.main

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.yzy.example.BR
import com.yzy.example.R
import com.yzy.example.databinding.ItemArticleListBinding
import com.yzy.example.repository.bean.ArticleBean

/**
 *   @auther : Aleyn
 *   time   : 2019/11/08
 */
class HomeListAdapter : BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<ItemArticleListBinding>>(R.layout.item_article_list) {
    override fun convert(holder: BaseDataBindingHolder<ItemArticleListBinding>, item: ArticleBean) {
        holder.dataBinding?.setVariable(BR.itemBean, item);
        holder.dataBinding?.executePendingBindings()
    }
}