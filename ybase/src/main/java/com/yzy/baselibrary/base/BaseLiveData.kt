package com.yzy.baselibrary.base

import android.os.Looper
import androidx.lifecycle.MutableLiveData

open class BaseLiveData<T: Any?> : MutableLiveData<T?>() {

    open fun update(value: T?) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.setValue(value)
        } else {
            postValue(value)
        }
    }
}