package com.yzy.example.http.response

/**
 *description: 数据为空类.
 *@date 2019/7/15
 *@author: yzy.
 */
open class EmptyException constructor(var code: Int = 0, var msg: String? = null) : Exception()