package com.yzy.commonlibrary.repository.dialog

import android.view.*
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import com.yzy.baselibrary.base.BaseFragmentDialog
import com.yzy.commonlibrary.R
import kotlinx.android.synthetic.main.dialog_action.view.dialogActionHint

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/9/24 13:03
 */
class ActionDialog : BaseFragmentDialog() {
  override fun initView(view: View) {
    textHint = view.dialogActionHint
    textHint?.text = hintText
  }

  override val contentLayout: Int =R.layout.dialog_action
  var hintText: String = "加载中..."
  private var textHint: TextView? = null

  companion object {
    fun newInstance(
      touchCancel: Boolean = true
    ): ActionDialog {
      val dialog = ActionDialog()
      dialog.mGravity = Gravity.CENTER
      dialog.touchOutside = touchCancel
      dialog.mWidth = ScreenUtils.getScreenWidth() / 3
      return dialog
    }
  }

  fun show(fragmentManager: FragmentManager) {
    textHint?.text = hintText
    show(fragmentManager, "ActionDialog")
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