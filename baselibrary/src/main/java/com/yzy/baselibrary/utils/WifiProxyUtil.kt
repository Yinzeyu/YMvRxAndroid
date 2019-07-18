package com.yzy.baselibrary.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import com.blankj.utilcode.util.NetworkUtils


/**
 *description: Wifi是否设置了代理的工具类.
 *@date 2019/1/16 11:30.
 *@author: YangYang.
 */
object WifiProxyUtil {

  /**
   * 是否Wifi设置了抓包代理
   */
  @SuppressLint("MissingPermission")
  fun isWifiProxy(): Boolean {
    if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI) {
      val proxyAddress: String? = System.getProperty("http.proxyHost")
      val portStr = System.getProperty("http.proxyPort")
      val proxyPort: Int = Integer.parseInt(portStr ?: "-1")
      return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
    }
    return false
  }
}