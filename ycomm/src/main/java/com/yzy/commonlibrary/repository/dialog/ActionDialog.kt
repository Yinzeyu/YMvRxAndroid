package com.yzy.commonlibrary.repository.dialog

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.ScreenUtils
import com.yzy.baselibrary.base.BaseFragmentDialog
import com.yzy.commonlibrary.R
import kotlinx.android.synthetic.main.dialog_action.*
import kotlinx.android.synthetic.main.dialog_action.view.dialogActionHint

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/9/24 13:03
 */
class ActionDialog : BaseFragmentDialog() {
  override fun contentLayout(): Int =R.layout.dialog_action
  override fun initView(view: View) {
    dialogActionHint?.text = hintText
  }
  var hintText: String = "加载中..."

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
    dialogActionHint?.text = hintText
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