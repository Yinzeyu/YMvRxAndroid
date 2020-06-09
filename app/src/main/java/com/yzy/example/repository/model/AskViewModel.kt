package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean


class AskViewModel : BaseViewModel<GankRepository>() {
    //每日一问数据
    var askDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()
    private var pageNo = 0

    /**
     * 获取每日一问数据
     */
    fun getAskData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1 //每日一问的页码从1开始
        }
        request({ repository.getAskData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            askDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message ?: "",
                    isRefresh = isRefresh,
                    listData = arrayListOf<ArticleDataBean>()
                )
            askDataState.postValue(listDataUiState)
        })
    }

    fun loadData() {
        pageNo++
        getAskData(false)
    }
}

