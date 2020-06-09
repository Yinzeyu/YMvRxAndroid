package com.yzy.example.component.main


import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.GankAndroidBean

class DynViewModel : BaseViewModel<GankRepository>() {
    val uiState = MutableLiveData<MutableList<GankAndroidBean>>()
    var pageSize = 20
    var page = 0
    private fun getAndroidSuspend() {
//        launchOnlyresult1({ repository.getAndroidSuspend(pageSize, page) },
//            success = {
//                uiState.value = it
//            }, complete = {
//                defUI.dismissDialog.postValue(null)
//            }, isShowDialog = loading
//        )
    }

    fun refreshRequest(loading: Boolean = true) {
        page =0
        getAndroidSuspend()
    }

    fun loadRequest(loading: Boolean = true) {
        page += 1
        getAndroidSuspend()
    }
}