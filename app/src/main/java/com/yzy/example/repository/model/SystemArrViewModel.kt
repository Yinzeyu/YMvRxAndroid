package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.state.ResultState
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.ClassifyBean
import com.yzy.example.repository.bean.NavigationBean


class SystemArrViewModel : BaseViewModel<GankRepository>() {

    var pageNo = 0

    //体系子栏目列表数据
    var systemChildDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()

    fun getSystemChildData(isRefresh: Boolean, cid: Int) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ repository.getSystemChildData(pageNo, cid) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(isSuccess = true, isRefresh = isRefresh, isEmpty = it.isEmpty(), hasMore = it.hasMore(), isFirstEmpty = isRefresh && it.isEmpty(), listData = it.datas)
            systemChildDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState = ListDataUiState(isSuccess = false, errMessage = it.message ?: "", isRefresh = isRefresh, listData = arrayListOf<ArticleDataBean>())
            systemChildDataState.postValue(listDataUiState)
        })
    }
    fun loadData(cid: Int) {
        pageNo++
        getSystemChildData(false,cid)
    }
}

