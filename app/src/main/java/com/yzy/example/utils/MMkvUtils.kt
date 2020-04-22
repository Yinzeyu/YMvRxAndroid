package com.yzy.example.utils

import com.yzy.baselibrary.utils.SharePreferencesUtils

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/10 15:14
 */
class MMkvUtils private constructor() {
    private object SingletonHolder {
        val holder = MMkvUtils()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private val USER_UID = "KKMV_KEY_USER_UID"
    private val USER_TOKEN = "KKMV_KEY_USER_TOKEN"

    fun getUid(): Long {
        return SharePreferencesUtils.getLong(USER_UID)
    }


    fun setUid(uid: Long) {
        SharePreferencesUtils.saveLong(USER_UID, uid)
    }

    fun getToken(): String? {
        return SharePreferencesUtils.getString(USER_TOKEN)
    }

    fun setToken(token: String) {
        SharePreferencesUtils.saveString(USER_TOKEN,token)
    }

    fun clearUserInfo() {
        SharePreferencesUtils.saveString(USER_UID,"")
        SharePreferencesUtils.saveString(USER_TOKEN,"")
    }
}