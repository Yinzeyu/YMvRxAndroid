package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.CollectUrlBean


class CollectUrlViewModel : BaseViewModel<GankRepository>() {
    //收藏de网址数据
//    var urlDataState: MutableLiveData<ListDataUiState<CollectUrlBean>> = MutableLiveData()
    fun getCollectUrlData() {
        request({ repository.collectUrlData() }, {
            //请求成功
//            val listDataUiState =
//                ListDataUiState(
//                    isRefresh = true,
//                    isSuccess = true,
//                    hasMore = false,
//                    isEmpty = it.isEmpty(),
//                    listData = it
//                )
//            urlDataState.postValue(listDataUiState)
        }, {
            //请求失败
//            val listDataUiState =
//                ListDataUiState(
//                    isSuccess = false,
//                    errMessage = it.errMsg,
//                    listData = arrayListOf<CollectUrlBean>()
//                )
//            urlDataState.postValue(listDataUiState)
        })
    }

//    fun loadData() {
//        pageNo++
//        getCollectAriticleData(false)
//    }
}

