package com.yzy.example.imModel

import android.content.Context
import android.graphics.Rect
import android.preference.PreferenceManager
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.getBottomStatusHeight
import java.util.HashSet
import kotlin.math.max


open class KeyboardAwareLinearLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : LinearLayoutCompat(context, attributeSet, defAttrStyle) {


    private val hiddenListeners = HashSet<OnKeyboardHiddenListener>()
    private val shownListeners = HashSet<OnKeyboardShownListener>()
    private var minKeyboardSize: Int = 0
    private var defaultCustomKeyboardSize: Int = 0
    private var statusBarHeight: Int = 0


    private var keyboardOpen = false

    init {
        val statusBarRes = resources.getIdentifier("status_bar_height", "dimen", "android")
        minKeyboardSize = SizeUtils.dp2px(50F)
        defaultCustomKeyboardSize = SizeUtils.dp2px(220F)
        statusBarHeight = if (statusBarRes > 0) resources.getDimensionPixelSize(statusBarRes) else 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        updateKeyboardState()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun updateKeyboardState() {
        val rect = Rect()
        //使用最外层布局填充，进行测算计算
        getWindowVisibleDisplayFrame(rect)
        val heightDiff = rootView.height - (rect.bottom - rect.top)
        val keyboardHeight = max(0, heightDiff - statusBarHeight - getBottomStatusHeight(context))
        if (keyboardHeight > minKeyboardSize) {
            setKeyboardPortraitHeight(keyboardHeight)
            if (!keyboardOpen) onKeyboardOpen()
        } else if (keyboardOpen) {
            onKeyboardClose()
        }
    }


    protected fun onKeyboardOpen() {
        keyboardOpen = true
        notifyShownListeners()
    }

    protected fun onKeyboardClose() {
        keyboardOpen = false
        notifyHiddenListeners()
    }

    fun isKeyboardOpen(): Boolean {
        return keyboardOpen
    }

    fun getKeyboardHeight(): Int {
        return getKeyboardPortraitHeight()
    }

    private fun getKeyboardPortraitHeight(): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt("keyboard_height_portrait", defaultCustomKeyboardSize)
    }

    private fun setKeyboardPortraitHeight(height: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("keyboard_height_portrait", height).apply()
    }

    fun postOnKeyboardClose(runnable: Runnable) {
        if (keyboardOpen) {
            addOnKeyboardHiddenListener(object : OnKeyboardHiddenListener {
                override fun onKeyboardHidden() {
                    removeOnKeyboardHiddenListener(this)
                    runnable.run()
                }
            })
        } else {
            runnable.run()
        }
    }

    fun postOnKeyboardOpen(runnable: Runnable) {
        if (!keyboardOpen) {
            addOnKeyboardShownListener(object : OnKeyboardShownListener {
                override fun onKeyboardShown() {
                    removeOnKeyboardShownListener(this)
                    runnable.run()
                }
            })
        } else {
            runnable.run()
        }
    }

    fun addOnKeyboardHiddenListener(listener: OnKeyboardHiddenListener) {
        hiddenListeners.add(listener)
    }

    fun removeOnKeyboardHiddenListener(listener: OnKeyboardHiddenListener) {
        hiddenListeners.remove(listener)
    }

    fun addOnKeyboardShownListener(listener: OnKeyboardShownListener) {
        shownListeners.add(listener)
    }

    fun removeOnKeyboardShownListener(listener: OnKeyboardShownListener) {
        shownListeners.remove(listener)
    }

    private fun notifyHiddenListeners() {
        val listeners = HashSet(hiddenListeners)
        for (listener in listeners) {
            listener.onKeyboardHidden()
        }
    }

    private fun notifyShownListeners() {
        val listeners = HashSet(shownListeners)
        for (listener in listeners) {
            listener.onKeyboardShown()
        }
    }

    interface OnKeyboardHiddenListener {
        fun onKeyboardHidden()
    }

    interface OnKeyboardShownListener {
        fun onKeyboardShown()
    }
}