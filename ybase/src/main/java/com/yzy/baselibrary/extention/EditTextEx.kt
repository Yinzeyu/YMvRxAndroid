package com.yzy.baselibrary.extention

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView


/**
 * description:  editText 监听事件
 *@date 2019/7/15
 *@author: yzy.
 */

inline fun TextView.addTextChangedListener(init: EditTextChangeListener.() -> Unit) {
    addTextChangedListener(EditTextChangeListener().apply(init))
}

class EditTextChangeListener : TextWatcher {
    private var afterTextChanged: ((s: Editable?) -> Unit)? = null
    private var beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    private var onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    fun afterTextChanged(listener: ((s: Editable?) -> Unit)?) {
        this.afterTextChanged = listener
    }

    fun beforeTextChanged(listener: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null) {
        this.beforeTextChanged = listener
    }

    fun onTextChanged(listener: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)?) {
        this.onTextChanged = listener
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

}