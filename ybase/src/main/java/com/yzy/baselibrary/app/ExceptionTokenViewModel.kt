package com.yzy.baselibrary.app

import androidx.lifecycle.MutableLiveData
import com.yzy.baselibrary.base.BaseRepository
import com.yzy.baselibrary.base.BaseViewModel


class ExceptionTokenViewModel : BaseViewModel<BaseRepository>() {
    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<Int>()


    fun loginReq(int: Int) {
        loginResult.postValue(int)
    }
}

