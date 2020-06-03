package com.yzy.example.app

import cat.ereza.customactivityoncrash.config.CaocConfig
import com.jeremyliao.liveeventbus.LiveEventBus
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.interceptor.CacheInterceptor
import com.yzy.baselibrary.http.interceptor.HeadInterceptor
import com.yzy.baselibrary.http.retrofitConfig
import com.yzy.baselibrary.widget.refresh.SmartRefreshLayout
import com.yzy.baselibrary.widget.refresh.header.ClassicsHeader
import com.yzy.example.R
import com.yzy.example.component.main.MainActivity
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.HeaderRequestIntercept
import com.yzy.example.http.RequestIntercept
import com.yzy.example.http.integration.AddCookiesInterceptor
import com.yzy.example.http.integration.ReceivedCookiesInterceptor


class App : BaseApplication() {
    override fun initInMainThread() {
        initLiveBus()
        retrofitConfig {
            context = this@App
            baseUrl = ApiConstants.Address.BASE_URL
            interceptors.add(RequestIntercept())
            interceptors.add(HeadInterceptor(mapOf()))
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            interceptors.add(CacheInterceptor())
            interceptors.add(AddCookiesInterceptor())
            interceptors.add(ReceivedCookiesInterceptor())
        }
        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(MainActivity::class.java) // 重启的activity
//            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
            .eventListener(null) //允许你指定事件侦听器，以便在库显示错误活动 default: null
            .apply()
    }

    override fun baseInitCreate() {
    }


    private fun initLiveBus() {
        LiveEventBus
            .config()
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

