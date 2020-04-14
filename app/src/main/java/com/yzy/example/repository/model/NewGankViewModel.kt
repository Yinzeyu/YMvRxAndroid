package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.base.ThrowableBean
import com.yzy.baselibrary.http.ExceptionHandle
import com.yzy.baselibrary.http.ResponseThrowable
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.BannerBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class NewGankViewModel : BaseViewModel<GankRepository>() {
    private var page = 0
    private var pageSize = 20
    private var articleBean: MutableList<ArticleBean> = mutableListOf()
    var uiState = MutableLiveData<BannerAndArticleBean>()
    @ExperimentalCoroutinesApi
    @FlowPreview
     fun getBanner(isRefresh: Boolean = false) :MutableLiveData<BannerAndArticleBean>{
        if (isRefresh) {
            page = 0
            articleBean.clear()
        }
        var bannerBean: MutableList<BannerBean> = mutableListOf()
        launchUI {
            launchFlow { repository.banner(1) }
                .flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        bannerBean = it.data
                        launchFlow { repository.article(page) }
                    } else throw ResponseThrowable(it.code(), it.msg())
                }
                .onStart {defUI.showDialog.postValue(null)}
                .flowOn(Dispatchers.IO)
                .onCompletion {defUI.dismissDialog.call() }
                .catch {
                    // 错误处理
                    val err = ExceptionHandle.handleException(it)
                    defUI.errorEvent.postValue(ThrowableBean(err.code,err.errMsg))
                }
                .collect {
                    articleBean.addAll(it.data.datas ?: mutableListOf())
                    val bannerAndArticleBean = BannerAndArticleBean(
                        bannerBean,
                        articleBean,
                        hasMore = (it.data.datas ?: mutableListOf()).size == pageSize
                    )
                    uiState.value=bannerAndArticleBean
                    page++
                }
        }
        return uiState
    }
}

