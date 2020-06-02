package com.yzy.example.utils

import com.blankj.utilcode.util.GsonUtils
import com.yzy.baselibrary.utils.SharePreferencesUtils
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

    fun setPersonalBean(personal: UserInfo) {
        SharePreferencesUtils.saveString(KEY_PERSONAL_BEAN, GsonUtils.toJson(personal))
    }

    fun getPersonalBean(): UserInfo? {
        var personalBean: UserInfo? = null
        val strBean = SharePreferencesUtils.getString(KEY_PERSONAL_BEAN)
        if (strBean.isNotEmpty() && strBean != "") {
            personalBean = GsonUtils.fromJson(strBean, UserInfo::class.java)
        }
        return personalBean
    }

    fun setToken(token: String) {
        SharePreferencesUtils.saveString(KEY_PERSONAL_TOKEN, token)
    }

    fun getToken(): String {
        return SharePreferencesUtils.getString(KEY_PERSONAL_TOKEN)
    }

    fun setExpire(expire: Long) {
        SharePreferencesUtils.saveLong(KEY_PERSONAL_EXPIRE, expire)
    }

    fun getExpire(): Long {
        return SharePreferencesUtils.getLong(KEY_PERSONAL_EXPIRE)
    }


    fun isLogin(): Boolean {
        val token = getToken()
        if (token != "" && token.isNotEmpty()) {
            return true
        }
        return false
    }


    fun remove() {
        SharePreferencesUtils.remove(KEY_PERSONAL_BEAN)
        SharePreferencesUtils.remove(KEY_PERSONAL_TOKEN)
        SharePreferencesUtils.remove(KEY_PERSONAL_EXPIRE)
    }
}