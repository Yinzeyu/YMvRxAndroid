package com.yzy.example.component.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.BaseDialogFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.databinding.FragmentLoginBinding
import com.yzy.example.utils.MMkvUtils
import kotlinx.android.synthetic.main.layout_login_dialog.*

class LoginDialog : BaseDialogFragment<NoViewModel,FragmentLoginBinding>() {
    var mainToLogin: (() -> Unit)? = null
    override fun initView(savedState: Bundle?) {
        loginBtn?.click {
            dismiss()
            MMkvUtils.instance.remove()
            mainToLogin?.invoke()
        }
    }

    companion object {
        fun newInstance(): LoginDialog {
            val dialog = LoginDialog()
            dialog.mGravity = Gravity.CENTER
            dialog.touchOutside = false
            dialog.isCancelable = false
            dialog.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
            return dialog
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, "LoginDialog")
    }

    override fun getLayoutId(): Int = R.layout.layout_login_dialog

}

//  DSL style
inline fun initLoginDialog(
    fragmentManager: FragmentManager,
    dsl: LoginDialog.() -> Unit
) {
    val dialog = LoginDialog.newInstance().apply(dsl)
    dialog.show(fragmentManager)
}
