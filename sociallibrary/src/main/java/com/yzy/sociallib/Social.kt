package com.yzy.sociallib

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.yzy.sociallib.callback.AuthCallback
import com.yzy.sociallib.callback.PayCallback
import com.yzy.sociallib.callback.ShareCallback
import com.yzy.sociallib.config.OperationType
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.config.SocialConstants
import com.yzy.sociallib.entity.OperationBean
import com.yzy.sociallib.entity.platform.PlatformConfig
import com.yzy.sociallib.entity.content.*

/**
 * description: 库对外暴露的入口类
 * @date 2019/7/15
 * @author: yzy.
 */
object Social {

  private const val TAG = "Social"
  private const val FAST_CLICK_DELAY_TIME = 3000
  /**
   * 上次处理的时间
   */
  private var last = 0L

  /***
   * 初始化各个平台的配置
   * 判断当前配置的平台哪些是可用状态
   * @param context 上下文
   * @param configs 配置的数组
   */
  fun init(context: Context, vararg configs: PlatformConfig) {
    for (config in configs) {
      if (!config.appkey.isNullOrEmpty()) {
        if (!PlatformManager.initPlat(context, config)) {
          Log.e("Social", "$TAG: ${config.name} 初始化失败")
        }
      }
    }
  }

  /**
   * 判断平台是否可用
   * @param type 平台类型
   * @return true 为安装了， false 为未安装
   */
  fun available4Plat(type: PlatformType): Boolean {
    return PlatformManager.availablePlatMap[type] != null
  }

  /**
   * 分享
   * @param context 上下文
   * @param type 平台
   * @param img 用于分享的图片或者其他类型分享的缩略图，除了文字分享，其他分享都需要传img
   * @param title 分享内容的标题
   * @param musicUrl 分享的音频网页的地址，分享音乐时必传musicUrl和img
   * @param musicDataUrl 分享的音频数据的地址
   * @param videoUrl 分享的视频的地址，分享视频时必传videoUrl和img , 微博平台传本地路径
   * @param videoLowBandUrl 分享的视频的地址供低带宽的环境下使用的视频链接，
   * 分享视频到微信平台时videoUrl和videoLowBandUrl必传其一。其他平台传videoUrl
   * @param webUrl 分享的网页的地址，分享网页时必传webUrl和img
   * @param textUrl 分享的文本的地址
   * @param text   分享文本的内容，文字分享时必传
   * @param textImgUrl 分享的文字图片的地址
   * @param description 分享内容的描述
   * @param atUser at的用户
   * @param onSuccess 成功回调
   * @param onError 失败回调
   * @param onCancel 取消回调
   */
  fun share(
    context: Context,
    type: PlatformType,
    img: Bitmap? = null,
    title: String? = null,
    musicUrl: String? = null,
    musicDataUrl: String? = null,
    videoUrl: String? = null,
    videoLowBandUrl: String? = null,
    webUrl: String? = null,
    textUrl: String? = null,
    text: String? = null,
    textImgUrl: String? = null,
    description: String? = null,
    atUser: String? = null,
    onSuccess: ((type: PlatformType) -> Unit)? = null,
    onError: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
    onCancel: ((type: PlatformType) -> Unit)? = null
  ) {
    val shareContent: ShareContent = when {
      // 音乐分享
      !musicUrl.isNullOrBlank() -> {
        ShareMusicContent(img, musicUrl, description, title, musicDataUrl)
      }
      // 视频分享
      !videoUrl.isNullOrBlank() -> {
        ShareVideoContent(img, videoLowBandUrl, description, videoUrl, title)
      }
      // 网页分享
      !webUrl.isNullOrBlank() -> {
        ShareWebContent(webUrl, title, description, img)
      }
      // 文本分享
      !textUrl.isNullOrBlank() -> {
        ShareTextContent(text, textUrl, atUser)
      }
      // 文字图片分享
      !textImgUrl.isNullOrBlank() -> {
        ShareTextImageContent(textImgUrl, text, img, atUser, title)
      }
      // 图片分享
      else -> {
        ShareImageContent(img)
      }
    }
    val callback = ShareCallback(
      onSuccess = onSuccess,
      onErrors = onError,
      onCancel = onCancel
    )
    val bean = OperationBean(context, type, OperationType.SHARE, callback, shareContent)
    performOperation(bean)
  }

