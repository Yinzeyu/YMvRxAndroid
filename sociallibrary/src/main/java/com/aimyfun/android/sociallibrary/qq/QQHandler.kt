package com.aimyfun.android.sociallibrary.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.aimyfun.android.sociallibrary.*
import com.aimyfun.android.sociallibrary.PlatformConfig.Platform
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.ShareListener
import com.aimyfun.android.sociallibrary.share_media.*
import com.aimyfun.android.sociallibrary.utils.BitmapUtils
import com.aimyfun.android.sociallibrary.utils.FilePathUtils
import com.aimyfun.android.sociallibrary.utils.FileUtils
import com.aimyfun.android.sociallibrary.utils.Utils
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzonePublish
import com.tencent.connect.share.QzoneShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject
import java.io.File
import java.util.*

/**
 * description: QQ 第三方 Handler
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class QQHandler : SSOHandler() {

    private var mTencent: Tencent? = null

    private var mConfig: PlatformConfig.QQ? = null
    private var mAuthListener: AuthListener? = null
    private var mShareListener: ShareListener? = null
    private var imagePath: String? = null

    private var file: File? = null

    private val mIUiListener = object : IUiListener {
        override fun onComplete(o: Any) {
            mShareListener?.onComplete(mConfig!!.name)
            fileDelete()
        }

        override fun onError(uiError: UiError) {
            val errmsg = ("errcode="
                + uiError.errorCode
                + " errmsg="
                + uiError.errorMessage
                + " errdetail="
                + uiError.errorDetail)
            Log.e("qq", errmsg)
            mShareListener?.onError(mConfig!!.name, SocialErrorCodeConstants.shareError)

            fileDelete()
        }

        override fun onCancel() {
            mShareListener?.onCancel(mConfig!!.name)
            fileDelete()
        }
    }

    override fun onCreate(
        context: Context,
        config: Platform?
    ) {
        this.mConfig = config as PlatformConfig.QQ
        this.mTencent = SocialApi.socialApi()
            .tenCent
    }

    override fun authorize(
        activity: Activity,
        authListener: AuthListener
    ) {
        this.mAuthListener = authListener

        this.mTencent!!.login(activity, "all", object : IUiListener {
            override fun onComplete(o: Any?) {
                if (null == o) {
                    Log.e("qq", "onComplete response=null")
                    mAuthListener!!.onError(mConfig!!.name, SocialErrorCodeConstants.authError)
                    return
                }
                val response = o as JSONObject?
                if (response != null) {
                    initOpenidAndToken(response)
                }
                mAuthListener!!.onComplete(mConfig!!.name, jsonToMap(response!!))

                //mTencent.logout(mActivity);
            }

            override fun onError(uiError: UiError) {
                val errmsg = ("errcode="
                    + uiError.errorCode
                    + " errmsg="
                    + uiError.errorMessage
                    + " errdetail="
                    + uiError.errorDetail)
                Log.e("qq", errmsg)
                mAuthListener!!.onError(mConfig!!.name, SocialErrorCodeConstants.authError)
            }

            override fun onCancel() {
                mAuthListener!!.onCancel(mConfig!!.name)
            }
        })
    }

    override fun share(
        activity: Activity,
        shareMedia: IShareMedia?,
        shareListener: ShareListener
    ) {
        this.mShareListener = shareListener
        imagePath = (FilePathUtils.getAppPath(Utils.getApp())
            + FilePathUtils.IMAGES
            + File.separator
            + "/socail_qq_img_tmp" + System.currentTimeMillis()
            + ".png")

        file = File(imagePath!!)
        fileDelete()
        val params = Bundle()
        val shareType: Int
        var title: String? = ""
        var description: String? = ""
        var url: String? = ""
        val bitmap: Bitmap?
        //网页分享
        if (shareMedia is ShareWebMedia) {
//图片保存本地
            bitmap = shareMedia.thumb
            shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
            title = shareMedia.title
            description = shareMedia.description
            url = shareMedia.webPageUrl
            //图片分享
        } else if (shareMedia is ShareImageMedia) {
//图片保存本地
            bitmap = shareMedia.image
            shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE
            //音乐分享
        } else if (shareMedia is ShareMusicMedia) {
//图片保存本地
            bitmap = shareMedia.thumb
            shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO
            title = shareMedia.title
            description = shareMedia.description
            url = shareMedia.url
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, shareMedia.aacUrl)
            //视频分享
        } else if (shareMedia is ShareVideoMedia) {
//图片保存本地
            bitmap = shareMedia.thumb
            shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
            title = shareMedia.title
            description = shareMedia.description
            url = shareMedia.videoUrl
        } else if (shareMedia is ShareTextImageMedia) {
            bitmap = shareMedia.thumb
            shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
            title = shareMedia.title
            description = shareMedia.description
            url = shareMedia.url
        } else {
            this.mShareListener?.onError(this.mConfig!!.name, SocialErrorCodeConstants.mediaError)
            return
        }
        if (bitmap != null) {
            BitmapUtils.saveBitmapFile(bitmap, imagePath!!)
        }
        if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description)
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url)
        }
        if (this.mConfig!!.name == PlatformType.QZONE) {
            params.putInt(
                QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD
            );
            //!这里是大坑 不能用SHARE_TO_QQ_IMAGE_LOCAL_URL
            val path_arr = ArrayList<String>()
            path_arr.add(imagePath!!)
//      params.putStringArrayList(
//          QzoneShare.SHARE_TO_QQ_IMAGE_URL,
//          path_arr
//      )
            params.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, "")
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, path_arr)// 图片地址ArrayList
            // 分享操作要在主线程中完成
            mTencent!!.publishToQzone(activity, params, mIUiListener)
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType)
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath)
            mTencent!!.shareToQQ(activity, params, mIUiListener)
        }
    }

    private fun fileDelete() {
        FileUtils.deleteFile(file)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener)
    }

    //要初始化open_id和token
    private fun initOpenidAndToken(jsonObject: JSONObject) {
        try {
            val token =
                jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN)
            val expires =
                jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID)

            mTencent!!.setAccessToken(token, expires)
            mTencent!!.openId = openId
        } catch (e: Exception) {
            e.message
        }

    }

    private fun jsonToMap(`val`: JSONObject): MutableMap<String, String?> {
        val map = mutableMapOf<String, String?>();

        val iterator = `val`.keys()

        while (iterator.hasNext()) {
            val var4 = iterator.next()
            if (var4 != null) {
                map[var4] = `val`.opt(var4) as String?
            }

        }
        return map
    }


//  private void publishToQzone () {
////分享类型
//    params.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD )
//    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");
//    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, "图片链接ArrayList");
//    params.putString(QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH,"本地视频地址");
//    Bundle extParams = new Bundle();
//    extParams.putString (QzonePublish. HULIAN_EXTRA_SCENE, "分享场景”);
//        extParams.putString (QzonePublish. HULIAN_CALL_BACK, "回调信息”);
//        params.putBundle(QzonePublish.PUBLISH_TO_QZONE_EXTMAP, extParams);
//    mTencent.publishToQzone(activity, params, new BaseUiListener());
//  }
//}
}
