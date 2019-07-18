package com.aimyfun.android.sociallibrary.weixin

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.aimyfun.android.sociallibrary.*
import com.aimyfun.android.sociallibrary.PlatformConfig.Platform
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.PayListener
import com.aimyfun.android.sociallibrary.listener.ShareListener
import com.aimyfun.android.sociallibrary.share_media.*
import com.aimyfun.android.sociallibrary.utils.BitmapUtils
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * description:  微信处理 Handler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class WXHandler : SSOHandler() {

    var wxApi: IWXAPI? = null
        private set

    var wxEventHandler: IWXAPIEventHandler? = null
        private set

    private var mConfig: PlatformConfig.Weixin? = null
    private var mAuthListener: AuthListener? = null
    private var mShareListener: ShareListener? = null

    override val isInstall: Boolean
        get() = if (this.wxApi == null) {
            false
        } else {
            this.wxApi!!.isWXAppInstalled
        }

    private var mPayListener: PayListener? = null

    init {
        this.wxEventHandler = object : IWXAPIEventHandler {
            override fun onResp(resp: BaseResp) {
                val type = resp.type
                when (type) {
                    //授权返回
                    ConstantsAPI.COMMAND_SENDAUTH -> this@WXHandler.onAuthCallback(resp as SendAuth.Resp)

                    //分享返回
                    ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX -> this@WXHandler.onShareCallback(
                        resp as SendMessageToWX.Resp
                    )
                    //支付返回
                    ConstantsAPI.COMMAND_PAY_BY_WX -> this@WXHandler.onPayCallback(resp as PayResp)
                    else -> {
                    }
                }
            }

            override fun onReq(req: BaseReq) {}
        }
    }

    override fun onCreate(
        context: Context,
        config: Platform?
    ) {
        this.mConfig = config as PlatformConfig.Weixin
        this.wxApi = SocialApi.socialApi().weChat
    }

    override fun authorize(
        activity: Activity,
        authListener: AuthListener
    ) {
        if (!isInstall) {
            authListener.onError(this.mConfig!!.name, SocialErrorCodeConstants.installedError)
            release()
            return
        }
        this.mAuthListener = authListener

        val req1 = SendAuth.Req()
        req1.scope = sScope
        req1.state = sState
        req1.transaction = buildTransaction("authorize")

        if (!this.wxApi!!.sendReq(req1)) {
            this.mAuthListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.loginError)
            release()
        }
    }

    /**
     * 验证回调
     */
    private fun onAuthCallback(resp: SendAuth.Resp) {
        when (resp.errCode) {
            //授权成功
            BaseResp.ErrCode.ERR_OK -> {
                val data = mutableMapOf<String, String?>()
                data["code"] = resp.code
                this.mAuthListener?.onComplete(PlatformType.WEIXIN, data)
                release()
            }

            //授权取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                this.mAuthListener?.onCancel(PlatformType.WEIXIN)
                release()
            }

            //授权失败
            else -> {
                mAuthListener?.onError(PlatformType.WEIXIN, SocialErrorCodeConstants.authError)
                release()
            }
        }
    }

    override fun share(
        activity: Activity,
        shareMedia: IShareMedia?,
        shareListener: ShareListener
    ) {
        if (!isInstall) {
            shareListener.onError(this.mConfig!!.name, SocialErrorCodeConstants.installedError)
            release()
            return
        }
        this.mShareListener = shareListener

        val msg = WXMediaMessage()
        var type: String

        //网页分享
        if (shareMedia is ShareWebMedia) {
            type = "webpage"
            //web object
            val webpageObject = WXWebpageObject()
            webpageObject.webpageUrl = shareMedia.webPageUrl
            msg.mediaObject = webpageObject
            msg.title = shareMedia.title
            msg.description = shareMedia.description
            if (shareMedia.thumb != null && shareMedia.thumb!!.isRecycled) {
                mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.bitmapError)
                return
            }
            msg.thumbData = BitmapUtils.bmpToByteArray(shareMedia.thumb!!, false)
            //文字分享
        } else if (shareMedia is ShareTextMedia) {
            type = "text"
            //text object
            val textObject = WXTextObject()
            textObject.text = shareMedia.description
            msg.mediaObject = textObject
            msg.description = shareMedia.description
            //图片分享
        } else if (shareMedia is ShareImageMedia) {
            type = "img"
            msg.mediaObject = WXImageObject(shareMedia.image)
            if (shareMedia.image != null && shareMedia.image!!.isRecycled) {
                mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.bitmapError)
                return
            }
            msg.thumbData = BitmapUtils.bmpToByteArray(shareMedia.image!!, false)
            //音乐分享
        } else if (shareMedia is ShareMusicMedia) {
            type = "music"
            val musicObject = WXMusicObject()
            musicObject.musicUrl = shareMedia.url
            musicObject.musicDataUrl = shareMedia.aacUrl
            msg.mediaObject = musicObject
            msg.title = shareMedia.title
            msg.description = shareMedia.description
            if (shareMedia.thumb != null && shareMedia.thumb!!.isRecycled) {
                mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.bitmapError)
                return
            }
            msg.thumbData = BitmapUtils.bmpToByteArray(shareMedia.thumb!!, false)
            //视频分享
        } else if (shareMedia is ShareVideoMedia) {
            type = "video"

            val videoObject = WXVideoObject()
            videoObject.videoUrl = shareMedia.videoUrl
            msg.mediaObject = videoObject
            msg.title = shareMedia.title
            msg.description = shareMedia.description
            if (shareMedia.thumb != null && shareMedia.thumb!!.isRecycled) {
                mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.bitmapError)
                return
            }
            msg.thumbData = BitmapUtils.bmpToByteArray(shareMedia.thumb!!, false)
        } else {
            this.mShareListener?.onError(
                this.mConfig!!.name, SocialErrorCodeConstants.mediaError
            )
            return
        }

        //发起request
        val req = SendMessageToWX.Req()
        req.message = msg
        req.transaction = buildTransaction(type)

        //分享好友
        if (this.mConfig!!.name == PlatformType.WEIXIN) {
            req.scene = SendMessageToWX.Req.WXSceneSession
            //分享朋友圈
        } else if (this.mConfig!!.name == PlatformType.WEIXIN_CIRCLE) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline
        }

        if (!this.wxApi!!.sendReq(req)) {
            this.mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.authError)
            release()
        }
    }

    private fun onShareCallback(resp: SendMessageToWX.Resp) {
        when (resp.errCode) {
            //分享成功
            BaseResp.ErrCode.ERR_OK -> {
                this.mShareListener?.onComplete(this.mConfig!!.name)
                release()
            }

            //分享取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                this.mShareListener?.onCancel(this.mConfig!!.name)
                release()
            }

            else    //分享失败
            -> {
                val err = TextUtils.concat(
                    "weixin share error (", resp.errCode.toString(),
                    "):", resp.errStr
                )
                Log.e("social", err.toString())
                mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.shareError)
                release()
            }
        }
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null)
            System.currentTimeMillis().toString()
        else
            type + System.currentTimeMillis()
    }

    override fun pay(
        activity: Activity,
        payBean: PayBean,
        shareListener: PayListener
    ) {
        mPayListener = shareListener
        if (!isInstall) {
            mPayListener!!.onError(this.mConfig!!.name, SocialErrorCodeConstants.installedError)
            release()
            return
        }
        val appId = payBean.appid
        val request = PayReq()
        request.appId = appId
        request.partnerId = payBean.partnerid
        request.prepayId = payBean.prepayid
        request.packageValue = payBean.packageX
        request.nonceStr = payBean.noncestr
        request.timeStamp = payBean.timestamp
        request.sign = payBean.sign
        wxApi!!.sendReq(request)
    }

    private fun onPayCallback(resp: PayResp) {
        if (!isInstall) {
            mPayListener!!.onError(this.mConfig!!.name, SocialErrorCodeConstants.installedError)
            release()
            Log.e("weiXinApi", "wx not install")
            return
        }
        when (resp.errCode) {
            //支付成功
            BaseResp.ErrCode.ERR_OK -> {
                if (this.mPayListener != null) {
                    this.mPayListener!!.onComplete(this.mConfig!!.name)
                }
                release()
            }
            //支付取消
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                if (this.mPayListener != null) {
                    this.mPayListener!!.onCancel(this.mConfig!!.name)
                }
                release()
            }

            else    //支付失败
            -> {
                val err = TextUtils.concat(
                    "weixin share error (", resp.errCode.toString(),
                    "):", resp.errStr
                )
                Log.e("social", err.toString())
                if (mPayListener != null) {
                    mPayListener!!.onError(this.mConfig!!.name, SocialErrorCodeConstants.payError)
                }
                release()
            }
        }
    }

    private fun release() {
        mShareListener = null
        mPayListener = null
//    wxEventHandler = null
        mAuthListener = null
        wxApi = null
    }

    companion object {
        /**
         * 默认scope 和 state
         */
        private var sScope = "snsapi_userinfo"
        private var sState = "wechat_sdk_微信登录"

        /**
         * 设置scope和state
         */
        fun setScopeState(
            scope: String,
            state: String
        ) {
            sScope = scope
            sState = state
        }
    }
}
