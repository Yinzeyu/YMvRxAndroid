package com.yzy.commonlibrary.repository.model

import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.commonlibrary.repository.bean.FuliBean
import com.yzy.baselibrary.base.MvRxViewModel
import com.yzy.commonlibrary.repository.GankRepository
import io.reactivex.functions.Consumer
import org.kodein.di.generic.instance

data class ConversationDetailState(
    /** 是否有更多数据 */
    val hasMore: Boolean = false,
    val fuliBean: List<FuliBean> = emptyList(),
    val request: Async<Any> = Uninitialized
) : MvRxState

private var isLoadMore = false

class GankViewModel(initialState: ConversationDetailState = ConversationDetailState()) :
    MvRxViewModel<ConversationDetailState>(initialState) {
    private val ganRepository: GankRepository by BaseApplication.INSTANCE.kodein.instance()

    private fun getFuli(month: Int, day: Int) = withState { state ->
        if (state.request is Loading) {
            return@withState
        }
        ganRepository.getSysMsgList(month, day).map {
            it.forEach { s ->
                Log.e("sssssssssss", s.url ?: "")
            }
            it
        }.execute {
            var hasMoreend: Boolean
            val list: List<FuliBean>?
            if (isLoadMore) {
                val list1 = it.invoke() ?: emptyList()
                if (list1.isEmpty()) {
                    hasMoreend = false
                    list = fuliBean
                } else {
                    hasMoreend = true
                    list = fuliBean + list1
                }
            } else {
                hasMoreend = false;
                list = it.invoke() ?: emptyList()
            }
            copy(
                hasMore = hasMoreend,
                fuliBean = list,
                request = it
            )
        }

    }

    //加载数据
    fun loadData(month: Int, day: Int) {
        isLoadMore = false
        getFuli(month, day)
    }

    //加载更多数据
    fun loadMoreData(month: Int, day: Int) {
        isLoadMore = true
        getFuli(month, day)
    }


}