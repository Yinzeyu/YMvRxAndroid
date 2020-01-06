package com.yzy.example.app

import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.http.RetrofitConfig
import com.yzy.example.R
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.RequestIntercept
import com.yzy.example.widget.RefreshHeader
import com.yzy.example.widget.file.AppFileDirManager


class App : BaseApplication() {

    override fun initInChildThread() {

    }

    override fun initInMainThread() {
        initMMKV()
        initLiveBus()
        RetrofitConfig {
            context = this@App
            baseUrl = ApiConstants.Address.BASE_URL
            interceptors.add(
                RequestIntercept(mutableListOf())
            )
        }
        //初始化存储文件的目录
        AppFileDirManager.initAppFile(this@App)
//        initSticker(this)
    }

    override fun baseInitCreate() {
    }
    private fun initMMKV() {
        MMKV.initialize(this)
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
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            RefreshHeader(context)
        }
    }
}

