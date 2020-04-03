package com.yzy.example.component.playlist.viewmoel

import androidx.lifecycle.viewModelScope
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.bean.VideoBean
import com.yzy.example.utils.VideoRandomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayPagerViewModel : BaseViewModel() {
    private var articleBean: MutableList<VideoBean> = mutableListOf()
    private val _bannerAndArticleResult: BaseLiveData<BaseUiModel<MutableList<VideoBean>>> =
        BaseLiveData()
    val uiState: BaseLiveData<BaseUiModel<MutableList<VideoBean>>> get() = _bannerAndArticleResult
    fun getVideoList(isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            if (isRefresh) {
                articleBean.clear()
                articleBean.addAll(VideoRandomUtils.instance.getVideoList())
            } else {
                articleBean.addAll(
                    VideoRandomUtils.instance.getVideoList(
                        idStart = articleBean.size.toLong(),
                        count = 14
                    )
                )
            }

            emitArticleUiState(
                showLoading = false,
                showSuccess = articleBean
            )
        }
    }

    private fun emitArticleUiState(
        showLoading: Boolean = false,
        showSuccess: MutableList<VideoBean>? = null
    ) {
        val uiModel = BaseUiModel(showLoading, showSuccess)
        _bannerAndArticleResult.update(uiModel)
    }
}