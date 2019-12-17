package com.yzy.example.component.main.model

import com.airbnb.mvrx.*
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.base.MvRxViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.GankAndroidBean
import org.kodein.di.generic.instance

/**
 * Description:
 * @author: yzy
 * @date: 2019/9/30 20:14
 */
data class DynState(
        val androidList: MutableList<GankAndroidBean> = mutableListOf(),
        val hasMore: Boolean = false,
        val request: Async<Any> = Uninitialized
) : MvRxState

class DynViewModel(
        state: DynState = DynState()
) : MvRxViewModel<DynState>(state) {
    private var page = 1
    private var pageSize = 20
    private val ganRepository: GankRepository by BaseApplication.getApp().kodein.instance()
    //加载更多
    fun refreshData() {
        getAndroidList(true)
    }

    //加载更多
    fun loadMoreData() {
        getAndroidList(false)
    }

    private fun getAndroidList(refresh: Boolean) = withState { state ->
        if (state.request is Loading) return@withState
        val tempPage = if (refresh) 1 else page + 1
        ganRepository.androidList(pageSize, tempPage)
                .execute {
                    val result: MutableList<GankAndroidBean> = it.invoke() ?: mutableListOf()
                    if (it is Success) page = tempPage
                    copy(
                            //只有刷新成功后才会清数据
                            androidList = if (refresh && it is Success) result//刷新成功
                            else if (result.isNullOrEmpty()) state.androidList//请求失败
                            else (state.androidList + result).toMutableList(),//加载更多
                            hasMore = if (it is Success) result.size == pageSize else false,
                            request = it
                    )
                }
    }
}