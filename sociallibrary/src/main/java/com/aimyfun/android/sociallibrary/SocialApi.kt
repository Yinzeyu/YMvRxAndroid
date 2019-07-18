package com.aimyfun.android.sociallibrary

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.text.TextUtils
import com.aimyfun.android.sociallibrary.ali.AliHandler
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.PayListener
import com.aimyfun.android.sociallibrary.listener.ShareListener
import com.aimyfun.android.sociallibrary.qq.QQHandler
import com.aimyfun.android.sociallibrary.share_media.IShareMedia
import com.aimyfun.android.sociallibrary.sina.SinaWBHandler
import com.aimyfun.android.sociallibrary.utils.Utils
import com.aimyfun.android.sociallibrary.weixin.WXHandler
import com.tencent.connect.auth.AuthAgent
import com.tencent.connect.common.UIListenerManager
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent
import java.util.*

/**
 * description: api调用统一入口
 *
 * @author yinzeyu
 * @date 2018/6/19 16:50
 */
class SocialApi private constructor() {
    private var sinaWBHandler: SinaWBHandler? = null
    private lateinit var mWeChat: IWXAPI
    private lateinit var mTenCent: Tencent

    /**
     * 传入token
     */
    private var authToken: String = ""

    private val mMapSSOHandler = HashMap<PlatformType, SSOHandler?>()

    fun setAuthToken(authToken: String) {
        this.authToken = authToken
    }

    private fun onInit(
        application: Application,
        weChatKey: String,
        qqKey: String,
        sInaWBKEy: String,
        aliKey: String
    ) {
        if (!TextUtils.isEmpty(weChatKey)) {
            PlatformConfig.setWeixin(weChatKey)
            mWeChat = WXAPIFactory.createWXAPI(application, weChatKey, true)
            mWeChat.registerApp(weChatKey)
        }
        if (!TextUtils.isEmpty(qqKey)) {
            PlatformConfig.setQQ(qqKey)
            // 必须 写此方法 否则会导致使用崩溃
            mTenCent = Tencent.createInstance(qqKey, application.applicationContext)
        }
        if (!TextUtils.isEmpty(sInaWBKEy)) {
            PlatformConfig.setSinaWB(sInaWBKEy)
        }
        if (!TextUtils.isEmpty(aliKey)) {
            PlatformConfig.setAli(aliKey)
        }
    }

    /**
     * 注册三方sdk
     *
     * @param application 全局context
     * @param weChatKey 微信key
     * @param qqKey qq key
     * @param sInaWBKEy 新浪 key
     * @param aliKey 支付宝key
     */
    fun init(
        application: Application,
        weChatKey: String,
        qqKey: String,
        sInaWBKEy: String,
        aliKey: String
    ) {
        onInit(application, weChatKey, qqKey, sInaWBKEy, aliKey)
    }

    fun getSSOHandler(platformType: PlatformType): SSOHandler? {
        if (mMapSSOHandler[platformType] == null) {
            when (platformType) {
                PlatformType.WEIXIN -> mMapSSOHandler[platformType] = WXHandler()

                PlatformType.WEIXIN_CIRCLE -> mMapSSOHandler[platformType] = WXHandler()

                PlatformType.QQ -> mMapSSOHandler[platformType] = QQHandler()

                PlatformType.QZONE -> mMapSSOHandler[platformType] = QQHandler()

                PlatformType.SINA_WB -> {
                    sinaWBHandler = SinaWBHandler()
                    mMapSSOHandler[platformType] = sinaWBHandler
                }
                PlatformType.ALI -> mMapSSOHandler[platformType] = AliHandler(authToken)

            }
        }

        return mMapSSOHandler[platformType]
    }

    /**
     * 第三方登录授权
     *
     * @param platformType 第三方平台
     * @param authListener 授权回调
     */
    fun doOauthVerify(
        activity: Activity,
        platformType: PlatformType,
        authListener: AuthListener
    ) {
        val ssoHandler = getSSOHandler(platformType)
        if (ssoHandler != null) {
            ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType))
            ssoHandler.authorize(activity, authListener)
        }

    }

    /**
     * 分享
     */
    fun doShare(
        activity: Activity,
        platformType: PlatformType,
        shareMedia: IShareMedia?,
        shareListener: ShareListener
    ) {
        val ssoHandler = getSSOHandler(platformType)
        if (ssoHandler != null) {
            ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType))
            ssoHandler.share(activity, shareMedia, shareListener)
        }

    }

    /**
     *支付
     */
    fun doPayment(
        activity: Activity,
        platformType: PlatformType,
        payInfoBean: PayBean,
        payListener: PayListener
    ) {
        val ssoHandler = getSSOHandler(platformType)
        if (ssoHandler != null) {
            ssoHandler.onCreate(Utils.getApp(), PlatformConfig.getPlatformConfig(platformType))
            ssoHandler.pay(activity, payInfoBean, payListener)
        }

    }

    /**
     * actvitiy result
     */
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        for ((_, value) in mMapSSOHandler) {
            if (value != null) {
                value.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun doNewIntent(intent: Intent?) {
        if (sinaWBHandler != null) {
            sinaWBHandler!!.onNewIntent(intent)
        }
    }

    /**
     * 优化内存泄漏 onDestroy 调用
     */
    fun release() {
        if (mMapSSOHandler.size > 0) {
            mMapSSOHandler.clear()
        }
        sinaWBHandler = null
        //因为腾讯没有提供get方法，无法释放资源，所以暂时使用反射
        try {
            //一次反射获取c对象
            val tangentField = mTenCent.javaClass.getDeclaredField("a")
            tangentField.isAccessible = true
            val c = tangentField.get(mTenCent) as com.tencent.connect.auth.c
            //二次返获取AuthAgent对象
            val authAgentField = c.javaClass.getDeclaredField("a")
            authAgentField.isAccessible = true
            val authAgent = authAgentField.get(c) as AuthAgent
            authAgent.releaseResource()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mTenCent.releaseResource()
        UIListenerManager.getInstance()
            .getListnerWithAction("action_login")
    }

    companion object {
        private var sSocialApiManager: SocialApi? = null
        @Synchronized
        fun socialApi(): SocialApi {
            return sSocialApiManager ?: SocialApi()
        }
    }

    /**
     * 获取微信 api
     */
    val weChat: IWXAPI
        get() = socialApi().mWeChat

    val tenCent: Tencent
        get() = socialApi().mTenCent

}
