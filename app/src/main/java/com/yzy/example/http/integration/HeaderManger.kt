package com.yzy.example.http.integration

import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
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

    //获取动态header
    fun getDynamicHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
//    val token = UserManager.getInstance().getToken()
//    if (token != null) {
//      headers["Authorization"] = "bearer $token"
//    } else {
//      headers["Authorization"] = ""
//    }
        return headers
    }

}
