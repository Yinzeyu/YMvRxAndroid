package com.aimyfun.android.sociallibrary.share_media

import android.graphics.Bitmap

/**
 * description:  网页分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class ShareWebMedia : IShareMedia {

  var webPageUrl: String? = null     //待分享的网页url
  var title: String? = null          //网页标题
  var description: String? = null    //网页描述
  var thumb: Bitmap? = null          //网页缩略图

}
