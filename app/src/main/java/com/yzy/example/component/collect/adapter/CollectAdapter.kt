package com.yzy.example.component.collect.adapter

import android.text.TextUtils
import android.widget.ImageView
import coil.api.load
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yzy.example.R
import com.yzy.example.extention.loadUrl
import com.yzy.example.extention.toHtml
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.widget.CollectView


class CollectAdapter(data: ArrayList<ArticleDataBean>) :
    BaseDelegateMultiAdapter<ArticleDataBean, BaseViewHolder>(data) {
    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null
    private val Ariticle = 1//文章类型
    private val Project = 2//项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧

    init {

//        setAdapterAnimion(SettingUtil.getListMode())

        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ArticleDataBean>() {
            override fun getItemType(data: List<ArticleDataBean>, position: Int): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    override fun convert(holder: BaseViewHolder, item: ArticleDataBean) {
        when (holder.itemViewType) {
            Ariticle -> {
                //文章布局的赋值
                item.run {
                    holder.setText(R.id.item_home_author, if (author.isEmpty()) "匿名用户" else author)
                    holder.setText(R.id.item_home_content, title.toHtml())
                    holder.setText(R.id.item_home_type2, chapterName.toHtml())
                    holder.setText(R.id.item_home_date, niceDate)
                    holder.getView<CollectView>(R.id.item_home_collect).isChecked = true
                    //隐藏所有标签
                    holder.setGone(R.id.item_home_top, true)
                    holder.setGone(R.id.item_home_type1, true)
                    holder.setGone(R.id.item_home_new, true)
                }
                holder.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(item, v, holder.adapterPosition)
                        }
                    })
            }
            Project -> {
                //项目布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_project_author,
                        if (author.isEmpty()) "匿名用户" else author
                    )
                    holder.setText(R.id.item_project_title, title.toHtml())
                    holder.setText(R.id.item_project_content, desc.toHtml())
                    holder.setText(R.id.item_project_type, chapterName.toHtml())
                    holder.setText(R.id.item_project_date, niceDate)
                    //隐藏所有标签
                    holder.setGone(R.id.item_project_top, true)
                    holder.setGone(R.id.item_project_type1, true)
                    holder.setGone(R.id.item_project_new, true)
                    holder.getView<CollectView>(R.id.item_project_collect).isChecked = true
                    holder.getView<ImageView>(R.id.item_project_imageview).loadUrl(envelopePic)
//                    Glide.with(context.applicationContext).load(envelopePic)
//                        .transition(DrawableTransitionOptions.withCrossFade(500))
//                        .into()
                }
                holder.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(item, v, holder.adapterPosition)
                        }
                    })
            }
        }


    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }


    interface OnCollectViewClickListener {
        fun onClick(item: ArticleDataBean, v: CollectView, position: Int)
    }
}


