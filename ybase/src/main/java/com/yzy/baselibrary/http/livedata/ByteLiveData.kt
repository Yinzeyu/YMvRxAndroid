package com.yzy.baselibrary.http.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 描述　:自定义的Short类型 MutableLiveData  提供了默认值，避免取值的时候还要判空
 */
class ByteLiveData(value: Byte = 0) : MutableLiveData<Byte>(value) {

    override fun getValue(): Byte {
        return super.getValue()!!
    }
}