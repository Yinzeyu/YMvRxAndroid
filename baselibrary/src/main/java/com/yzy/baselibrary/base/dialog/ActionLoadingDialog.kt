package com.yzy.baselibrary.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.R
import com.yzy.baselibrary.extention.showTextNotNull

/**
 *description: 操作类型的loading默认点击外部不消失，按返回键不消失.
 *@date 2019/7/15
 *@author: yzy.
 */
class ActionLoadingDialog : BaseFragmentDialog() {

    private var loadingTv: TextView? = null
    private var tips: String? = null

    init {
        touchOutside = false
        lowerBackground = true
        isCancelable = false
    }

    override fun setView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_loading_action, container, false)
        loadingTv = view.findViewById(R.id.loadingTv)
        loadingTv?.showTextNotNull(tips)
        return view
    }

    fun setTips(text: String?) {
        tips = text
        loadingTv?.showTextNotNull(tips)
    }

    companion object {
        fun newInstance(): ActionLoadingDialog = ActionLoadingDialog()
    }
}

//  DSL style
inline fun actionLoadingDialog(
    fragmentManager: FragmentManager,
    dsl: ActionLoadingDialog.() -> Unit
): ActionLoadingDialog {
    val dialog = ActionLoadingDialog.newInstance()
    dialog.apply(dsl)
    dialog.show(fragmentManager, "ActionLoadingDialog")
    return dialog
}