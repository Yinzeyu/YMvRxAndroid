package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.example.http.ListDataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.ArticleDataBean
import com.yzy.example.repository.bean.SystemBean


class SystemViewModel : BaseViewModel<GankRepository>() {

    //体系数据
    var systemDataState: MutableLiveData<ListDataUiState<SystemBean>> = MutableLiveData()

    /**
     * 获取体系数据
     */
    fun getSystemData() {
        request({repository.getSystemData() }, {
            //请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            systemDataState.postValue(dataUiState)
        }, {
            //请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.message?:"",
                    listData = arrayListOf<SystemBean>()
                )
            systemDataState.postValue(dataUiState)
        })
    }
}

