package com.yzy.baselibrary.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.R
import com.yzy.baselibrary.extention.showTextNotNull

/**
 *description: 操作类型的loading默认点击外部不消失，按返回键不消失.
 *@date 2019/7/15
 *@author: yzy.
 */
class ActionLoadingDialog(override val contentLayout: Int = R.layout.dialog_loading_action) :
    BaseFragmentDialog() {
    override fun initView(view: View) {
        loadingTv = view.findViewById(R.id.loadingTv)
        ivCenterView = view.findViewById(R.id.ivCenterView)
        ivCenterView?.setImageResource(res)
        loadingTv?.showTextNotNull(tips)
    }

    private var loadingTv: TextView? = null
    private var ivCenterView: ImageView? = null
    private var tips: String? = null
    private var res: Int = 0

    init {
        touchOutside = false
        lowerBackground = true
        isCancelable = false
    }

    fun setTips(text: String?) {
        tips = text
        loadingTv?.showTextNotNull(tips)
    }

    fun setImageRes(resImage: Int) {
        res = resImage
        ivCenterView?.setImageResource(res)
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