package com.yzy.example.constants

import com.yzy.example.BuildConfig

interface ApiConstants {
    //请求地址相关
    interface Address {
        companion object {
            internal const val FORCE_RELEASE = false //是否强制使用正式服
            private const val HTTP = "http://"
            private const val HTTPS = "https://"
            private const val DEV = ""//测试服地址
            private const val REL = ""//正式服地址
            private const val WWW = ""//外部访问地址
            private const val AGREEMENT = "agreement"//协议下统一拼接路径
            val BASE_URL = "https://www.wanandroid.com/"
            const val GANK_URL = "https://gank.io/"
            private val H5_PREFIX = if (BuildConfig.DEBUG) {//外部链接访问的前缀
                HTTPS + DEV
            } else {
                HTTPS + REL
            }

        }
    }
}