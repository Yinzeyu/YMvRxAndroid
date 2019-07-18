package com.yzy.sociallib.config

/**
 * description: 静态常量
 *@date 2019/7/15
 *@author: yzy.
 */
interface SocialConstants {
    companion object {
        /**
         * 平台未安装
         */
        const val PLAT_NOT_INSTALL: Int = 1001

        /**
         * 平台不支持该操作
         */
        const val OPERATION_NOT_SUPPORT: Int = 1002

        /**
         * 发送失败
         */
        const val LOGIN_ERROR: Int = 1003

        /**
         * 授权失败
         */
        const val AUTH_ERROR: Int = 1004

        /**
         * 图片资源被释放
         */
        const val BITMAP_ERROR: Int = 1005

        /**
         * 类型错误
         */
        const val MEDIA_ERROR: Int = 1006
        /**
         * 分享失败
         */
        const val SHARE_ERROR: Int = 1007
        /**
         * accessToken is not SessionValid
         */
        const val ACCESS_TOKEN_ERROR: Int = 1008
        /**
         * key错误
         */
        const val KEY_ERROR: Int = 1009
        /**
         * 支付失败
         */
        const val PAY_ERROR: Int = 1010

        /**
         * 上下文对象错误
         */
        const val CONTEXT_ERROR: Int = 1011

        /**
         * 回调参数的类型错误
         */
        const val CALLBACK_CLASSTYPE_ERROR: Int = 1012

        /**
         * 获取平台对应的handler错误
         */
        const val PLAT_HANDLER_ERROR = 1013
    }

}