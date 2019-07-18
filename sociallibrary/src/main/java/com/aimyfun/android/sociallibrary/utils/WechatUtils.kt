package com.aimyfun.android.sociallibrary.utils

import com.aimyfun.android.sociallibrary.SocialApi

/**
 * description :  微信安装工具类
 *
 * @author : 尹泽宇
 * @date : 2018/8/14 9:42
 */
object WechatUtils {
  val isInstallWechat: Boolean
    get() = SocialApi.socialApi().weChat.isWXAppInstalled
}
