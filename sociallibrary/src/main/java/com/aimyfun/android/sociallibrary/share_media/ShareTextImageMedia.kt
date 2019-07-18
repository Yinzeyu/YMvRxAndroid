package com.aimyfun.android.sociallibrary.share_media

import android.graphics.Bitmap

/**
 * description:  文字图片分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class ShareTextImageMedia : IShareMedia {
  var url: String? = null       //音乐url
  var title: String? = null          //标题
  var description: String? = null    //描述
  var thumb: Bitmap? = null          //缩略图
  var atUser: String? = null
}
