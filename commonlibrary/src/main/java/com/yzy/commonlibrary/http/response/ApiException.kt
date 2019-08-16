package com.yzy.commonlibrary.http.response

/**
 *description: 网络请求异常的Exception.
 *@date 2019/7/15
 *@author: yzy.
 */
open class ApiException constructor(var code: Int = 0, var msg: String? = null, val fields: Map<String, Any>? = null) : Exception()