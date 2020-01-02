package com.yzy.example.imModel

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.blankj.utilcode.util.KeyboardUtils

class InputAwareLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : KeyboardAwareLinearLayout(context, attributeSet, defAttrStyle),
    KeyboardAwareLinearLayout.OnKeyboardShownListener {

    private var current: InputView? = null

    init {
        addOnKeyboardShownListener(this)
    }


    override fun onKeyboardShown() {
        hideAttachedInput(true)
    }

    fun show(imeTarget: EditText, input: InputView) {
        if (isKeyboardOpen()) {
            hideSoftKeyboard(imeTarget, Runnable {
                hideAttachedInput(true)
                input.show(getKeyboardHeight(), true)
                current = input
            })
        } else {
            current?.hide(true)
            input.show(getKeyboardHeight(), current != null)
            current = input
        }
    }

    fun getCurrentInput(): InputView? {
        return current
    }

    fun hideCurrentInput(imeTarget: EditText) {
        if (isKeyboardOpen()) {
            hideSoftKeyboard(imeTarget, Runnable { })
        } else {
            hideAttachedInput(false)
        }
    }

    fun hideAttachedInput(instant: Boolean) {
        current?.hide(instant)
        current = null
    }

    fun isInputOpen(): Boolean {
        return isKeyboardOpen() || current?.isShowing() == true
    }

    fun showSoftKeyboard(inputTarget: EditText) {
        postOnKeyboardOpen(Runnable { hideAttachedInput(true) })
        inputTarget.post {
            inputTarget.requestFocus()
            KeyboardUtils.showSoftInput(inputTarget)
        }
    }

    private fun hideSoftKeyboard(inputTarget: EditText, runAfterClose: Runnable?) {
        if (runAfterClose != null) postOnKeyboardClose(runAfterClose)
        KeyboardUtils.hideSoftInput(inputTarget)
    }


    /**
     * 根据键盘显示隐藏做出相应处理的接口
     */
    interface InputView {
        fun show(height: Int, immediate: Boolean)

        fun hide(immediate: Boolean)

        fun isShowing(): Boolean
    }
}