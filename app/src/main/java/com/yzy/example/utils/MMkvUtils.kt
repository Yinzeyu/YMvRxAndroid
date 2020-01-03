package com.yzy.example.utils

import com.tencent.mmkv.MMKV


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

    private val USER_UID = "KKMV_KEY_USER_UID"
    private val KEYBOARD_HEIGHT_PORTRAIT = "keyboard_height_portrait"
    private val USER_TOKEN = "KKMV_KEY_USER_TOKEN"
    private val USER_BEAN = "KKMV_KEY_USER_BEAN"
    private val USER_DRUGSTORE_ID = "KKMV_KEY_USER_DRUGSTORE_ID"

    fun getUid(): Long {
        return MMKV.defaultMMKV().decodeLong(USER_UID, 0L)
    }

    fun setUid(uid: Long) {
        MMKV.defaultMMKV().encode(USER_UID, uid)
    }

    fun getDrugstoreId(): Int {
        return MMKV.defaultMMKV().decodeInt(USER_DRUGSTORE_ID, 0)
    }

    fun setDrugstoreId(drugstoreId: Int) {
        MMKV.defaultMMKV().encode(USER_DRUGSTORE_ID, drugstoreId)
    }

    fun getToken(): String? {
        return MMKV.defaultMMKV().decodeString(USER_TOKEN)
    }

    fun setToken(token: String) {
        MMKV.defaultMMKV().encode(USER_TOKEN, token)
    }

    fun getKeyboardHeightPortrait(): Int? {
        return MMKV.defaultMMKV().decodeInt(KEYBOARD_HEIGHT_PORTRAIT)
    }

    fun setKeyboardHeightPortrait(height: Int) {
        MMKV.defaultMMKV().encode(KEYBOARD_HEIGHT_PORTRAIT, height)
    }

    fun getUserBean(): String? {
        return MMKV.defaultMMKV().decodeString(USER_BEAN)
    }

    fun setUserBean(userBean: String) {
        MMKV.defaultMMKV().encode(USER_BEAN, userBean)
    }

    fun clearUserInfo() {
        MMKV.defaultMMKV().removeValueForKey(USER_UID)
        MMKV.defaultMMKV().removeValueForKey(USER_TOKEN)
        MMKV.defaultMMKV().removeValueForKey(USER_DRUGSTORE_ID)
    }
}