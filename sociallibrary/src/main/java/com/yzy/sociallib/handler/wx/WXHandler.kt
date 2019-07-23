package com.yzy.sociallib.handler.wx

import android.content.Context
import android.util.Log
import com.yzy.sociallib.PlatformManager
import com.yzy.sociallib.callback.AuthCallback
import com.yzy.sociallib.callback.OperationCallback
import com.yzy.sociallib.callback.PayCallback
import com.yzy.sociallib.callback.ShareCallback
import com.yzy.sociallib.config.OperationType
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.config.SocialConstants
import com.yzy.sociallib.entity.platform.PlatformConfig
import com.yzy.sociallib.handler.SSOHandler
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yzy.sociallib.entity.content.*
import com.yzy.sociallib.extention.*

/**
 * description: 微信处理
 * @date 2019/7/15
 * @author: yzy.
 */
class WXHandler(context: Context, config: PlatformConfig) : SSOHandler() {

    companion object {
        const val TAG = "WXHandler"
        private val opList = listOf(
            OperationType.PAY,
            OperationType.AUTH,
            OperationType.SHARE
        )
    }

    private val sScope = "snsapi_userinfo"
    private val sState = "wechat_sdk_微信登录"
    private lateinit var mCallback: OperationCallback
    private lateinit var mShareType: PlatformType
    private lateinit var mPayType: PlatformType

    var wxAPI: IWXAPI? = WXAPIFactory.createWXAPI(context, config.appkey, true)
        private set
    var wxEventHandler: IWXAPIEventHandler
        private set


    init {
        wxAPI?.registerApp(config.appkey)
        wxEventHandler = object : IWXAPIEventHandler {
            override fun onResp(resp: BaseResp?) {
                val type = resp?.type ?: -1
                when (type) {
                    //授权返回
                    ConstantsAPI.COMMAND_SENDAUTH -> this@WXHandler.onAuthCallback(resp as SendAuth.Resp)
                    //分享返回
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX ->
                        this@WXHandler.onShareCallback(resp as SendMessageToWX.Resp)
                    //支付返回
                    ConstantsAPI.COMMAND_PAY_BY_WX -> this@WXHandler.onPayCallback(resp as PayResp)
                    else -> {
                        Log.e("Social", "$TAG : wxEventHandler 回调为null")
                    }
                }
            }

            override fun onReq(p0: BaseReq?) {
            }

        }
    }

    override val isInstalled: Boolean
        get() {
            if (wxAPI == null) {
                return false
            }
            return wxAPI!!.isWXAppInstalled
        }

    override fun pay(type: PlatformType, content: PayContent, callback: OperationCallback) {
        setPlatCurrentHandler(callback)
        mPayType = type
        if (wxAPI == null) {
            callback.onErrors
                ?.invoke(type, SocialConstants.PAY_ERROR, "$TAG :wxAPI 为null")
            release()
            return
        }

        if (content is WXPayContent) {
            val request = PayReq().apply {
                appId = content.appid
                partnerId = content.partnerid
                prepayId = content.prepayid
                packageValue = content.packageX
                nonceStr = content.noncestr
                timeStamp = content.timestamp
                sign = content.sign
            }
            wxAPI?.sendReq(request)
        }
    }

    override fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {
        setPlatCurrentHandler(callback)
        if (wxAPI == null) {
            callback.onErrors
                ?.invoke(type, SocialConstants.PAY_ERROR, "$TAG :wxAPI 为null")
            release()
            return
        }

        mShareType = type

        val msg = WXMediaMessage()
        val contentType: String
        when (content) {
            is ShareImageContent -> {  // 图片分享
                contentType = "img"
                msg.setImgMsg(content)
            }
            is ShareMusicContent -> {  // 音乐分享
                contentType = "music"
                msg.setMusicMsg(content)
            }
            is ShareTextContent -> {  // 文字分享
                contentType = "text"
                msg.setTextMsg(content)
            }
            is ShareVideoContent -> {  // 视频分享
                contentType = "video"
                msg.setVideoMsg(content)
            }
            is ShareWebContent -> {  // 网页分享
                contentType = "webpage"
                msg.setWebMsg(content)
            }
            else -> {
                callback.onErrors
                    ?.invoke(type, SocialConstants.MEDIA_ERROR, "$TAG : content 类型错误")
                release()
                return
            }
        }
        // 当需mediaObject对应的是要图片的content
        // msg.mediaObject 没有被赋值说明 content.img == null 或者 content.img 被回收了
        if (msg.mediaObject == null) {
            callback.onErrors
                ?.invoke(type, SocialConstants.BITMAP_ERROR, "$TAG : content.img 错误")
            release()
            return
        }
        //发起request
        val req = SendMessageToWX.Req().apply {
            message = msg
            transaction = buildTransaction(contentType)
        }
        req.scene = if (type == PlatformType.WEIXIN) WXSceneSession else WXSceneTimeline

        wxAPI?.let {
            if (!it.sendReq(req)) {
                callback.onErrors
                    ?.invoke(type, SocialConstants.SHARE_ERROR, "$TAG : 微信分享请求失败")
                release()
            }

        }
    }

