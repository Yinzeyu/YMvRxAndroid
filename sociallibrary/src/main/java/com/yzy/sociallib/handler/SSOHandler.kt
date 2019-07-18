package com.yzy.sociallib.handler

import com.yzy.sociallib.callback.OperationCallback
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.entity.content.AuthContent
import com.yzy.sociallib.entity.content.OperationContent
import com.yzy.sociallib.entity.content.PayContent
import com.yzy.sociallib.entity.content.ShareContent

/**
 * description: handler的抽象类
 * @date 2019/7/15
 * @author: yzy.
 */
abstract class SSOHandler {

  /**
   * 判断是否安装平台
   */
  open val isInstalled: Boolean
    get() = true

  /**
   * 重写onActivityResult
   */
  open fun onActivityResult(content: OperationContent) {

  }

  /**
   *  支付
   */
  open fun pay(type: PlatformType, content: PayContent, callback: OperationCallback) {

  }

  /**
   * 分享
   */
  open fun share(type: PlatformType, content: ShareContent, callback: OperationCallback) {
  }

  /**
   * 授权
   */
  open fun authorize(type: PlatformType, callback: OperationCallback, content: AuthContent? = null) {
  }

  /**
   * 资源释放
   */
  open fun release() {
  }
}