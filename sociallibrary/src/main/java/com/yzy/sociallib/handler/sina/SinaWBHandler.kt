package com.yzy.sociallib.handler.sina

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yzy.sociallib.PlatformManager
import com.yzy.sociallib.callback.AuthCallback
import com.yzy.sociallib.callback.OperationCallback
import com.yzy.sociallib.callback.ShareCallback
import com.yzy.sociallib.config.OperationType
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.config.SocialConstants
import com.yzy.sociallib.entity.platform.PlatformConfig
import com.yzy.sociallib.entity.platform.SinaPlatConfigBean
import com.yzy.sociallib.handler.SSOHandler
import com.yzy.sociallib.utils.AppUtils
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import com.yzy.sociallib.entity.content.*
import com.yzy.sociallib.extention.*


/**
 * description: 新浪微博处理
 *@date 2019/7/15
 *@author: yzy.
 */
class SinaWBHandler(context: Context, config: PlatformConfig) : SSOHandler() {

  companion object {
    const val TAG = "SinaWBHandler"
    private val opList = listOf(
        OperationType.AUTH,
        OperationType.SHARE
    )
  }

  private var mContext = context
  private var mSinaWBHandler: WbShareHandler? = null
  private var mSsoHandler: SsoHandler? = null
  private lateinit var mShareCallback: ShareCallback
  private lateinit var mAuthCallback: AuthCallback

  private var mWbShareCallback: WbShareCallback? = object : WbShareCallback {
    override fun onWbShareFail() {
      mShareCallback.onErrors?.invoke(
        PlatformType.SINA_WEIBO,
              SocialConstants.SHARE_ERROR,
              "$TAG : 微博分享失败")
      release()
    }

    override fun onWbShareCancel() {
      mShareCallback.onCancel?.invoke(PlatformType.SINA_WEIBO)
    }

    override fun onWbShareSuccess() {
      mShareCallback.onSuccess?.invoke(PlatformType.SINA_WEIBO)
    }
  }

  init {
    if (config is SinaPlatConfigBean){
      WbSdk.install(context, AuthInfo(context, config.appkey, config.redirectUrl, config.scope))
    }
  }

  override val isInstalled: Boolean
    get() {
      return AppUtils.isAppInstalled("com.sina.weibo", mContext)
    }

  override fun onActivityResult(content: OperationContent) {
    when(content){
      is ActivityResultContent -> {
        if (mSsoHandler != null) {
          mSsoHandler?.authorizeCallBack(content.request, content.result, content.data)
        } else {
          Log.e("AimySocial", "$TAG :授权回调的ssohandler为null")
        }
      }
      is NewIntentContent -> {
        if (mSinaWBHandler != null) {
          mSinaWBHandler?.doResultIntent(content.intent, mWbShareCallback)
        } else {
          Log.e("AimySocial", "$TAG :分享回调的sinawbhandler为null")
        }
      }
    }
  }

