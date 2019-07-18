package com.aimyfun.android.sociallibrary.listener

import com.aimyfun.android.sociallibrary.PlatformType

/**
 * description:
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
interface AuthListener {
  fun onComplete(
    platform_type: PlatformType,
    map: MutableMap<String, String?>
  )

  fun onError(
    platform_type: PlatformType,
    errorCode: Int
  )

  fun onCancel(platform_type: PlatformType)
}
