package com.yzy.example.imModel

import android.content.Intent
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager

class ConversationExtension(
    private val activity: FragmentActivity,
    private val inputContainerLayout: FrameLayout,//包含整个输入区域的FrameLayout
    private val extViewPager: ViewPager
) {
    /**
     * 扩展的列表
     */
    private var extList = mutableListOf<ConversationExt>()
    private var adapter: ConversationExtPagerAdapter? = null

    private var hideOnScroll = true

    /**
     * 初始化，需要在所有的扩展菜单添加之后
     */
    fun init() {
        extList.addAll(ConversationExtManager.getInstance().getConversationExtList())
        setupExtViewPager(extViewPager)
        for (i in extList.indices) {
            extList[i].onBind(activity, this, i)
        }
    }

    private fun setupExtViewPager(viewPager: ViewPager) {
        if (extList.isEmpty()) {
            return
        }
        adapter =
            ConversationExtPagerAdapter(extList, object : ConversationExtPageView.OnExtViewClickListener {
                override fun onClick(index: Int) {
                    if (index > -1 && index < extList.size) {
                        extList[index].execute(inputContainerLayout)
                    }
                }
            })
        viewPager.adapter = adapter
    }

    /**
     * 设置icon的样式
     */
    fun setImageViewStyle(style: ImageView.() -> Unit) {
        adapter?.setImageViewStyle(style)
    }

    /**
     * 设置文字的样式
     */
    fun setTextViewStyle(style: TextView.() -> Unit) {
        adapter?.setTextViewStyle(style)
    }

    /**
     * 重置所有扩展菜单
     */
    fun reset() {
        var childCount = inputContainerLayout.childCount
        // 不删除最下层的layout，最下层是咱们的input panel
        while (--childCount > 0) {
            inputContainerLayout.removeViewAt(childCount)
        }
        hideOnScroll = true
    }

    /**
     * 是否可以滑动
     */
    fun canHideOnScroll(): Boolean {
        return hideOnScroll
    }

    fun disableHideOnScroll() {
        this.hideOnScroll = false
    }

    /**
     * 低16位是合法的request code
     *
     *
     * 第15位强制置1，表示从ConversationExtension发起的
     *
     *
     * 第14-7位，共8个位，是[ConversationExt]可用所有request code, 即request code只能在0-256
     *
     *
     * 第6-0位，共7个位，是index
     *
     * @param intent
     * @param requestCode
     * @param index
     */
    fun startActivityForResult(intent: Intent, requestCode: Int, index: Int) {
        activity.startActivityForResult(intent, getExtensionRequestCode(requestCode, index))
    }

    /**
     * 获取跳转Activity的RequestCode
     */
    fun getExtensionRequestCode(requestCode: Int, index: Int): Int {
        var extRequestCode = requestCode shl 7 or REQUEST_CODE_MIN
        extRequestCode += index
        return extRequestCode
    }

    /**
     * 跳转到菜单功能操作之后的ActivityResult
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val index = requestCode and 0x7F
        if (index > -1 && index < extList.size) {
            extList[index].onActivityResult(requestCode shr 7 and 0xFF, resultCode, data)
        }
    }

    companion object {
        /**
         * requestCode的最小值
         */
        const val REQUEST_CODE_MIN = 0x8000
    }
}