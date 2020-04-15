package com.yzy.example.component.main


import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.GankAndroidBean

class DynViewModel : BaseViewModel<GankRepository>() {
    val uiState = MutableLiveData<MutableList<GankAndroidBean>>()
    fun getAndroidSuspend(page: Int,loading:Boolean=true) {

        launchOnlyresult1({ repository.getAndroidSuspend(20, page) },
            success =  {
            uiState.value=it
        },complete = {
            defUI.dismissDialog.postValue(null)
        },isShowDialog = loading)
    }

}