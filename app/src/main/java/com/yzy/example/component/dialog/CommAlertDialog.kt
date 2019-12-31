package com.yzy.example.component.dialog

import android.text.TextUtils
import android.view.*
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.BaseFragmentDialog
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import com.yzy.example.extention.loadCircle
import com.yzy.example.utils.AimyInputHelper
import kotlinx.android.synthetic.main.dialog_comm_alert.view.*
import kotlinx.android.synthetic.main.layout_apply2_join_circle.view.*

/**
 * description: 通用的提示框
 * @date: 2019-05-21 17:55
 * @author: Grieey
 */
class CommAlertDialog : BaseFragmentDialog() {

  /**
   * 提示框的类型
   */
  var type = AlertDialogType.DOUBLE_BUTTON
  var title: String? = null
  var content: String? = null
  /**
   * 默认 取消
   */
  var cancelText: String? = null
  /**
   * 默认 确定
   */
  var confirmText: String? = null
  var confirmTextColor: Int? = null
  var cancelCallback: (() -> Unit)? = null
  var confirmCallback: (() -> Unit)? = null

  var edit2SomebodyCallback: ((text: String) -> Unit)? = null
  var edit2SomebodyCover: String? = null
  var edit2SomebodyName: String? = null
  var edit2SomeBodyContentMust = false
  var centerHorizontal: Boolean = false

  /**
   * 带编辑框的弹窗的编辑框样式
   * 默认样式：
   * android:padding="12dp"
   * android:gravity="start"
   * android:textColor="@color/c_ff333333"
   * android:textSize="12sp"
   * android:textColorHint="@color/c_ff999999"
   * android:hint="0-20字简介"
   */
  var edit2SomebodyStyle: (EditText.() -> Unit)? = null

  override fun initView(view: View) {
    initType(view)
    initView(view)
    initEdit2Somebody(view)
    view.commAlertCancel.pressEffectAlpha()
    view.commAlertConfirm.pressEffectAlpha()
    title?.let {
      view.commAlertTitle.text = it
    }
    view.commAlertTitle.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
    content?.let {
      view.commAlertContent.post {
        if (view.commAlertContent.lineCount == 1){
          view.commAlertContent.gravity = Gravity.CENTER_HORIZONTAL
        }
      }
      view.commAlertContent.text = it
    }

    view.commAlertCancel.click {
      cancelCallback?.invoke()
      dismiss()
    }
    cancelText?.let {
      view.commAlertCancel.text = it
    }

    view.commAlertConfirm.click {
      confirmCallback?.invoke()
      dismiss()
    }
    confirmText?.let {
      view.commAlertConfirm.text = it
    }
    confirmTextColor?.let {
      view.commAlertConfirm.setTextColor(it)
    }
    if (centerHorizontal) view.commAlertContent.gravity = Gravity.CENTER_HORIZONTAL
  }

  private fun initEdit2Somebody(view: View) {
    edit2SomebodyName?.let {
      view.applyName.text = it
    }
    edit2SomebodyCover?.let {
      view.applyCover.loadCircle(it)
    }
    edit2SomebodyCallback?.let {edT->
      AimyInputHelper.wrapCommLimit(view.applyEt, 40, 0) { hasInputLength, _ ->
        if (edit2SomeBodyContentMust) {
          if (hasInputLength > 0) {
            view.commAlertConfirm.isEnabled = true
            view.commAlertConfirm.alpha = 1f
          } else {
            view.commAlertConfirm.isEnabled = false
            view.commAlertConfirm.alpha = 0.2f
          }
        }
      }
      view.commAlertConfirm.click {
        edT.invoke(view.applyEt.text.toString())
        dismiss()
      }
    }
    edit2SomebodyStyle?.let {
      view.applyEt.apply(it)
    }

    if (edit2SomeBodyContentMust) {
      view.commAlertConfirm.isEnabled = false
      view.commAlertConfirm.alpha = 0.2f
    }

  }

  private fun initType(view: View) {
    when (type) {
      AlertDialogType.SINGLE_BUTTON -> {
        view.commAlertConfirm.visible()
        view.commAlertContent.visible()
      }
      AlertDialogType.DOUBLE_BUTTON -> {
        view.commAlertCancel.visible()
        view.commBtnLine.visible()
        view.commAlertContent.visible()
      }
      AlertDialogType.EDIT2_SOMEBODY -> {
        view.commAlertCancel.visible()
        view.commBtnLine.visible()
        view.commApply.visible()
      }
    }
  }

  override fun contentLayout(): Int =R.layout.dialog_comm_alert


  companion object {
    fun newInstance(): CommAlertDialog = CommAlertDialog()
  }
}

/**
 * 提示框类型
 */
enum class AlertDialogType {
  /**
   * 单个按钮的文本提示
   */
  SINGLE_BUTTON,
  /**
   * 取消、确认按钮的文本提示
   */
  DOUBLE_BUTTON,
  /**
   * 带头像名称和编辑文本类型的弹窗
   */
  EDIT2_SOMEBODY
}

//  DSL style
inline fun commAlertDialog(
  fragmentManager: FragmentManager,
  dsl: CommAlertDialog.() -> Unit
) {
  val dialog = CommAlertDialog.newInstance()
      .apply(dsl)
  dialog.mGravity = Gravity.CENTER
  dialog.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
  dialog.touchOutside = false
  dialog.show(fragmentManager, "CommAlertDialog")
}