  /**
   * 授权
   * @param context 上下文
   * @param type 授权平台
   * @param aliAuthToken 支付宝授权信息
   * @param onSuccess 成功回调
   * @param onError 失败回调
   * @param onCancel 取消回调
   */
  fun auth(
    context: Context,
    type: PlatformType,
    aliAuthToken: String? = null,
    onSuccess: ((type: PlatformType, data: Map<String, String?>?) -> Unit)? = null,
    onError: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
    onCancel: ((type: PlatformType) -> Unit)? = null
  ) {
    val callback = AuthCallback(
      onSuccess = onSuccess,
      onErrors = onError,
      onCancel = onCancel
    )
    var content = AuthContent()
    aliAuthToken?.let {
      content = AliAuthContent(it)
    }
    val bean = OperationBean(context, type, OperationType.AUTH, callback, content)
    performOperation(bean)
  }

  /**
   * 支付
   * @param context 上下文
   * @param type 支付平台类型
   * @param appid 微信开放平台审核通过的应用APPID  微信支付必传
   * @param noncestr 随机字符串，不长于32位。推荐随机数生成算法  微信支付必传
   * @param packageX 支付平台类型，这个有默认值为“Sign=WXPay”
   * @param prepayid 微信返回的支付交易会话ID  微信支付必传
   * @param partnerid 微信支付分配的商户号 微信支付必传
   * @param timestamp 时间戳 微信支付必传
   * @param sign 签名   微信支付必传
   * @param submitPayType 支付类型  微信支付必传
   * @param out_trade_no
   * @param orderNumber 订单号 支付宝支付必传
   * @param orderInfo 支付宝的订单信息
   * @param onSuccess 成功回调
   * @param onError 失败回调
   * @param onCancel 取消回调
   */
  fun pay(
    context: Context,
    type: PlatformType,
    appid: String? = null,
    noncestr: String? = null,
    packageX: String? = "Sign=WXPay",
    partnerid: String? = null,
    prepayid: String? = null,
    timestamp: String? = null,
    sign: String? = null,
    submitPayType: Int = 0,
    params: String? = null,
    out_trade_no: String? = null,
    orderNumber: String? = null,
    orderInfo: String? = null,
    onSuccess: ((type: PlatformType) -> Unit)? = null,
    onError: ((type: PlatformType, errorCode: Int, errorMsg: String?) -> Unit)? = null,
    onCancel: ((type: PlatformType) -> Unit)? = null
  ) {
    var payContent = PayContent()
    when (type) {
      PlatformType.WEIXIN -> {
        if (appid.isNullOrBlank()
          || noncestr.isNullOrBlank()
          || partnerid.isNullOrBlank()
          || prepayid.isNullOrBlank()
          || timestamp.isNullOrBlank()
          || sign.isNullOrBlank()
        ) {
          onError?.invoke(PlatformType.ALI, SocialConstants.PAY_ERROR, "$TAG 必传参数 不能为空或者null")
          return
        }
        payContent = WXPayContent(
          params,
          appid,
          out_trade_no,
          noncestr,
          packageX,
          partnerid,
          prepayid,
          timestamp,
          orderNumber,
          sign,
          submitPayType
        )
      }
      PlatformType.ALI -> {
        if (orderInfo.isNullOrBlank()) {
          onError?.invoke(PlatformType.ALI, SocialConstants.PAY_ERROR, "$TAG orderInfo 不能为空或者null")
          return

        }
        payContent = AliPayContent(orderInfo)
      }
      else -> {
      }
    }
    val callback = PayCallback(
      onSuccess = onSuccess,
      onErrors = onError,
      onCancel = onCancel
    )
    val bean = OperationBean(context, type, OperationType.PAY, callback, payContent)
    performOperation(bean)
  }

  /**
   * 授权回调转发
   */
  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    val content: OperationContent
    // 等于1时是微博分享的回调
    if (requestCode == 1) {
      content = NewIntentContent(data)
    } else {
      content = ActivityResultContent(requestCode, resultCode, data)
    }
    PlatformManager.currentHandler?.let {
      Log.d("sociallib1", PlatformManager.currentHandler.toString())
      OperationManager.instance.performActivityResult(it, content)
    }
  }

  private fun performOperation(bean: OperationBean) {
    // 两次处理的时间间隔不能小于3s
    if (System.currentTimeMillis() - last < FAST_CLICK_DELAY_TIME) {
      Log.d("Social", "$TAG 重复处理 上次处理时间$last 本次处理时间 ${System.currentTimeMillis()}")
      return
    }

    last = System.currentTimeMillis()
    OperationManager.instance.perform(bean)
  }
}