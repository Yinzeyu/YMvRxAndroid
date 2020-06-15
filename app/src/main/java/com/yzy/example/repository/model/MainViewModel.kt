package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yzy.baselibrary.base.BaseRepository
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.livedata.IntLiveData
import com.yzy.baselibrary.http.livedata.StringLiveData
import com.yzy.example.http.DataUiState
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.IntegralBean

class MainViewModel(var state: SavedStateHandle) : BaseViewModel<GankRepository>(){
    private val mainKey = "mainKey"
    fun setValue(value: Int) = state.set(mainKey, value)
    private fun getValue(): Int? = state.get(mainKey)
    fun loadPosition():Int{
        if (getValue() != null){
            return getValue()?:-1
        }
        return -1
    }
    var meData = MutableLiveData<DataUiState<IntegralBean>>()
    var name = StringLiveData("请先登录~")

    var integral = IntLiveData(0)

    var info = StringLiveData("id：--　排名：-")
    fun getIntegral() {
        request({ repository.getIntegral() }, success = {
            meData.postValue(DataUiState(isSuccess =true,data = it))

        },error = {
            meData.postValue(DataUiState(isSuccess =false,errMessage = it.errMsg,data = null,errCode = it.code))
        })
    }
}