package com.yzy.example.utils

import com.blankj.utilcode.util.GsonUtils
import com.yzy.baselibrary.utils.getSpValue
import com.yzy.baselibrary.utils.putSpValue
import com.yzy.baselibrary.utils.removeSpValue
import com.yzy.example.repository.bean.UserInfo

/**
 * Description:
 * @author: yzy
 * @date: 2019/10/10 15:14
 */
class MMkvUtils private constructor() {
    private object SingletonHolder {
        val holder = MMkvUtils()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private val KEY_PERSONAL_BEAN = "KEY_PERSONAL_BEAN"
    private val KEY_PERSONAL_TOKEN = "KEY_PERSONAL_TOKEN"
    private val KEY_PERSONAL_EXPIRE = "KEY_PERSONAL_EXPIRE"
    private val KEY_COOKIE = "KEY_COOKIE"
    private val NIGHT_MODE = "NIGHT_MODE"

    fun setPersonalBean(personal: UserInfo) =  putSpValue(KEY_PERSONAL_BEAN, GsonUtils.toJson(personal))
    fun getPersonalBean(): UserInfo? {
        var personalBean: UserInfo? = null
        val strBean = getSpValue(KEY_PERSONAL_BEAN,"")
        if (strBean.isNotEmpty() && strBean != "") {
            personalBean = GsonUtils.fromJson(strBean, UserInfo::class.java)
        }
        return personalBean
    }

    fun setToken(token: String)= putSpValue(KEY_PERSONAL_TOKEN, token)

    fun getToken(): String = getSpValue(KEY_PERSONAL_TOKEN,"")

    fun setCookie(cookie: MutableSet<String>) =   putSpValue(KEY_COOKIE, cookie)

    fun getCookie(): MutableSet<String>?  = getSpValue(KEY_COOKIE, mutableSetOf())

    fun setExpire(expire: Long)  = putSpValue(KEY_PERSONAL_EXPIRE, expire)

    fun getExpire(): Long = getSpValue(KEY_PERSONAL_EXPIRE, 0)

    fun getNightMode(): Boolean = getSpValue(NIGHT_MODE,false)

    fun setNightMode(mode: Boolean) =  putSpValue(NIGHT_MODE, mode)

    fun isLogin(): Boolean {
        val token = getToken()
        if (token != "" && token.isNotEmpty()) {
            return true
        }
        return false
    }


    fun remove() {
        removeSpValue(KEY_PERSONAL_BEAN)
        removeSpValue(KEY_PERSONAL_TOKEN)
        removeSpValue(KEY_PERSONAL_EXPIRE)
    }
}