package com.yzy.sociallib.utils

/**
 * description: 用于获取回调的map返回值
 * @date 2019/7/15
 * @author: yzy.
 */
object CallbackDataUtil {
    private const val UID = "uid"
    private const val ID = "id"
    private const val CODE = "code"
    private const val ACCESS_TOKEN = "access_token"
    private const val REFRESH_TOKEN = "refresh_token"
    private const val EXPIRE_TIME = "expire_time"
    private const val OPEN_ID = "openid"

    /**
     * 获取微博授权成功后返回的uid
     */
    fun getID(data: Map<String, String?>, default: String): String {
        return when {
            data.containsKey(UID) -> data[UID] as String
            data.containsKey(ID) -> data[ID] as String
            data.containsKey(OPEN_ID) -> data[OPEN_ID] as String
            data.containsKey(CODE) -> data[CODE] as String
            else -> default
        }
    }

    /**
     * 获取微博授权成功后返回的access_token
     */
    fun getAccessToken(data: Map<String, String?>, default: String): String {
        return if (data.containsKey(ACCESS_TOKEN)) data[ACCESS_TOKEN] as String else default
    }

    /**
     * 获取微博授权成功后返回的REFRESH_TOKEN
     */
    fun getRefreshToken(data: Map<String, String?>, default: String): String {
        return if (data.containsKey(REFRESH_TOKEN)) data[REFRESH_TOKEN] as String else default
    }

    /**
     * 获取微博授权成功后返回的expire_time
     */
    fun getExpireTime(data: Map<String, String?>, default: String): String {
        return if (data.containsKey(EXPIRE_TIME)) data[EXPIRE_TIME] as String else default
    }
}