package com.yzy.example.component.album

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.baselibrary.extention.setClickNotNull
import com.yzy.example.R

/**
 * Description: 图库选择的图片item
 * @author: caiyoufei
 * @date: 19-5-15 下午6:51
 */
@EpoxyModelClass(layout = R.layout.item_pic_camera)
abstract class PicCameraItem : BaseEpoxyModel<BaseEpoxyHolder>() {
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  var onItemClick: (() -> Unit)? = null
  //图片大小
  private val sizeFloat = ((Utils.getApp().resources.displayMetrics.widthPixels
      - SizeUtils.dp2px(3f)) / 4f)
  private val sizeInt =
    if (sizeFloat > sizeFloat.toInt()) (sizeFloat + 1).toInt() else sizeFloat.toInt()

  override fun onBind(itemView: View) {
    itemView.layoutParams.width = sizeInt
    itemView.layoutParams.height = sizeInt
    itemView.setClickNotNull(onItemClick)
    itemView.pressEffectAlpha()
  }
}