package com.yzy.example.component.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.BaseDialogFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import com.yzy.example.extention.loadCircle
import com.yzy.example.utils.AimyInputHelper
import kotlinx.android.synthetic.main.dialog_comm_alert.*
import kotlinx.android.synthetic.main.layout_apply2_join_circle.*

/**
 * description: 通用的提示框
 * @date: 2019-05-21 17:55
 * @author: Grieey
 */
class CommAlertDialog : BaseDialogFragment<NoViewModel,ViewDataBinding>() {

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

  override fun initView(savedState: Bundle?) {
    initType()
    initEdit2Somebody()
    commAlertCancel.pressEffectAlpha()
    commAlertConfirm.pressEffectAlpha()
    title?.let {
      commAlertTitle.text = it
    }
   commAlertTitle.visibility = if (TextUtils.isEmpty(title))View.GONE else View.VISIBLE
    content?.let {
     commAlertContent.post {
        if (commAlertContent.lineCount == 1){
         commAlertContent.gravity = Gravity.CENTER_HORIZONTAL
        }
      }
     commAlertContent.text = it
    }

   commAlertCancel.click {
      cancelCallback?.invoke()
      dismiss()
    }
    cancelText?.let {
     commAlertCancel.text = it
    }

   commAlertConfirm.click {
      confirmCallback?.invoke()
      dismiss()
    }
    confirmText?.let {
     commAlertConfirm.text = it
    }
    confirmTextColor?.let {
     commAlertConfirm.setTextColor(it)
    }
    if (centerHorizontal)commAlertContent.gravity = Gravity.CENTER_HORIZONTAL
  }

  private fun initEdit2Somebody() {
    edit2SomebodyName?.let {
     applyName.text = it
    }
    edit2SomebodyCover?.let {
  applyCover.loadCircle(it)
    }
    edit2SomebodyCallback?.let {edT->
      AimyInputHelper.wrapCommLimit(applyEt, 40, 0) { hasInputLength, _ ->
        if (edit2SomeBodyContentMust) {
          if (hasInputLength > 0) {
           commAlertConfirm.isEnabled = true
           commAlertConfirm.alpha = 1f
          } else {
           commAlertConfirm.isEnabled = false
           commAlertConfirm.alpha = 0.2f
          }
        }
      }
     commAlertConfirm.click {
        edT.invoke(applyEt.text.toString())
        dismiss()
      }
    }
    edit2SomebodyStyle?.let {
     applyEt.apply(it)
    }

    if (edit2SomeBodyContentMust) {
     commAlertConfirm.isEnabled = false
     commAlertConfirm.alpha = 0.2f
    }

  }

  private fun initType() {
    when (type) {
      AlertDialogType.SINGLE_BUTTON -> {
        commAlertConfirm.visible()
       commAlertContent.visible()
      }
      AlertDialogType.DOUBLE_BUTTON -> {
        commAlertCancel.visible()
        commBtnLine.visible()
       commAlertContent.visible()
      }
      AlertDialogType.EDIT2_SOMEBODY -> {
       commAlertCancel.visible()
        commBtnLine.visible()
        commApply.visible()
      }
    }
  }



  companion object {
    fun newInstance(): CommAlertDialog = CommAlertDialog()
  }

  override fun getLayoutId(): Int = R.layout.dialog_comm_alert


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