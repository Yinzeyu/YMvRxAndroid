package com.aimyfun.android.sociallibrary.listener

import com.aimyfun.android.sociallibrary.PlatformType

/**
 * description: 分享回调监听
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
interface ShareListener {
  fun onComplete(platform_type: PlatformType)

  fun onError(
    platform_type: PlatformType,
    errorCode: Int
  )

  fun onCancel(platform_type: PlatformType)
}
