package com.yzy.example.repository.model

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.request
import com.yzy.baselibrary.http.livedata.IntLiveData
import com.yzy.baselibrary.http.livedata.StringLiveData
import com.yzy.example.http.response.BaseResponse
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.IntegralBean


class MeViewModel : BaseViewModel<GankRepository>() {
//    var meData = MutableLiveData<DataUiState<IntegralBean>>()
    var name = StringLiveData("请先登录~")

    var integral = IntLiveData(0)

    var info = StringLiveData("id：--　排名：-")
    fun getIntegral() {
        request({ repository.getIntegral() }, success = {
//            meData.postValue(DataUiState(isSuccess = true, data = it))

        }, error = {
//            meData.postValue(DataUiState(isSuccess = false, errMessage = it.errMsg, data = null))
        })
    }
}

data class AsyncBean(
    var integralBean1: BaseResponse<IntegralBean>,
    var time:Long=0 ,
    var endtime:Long=0
)
