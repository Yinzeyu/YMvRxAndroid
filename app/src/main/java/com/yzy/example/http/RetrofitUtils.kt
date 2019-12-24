package com.yzy.example.http

import android.content.Context
import com.yzy.baselibrary.di.RetrofitConfig
import com.yzy.example.constants.ApiConstants

object RetrofitUtils {
    fun init(app: Context) {
        RetrofitConfig {
            context = app
            baseUrl = ApiConstants.Address.BASE_URL
            interceptors.add(
                RequestIntercept(mutableListOf())
            )
        }
    }
}
