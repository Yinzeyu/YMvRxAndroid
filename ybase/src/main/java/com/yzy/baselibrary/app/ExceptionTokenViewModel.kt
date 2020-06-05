package com.yzy.baselibrary.app

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseRepository
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.http.livedata.UnPeekNotNullLiveData


class ExceptionTokenViewModel : BaseViewModel<BaseRepository>() {
    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = UnPeekNotNullLiveData<Int>()


    fun loginReq(int: Int) {
        loginResult.postValue(int)
    }
}

