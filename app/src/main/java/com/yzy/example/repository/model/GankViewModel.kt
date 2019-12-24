package com.yzy.example.repository.model

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.yzy.baselibrary.base.MvRxViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.BannerBean

data class ConversationDetailState(
        /** 是否有更多数据 */
        val banners: MutableList<BannerBean> = mutableListOf(),
        val request: Async<Any> = Uninitialized
) : MvRxState

private var isLoadMore = false

class GankViewModel(initialState: ConversationDetailState = ConversationDetailState()) :
        MvRxViewModel<ConversationDetailState>(initialState) {
    private val ganRepository: GankRepository by lazy {
        GankRepository()
    }

    private fun getBanner() = withState { state ->
        if (state.request is Loading) {
            return@withState
        }
//        ganRepository.banner().execute {
//            copy(
//                    banners = it() ?: mutableListOf(),
//                    request = it
//            )
//        }
    }

    //加载数据
    fun loadData() {
        isLoadMore = false
//        getBanner()
    }


}

