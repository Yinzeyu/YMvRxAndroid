package com.yzy.example

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.yzy.commonlibrary.comm.subscribeLifecycle

/**
 *description: 融云登录成功的全局.
 *@date 2019/7/8 15:29.
 *@author: YangYang.
 */
object IMLoginGlobal {

    private const val LIVEDATA_KEY_MESSAGE_IM_LOGIN =
            "livedata_key_message_im_login"

    fun addObserve(
            lifecycleOwner: LifecycleOwner,
            messageCallBack: (isSuccess: Boolean) -> Unit
    ) {
        val observer = Observer<Boolean> {
            it?.let { isSuccess ->
                messageCallBack.invoke(isSuccess)
            }
        }
        subscribeLifecycle(lifecycleOwner) {
            onStart = {
                LiveEventBus
                        .get(LIVEDATA_KEY_MESSAGE_IM_LOGIN, Boolean::class.java)
                        .observeForever(observer)
            }
            onDestroy = {
                LiveEventBus
                        .get(LIVEDATA_KEY_MESSAGE_IM_LOGIN, Boolean::class.java)
                        .removeObserver(observer)
            }
        }
    }

    fun setImLogin(isSuccess: Boolean) {
        LiveEventBus
                .get(LIVEDATA_KEY_MESSAGE_IM_LOGIN, Boolean::class.java)
                .post(isSuccess)
    }
}
