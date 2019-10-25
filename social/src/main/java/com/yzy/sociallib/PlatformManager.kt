package com.yzy.sociallib

import android.content.Context
import android.util.Log
import com.yzy.sociallib.config.OperationType
import com.yzy.sociallib.config.PlatformType
import com.yzy.sociallib.entity.platform.Platform
import com.yzy.sociallib.entity.platform.PlatformConfig
import com.yzy.sociallib.handler.SSOHandler
import com.yzy.sociallib.handler.ali.AliHandler
import com.yzy.sociallib.handler.qq.QQHandler
import com.yzy.sociallib.handler.sina.SinaWBHandler
import com.yzy.sociallib.handler.wx.WXHandler


/**
 * description: 平台配置的管理类
 * @date 2019/7/15
 * @author: yzy.
 */
internal object PlatformManager {

  private const val TAG = "PlatformManager"

  /***
   * 保存可用的平台的信息
   */
  var availablePlatMap: HashMap<PlatformType, Platform> = java.util.HashMap()

  /**
   * 当前正在处理的handler
   */
  var currentHandler: SSOHandler? = null

  /**
   * 当前正在处理的handler的map
   */
  var currentHandlerMap: MutableMap<Int, SSOHandler> = hashMapOf()


  /**
   * 初始化平台配置
   * 新增平台时，维护这个方法
   * @param context 上下文
   * @param config 平台配置
   * @return boolean 初始化是否成功
   */
  fun initPlat(context: Context, config: PlatformConfig): Boolean {
    when (config.name) {
      PlatformType.WEIXIN -> {
        val wxHandler = WXHandler(context, config)
        if (wxHandler.isInstalled) {
          // 微信平台支持的操作
          val opList = wxHandler.getAvailableOperations()
          wxHandler.release()
          availablePlatMap[PlatformType.WEIXIN] = Platform(config, opList)
          availablePlatMap[PlatformType.WEIXIN_CIRCLE] = Platform(config, opList)
          return true
        }
      }

      PlatformType.QQ -> {
        val qqHandler = QQHandler(context, config)
        if (qqHandler.isInstalled) {
          val opList = qqHandler.getAvailableOperation()
          availablePlatMap[PlatformType.QQ] = Platform(config, opList)
          availablePlatMap[PlatformType.QQ_ZONE] = Platform(config, opList)
          return true
        }
      }

      PlatformType.SINA_WEIBO -> {
        val wbHandler = SinaWBHandler(context, config)
        if (wbHandler.isInstalled) {
          val opList = wbHandler.getAvailableOperation()
          wbHandler.release()
          availablePlatMap[PlatformType.SINA_WEIBO] = Platform(config, opList)
          return true
        }
      }

      PlatformType.ALI -> {
        val aliHandler = AliHandler(context, config)
        if (aliHandler.isInstalled) {
          val opList = aliHandler.getAvailableOperation()
          aliHandler.release()
          availablePlatMap[PlatformType.ALI] = Platform(config, opList)
          return true
        }
      }

      else -> {
        Log.d(TAG, "初始化出错：不存在的平台")
        return false
      }

    }
    return false
  }

  /***
   * 平台是否支持对应的操作
   * @param platType 操作类型
   * @param opType 平台
   */
  fun available4PlatAndOperation(platType: PlatformType, opType: OperationType): Boolean {
    if (!availablePlatMap.contains(platType)) {
      return false
    }
    val platform: Platform? = availablePlatMap[platType]
    return platform?.availableOperationType?.contains(opType) ?: false
  }

  /**
   * 获取对应的handler
   */
  fun getPlatHandler(context: Context, type: PlatformType): SSOHandler? {
    return when (type) {
      PlatformType.WEIXIN -> {
        val config = availablePlatMap[PlatformType.WEIXIN]?.platConfig ?: return null
        WXHandler(context, config)
      }
      PlatformType.WEIXIN_CIRCLE -> {
        val config = availablePlatMap[PlatformType.WEIXIN]?.platConfig ?: return null
        WXHandler(context, config)
      }
      PlatformType.QQ -> {
        val config = availablePlatMap[PlatformType.QQ]?.platConfig ?: return null
        QQHandler(context, config)
      }
      PlatformType.QQ_ZONE -> {
        val config = availablePlatMap[PlatformType.QQ]?.platConfig ?: return null
        QQHandler(context, config)
      }
      PlatformType.SINA_WEIBO -> {
        val config = availablePlatMap[PlatformType.SINA_WEIBO]?.platConfig ?: return null
        SinaWBHandler(context, config)
      }
      PlatformType.ALI -> {
        val config = availablePlatMap[PlatformType.ALI]?.platConfig ?: return null
        AliHandler(context, config)
      }
    }
  }


}