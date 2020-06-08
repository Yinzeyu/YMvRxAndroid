package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.NavigationBean


class NavigationViewModel : BaseViewModel<GankRepository>() {
    //导航数据
    var navigationDataState: MutableLiveData<ListDataUiState<NavigationBean>> =
        MutableLiveData()

    /**
     * 获取导航数据
     */
    fun getNavigationData() {
        request({ repository.getNavigationData() }, success = {
            //请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            navigationDataState.postValue(dataUiState)
        }, error = {
            //请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message ?: "",
                    listData = arrayListOf<NavigationBean>()
                )
            navigationDataState.postValue(dataUiState)
        }, emptyView = {
            //请求失败
            val dataUiState = ListDataUiState<NavigationBean>(isSuccess = true,isEmpty = true)
            navigationDataState.postValue(dataUiState)
        })
    }
}

