package com.aimyfun.android.sociallibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.aimyfun.android.sociallibrary.PlatformConfig.Platform
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.PayListener
import com.aimyfun.android.sociallibrary.listener.ShareListener
import com.aimyfun.android.sociallibrary.share_media.IShareMedia

/**
 * Created by tsy on 16/8/4.
 */
abstract class SSOHandler {

  /**
   * 是否安装
   *
   * @return 检查是否安装
   */
  open val isInstall: Boolean
    get() = true

  /**
   * 初始化
   *
   * @param config 配置信息
   */
  open fun onCreate(
    context: Context,
    config: Platform?
  ) {
  }

  /**
   * 登录授权
   *
   * @param authListener 授权回调
   */
  open fun authorize(
    activity: Activity,
    authListener: AuthListener
  ) {

  }

  /**
   * 分享
   *
   * @param shareMedia 分享内容
   * @param shareListener 分享回调
   */
  open fun share(
    activity: Activity,
    shareMedia: IShareMedia?,
    shareListener: ShareListener
  ) {

  }

  /**
   * 分享
   *
   * @param payBean 支付 bean
   * @param shareListener 分享回调
   */
  open fun pay(
    activity: Activity,
    payBean: PayBean,
    shareListener: PayListener
  ) {

  }

  /**
   * 重写onActivityResult
   */
  open fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {

  }
}
