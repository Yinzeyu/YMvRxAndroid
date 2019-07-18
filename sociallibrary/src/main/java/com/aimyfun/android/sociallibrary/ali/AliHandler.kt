package com.aimyfun.android.sociallibrary.ali

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.aimyfun.android.sociallibrary.PayBean
import com.aimyfun.android.sociallibrary.PlatformConfig
import com.aimyfun.android.sociallibrary.PlatformConfig.Platform
import com.aimyfun.android.sociallibrary.SSOHandler
import com.aimyfun.android.sociallibrary.SocialErrorCodeConstants
import com.aimyfun.android.sociallibrary.listener.AuthListener
import com.aimyfun.android.sociallibrary.listener.PayListener
import com.alipay.sdk.app.AuthTask
import com.alipay.sdk.app.PayTask

/**
 * description:
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class AliHandler(
    /**
     * 传入token
     */
    private val authToken: String
) : SSOHandler() {

    /**
     * type
     */
    private var mConfig: PlatformConfig.Ali? = null
    /**
     * 支付宝登录
     */
    private var mAuthListener: AuthListener? = null

    /**
     * 支付宝支付
     */
    private var mPayListener: PayListener? = null
    private var mHandler: Handler? = null

    override val isInstall: Boolean
        get() = super.isInstall

    override fun onCreate(
        context: Context,
        config: Platform?
    ) {
        super.onCreate(context, config)
        this.mConfig = config as PlatformConfig.Ali
    }

    private fun setHandle(mAce: Activity) {
        mHandler = object : Handler(mAce.mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    SDK_PAY_FLAG -> {
                        val resultStatus = (msg.obj as Map<*, *>)["resultStatus"]
                        if (ALI_9000 == resultStatus) {
                            mPayListener!!.onComplete(mConfig!!.name)
                        } else if (ALI_6001 == resultStatus) {
                            mPayListener!!.onCancel(mConfig!!.name)
                        } else {
                            mPayListener!!.onError(
                                mConfig!!.name,
                                SocialErrorCodeConstants.authError
                            )
                        }
                    }
                    SDK_AUTH_FLAG -> {
                        val resultStatus = (msg.obj as MutableMap<*, *>)["resultStatus"]
                        if (ALI_9000 == resultStatus) {
                            val result = (msg.obj as MutableMap<*, *>)["result"]
                            val aliToken = getAliToken(result.toString())
                            val map = mutableMapOf<String, String?>()
                            if (aliToken != null) {
                                map["auth_code"] = aliToken["auth_code"];
                                map["auth_code"] = aliToken["auth_code"]
                            }
                            if (aliToken != null) {
                                map["user_id"] = aliToken["user_id"]
                            }
                            mAuthListener!!.onComplete(mConfig!!.name, map)
                        } else if (ALI_6001 == resultStatus) {
                            mAuthListener!!.onCancel(mConfig!!.name)
                        } else {
                            mAuthListener!!.onError(
                                mConfig!!.name,
                                SocialErrorCodeConstants.payError
                            )
                        }
                    }
                    else -> {
                    }
                }
                remove()
            }
        }
    }

    private fun remove() {
        if (mHandler != null) {
            mHandler!!.removeCallbacksAndMessages(null)
        }
    }

    override fun authorize(
        activity: Activity,
        authListener: AuthListener
    ) {
        if (TextUtils.isEmpty(authToken)) {
            Log.e("aliToken", "no authToken")
            return
        }
        setHandle(activity)
        this.mAuthListener = authListener
        auth(activity)
    }

    /**
     * 调用该方法进行支付宝sdk调用
     */
    private fun auth(
        activity: Activity
    ) {
        // 必须异步调用
        Thread {
            // 构造AuthTask 对象
            // 调用授权接口，获取授权结果
            val result = AuthTask(activity).authV2(authToken, true)
            val msg = Message()
            msg.what = SDK_AUTH_FLAG
            msg.obj = result
            if (mHandler != null) {
                mHandler!!.sendMessage(msg)
            }
        }.start()
    }

    override fun pay(activity: Activity, payBean: PayBean, shareListener: PayListener) {
        mPayListener = shareListener
        mPayListener!!.onError(mConfig!!.name, SocialErrorCodeConstants.payError)
        setHandle(activity)
        pay(payBean.params, activity)
        return
    }

    /**
     * 调用该方法进行支付宝sdk调用
     */
    private fun pay(
        payInfo: String?,
        activity: Activity
    ) {
        // 必须异步调用
        Thread {
            // 构造AuthTask 对象
            // 调用授权接口，获取授权结果
            val result = PayTask(activity).payV2(payInfo!!.replace("&amp", "&"), true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            if (mHandler != null) {
                mHandler!!.sendMessage(msg)
            }
        }.start()
    }

    companion object {
        private val SDK_PAY_FLAG = 11
        private val SDK_AUTH_FLAG = 2
        /**
         * 支付宝支付成功
         */
        val ALI_9000 = "9000"
        /**
         * 支付宝支付失败
         */
        val ALI_4000 = "4000"
        /**
         * 支付宝支付取消
         */
        val ALI_6001 = "6001"

        val STRING_ONE = "0"

        private fun getAliToken(str: String): MutableMap<String, String>? {
            if (TextUtils.isEmpty(str)) return null
            val map = mutableMapOf<String, String>()
            val udderCode = str.split("&".toRegex())
                .dropLastWhile({ it.isEmpty() })
                .toTypedArray()
            for (s in udderCode) {
                var value: String
                val split = s.split("=".toRegex())
                    .dropLastWhile({ it.isEmpty() })
                    .toTypedArray()
                try {
                    if (split.size > 1) {
                        value = split[1]
                    } else {
                        value = ""
                    }
                } catch (e: Exception) {
                    e.message
                    value = ""
                }

                val key = split[0]
                if ("auth_code" == key) {
                    map["auth_code"] = value
                } else if ("user_id" == key) {
                    map["user_id"] = value
                } else if ("success" == key) {
                    map["success"] = value
                } else if ("result_code" == key) {
                    map["result_code"] = value
                }
            }
            return map
        }
    }
}
