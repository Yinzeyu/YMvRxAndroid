package com.yzy.pj.im.entity

import android.os.Bundle

/**
 *description: 发出的Action.
 *@date 2019/3/11 15:08.
 *@author: yzy.
 */
data class IMActionEntity(val action: String, var data: Bundle? = null) {
    companion object {
        /**
         * 数据的Key:异常code
         */
        const val DATA_KEY_ERROR_CODE = "data_key_error_code"
        /**
         * 数据的Key:异常信息
         */
        const val DATA_KEY_ERROR_MESSAGE = "data_key_error_message"

        /**
         * 获取ErrorCode
         */
        fun getErrorCode(data: Bundle?): Int? {
            return data?.getInt(DATA_KEY_ERROR_CODE)
        }

        /**
         * 获取ErrorCode
         */
        fun getErrorMessage(data: Bundle?): String? {
            return data?.getString(DATA_KEY_ERROR_MESSAGE)
        }
    }
}