package com.yzy.sociallib.extention

import android.net.Uri
import com.yzy.sociallib.handler.qq.QQHandler
import com.sina.weibo.sdk.api.*
import com.tencent.connect.share.QQShare
import com.tencent.mm.opensdk.modelmsg.*
import com.yzy.sociallib.entity.content.*
import com.yzy.sociallib.utils.BitmapUtils
import java.io.File

/**
 * description: 分享消息的扩展
 *@date 2019/7/15
 *@author: yzy.
 */

fun WXMediaMessage.setImgMsg(content: ShareImageContent): WXMediaMessage {
  content.img?.let {
    if (!it.isRecycled) {
      mediaObject = WXImageObject(content.img)
      thumbData = BitmapUtils.bmpToByteArray(content.img, false)
    }
  }
  return this
}

fun WXMediaMessage.setMusicMsg(content: ShareMusicContent): WXMediaMessage {
  content.img?.let {
    val musicObject = WXMusicObject().apply {
      musicUrl = content.url
      musicDataUrl = content.aacUrl
    }
    mediaObject = musicObject
    title = content.title
    description = content.description
    thumbData = BitmapUtils.bmpToByteArray(content.img, false)
  }
  return this
}

fun WXMediaMessage.setTextMsg(content: ShareTextContent): WXMediaMessage {
  //text object
  val textObject = WXTextObject().apply {
    text = content.description
  }
  mediaObject = textObject
  description = content.description
  return this
}

fun WXMediaMessage.setVideoMsg(content: ShareVideoContent): WXMediaMessage {
  content.img?.let {
//    val videoObject = WXVideoObject().apply {
//      videoUrl = content.videoUrl
//    }
    val videoObject = WXVideoObject()
    videoObject.videoUrl = content.videoUrl
    videoObject.videoLowBandUrl = content.url
    mediaObject = videoObject
    title = content.title
    description = content.description
    thumbData = BitmapUtils.bmpToByteArray(content.img, false)
  }
  return this
}

fun WXMediaMessage.setWebMsg(content: ShareWebContent): WXMediaMessage {
  content.img?.let {
    //web object
    val webpageObject = WXWebpageObject().apply {
      webpageUrl = content.webPageUrl
    }
    mediaObject = webpageObject
    title = content.title
    description = content.description
    thumbData = BitmapUtils.bmpToByteArray(content.img, false)
  }
  return this
}

fun QQHandler.ShareParamBean.setWebParam(content: ShareWebContent): QQHandler.ShareParamBean{
  shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
  bitmap = content.img
  title = content.title
  description = content.description
  url = content.webPageUrl
  return this
}

fun QQHandler.ShareParamBean.setImgageParam(content: ShareImageContent): QQHandler.ShareParamBean{
  bitmap = content.img
  shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE
  return this
}

fun QQHandler.ShareParamBean.setMusicParam(content: ShareMusicContent): QQHandler.ShareParamBean{
  bitmap = content.img
  shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO
  title = content.title
  description = content.description
  url = content.url
  return this
}

fun QQHandler.ShareParamBean.setVideoParam(content: ShareVideoContent): QQHandler.ShareParamBean{
  bitmap = content.img
  shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
  title = content.title
  description = content.description
  url = content.videoUrl
  return this
}

fun QQHandler.ShareParamBean.setTextImgParam(content: ShareTextImageContent): QQHandler.ShareParamBean{
  bitmap = content.img
  shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
  title = content.title
  description = content.description
  url = content.url
  return this
}

fun WeiboMultiMessage.setTextMsg(content: ShareTextContent):WeiboMultiMessage{
  val atUser = content.atUser
  val textObject = TextObject()
  textObject.text = content.description +
      "${if (!content.url.isNullOrBlank()) content.url else ""}" + atUser
  this.textObject = textObject
  return this
}

fun WeiboMultiMessage.setImgMsg(content: ShareImageContent):WeiboMultiMessage{
  content.img?.let {
    if (!it.isRecycled) {
      val imageObject = ImageObject()
      imageObject.setImageObject(content.img)
      this.imageObject = imageObject
    }
  }
  return this
}

fun WeiboMultiMessage.setTextImgMsg(content: ShareTextImageContent):WeiboMultiMessage{
  content.img?.let {
    if (!it.isRecycled) {
      val imageObject = ImageObject()
      imageObject.setImageObject(content.img)
      this.imageObject = imageObject

      val atUser = content.atUser
      val textObject = TextObject()
      textObject.text = content.description +
          "${if (!content.url.isNullOrBlank()) content.url else ""}" + atUser
      this.textObject = textObject
    }
  }
  return this
}

fun WeiboMultiMessage.setVideoMsg(content: ShareVideoContent):WeiboMultiMessage{
  content.img?.let {
    if (!it.isRecycled){
      val videoSourceObject = VideoSourceObject()
      videoSourceObject.actionUrl = content.videoUrl
      videoSourceObject.title = content.title
      videoSourceObject.thumbData = BitmapUtils.bmpToByteArray(content.img, false)
      videoSourceObject.description = content.description
      videoSourceObject.videoPath = Uri.fromFile(File(content.videoUrl?:""))
      this.videoSourceObject = videoSourceObject
    }
  }

  return this
}

fun WeiboMultiMessage.setWebMsg(content: ShareWebContent):WeiboMultiMessage{
  content.img?.let {
    if(!it.isRecycled){
      val mediaObject = WebpageObject()
      mediaObject.thumbData = BitmapUtils.bmpToByteArray(content.img, false)
      mediaObject.title = content.title
      mediaObject.actionUrl = content.webPageUrl
      mediaObject.description = content.description
      this.mediaObject = mediaObject
    }
  }
  return this
}

/**
 * 检测消息中是否至少包含一种正确消息数据
 * @return true 包含, false 不包含
 */
fun WeiboMultiMessage.verificateMsg():Boolean{
  // 文本分享时，text不能为空
  if (textObject != null && textObject.checkArgs()){
    return true
  }

  // 图片分享时, 图片数据不能为空
  if (imageObject != null && imageObject.imageData != null){
   return true
  }

  if(mediaObject != null
    && !mediaObject.actionUrl.isNullOrBlank()
    && mediaObject.thumbData != null){
    return true
  }

  if (videoSourceObject != null
    && !videoSourceObject.actionUrl.isNullOrBlank()
    && videoSourceObject.thumbData != null){
    return true
  }
  return false
}

