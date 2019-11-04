package com.yzy.pj.app

import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yzy.baselibrary.di.GlobeConfigModule
import com.yzy.baselibrary.http.RequestIntercept
import com.yzy.commonlibrary.CommonApplication
import com.yzy.commonlibrary.constants.ApiConstants
import com.yzy.commonlibrary.constants.StringConstants
import com.yzy.commonlibrary.integration.HeaderHttpHandler
import com.yzy.commonlibrary.refresh.RefreshHeader
import com.yzy.commonlibrary.repository.blackRepositoryModel
import com.yzy.pj.BuildConfig
import com.yzy.pj.R
import com.yzy.pj.imModel.IMUtils
import com.yzy.pj.push.PushModel
import io.rong.imlib.RongIMClient
import org.kodein.di.Kodein


class App : CommonApplication() {
    override fun initInChildThread() {

    }

    override fun initInMainThread() {
        super.initInMainThread()
        PushModel.getPushModel().initMiPush(
                this,
                StringConstants.Push.XM_RELEASE_KEY,
                StringConstants.Push.XM_RELEASE_SECRET
        )
        PushModel.getPushModel().initHWPush(this)
//        initBug()
        RongIMClient.init(this, "mgb7ka1nmdndg")
        IMUtils.init(this@App)
    }

    override fun baseInitCreate() {
        super.baseInitCreate()
        initUM()
    }

    private fun initUM() {
        val umKey =
                if (BuildConfig.DEBUG) StringConstants.Push.UM_DEBUG_KEY else StringConstants.Push.UM_RELEASE_KEY
        val umSecret =
                if (BuildConfig.DEBUG) StringConstants.Push.UM_DEBUG_MESSAGE_SECRET else StringConstants.Push.UM_RELEASE_MESSAGE_SECRET
        PushModel.getPushModel().initUM(this, umKey, umSecret, "Umeng")
        //  魅族通道
        PushModel.getPushModel().initMZPush(
                this,
                StringConstants.Push.MZ_RELEASE_KEY,
                StringConstants.Push.MZ_RELEASE_SECRET
        )
        //  OPPO通道
        PushModel.getPushModel().initOppoPush(
                this,
                StringConstants.Push.OPPO_RELEASE_KEY,
                StringConstants.Push.OPPO_RELEASE_SECRET
        )
        //  VIVO通道
        PushModel.getPushModel().initVivoPush(this)
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
