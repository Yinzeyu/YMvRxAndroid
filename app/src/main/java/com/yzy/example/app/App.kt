package com.yzy.example.app

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.di.GlobeConfigModule
import com.yzy.baselibrary.http.RequestIntercept
import com.yzy.example.R
import com.yzy.example.constants.ApiConstants
import com.yzy.example.http.integration.HeaderHttpHandler
import com.yzy.example.imModel.IMUtils
import com.yzy.example.repository.blackRepositoryModel
import com.yzy.example.widget.RefreshHeader
import io.rong.imlib.RongIMClient
import org.kodein.di.Kodein


class App : BaseApplication() {
    //数据库
//    private val boxStore: BoxStore by kodein.instance()

    override fun initInChildThread() {

    }

    override fun initInMainThread() {
//        initObjectDebug()
        RongIMClient.init(this, "mgb7ka1nmdndg")
        IMUtils.init(this@App)

    }

    override fun baseInitCreate() {
        super.baseInitCreate()
        initUM()
    }

    private fun initUM() {
//        val umKey =
//                if (BuildConfig.DEBUG) StringConstants.Push.UM_DEBUG_KEY else StringConstants.Push.UM_RELEASE_KEY
//        val umSecret =
//                if (BuildConfig.DEBUG) StringConstants.Push.UM_DEBUG_MESSAGE_SECRET else StringConstants.Push.UM_RELEASE_MESSAGE_SECRET
//        PushModel.getPushModel().initUM(this, umKey, umSecret, "Umeng")
//        //  魅族通道
//        PushModel.getPushModel().initMZPush(
//                this,
//                StringConstants.Push.MZ_RELEASE_KEY,
//                StringConstants.Push.MZ_RELEASE_SECRET
//        )
//        //  OPPO通道
//        PushModel.getPushModel().initOppoPush(
//                this,
//                StringConstants.Push.OPPO_RELEASE_KEY,
//                StringConstants.Push.OPPO_RELEASE_SECRET
//        )
//        //  VIVO通道
//        PushModel.getPushModel().initVivoPush(this)
    }

    override fun initKodein(builder: Kodein.MainBuilder) {
        super.initKodein(builder)
        val build = GlobeConfigModule
            .builder()
            .baseUrl(ApiConstants.Address.BASE_URL)
            .addInterceptor(RequestIntercept(HeaderHttpHandler()))
            .build()
        builder.import(build.globeConfigModule)
//        builder.import(databaseModule)
        builder.import(blackRepositoryModel)
    }

    /**
     * 数据库调试
     */
//    private fun initObjectDebug() {
//        if (BuildConfig.DEBUG) {
//            AndroidObjectBrowser(boxStore).start(this)
//        }
//    }
//
//    private val databaseModule = Kodein.Module("databaseModule") {
//        bind<BoxStore>() with singleton {
//            MyObjectBox.builder().androidContext(this@App).build();
//        }
//    }

    init {
        //设置全局的Header构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            RefreshHeader(context)
        }
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
//            //指定为经典Footer，默认是 BallPulseFooter
//            ClassicsFooter(context).setDrawableSize(20f)
//        }
    }
}

