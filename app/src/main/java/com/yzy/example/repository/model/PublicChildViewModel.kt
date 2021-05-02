package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
//import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean


class PublicChildViewModel : BaseViewModel<GankRepository>() {

    var pageNo = 1

//    var publicDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()

    fun getPublicData(isRefresh: Boolean, cid: Int) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ repository.getPublicData(pageNo, cid) }, {
            //请求成功
            pageNo++
//            val listDataUiState =
//                ListDataUiState(isSuccess = true, isRefresh = isRefresh, isEmpty = it.isEmpty(), hasMore = it.hasMore(), isFirstEmpty = isRefresh && it.isEmpty(), listData = it.datas)
//            publicDataState.postValue(listDataUiState)
        }, {
            //请求失败
//            val listDataUiState = ListDataUiState(isSuccess = false, errMessage = it.message ?: "", isRefresh = isRefresh, listData = arrayListOf<ArticleDataBean>())
//            publicDataState.postValue(listDataUiState)
        })
    }
    fun loadData(cid: Int) {
        pageNo++
        getPublicData(false,cid)
    }
}