  override fun share(
    type: PlatformType,
    content: ShareContent,
    callback: OperationCallback
  ) {
    if (mContext !is Activity) {
      callback.onErrors?.invoke(
        PlatformType.SINA_WEIBO,
              SocialConstants.CALLBACK_CLASSTYPE_ERROR,
              "$TAG : context 不是activiy或者fragment")
      return
    }
    if (callback !is ShareCallback) {
      callback.onErrors?.invoke(
        PlatformType.SINA_WEIBO,
              SocialConstants.CALLBACK_CLASSTYPE_ERROR,
              "$TAG : callback 类型错误")
      return
    }
    mShareCallback = callback

    val activity: Activity = mContext as Activity
    PlatformManager.currentHandler = this
//    PlatformManager.currentHandlerMap[this.hashCode()] = this
    mSinaWBHandler = WbShareHandler(activity)
    mSinaWBHandler?.registerApp()

    if (mSinaWBHandler == null) {
      callback.onErrors
        ?.invoke(
          PlatformType.SINA_WEIBO,
          SocialConstants.MEDIA_ERROR,
          "$TAG : 分享时 sinawbhandler 为 null")
      return
    }
    mSsoHandler = SsoHandler(activity)
    val weiboMessage = WeiboMultiMessage()
    when (content) {
      //文字分享
      is ShareTextContent -> weiboMessage.setTextMsg(content)
      //图片分享
      is ShareImageContent -> weiboMessage.setImgMsg(content)
      //图片文字分享
      is ShareTextImageContent -> weiboMessage.setTextImgMsg(content)
      // 视频
      is ShareVideoContent -> weiboMessage.setVideoMsg(content)
      // 网页
      is ShareWebContent -> weiboMessage.setWebMsg(content)
      else -> {
        callback.onErrors
            ?.invoke(
              PlatformType.SINA_WEIBO,
                SocialConstants.MEDIA_ERROR,
                "$TAG : content 类型错误")
        return
      }
    }

    if (!weiboMessage.verificateMsg()){
      callback.onErrors
        ?.invoke(
          PlatformType.SINA_WEIBO,
          SocialConstants.MEDIA_ERROR,
          "$TAG : 参数错误")
      return
    }

    mSinaWBHandler?.shareMessage(weiboMessage, false)
  }

  override fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent?) {
    if (mContext !is Activity) {
      callback.onErrors?.invoke(
        PlatformType.SINA_WEIBO,
              SocialConstants.CALLBACK_CLASSTYPE_ERROR,
              "$TAG : context 类型错误")
      return
    }
    val activity: Activity = mContext as Activity
    if (callback !is AuthCallback) {
      mAuthCallback.onErrors
          ?.invoke(
            PlatformType.SINA_WEIBO,
              SocialConstants.CALLBACK_CLASSTYPE_ERROR,
              "$TAG : callback 类型错误")
      return
    }

    PlatformManager.currentHandler = this
//    PlatformManager.currentHandlerMap[this.hashCode()] = this
    mSsoHandler = SsoHandler(activity)
    if (mSsoHandler == null) {
      callback.onErrors
        ?.invoke(
          PlatformType.SINA_WEIBO,
          SocialConstants.CALLBACK_CLASSTYPE_ERROR,
          "$TAG : 授权时 mSsoHandler 为null")
      return
    }
    mAuthCallback = callback

    mSsoHandler?.let {
      it.authorize(object : WbAuthListener {
        override fun onSuccess(accessToken: Oauth2AccessToken?) {
          accessToken?.let {token ->
            // 从 Bundle 中解析 Token
            if (token.isSessionValid) {
              val map = mutableMapOf<String, String?>()
              map["uid"] = token.uid
              map["access_token"] = token.token
              map["refresh_token"] = token.refreshToken
              map["expire_time"] = "" + token.expiresTime
              mAuthCallback.onSuccess?.invoke(type, map)
            } else {
              mAuthCallback.onErrors?.invoke(
                PlatformType.SINA_WEIBO,
                      SocialConstants.ACCESS_TOKEN_ERROR,
                      "$TAG : 授权回调的isSessionValid 为 false")
            }
            release()
          }
        }

        override fun onFailure(p0: WbConnectErrorMessage?) {
          mAuthCallback.onErrors?.invoke(
            PlatformType.SINA_WEIBO,
                  SocialConstants.ACCESS_TOKEN_ERROR,
                  "$TAG : 授权失败 $p0")
          release()
        }

        override fun cancel() {
          mAuthCallback.onCancel?.invoke(type)
          release()
        }
      })
    }
  }

  override fun release() {
    mSinaWBHandler = null
    mWbShareCallback = null
    mSsoHandler = null
//    PlatformManager.currentHandlerMap.remove(this.hashCode())
    PlatformManager.currentHandler = null
  }

  /**
   * 获取该平台支持的操作
   */
  fun getAvailableOperation(): List<OperationType> {
    return opList
  }

}