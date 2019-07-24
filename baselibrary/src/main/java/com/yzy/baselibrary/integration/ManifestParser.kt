package com.yzy.baselibrary.integration

import android.app.Application
import android.content.pm.PackageManager
import java.util.*

/**
 *description: Manifest进行配置的解析类.
 *@date 2019/7/15
 *@author: yzy.
 */
class ManifestParser constructor(private var context: Application) {

  private val MODULE_VALUE = "ConfigModule"

  private fun parseModule(className: String): ConfigModule {
    val clazz: Class<*>
    try {
      clazz = Class.forName(className)
    } catch (e: ClassNotFoundException) {
      throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
    }

    val module: Any
    try {
      module = clazz.newInstance()
    } catch (e: InstantiationException) {
      throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz",
          e)
    } catch (e: IllegalAccessException) {
      throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz",
          e)
    }

    if (module !is ConfigModule) {
      throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
    }
    return module
  }

  fun parse(): List<ConfigModule> {
    val modules = ArrayList<ConfigModule>()
    try {
      val appInfo = context.packageManager.getApplicationInfo(
          context.packageName, PackageManager.GET_META_DATA)
      if (appInfo.metaData != null) {
        for (key in appInfo.metaData.keySet()) {
          if (MODULE_VALUE == appInfo.metaData.get(key)) {
            modules.add(parseModule(key))
          }
        }
      }
    } catch (e: PackageManager.NameNotFoundException) {
      throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
    }

    return modules
  }
}