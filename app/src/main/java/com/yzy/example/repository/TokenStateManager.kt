package com.yzy.example.repository

import com.yzy.baselibrary.http.livedata.UnPeekLiveData


/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　: 网络变化管理者
 */
class TokenStateManager private constructor() {

    val mNetworkStateCallback = UnPeekLiveData<Boolean>()

//    init {
//        //mNetworkStateCallback值为null时或者,不为空但是没有网络时才能初始化设值有网络
//        if (mNetworkStateCallback.value == null || !mNetworkStateCallback.value!!.isSuccess) {
//            mNetworkStateCallback.postValue()
//        }
//    }

    companion object {
        val instance: TokenStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TokenStateManager()
        }
    }
}