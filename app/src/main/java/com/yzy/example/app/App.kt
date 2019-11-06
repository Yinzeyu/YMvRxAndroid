package com.yzy.example.app

import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yzy.baselibrary.di.GlobeConfigModule
import com.yzy.baselibrary.http.RequestIntercept
import com.yzy.commonlibrary.CommonApplication
import com.yzy.commonlibrary.constants.ApiConstants
import com.yzy.commonlibrary.integration.HeaderHttpHandler
import com.yzy.commonlibrary.refresh.RefreshHeader
import com.yzy.commonlibrary.repository.blackRepositoryModel
import com.yzy.example.BuildConfig
import com.yzy.example.R
import com.yzy.example.imModel.IMUtils
import io.rong.imlib.RongIMClient
import org.kodein.di.Kodein


class App : CommonApplication() {
    override fun initInChildThread() {

    }

    override fun initInMainThread() {
        super.initInMainThread()
//        PushModel.getPushModel().initMiPush(
//                this,
//                StringConstants.Push.XM_RELEASE_KEY,
//                StringConstants.Push.XM_RELEASE_SECRET
//        )
//        PushModel.getPushModel().initHWPush(this)
//        initBug()
        RongIMClient.init(this, "mgb7ka1nmdndg")
        IMUtils.init(this@App)
        initARouter()
    }

    override fun baseInitCreate() {
        super.baseInitCreate()
        initUM()
    }
    /**
     * 初始化ARouter
     */
    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()// 打印日志
            ARouter.openDebug()// 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.printStackTrace()//打印日志的时候打印线程堆栈
        }
        ARouter.init(this)//初始化
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
        builder.import(blackRepositoryModel)
    }

//    private fun initBug() {
    //初始化key
//        CrashReport.initCrashReport(
//            applicationContext,
//            if (BuildConfig.DEBUG) "8e9eedd10f" else "2ae17bde1d",
//            false
//        )
//    }

//    private fun initSocial() {
//        Social.init(
//            applicationContext,
//            CommPlatConfigBean(PlatformType.WEIXIN, ""),  // 微信key
//            CommPlatConfigBean(PlatformType.QQ, appkey = "") // qqkey
//        )
//    }

    init {
        //设置全局的Header构建器
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            RefreshHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }
}
