package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
//import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ClassifyBean

class ProjectViewModel(var state: SavedStateHandle) : BaseViewModel<GankRepository>() {
    private val projectTitleKey = "projectTitleKey"
//    fun setValue(value: ListDataUiState<ClassifyBean>) = state.set(projectTitleKey, value)
//    private fun getValue(): MutableLiveData<ListDataUiState<ClassifyBean>>? = state.getLiveData(projectTitleKey)
//    fun clear(){
//        state.remove<ListDataUiState<ClassifyBean>>(projectTitleKey)
//    }
//    var titleDataState: MutableLiveData<ListDataUiState<ClassifyBean>> = MutableLiveData()
    fun loadLocal() {
//        if (getValue() != null  && getValue()?.value != null){
//            titleDataState.postValue(getValue()?.value)
//            clear()
//            return
//        }
        getProjectTitleData()
    }

    private fun getProjectTitleData() {
        request({ repository.getProjecTitle() }, success = {
//            titleDataState.postValue(ListDataUiState(isSuccess = true, listData =it ))
        }, error = {
//            titleDataState.postValue(ListDataUiState(
//                isSuccess = false,
//                errCode = it.code,
//                errMessage = it.errMsg ))
        },emptyView = {
//            titleDataState.postValue(ListDataUiState(isSuccess = true, isEmpty = true ))
        })
    }


}