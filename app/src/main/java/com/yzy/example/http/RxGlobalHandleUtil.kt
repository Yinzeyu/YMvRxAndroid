package com.yzy.example.http

import com.yzy.example.http.retry.RetryConfig
import com.blankj.utilcode.util.NetworkUtils
import com.yzy.example.http.response.ApiException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.net.ConnectException
import javax.security.auth.login.LoginException


/**
 *description: 网络请求全局拦截及异常处理工具类.
 *@date 2019/7/15
 *@author: yzy.
 */
object RxGlobalHandleUtil {
    fun <T> globalHandle(): GlobalHandleTransformer<T> {
        return GlobalHandleTransformer(
                upStreamSchedulerProvider = { AndroidSchedulers.mainThread() },
                downStreamSchedulerProvider = { AndroidSchedulers.mainThread() },
                globalOnNextInterceptor = {
                    //全局成功预处理
                    when (when {
                        it.code > 0 -> it.code
                        else -> 0
                    }) {
                        ErrorCode.SUCCESS -> {
                            //请求成功返回正常
                            Observable.just(it.data)
                        }
                        ErrorCode.NEED_LOGIN -> {
                            //请求成功返回登录异常
                            Observable.error(ApiException(it.code, it.message))
                        }
                        ErrorCode.TOKEN_EXPIRED -> {
                            //token过期需要刷新token
                            Observable.error(ApiException())
                        }
                        //其他请求成功，返回异常
                        else -> {
                            ApiErrorMessageHelper.showToastMessage(it.message)
                            Observable.error(ApiException(it.code, it.message))
                        }
                    }
                },
                globalOnErrorResume = {
                    //全局异常预处理，
                    when (it) {
                        is LoginException -> {

                        }
                    }
                    Observable.error<T>(it)
                },
                retryConfigProvider = {
                    //异常重试的配置
                    when (it) {
                        is ConnectException -> {
                            //请求连接异常的配置
                            if (NetworkUtils.isConnected()) {
                                //有网络连接重试三次
                                RetryConfig(maxRetries = 3, delay = 500)
                            } else {
                                //无网络连接不重试
                                RetryConfig()
                            }
                        }
                        else -> RetryConfig()
                    }
                }
        )
    }
}