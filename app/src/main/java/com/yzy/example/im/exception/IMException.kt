package com.yzy.example.im.exception

/**
 *description: IM操作异常的Exception.
 *@date 2019/5/30 17:34.
 *@author: yzy.
 */
class IMException(val errorCode: Int) : Exception() {

    companion object {
        /**
         * 异常的CODE:未知异常
         */
        const val ERROR_UNKNOWN = -100001
        /**
         * 异常的CODE:操作失败
         */
        const val ERROR_OPERATE_FAILED = -100002
    }
}