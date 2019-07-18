package com.yzy.pj

import com.yzy.commonlibrary.CommonApplication
import com.yzy.commonlibrary.constants.StringConstants
import com.yzy.commonlibrary.repository.blackRepositoryModel
import com.yzy.pj.push.PushModel
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

}
