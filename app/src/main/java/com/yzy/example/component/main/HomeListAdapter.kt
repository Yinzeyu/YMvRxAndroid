package com.yzy.example.component.main

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.repository.bean.ArticleBean

/**
 *   @auther : yzy
 *   time   : 2019/11/08
 */
class HomeListAdapter : BaseQuickAdapter<ArticleBean, BaseViewHolder>(R.layout.item_article_list) ,
    LoadMoreModule {
    override fun convert(holder: BaseViewHolder,item: ArticleBean) {
        holder.setText(R.id.itemArticleUser,item.showAuthor)
        holder.setText(R.id.itemArticleTime,item.showTime)
        holder.setText(R.id.itemArticleType,item.showTime)
        holder.setText(R.id.itemArticleDes,item.showInfo)
    }
}