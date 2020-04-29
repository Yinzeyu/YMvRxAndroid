package com.yzy.example.component.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommDialogFragment

/**
 * Description:
 * @date: 2019/9/24 13:03
 */
class UpdateDialog : CommDialogFragment<NoViewModel,ViewDataBinding>() {
    override fun getLayoutId(): Int = R.layout.dialog_update


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
    override fun initView(savedState: Bundle?) {
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