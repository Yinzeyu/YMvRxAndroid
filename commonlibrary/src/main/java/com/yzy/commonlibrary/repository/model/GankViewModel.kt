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
    /** 圈子 */
    val fuliBean: List<FuliBean> = emptyList(),
    val request: Async<Any> = Uninitialized
) : MvRxState

class GankViewModel(initialState: ConversationDetailState = ConversationDetailState()) :
    MvRxViewModel<ConversationDetailState>(initialState) {
    private val ganRepository: GankRepository by BaseApplication.INSTANCE.kodein.instance()

    fun getFuli(month: Int, day: Int) = withState { state ->
                if (state.request is Loading) {
            return@withState
        }
        ganRepository.getSysMsgList(month, day).map {
            it.forEach { s ->
                Log.e("sssssssssss", s.url?:"")
            }
            it
        }.execute {
            copy(
                fuliBean = it.invoke() ?: emptyList()
                , request = it
            )
        }

    }
}