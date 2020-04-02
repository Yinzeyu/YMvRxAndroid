package com.yzy.example.component.playlist.viewmoel

import androidx.lifecycle.viewModelScope
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.bean.VideoBean
import com.yzy.example.utils.VideoRandomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//data class PlayPagerState(
//    val videoList: MutableList<VideoBean> = mutableListOf(),
//    val hasMore: Boolean = true,
//    val request: Async<Any> = Uninitialized
//) : MvRxState
//
//class PlayPagerViewModel(
//    state: PlayPagerState = PlayPagerState()
//) : MvRxViewModel<PlayPagerState>(state) {
//
//    //加载数据
//    fun loadData() {
//        Observable.just(VideoRandomUtils.instance.getVideoList())
//            .compose(RxUtils.instance.rx2SchedulerHelperODelay())
//            .execute {
//                copy(videoList = it.invoke() ?: mutableListOf(), request = it)
//            }
//    }
//
//    //加载更多
//    fun loadMore() = withState { state ->
//        if (state.request is Loading || !state.hasMore) return@withState
//        Observable.just(
//            VideoRandomUtils.instance.getVideoList(
//                idStart = state.videoList.size.toLong(),
//                count = 14
//            )
//        )
//            .compose(RxUtils.instance.rx2SchedulerHelperO())
//            .execute {
//                val suc = it is Success
//                copy(
//                    videoList = if (suc) (state.videoList + (it.invoke()
//                        ?: mutableListOf())).toMutableList()
//                    else state.videoList,
//                    hasMore = if (suc) false else state.hasMore,
//                    request = it
//                )
//            }
//    }
//}


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