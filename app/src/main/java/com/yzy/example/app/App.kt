package com.yzy.example.app

import com.jeremyliao.liveeventbus.LiveEventBus
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.ClientUtils
import com.yzy.baselibrary.http.retrofitConfig
import com.yzy.baselibrary.widget.refresh.SmartRefreshLayout
import com.yzy.baselibrary.widget.refresh.header.ClassicsHeader
import com.yzy.example.R
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.HeaderRequestIntercept
import com.yzy.example.http.RequestIntercept


class App : BaseApplication() {
    override fun initInMainThread() {
        initLiveBus()
        retrofitConfig {
            context = this@App
            baseUrl = ApiConstants.Address.BASE_URL
            interceptors.add(RequestIntercept())
            interceptors.add(HeaderRequestIntercept())
        }
    }

    override fun baseInitCreate() {
    }


    private fun initLiveBus() {
        LiveEventBus
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
            .autoClear(false)
    }

    init {
        //设置全局的Header构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.white, android.R.color.black)//全局设置主题颜色
            ClassicsHeader(context)
        }
    }
}

