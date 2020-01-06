//package com.yzy.example.http.retry
//
//import io.reactivex.Single
//
///**
// *description: 请求重试的配置.
// *@date 2019/7/15
// *@author: yzy.
// */
////重试次数
//private const val DEFAULT_RETRY_TIMES = 1
////重试间隔时间
//private const val DEFAULT_DELAY_DURATION = 500
//
//data class RetryConfig(val maxRetries: Int = DEFAULT_RETRY_TIMES,
//                       val delay: Int = DEFAULT_DELAY_DURATION,
//                       val condition: () -> Single<Boolean> = { Single.just(false) })