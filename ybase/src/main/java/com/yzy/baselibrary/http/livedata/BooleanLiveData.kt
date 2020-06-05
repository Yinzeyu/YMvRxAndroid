package com.yzy.baselibrary.http.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 描述　:自定义的Boolean类型 MutableLiveData  提供了默认值，避免取值的时候还要判空
 */
class BooleanLiveData(value: Boolean = false) : MutableLiveData<Boolean>(value) {

    override fun getValue(): Boolean {
        return super.getValue()!!
    }
}