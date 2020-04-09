package com.yzy.example.component.main.item

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.yzy.example.R
import com.yzy.example.databinding.ItemArticleListBinding
import com.yzy.example.repository.bean.ArticleBean

/**
 *   @auther : Aleyn
 *   time   : 2019/11/08
 */
class HomeListAdapter : BaseQuickAdapter<ArticleBean, BaseDataBindingHolder<ItemArticleListBinding>>(R.layout.item_article_list) {
    override fun convert(holder: BaseDataBindingHolder<ItemArticleListBinding>, item: ArticleBean) {
        holder.dataBinding?.itemBean =item
        holder.dataBinding?.executePendingBindings()
    }

//    override fun convert(helper: BaseDataBindingHolder<ItemArticleListBinding>, item: ArticleBean) {
//        helper.dataBinding?.itemBean =item
//        helper.dataBinding?.executePendingBindings()
//    }
//
//    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
//        val binding = DataBindingUtil.inflate<ItemArticleListBinding>(parent., layoutResId, parent, false) ?: return super.getItemView(layoutResId, parent)
//        return binding.root.apply { setTag(R.id.BaseQuickAdapter_databinding_support, binding)
//        }
//    }
//
//
//    override fun convert(helper: BaseViewHolder, item: ArticleBean) {
//        with(helper) {
//            setText(R.id.tv_project_list_atticle_type, item.chapterName)
//            setText(R.id.tv_project_list_atticle_title, item.title)
//            setText(R.id.tv_project_list_atticle_time, item.niceDate)
//            setText(R.id.tv_project_list_atticle_auther, item.author)
//            val imageView = helper.getView<ImageView>(R.id.iv_project_list_atticle_ic)
//            if (!item.envelopePic.isNullOrEmpty()) {
//                Glide.with(imageView).load(item.envelopePic).into(imageView)
//            }
//        }
//    }

}