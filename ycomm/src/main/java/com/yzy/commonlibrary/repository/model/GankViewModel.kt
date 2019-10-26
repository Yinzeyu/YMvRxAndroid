package com.yzy.commonlibrary.repository.model

import android.util.Log
import com.airbnb.mvrx.*
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.base.MvRxViewModel
import com.yzy.commonlibrary.repository.GankRepository
import com.yzy.commonlibrary.repository.bean.*
import io.reactivex.functions.Consumer
import org.kodein.di.generic.instance

data class ConversationDetailState(
    /** 是否有更多数据 */
    val banners: MutableList<BannerBean> = mutableListOf(),
    val request: Async<Any> = Uninitialized,
    val fuliBean: List<GankAndroidBean> = emptyList(),
    val Frequest: Async<Any> = Uninitialized
) : MvRxState

private var isLoadMore = false

class GankViewModel(initialState: ConversationDetailState = ConversationDetailState()) :
    MvRxViewModel<ConversationDetailState>(initialState) {
    private val ganRepository: GankRepository by BaseApplication.getApp().kodein.instance()

    private fun getBanner() = withState { state ->
        if (state.request is Loading) {
            return@withState
        }
        ganRepository.banner().execute {
            copy(
                banners = it() ?: mutableListOf(),
                request = it
            )
        }
    }

    //加载数据
    fun loadData() {
        isLoadMore = false
        getBanner()
    }

    private fun getFuli(month: Int, day: Int) = withState { state ->
        if (state.Frequest is Loading) {
            return@withState
        }
        ganRepository.getSysMsgList(month, day).execute {
            val list: List<GankAndroidBean>?
            list = if (isLoadMore) {
                val list1 = it.invoke() ?: emptyList()
                if (list1.isEmpty()) {
                    fuliBean
                } else {
                    fuliBean + list1
                }
            } else {
                it.invoke() ?: emptyList()
            }

            copy(
                fuliBean = list ?: mutableListOf()
                , Frequest = it
            )
        }

    }

    //加载数据
    fun load1Data(month: Int, day: Int) {
        isLoadMore = false
        getFuli(month, day)
    }

}

