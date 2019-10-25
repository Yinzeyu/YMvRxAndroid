package com.yzy.sociallib.callback

import com.yzy.sociallib.config.PlatformType

/**
 * description: 操作的回调
 *@date 2019/7/15
 *@author: yzy.
 */
interface OperationCallback {
  /**
   * type : 平台类型
   * errorCode : 错误码
   * data : 错误信息
   */
  var onErrors: ((type: PlatformType, errorCode: Int, errorMsg:String?) -> Unit)?
}