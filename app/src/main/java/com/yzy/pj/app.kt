package com.yzy.pj

import com.blankj.utilcode.util.LogUtils
import com.tencent.bugly.crashreport.CrashReport
import com.yzy.commonlibrary.CommonApplication
import com.yzy.commonlibrary.constants.StringConstants
import com.yzy.commonlibrary.repository.blackRepositoryModel
import com.yzy.pj.push.PushModel
import com.yzy.sociallib.AimySocial
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.entity.platform.CommPlatConfigBean
import com.yzy.sociallib.entity.platform.SinaPlatConfigBean
import org.kodein.di.Kodein

class app : CommonApplication() {

    override fun initInMainProcess() {
        super.initInMainProcess()
        PushModel.getPushModel().initMiPush(
            this,
            StringConstants.Push.XM_RELEASE_KEY,
            StringConstants.Push.XM_RELEASE_SECRET
        )
        PushModel.getPushModel().initHWPush(this)
        initBug()
        initLog()
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
        builder.import(blackRepositoryModel)
    }

    private fun initBug() {
        //初始化key
        CrashReport.initCrashReport(
            applicationContext,
            if (BuildConfig.DEBUG) "8e9eedd10f" else "2ae17bde1d",
            false
        )
    }

    private fun initLog() {
        LogUtils.getConfig()
            .setLogSwitch(BuildConfig.DEBUG)//log开关
            .setGlobalTag("Aimy").stackDeep = 3//log栈
    }

    private fun initSocial() {
        AimySocial.init(
            applicationContext,
            CommPlatConfigBean(PlatformType.WEIXIN, ""),  // 微信key
            CommPlatConfigBean(PlatformType.QQ, appkey = ""), // qqkey
            SinaPlatConfigBean(
                PlatformType.SINA_WEIBO,
                appkey = "1472835731",
                redirectUrl = "http://www.meda.cc",
                scope = (
                        "email,direct_messages_read,direct_messages_write,"
                                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                                + "follow_app_official_microblog," + "invitation_write")
            )
        )
    }
}
