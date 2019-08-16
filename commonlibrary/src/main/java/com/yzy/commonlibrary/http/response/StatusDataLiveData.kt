package com.yzy.commonlibrary.http.response

import androidx.lifecycle.MediatorLiveData

/**
 *description: 数据类型为StatusData的liveData.
 *@date 2019/7/15
 *@author: yzy.
 */
class StatusDataLiveData<T> : MediatorLiveData<StatusData<T>>()