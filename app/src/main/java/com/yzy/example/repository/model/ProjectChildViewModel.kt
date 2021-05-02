package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
//import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean

class ProjectChildViewModel(var state: SavedStateHandle) : BaseViewModel<GankRepository>() {
//    fun setValue(childKey:String,value: ListDataUiState<ArticleDataBean>) = state.set(childKey, value)
//    private fun getValue(childKey:String): MutableLiveData<ListDataUiState<ArticleDataBean>>? = state.getLiveData(childKey)
//    fun clear(childKey:String){
//        state.remove<ListDataUiState<ArticleDataBean>>(childKey)
//    }
    fun loadLocal(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        val childKey = "projectChild$cid"
//        if (getValue(childKey) != null  && getValue(childKey)?.value != null){
//            projectDataState.postValue(getValue(childKey)?.value)
//            clear(childKey)
//            return
//        }
        getProjectData(isRefresh, cid, isNew)
    }
    //页码
    var pageNo = 1

//    var projectDataState: MutableLiveData<ListDataUiState<ArticleDataBean>> = MutableLiveData()

    private fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }
        request({repository.getProjectData(pageNo, cid, isNew)},{
            //请求成功
            pageNo++
//            val listDataUiState =
//                ListDataUiState(
//                    isSuccess = true,
//                    isRefresh = isRefresh,
//                    isEmpty = it.isEmpty(),
//                    hasMore = it.hasMore(),
//                    isFirstEmpty = isRefresh && it.isEmpty(),
//                    listData = it.datas
//                )
//            projectDataState.postValue(listDataUiState)
        },{
            //请求失败
//            val listDataUiState =
//                ListDataUiState(
//                    isSuccess = false,
//                    errMessage = it.errMsg,
//                    isRefresh = isRefresh,
//                    listData = arrayListOf<ArticleDataBean>()
//                )
//            projectDataState.postValue(listDataUiState)
        })
    }
}