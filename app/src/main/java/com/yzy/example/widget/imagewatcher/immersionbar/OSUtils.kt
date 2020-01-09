package com.yzy.example.widget.imagewatcher.immersionbar

import android.annotation.SuppressLint
import android.text.TextUtils

/**
 * 手机系统判断
 *
 * @author geyifeng
 * @date 2017/4/18
 */
object OSUtils {
    private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"
    private const val KEY_DISPLAY = "ro.build.display.id"
    /**
     * 判断是否为miui
     * Is miui boolean.
     *
     * @return the boolean
     */
    val isMIUI: Boolean
        get() {
            val property = getSystemProperty(KEY_MIUI_VERSION_NAME, "")
            return !TextUtils.isEmpty(property)
        }

    /**
     * 判断miui版本是否大于等于6
     * Is miui 6 later boolean.
     *
     * @return the boolean
     */
    val isMIUI6Later: Boolean
        get() {
            val version = mIUIVersion
            val num: Int
            return if (!version.isEmpty()) {
                try {
                    num = Integer.valueOf(version.substring(1))
                    num >= 6
                } catch (e: NumberFormatException) {
                    false
                }
            } else {
                false
            }
        }

    /**
     * 获得miui的版本
     * Gets miui version.
     *
     * @return the miui version
     */
    val mIUIVersion: String
        get() = if (isMIUI) getSystemProperty(
            KEY_MIUI_VERSION_NAME,
            ""
        ) else ""

    /**
     * 判断是否为emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    val isEMUI: Boolean=!TextUtils.isEmpty(getSystemProperty(KEY_EMUI_VERSION_NAME, ""))


    /**
     * 得到emui的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    val eMUIVersion: String
        get() = if (isEMUI) getSystemProperty(
            KEY_EMUI_VERSION_NAME,
            ""
        ) else ""

    /**
     * 判断是否为emui3.1版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    val isEMUI3_1: Boolean
        get() {
            val property = eMUIVersion
            return if ("EmotionUI 3" == property || property.contains("EmotionUI_3.1")) {
                true
            } else false
        }

    /**
     * 判断是否为emui3.0版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    val isEMUI3_0: Boolean
        get() {
            val property = eMUIVersion
            return if (property.contains("EmotionUI_3.0")) {
                true
            } else false
        }

    /**
     * 判断是否为emui3.x版本
     * Is emui 3 x boolean.
     *
     * @return the boolean
     */
    val isEMUI3_x: Boolean
        get() = isEMUI3_0 || isEMUI3_1

    /**
     * 判断是否为flymeOS
     * Is flyme os boolean.
     *
     * @return the boolean
     */
    val isFlymeOS: Boolean
        get() = flymeOSFlag.toLowerCase().contains("flyme")

    /**
     * 判断flymeOS的版本是否大于等于4
     * Is flyme os 4 later boolean.
     *
     * @return the boolean
     */
    val isFlymeOS4Later: Boolean
        get() {
            val version = flymeOSVersion
            val num: Int
            return if (!version.isEmpty()) {
                try {
                    num = if (version.toLowerCase().contains("os")) {
                        Integer.valueOf(version.substring(9, 10))
                    } else {
                        Integer.valueOf(version.substring(6, 7))
                    }
                    num >= 4
                } catch (e: NumberFormatException) {
                    false
                }
            } else false
        }

    /**
     * 判断flymeOS的版本是否等于5
     * Is flyme os 5 boolean.
     *
     * @return the boolean
     */
    val isFlymeOS5: Boolean
        get() {
            val version = flymeOSVersion
            val num: Int
            return if (!version.isEmpty()) {
                try {
                    num = if (version.toLowerCase().contains("os")) {
                        Integer.valueOf(version.substring(9, 10))
                    } else {
                        Integer.valueOf(version.substring(6, 7))
                    }
                    num == 5
                } catch (e: NumberFormatException) {
                    false
                }
            } else false
        }

    /**
     * 得到flymeOS的版本
     * Gets flyme os version.
     *
     * @return the flyme os version
     */
    val flymeOSVersion: String
        get() = if (isFlymeOS) getSystemProperty(KEY_DISPLAY, "") else ""

    private val flymeOSFlag: String
        private get() = getSystemProperty(KEY_DISPLAY, "")

    private fun getSystemProperty(key: String, defaultValue: String): String {
        try {
            @SuppressLint("PrivateApi") val clz =
                Class.forName("android.os.SystemProperties")
            val method =
                clz.getMethod("get", String::class.java, String::class.java)
            return method.invoke(clz, key, defaultValue) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }
}