package com.yzy.example.component.dialog

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import com.yzy.baselibrary.base.BaseFragmentDialog
import com.yzy.example.R
import com.yzy.example.component.comm.CommDialogFragment
import kotlinx.android.synthetic.main.dialog_action.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/9/24 13:03
 */
class UpdateDialog : CommDialogFragment() {
    override fun contentLayout(): Int = R.layout.dialog_update
    override fun initView(view: View) {
    }


    companion object {
        fun newInstance(
                touchCancel: Boolean = true
        ): UpdateDialog {
            val dialog = UpdateDialog()
            dialog.mGravity = Gravity.RIGHT
            dialog.touchOutside = touchCancel
            dialog.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.mHeight = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.mSoftInputMode= SOFT_INPUT_ADJUST_PAN
            return dialog
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "UpdateDialog")
    }

}

//  DSL style
inline fun updateDialog(
        fragmentManager: FragmentManager,
        dsl: UpdateDialog.() -> Unit
) {
    val dialog = UpdateDialog.newInstance()
            .apply(dsl)
    dialog.show(fragmentManager)
}