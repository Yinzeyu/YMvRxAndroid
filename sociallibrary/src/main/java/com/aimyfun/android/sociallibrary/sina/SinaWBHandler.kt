package com.aimyfun.android.sociallibrary.sina

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.aimyfun.android.sociallibrary.PlatformConfig
import com.aimyfun.android.sociallibrary.PlatformConfig.Platform
import com.aimyfun.android.sociallibrary.SSOHandler
import com.aimyfun.android.sociallibrary.SocialErrorCodeConstants
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.ShareListener
import com.aimyfun.android.sociallibrary.share_media.IShareMedia
import com.aimyfun.android.sociallibrary.share_media.ShareImageMedia
import com.aimyfun.android.sociallibrary.share_media.ShareTextImageMedia
import com.aimyfun.android.sociallibrary.share_media.ShareTextMedia
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler

/**
 * description:  新浪微博 第三方Hnadler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class SinaWBHandler : SSOHandler() {

    private var mSsoHandler: SsoHandler? = null
    private var wbShareCallback: WbShareCallback? = null

    var config: PlatformConfig.SinaWB? = null
        private set
    private var mAuthListener: AuthListener? = null

    var shareListener: ShareListener? = null
        private set

    private var shareHandler: WbShareHandler? = null

    override fun onCreate(
        context: Context,
        config: Platform?
    ) {
        this.config = config as PlatformConfig.SinaWB
        /**
         * TODO 这个参数必须传值 考虑到动态会改变
         */
        if (null == this.config || null == this.config!!.appKey || TextUtils.isEmpty(
                this.config!!.appKey
            )
        ) {
            Log.e("SinaWBHandler", "sina key")
            return
        }
        val weiBoCallBackUrl = ""
        if (TextUtils.isEmpty(weiBoCallBackUrl)) {
            Log.e("SinaWBHandler", "sina url")
            return
        }
        // 初始化微博服务
        WbSdk.install(
            context,
            AuthInfo(
                context, this.config!!.appKey,
                weiBoCallBackUrl,
                SCOPE
            )
        )
        initShareLister()
    }

    override fun authorize(
        activity: Activity,
        authListener: AuthListener
    ) {
        this.mAuthListener = authListener

        this.mSsoHandler = SsoHandler(activity)

        mSsoHandler!!.authorize(object : WbAuthListener {
            override fun onSuccess(accessToken: Oauth2AccessToken) {
                // 从 Bundle 中解析 Token
                if (accessToken.isSessionValid) {
                    val map = mutableMapOf<String, String?>()
                    map["uid"] = accessToken.uid
                    map["access_token"] = accessToken.token
                    map["refresh_token"] = accessToken.refreshToken
                    map["expire_time"] = "" + accessToken.expiresTime
                    mAuthListener!!.onComplete(config!!.name, map)
                    release()
                } else {
                    mAuthListener!!.onError(
                        config!!.name,
                        SocialErrorCodeConstants.accessTokenError
                    )
                    release()
                }
            }

            override fun cancel() {
                mAuthListener!!.onCancel(config!!.name)
            }

            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
                "errmsg=" + wbConnectErrorMessage.errorMessage
                mAuthListener!!.onError(config!!.name, SocialErrorCodeConstants.authError)
                release()
            }
        })
    }

    override fun share(
        activity: Activity,
        shareMedia: IShareMedia?,
        shareListener: ShareListener
    ) {
        this.shareListener = shareListener
        if (shareHandler == null) {
            shareListener.onError(this.config!!.name, SocialErrorCodeConstants.keyError)
            return
        }
        shareHandler = WbShareHandler(activity)
        shareHandler!!.registerApp()
        //}
        this.mSsoHandler = SsoHandler(activity)

        val weiboMessage = WeiboMultiMessage()
        //文字分享
        if (shareMedia is ShareTextMedia) {
            val atUser = if (TextUtils.isEmpty(shareMedia.atUser)) "" else shareMedia.atUser
            val textObject = TextObject()
            if (!TextUtils.isEmpty(shareMedia.url)) {
                textObject.text = shareMedia.description + shareMedia.url + atUser
            } else {
                textObject.text = shareMedia.description!! + atUser!!
            }
            weiboMessage.textObject = textObject
            //图片分享
        } else if (shareMedia is ShareImageMedia) {
            val imageObject = ImageObject()
            imageObject.setImageObject(shareMedia.image)
            weiboMessage.imageObject = imageObject
        } else if (shareMedia is ShareTextImageMedia) {
            val atUser = if (TextUtils.isEmpty(shareMedia.atUser)) "" else shareMedia.atUser
            val textObject = TextObject()
            if (!TextUtils.isEmpty(shareMedia.url)) {
                textObject.text = shareMedia.description + shareMedia.url + atUser
            } else {
                textObject.text = shareMedia.description!! + atUser!!
            }
            weiboMessage.textObject = textObject
            val imageObject = ImageObject()
            imageObject.setImageObject(shareMedia.thumb)
            weiboMessage.imageObject = imageObject
        } else {
            if (this.shareListener != null) {
                this.shareListener!!.onError(
                    this.config!!.name,
                    SocialErrorCodeConstants.mediaError
                )
            }
            return
        }
        if (shareHandler != null) {
            shareHandler!!.shareMessage(weiboMessage, false)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (mSsoHandler != null) {
            mSsoHandler!!.authorizeCallBack(requestCode, resultCode, data)
        }
    }

    private fun initShareLister() {
        wbShareCallback = object : WbShareCallback {
            override fun onWbShareSuccess() {
                shareListener!!.onComplete(config!!.name)
                release()
            }

            override fun onWbShareCancel() {
                shareListener!!.onCancel(config!!.name)
                release()
            }

            override fun onWbShareFail() {
                shareListener!!.onError(config!!.name, SocialErrorCodeConstants.shareError)
                release()
            }
        }
    }

    fun onNewIntent(intent: Intent?) {
        if (shareHandler != null) {
            shareHandler!!.doResultIntent(intent, wbShareCallback)
        }
    }

    private fun release() {
        shareListener = null
        wbShareCallback = null
        shareHandler = null
        mSsoHandler = null
    }

    companion object {
        val SCOPE = (
            "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog," + "invitation_write")
    }
}