    override fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent?) {
        setPlatCurrentHandler(callback)
        if (wxAPI == null) {
            callback.onErrors
                ?.invoke(type, SocialConstants.PAY_ERROR, "$TAG :wxAPI 为null")
            release()
            return
        }

        val req = SendAuth.Req().apply {
            scope = sScope
            state = sState
            transaction = buildTransaction("authorize")
        }
        wxAPI?.let {
            if (!it.sendReq(req)) {
                callback.onErrors?.invoke(
                    type,
                    SocialConstants.LOGIN_ERROR,
                    "$TAG : 微信授权请求出错"
                )
                release()
            }

        }

    }

    override fun release() {
        wxAPI?.unregisterApp()
        wxAPI?.detach()
        wxAPI = null

        // 移除map中当前handler的数据
//    PlatformManager.currentHandlerMap.remove(this.hashCode())
        PlatformManager.currentHandler = null
    }

    /***
     * 获取对应平台支持的操作
     */
    fun getAvailableOperations(): List<OperationType> {
        return opList
    }

    private fun buildTransaction(type: String): String {
        return type + System.currentTimeMillis()
    }

    /***
     * 授权回调
     */
    private fun onAuthCallback(resp: SendAuth.Resp) {
        if (mCallback !is AuthCallback) {
            mCallback.onErrors
                ?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.AUTH_ERROR,
                    "$TAG : callback 类型错误"
                )
            return
        }
        val callback = mCallback as AuthCallback
        when (resp.errCode) {
            //授权成功
            BaseResp.ErrCode.ERR_OK -> {
                val data = mutableMapOf<String, String?>()
                data["code"] = resp.code
                callback.onSuccess?.invoke(PlatformType.WEIXIN, data)
            }

            //授权取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback.onCancel?.invoke(PlatformType.WEIXIN)
            }

            //授权失败
            else -> {
                callback.onErrors
                    ?.invoke(
                        PlatformType.WEIXIN,
                        SocialConstants.AUTH_ERROR,
                        "$TAG : 授权失败"
                    )
            }
        }
        release()
    }

    /**
     * 分享回调
     */
    private fun onShareCallback(resp: SendMessageToWX.Resp) {
        if (mCallback !is ShareCallback) {
            mCallback.onErrors
                ?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.AUTH_ERROR,
                    "$TAG : 授权失败"
                )
            return
        }
        val callback = mCallback as ShareCallback
        when (resp.errCode) {
            //分享成功
            BaseResp.ErrCode.ERR_OK -> {
                callback.onSuccess?.invoke(mShareType)
            }

            //分享取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback.onCancel?.invoke(mShareType)
            }

            // 分享失败
            else -> {
                callback.onErrors
                    ?.invoke(
                        mShareType,
                        SocialConstants.SHARE_ERROR,
                        "$TAG 分享失败:code:${resp.errCode},str:${resp.errStr}"
                    )
            }
        }
        release()
    }

    /**
     * 支付回调
     */
    private fun onPayCallback(resp: PayResp) {
        if (mCallback !is PayCallback) {
            mCallback.onErrors
                ?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.AUTH_ERROR,
                    "$TAG : 授权失败"
                )
            return
        }
        val callback = mCallback as PayCallback
        when (resp.errCode) {
            //支付成功
            BaseResp.ErrCode.ERR_OK -> {
                callback.onSuccess?.invoke(PlatformType.WEIXIN)
            }
            //支付取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                callback.onCancel?.invoke(PlatformType.WEIXIN)
            }

            // 支付失败
            else -> {
                callback.onErrors?.invoke(
                    PlatformType.WEIXIN,
                    SocialConstants.PAY_ERROR, "$TAG : 支付失败:code:${resp.errCode},str:${resp.errStr}"
                )
            }
        }
        release()
    }

    /**
     * 设置PlatformManager.currentHandler,用于回调
     */
    private fun setPlatCurrentHandler(callback: OperationCallback) {
        mCallback = callback
//    PlatformManager.currentHandlerMap[this.hashCode()] = this
        PlatformManager.currentHandler = this
    }
}