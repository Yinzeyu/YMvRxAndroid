package com.yzy.example.repository.model

import com.airbnb.mvrx.*
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.base.MvRxViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleBean
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.BannerBean
import io.reactivex.functions.BiFunction
import org.kodein.di.generic.instance

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/9/30 16:36
 */
data class HomeState(
        val banners: MutableList<BannerBean>? = mutableListOf(),
        var hasMore: Boolean = false,
        val articles: MutableList<ArticleBean> = mutableListOf(),
        var request: Async<Any> = Uninitialized
) : MvRxState

class HomeViewModel(
        state: HomeState = HomeState()
) : MvRxViewModel<HomeState>(state) {
    private var page = 0
    private val ganRepository: GankRepository by BaseApplication.getApp().kodein.instance()

    //刷新数据
    fun refreshData() = withState { state ->
        if (state.request is Loading) return@withState
        val article = ganRepository.article(0)
        ganRepository.banner().zipWith(article, BiFunction<MutableList<BannerBean>, ArticleDataBean,
                Pair<MutableList<BannerBean>, ArticleDataBean>> { t1, t2 ->
            Pair(t1, t2)
        }).execute {
            val suc = it is Success
            if (suc) page = 0
            val pair = it.invoke()
            val bannerList = pair?.first ?: mutableListOf()
            val articleList = pair?.second?.datas ?: mutableListOf()
            val hasMore = pair?.second?.datas?.isNullOrEmpty() == false
            copy(
                    banners = if (suc) bannerList else state.banners,
                    hasMore = if (suc) hasMore else state.hasMore,
                    articles = if (suc) articleList else state.articles,
                    request = it
            )
        }

    }

    //加载更多
    fun loadMoreData() = withState { state ->
        if (state.request is Loading) return@withState
        ganRepository.article(page + 1)
                .execute {
                    val suc = it is Success
                    if (suc) page += 1
                    val articleList = it.invoke()?.datas ?: mutableListOf()
                    copy(
                            articles = if (suc) (state.articles + articleList).toMutableList() else state.articles,
                            request = it
                    )
                }
    }
}