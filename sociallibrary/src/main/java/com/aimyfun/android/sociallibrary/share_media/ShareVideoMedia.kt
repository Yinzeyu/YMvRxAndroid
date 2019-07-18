package com.aimyfun.android.sociallibrary.share_media

import android.graphics.Bitmap

/**
 * description:  视频分享 实体类
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class ShareVideoMedia : IShareMedia {
  var videoUrl: String? = null       //视频url
  var title: String? = null          //标题
  var description: String? = null    //描述
  var thumb: Bitmap? = null          //缩略图
  var url: String? = null          //网页
}
