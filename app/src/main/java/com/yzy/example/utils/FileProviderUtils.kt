package com.yzy.example.utils

import com.blankj.utilcode.util.AppUtils

/**
 * Description:
 * @author: caiyoufei
 * @date: 19-5-30 上午10:40
 */
class FileProviderUtils private constructor() {
  private object SingletonHolder {
    val holder = FileProviderUtils()
  }

  companion object {
    val instance = SingletonHolder.holder
  }

  val PATH = AppUtils.getAppPackageName() + ".provider"
}