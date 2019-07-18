package com.yzy.commonlibrary.http

import com.yzy.baselibrary.http.response.ApiException


/**
 *description: 用户登录异常的Exception.
 *@date 2019/7/15
 *@author: yzy.
 */
class LoginException(code: Int = 0, msg: String? = null) : ApiException(code, msg)