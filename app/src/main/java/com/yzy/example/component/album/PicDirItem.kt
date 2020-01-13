package com.yzy.example.component.album

import android.view.View
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.pressEffectBgColor
import com.yzy.baselibrary.extention.setClickNotNull
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.utils.album.entity.LocalMediaFolder
import kotlinx.android.synthetic.main.item_pic_dir.view.*

/**
 * Description:图片选择的文件夹item
 * @author: caiyoufei
 * @date: 19-5-15 下午3:29
 */
@EpoxyModelClass(layout = R.layout.item_pic_dir)
abstract class PicDirItem : BaseEpoxyModel<BaseEpoxyHolder>() {
  @EpoxyAttribute
  var mediaDir: LocalMediaFolder? = null
  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  var onItemClick: ((bean: LocalMediaFolder?) -> Unit)? = null

  override fun onBind(itemView: View) {
    setListener(itemView)
    mediaDir?.let {
      itemView.itemPicDirTitle.text = it.name ?: "*"
      itemView.itemPicDirCover.load(it.firstImagePath ?: "")
      itemView.itemPicDirCheck.visibility = if (it.checkedNum > 0) View.VISIBLE else View.GONE
    }
  }

  private fun setListener(view: View) {
    view.setClickNotNull(mediaDir, onItemClick)
    view.pressEffectBgColor()
  }
}