package com.yzy.example.component.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.BaseDialogFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import kotlinx.android.synthetic.main.dialog_action.*

/**
 * Description:
 * @author: yzy
 * @date: 2019/9/24 13:03
 */
class ActionDialog : BaseDialogFragment<NoViewModel,ViewDataBinding>() {

    var hintText: String = "加载中..."

    companion object {
        fun newInstance(
                touchCancel: Boolean = true
        ): ActionDialog {
            val dialog = ActionDialog()
            dialog.mGravity = Gravity.CENTER
            dialog.touchOutside = touchCancel
            dialog.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return dialog
        }
    }

    fun show(fragmentManager: FragmentManager) {
        dialogActionHint?.text = hintText
        show(fragmentManager, "ActionDialog")
    }

    override fun getLayoutId(): Int =R.layout.dialog_action

    override fun initView(savedState: Bundle?) {
        dialogActionHint?.text = hintText
    }

}

//  DSL style
inline fun actionDialog(
        fragmentManager: FragmentManager,
        dsl: ActionDialog.() -> Unit
) {
    val dialog = ActionDialog.newInstance()
            .apply(dsl)
    dialog.show(fragmentManager)
}