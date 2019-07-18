package com.aimyfun.android.sociallibrary

import java.util.HashMap

/**
 * description:第三方平台配置信息存储
 *
 * @author yinzeyu
 * @date 2018/6/19 16:50
 */
object PlatformConfig {

  private var configs: MutableMap<PlatformType, Platform> = HashMap()

  init {
    configs[PlatformType.WEIXIN] = Weixin(PlatformType.WEIXIN)
    configs[PlatformType.WEIXIN_CIRCLE] = Weixin(PlatformType.WEIXIN_CIRCLE)
    configs[PlatformType.QQ] = QQ(PlatformType.QQ)
    configs[PlatformType.QZONE] = QQ(PlatformType.QZONE)
    configs[PlatformType.SINA_WB] = SinaWB(PlatformType.SINA_WB)
    configs[PlatformType.ALI] = Ali(PlatformType.ALI)
  }

  interface Platform {
    val name: PlatformType
  }

  //微信
  class Weixin(override val name: PlatformType) : Platform {
    internal var appId: String? = null
  }

  /**
   * 设置微信配置信息
   */
  fun setWeixin(appId: String) {
    val weixin = configs[PlatformType.WEIXIN] as Weixin
    weixin.appId = appId

    val weiringCircle = configs[PlatformType.WEIXIN_CIRCLE] as Weixin
    weiringCircle.appId = appId
  }

  //微信
  class Ali internal constructor(override val name: PlatformType) : Platform {
    internal var appId: String? = null
  }

  /**
   * 设置微信配置信息
   */
  fun setAli(appId: String) {
    val ali = configs[PlatformType.ALI] as Ali
    ali.appId = appId
  }

  //qq
  class QQ(override val name: PlatformType) : Platform {
    var appId: String? = null
  }

  /**
   * 设置qq配置信息
   */
  fun setQQ(appId: String) {
    val qq = configs[PlatformType.QQ] as QQ
    qq.appId = appId

    val qzone = configs[PlatformType.QZONE] as QQ
    qzone.appId = appId
  }

  /**
   * qq
   */
  class SinaWB(override val name: PlatformType) : Platform {
    var appKey: String? = null
  }

  /**
   * 设置新浪微博配置信息
   */
  fun setSinaWB(appKey: String) {
    val sinaWB = configs[PlatformType.SINA_WB] as SinaWB
    sinaWB.appKey = appKey
  }

  fun getPlatformConfig(platformType: PlatformType): Platform? {
    return configs[platformType]
  }
}
