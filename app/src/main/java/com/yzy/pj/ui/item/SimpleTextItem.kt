package com.yzy.pj.ui.item

import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectBgColor
import com.yzy.pj.R
import kotlinx.android.synthetic.main.item_simple_text.view.*

/**
 * Description:简单的文字显示item
 * @author: caiyoufei
 * @date: 2019/10/17 16:59
 */
@EpoxyModelClass(layout = R.layout.item_simple_text)
abstract class SimpleTextItem : BaseEpoxyModel<BaseEpoxyHolder>() {
  @EpoxyAttribute
  var msg: String = ""
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  var onItemClick: ((msg: String?) -> Unit)? = null
  @EpoxyAttribute
  var textSizePx: Float = SizeUtils.dp2px(14f)
      .toFloat()
  @EpoxyAttribute
  var paddingStartPx: Int = SizeUtils.dp2px(12f)
  @EpoxyAttribute
  var paddingEndPx: Int = SizeUtils.dp2px(12f)
  @EpoxyAttribute
  var paddingTopPx: Int = SizeUtils.dp2px(10f)
  @EpoxyAttribute
  var paddingBottomPx: Int = SizeUtils.dp2px(10f)
  @EpoxyAttribute
  var textColor: Int = Color.parseColor("#333333")
  @EpoxyAttribute
  var gravity: Int = Gravity.CENTER
  @EpoxyAttribute
  var needBold: Boolean = false

  override fun onBind(itemView: View) {
//    itemView.itemSimpleTv.setTextColor(textColor)
//    itemView.itemSimpleTv.gravity = gravity
//    itemView.itemSimpleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx)
//    itemView.itemSimpleTv.typeface =
//      Typeface.defaultFromStyle(if (needBold) Typeface.BOLD else Typeface.NORMAL)
//    itemView.itemSimpleTv.setPadding(paddingStartPx, paddingTopPx, paddingEndPx, paddingBottomPx)
//    itemView.itemSimpleTv.text = msg
//    if (onItemClick == null) {
//      itemView.pressEffectDisable()
//    } else {
//      itemView.pressEffectBgColor()
//    }
//    itemView.click { onItemClick?.invoke(msg) }
  }
}