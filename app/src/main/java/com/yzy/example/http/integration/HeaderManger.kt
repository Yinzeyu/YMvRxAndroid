package com.yzy.example.http.integration

import android.os.UserManager
import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import com.yzy.example.http.integration.HeaderManger.Companion.getInstance
import com.yzy.example.utils.MMkvUtils
import java.util.*

/**
 * description :
 *@date 2019/7/15
 *@author: yzy.
 */
internal class HeaderManger {
    private object Holder {
        val instance = HeaderManger()
    }

    companion object {
        fun getInstance(): HeaderManger = Holder.instance
    }

    //获取固定header
    fun getStaticHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        val map = HashMap<String, String>()
        map["platform"] = "0"  //1：ios    0:Android
        map["appVersion"] = AppUtils.getAppVersionName()
        headers["Connection"] = "close"
        headers["Accept"] = "*/*"
        headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"
        headers["Charset"] = "UTF-8"
        headers["aimy-divers"] = Gson().toJson(map)
        return headers
    }

    private var baseUrl: String? = null

    /**
     * 动态获取需要更改的Url
     */
    fun getDynamicBaseUrl(): String? {
        return baseUrl
    }
    /**
     * 动态设置需要更改的Url
     * 1.在请求之前设置此Url
     * 2.使用完之后记得设置为null
     */
    fun setDynamicBaseUrl(url: String?): String? {
        baseUrl =url
        return baseUrl
    }
    //获取动态header
    fun getDynamicHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        val token = MMkvUtils.instance.getToken()
        if (token != null) {
            headers["token"] = "$token"
        } else {
            headers["token"] = ""
        }
        return headers
    }
}
