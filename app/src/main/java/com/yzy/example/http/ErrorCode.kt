package com.yzy.example.http

//
//
///**
// * description : 服务器返回的错误码
// *@date 2019/7/15
// *@author: yzy.
// */
interface ErrorCode {
    //服务器返回的错误
    companion object {
        const val SUCCESS = 0//成功返回数据
        const val NEED_LOGIN = 302
        const val TOKEN_EXPIRED = 317
    }
}