package com.yzy.example.imModel

import android.content.Context
import android.content.Intent
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity

abstract class ConversationExt {
    protected var context: FragmentActivity? = null
    private var index: Int = 0
    protected var extension: ConversationExtension? = null

    /**
     * 排序的优先级
     */
    abstract fun priority(): Int

    /**
     * icon
     */
    abstract fun iconResId(): Int

    /**
     * title
     */
    abstract fun title(context: Context): String

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)

    /**
     * 执行扩展的操作
     */
    abstract fun execute(inputContainerLayout: FrameLayout)

    /**
     * 是否显示此扩展，根据一些判断如会话类型
     *
     * @return 返回true，表示不显示
     */
    fun filter(): Boolean {
        return false
    }

    /**
     * 和会话界面绑定之后调用
     *
     * @param activity
     */

    open fun onBind(
        activity: FragmentActivity,
        conversationExtension: ConversationExtension,
        index: Int
    ) {
        this.context = activity
        this.extension = conversationExtension
        this.index = index
    }

    /**
     * 跳转页面
     */
    protected fun startActivity(intent: Intent) {
        context?.startActivity(intent)
    }

    /**
     * @param intent
     * @param requestCode 必须在0-256范围之内, 扩展[ConversationExt]内部唯一即可
     */
    protected fun startActivityForResult(intent: Intent, requestCode: Int) {
        if (requestCode < 0 || requestCode > 256) {
            throw IllegalArgumentException("request code should in [0, 256]")
        }
        extension?.startActivityForResult(intent, requestCode, index)
    }

    /**
     *获取扩展跳转Activity的RequestCode
     */
    protected fun getExtentionRequestCode(requestCode: Int): Int? {
        if (requestCode < 0 || requestCode > 256) {
            throw IllegalArgumentException("request code should in [0, 256]")
        }
        return extension?.getExtensionRequestCode(requestCode, index)
    }
}