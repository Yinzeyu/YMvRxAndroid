package com.yzy.example.repository.model

import androidx.lifecycle.viewModelScope
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleBean
import com.yzy.example.repository.bean.BannerAndArticleBean
import com.yzy.example.repository.bean.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewGankViewModel : BaseViewModel() {
    private var page = 0
    private var pageSize = 20
    private val ganRepository: GankRepository by lazy { GankRepository() }
   private var articleBean: MutableList<ArticleBean> = mutableListOf()
    private val _bannerAndArticleResult: BaseLiveData<BaseUiModel<BannerAndArticleBean>> = BaseLiveData()
    val uiState: BaseLiveData<BaseUiModel<BannerAndArticleBean>> get() = _bannerAndArticleResult
    fun getBanner( isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            emitArticleUiState(showLoading = articleBean.size <= 0)
            if (isRefresh) page = 0
            val result = withContext(Dispatchers.IO) { ganRepository.banner(1) }
            val article = withContext(Dispatchers.IO) { ganRepository.article(page) }
            if (result is DataResult.Success && article is DataResult.Success){
                if (isRefresh){
                    articleBean.clear()
                }
                articleBean.addAll(article.data.datas ?: mutableListOf())
                emitArticleUiState(
                    showLoading = false,
                    showSuccess = BannerAndArticleBean(result.data, articleBean,
                        hasMore=(article.data.datas ?: mutableListOf()).size == pageSize
                    )
                )
                page++
            } else if (result is DataResult.Error){
                emitArticleUiState(showLoading = false, showError = result.exception.message)
            }

        }
    }

    private fun emitArticleUiState(
        showLoading: Boolean = false,
        showError: String? = null,
        showSuccess: BannerAndArticleBean? = null
    ) {
        val uiModel = BaseUiModel(showLoading, showError, showSuccess)
        _bannerAndArticleResult.update(uiModel)
    }
}

