package com.yzy.commonlibrary.integration

import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.commonlibrary.constants.StringConstants
import com.yzy.commonlibrary.db.UserBean
import com.yzy.commonlibrary.repository.local.UserLocalDataSource

/**
 * description :
 *
 *@date 2019/7/15
 *@author: yzy.
 */
class UserManager private constructor() {
    private val userLocalDataSource = UserLocalDataSource()
    private var mUserBean: UserBean? = null


    private object Holder {
        val instance = UserManager()
    }

    companion object {
        fun getInstance(): UserManager = Holder.instance
    }

    //保存用户信息
    fun saveUser(userBean: UserBean) {
        userLocalDataSource.getUserBean() != null
        mUserBean = userBean
        userLocalDataSource.saveUserBean(userBean)
//    if (!has) {
//      MessageModule.initlogin(Utils.getApp())
//      //初始化连麦
//      RTVoiceManager.init(BaseApplication.INSTANCE)
//    }
//    UserInfoGlobal.setValue(userBean)
        CrashReport.setUserId(userBean.userId.toString())
    }

    //获取用户信息
    fun getUserBean(): UserBean? {
        if (mUserBean == null) mUserBean = userLocalDataSource.getUserBean()
        return mUserBean
    }

    //获取用户ID
    fun getUserID(): Long? {
        if (mUserBean == null) {
            mUserBean = userLocalDataSource.getUserBean()
        }
        return mUserBean?.userId
    }

    //获取用户cityCode
    fun getUserCityCode(): String {
        return mUserBean?.cityCode ?: ""
    }

    //用户登出
    fun loginOut() {
        clearUser()
        clearToken()
    }

    //清除用户信息
    private fun clearUser() {
        mUserBean = null
        userLocalDataSource.clearUserBean()
    }

    //保存Token
    internal fun saveToken(token: String) {
        MMKV.defaultMMKV().encode(StringConstants.Mmkv.KEY_TOKEN, token)
    }

    //获取Token
    fun getToken(): String? {
        return MMKV.defaultMMKV().decodeString(StringConstants.Mmkv.KEY_TOKEN)
    }

    //清除Token
    private fun clearToken() {
        MMKV.defaultMMKV().removeValueForKey(StringConstants.Mmkv.KEY_TOKEN)
    }
}