package com.yzy.commonlibrary.http


/**
 * description : 服务器返回的错误码
 *@date 2019/7/15
 *@author: yzy.
 */
interface ErrorCode {
  //服务器返回的错误
  companion object {
    const val SUCCESS = 200//成功返回数据
    const val NEED_LOGIN = 302//需要登录
    const val TOKEN_EXPIRED = 317//token过期
    const val VERIFY_ERROR = 304//验证码错误
    const val VERIFY_EXPIRD = 309//验证码过期
    const val ACCOUNT_NO_PERMISSION = 305//没有权限登录
    const val ACCOUNT_SEAL = 307//账号被查封
    const val VERIFY_OFTEN = 310//发送验证码过于频繁
    const val BIND_ALREADY_PHONE = 308//绑定手机号冲突
    const val BIND_ALREADY_PLATFORM = 338//绑定三方冲突
    const val BIND_THIRD_ALREADY = 455 //三方账号已被绑定

    const val GAME_MATCH_PUNISHMENT = 327//游戏匹配惩罚
    const val GIVING_INSUFFICIENT_BALANCE = 325//余额不足

    //送礼物出现的异常数据
    const val USER_NOUSER = 314//用户不存在
    const val USER_SHIELDING = 324//被屏蔽

    //动态相关的
    const val FEED_DELETED = 404//动态被删除
    const val NEED_REGISTER = 303//需要注册

    const val USER_ID_ERROR = 404 // 外链跳转时userid有误
    const val REPORT_AGAIN_ERROR = 406 // 重复举报

    const val CIRCLE_NEED_APPLY = 431 //加入圈子时圈子需要审核
  }
}

/**
 * 服务器返回异常的类型相关判断工具类
 */
object ErrorCodeUtils {
  private val ggCodeList = listOf(
    504,//获取缓存锁异常
    503,//服务端HTTP请求错误
    502,//json反序列化失败
    501,//json序列化失败
    500,//服务器异常
    335,//分布式锁处理中
    321,//数据异常
    319,//内容解析失败
    301//请求冲突或数据库唯一冲突
  )

  private val dontShowMsgList = listOf(
    ErrorCode.NEED_LOGIN,//需要登录
    ErrorCode.ACCOUNT_NO_PERMISSION,//没有权限登录
    ErrorCode.ACCOUNT_SEAL,//账号被查封
    ErrorCode.GAME_MATCH_PUNISHMENT,//游戏匹配惩罚
    ErrorCode.GIVING_INSUFFICIENT_BALANCE,//余额不足
    ErrorCode.USER_NOUSER,//用户不存在
    ErrorCode.USER_SHIELDING,//被屏蔽
    ErrorCode.BIND_THIRD_ALREADY,//绑定三方冲突
    ErrorCode.BIND_ALREADY_PHONE,//绑定手机号冲突
    ErrorCode.FEED_DELETED,//动态被删除
    ErrorCode.NEED_REGISTER,//需要注册
    ErrorCode.REPORT_AGAIN_ERROR,//重复举报
    300//参数错误
  )

  /**
   * 异常是否属于提示“啊哦，服务器抽搐了，请稍后重试~”
   */
  fun isShowSeverGG(errorCode: Int): Boolean {
    return ggCodeList.contains(errorCode)
  }

  /**
   * 是否需要展示服务端提示的msg
   */
  fun isNeedShowMsg(errorCode: Int): Boolean {
    return !dontShowMsgList.contains(errorCode)
  }

  /**
   * 是否登录异常需要重新登录
   */
  fun isNeedLogin(errorCode: Int): Boolean {
    return errorCode == ErrorCode.NEED_LOGIN
  }
}