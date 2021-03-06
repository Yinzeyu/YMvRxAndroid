package com.yzy.baselibrary.http.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 描述　:自定义的Boolean类型 MutableLiveData  提供了默认值，避免取值的时候还要判空
 */
class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value) {

    override fun getValue(): Int {
        return super.getValue()!!
    }
}