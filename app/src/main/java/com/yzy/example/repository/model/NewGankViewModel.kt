package com.yzy.example.repository.model

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.http.ExceptionHandle
import com.yzy.baselibrary.http.ResponseThrowable
import com.yzy.baselibrary.http.event.SingleLiveEvent
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.BannerBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class NewGankViewModel : BaseViewModel() {
    private var page = 0
    private var pageSize = 20
    private val ganRepository: GankRepository by lazy { GankRepository() }
    private var articleBean: MutableList<ArticleBean> = mutableListOf()
    private val _bannerAndArticleResult: BaseLiveData<BaseUiModel<BannerAndArticleBean>> = BaseLiveData()
    val uiState: BaseLiveData<BaseUiModel<BannerAndArticleBean>> get() = _bannerAndArticleResult
    @ExperimentalCoroutinesApi
    @FlowPreview
     fun getBanner(isRefresh: Boolean = false) {
        if (isRefresh) {
            page = 0
            articleBean.clear()
        }
        var bannerBean: MutableList<BannerBean> = mutableListOf()
        launchUI {
            launchFlow { ganRepository.banner(1) }
                .flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        bannerBean = it.data
                        launchFlow { ganRepository.article(page) }
                    } else throw ResponseThrowable(it.code(), it.msg())
                }
                .onStart { emitArticleUiState(showLoading = articleBean.size <= 0) }
                .flowOn(Dispatchers.IO)
                .onCompletion { emitArticleUiState(showLoading = false) }
                .catch {
                    // 错误处理
                    val err = ExceptionHandle.handleException(it)
                    LogUtils.d("${err.code}: ${err.errMsg}")
                }
                .collect {
                    articleBean.addAll(it.data.datas ?: mutableListOf())
                    val bannerAndArticleBean = BannerAndArticleBean(
                        bannerBean,
                        articleBean,
                        hasMore = (it.data.datas ?: mutableListOf()).size == pageSize
                    )
                    emitArticleUiState(
                        showLoading = false,
                        isRefresh = false,
                        success = bannerAndArticleBean
                    )
                    page++
                }
        }
    }
    private fun emitArticleUiState(
        showLoading: Boolean = false,
        isRefresh: Boolean = false,
        success: BannerAndArticleBean? = null
    ) {
        val uiModel = BaseUiModel(showLoading =showLoading, success = success)
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _bannerAndArticleResult.value=uiModel
            }
        }
    }
}

