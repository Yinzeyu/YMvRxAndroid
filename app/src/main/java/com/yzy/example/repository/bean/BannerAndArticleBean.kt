package com.yzy.example.repository.bean


/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/13 18:10
 */
data class BannerAndArticleBean(
    var bannerBean: MutableList<BannerBean> = mutableListOf(),
    var articleBean: MutableList<ArticleBean> = mutableListOf(),
    var hasMore: Boolean = false,
    val exception: Exception? = null
    )
