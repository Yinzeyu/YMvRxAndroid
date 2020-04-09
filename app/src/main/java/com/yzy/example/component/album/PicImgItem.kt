//package com.yzy.example.component.album
//
//import android.view.View
//
//import com.airbnb.epoxy.EpoxyAttribute
//import com.airbnb.epoxy.EpoxyModelClass
//import com.blankj.utilcode.util.SizeUtils
//import com.blankj.utilcode.util.Utils
//import com.yzy.baselibrary.base.BaseEpoxyHolder
//import com.yzy.baselibrary.base.BaseEpoxyModel
//import com.yzy.baselibrary.extention.click
//import com.yzy.baselibrary.extention.gone
//import com.yzy.baselibrary.extention.setClickNotNull
//import com.yzy.baselibrary.extention.visible
//import com.yzy.example.R
//import com.yzy.example.extention.load
//import com.yzy.example.utils.album.entity.LocalMedia
//import kotlinx.android.synthetic.main.item_pic_select.view.*
//
///**
// * Description: 图库选择的图片item
// * @author: yzy
// * @date: 19-5-15 下午6:51
// */
//@EpoxyModelClass(layout = R.layout.item_pic_select)
//abstract class PicImgItem : BaseEpoxyModel<BaseEpoxyHolder>() {
//  @EpoxyAttribute
//  var checkedIndex: Int? = 1
//  @EpoxyAttribute
//  var localMedia: LocalMedia? = null
//  @EpoxyAttribute
//  var onlySelOne: Boolean = false
//  @EpoxyAttribute
//  var mixing: Boolean = false
//
//  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
//  var onItemClick: ((bean: LocalMedia?) -> Unit)? = null
//  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
//  var onClickCheck: ((bean: LocalMedia?, view: View) -> Unit)? = null
//  //图片大小
//  private val sizeFloat = ((Utils.getApp().resources.displayMetrics.widthPixels
//      - SizeUtils.dp2px(3f)) / 4f)
//  private val sizeInt =
//    if (sizeFloat > sizeFloat.toInt()) (sizeFloat + 1).toInt() else sizeFloat.toInt()
//
//  override fun onBind(itemView: View) {
//    itemView.layoutParams.width = sizeInt
//    itemView.layoutParams.height = sizeInt
//    setListener(itemView)
//    localMedia?.let {
//      //封面
//      itemView.itemPicCover.load(it.path)
//      //视频
//      if (it.isVideo) {
//        itemView.itemPicCheckLayout.gone()
//        itemView.itemPicVideoIv.visibility = View.VISIBLE
//        itemView.itemPicVideoTv.visibility = View.VISIBLE
//        itemView.itemPicVideoLayer.visibility = View.VISIBLE
//        itemView.itemPicLarge.visibility = View.GONE
//        itemView.itemPicVideoTv.text = it.durationTime
//      } else {//图片
//        itemView.itemPicCheckLayout.visible()
//        itemView.itemPicVideoIv.visibility = View.GONE
//        itemView.itemPicVideoTv.visibility = View.GONE
//        itemView.itemPicVideoLayer.visibility = View.GONE
//        when {
//          it.isWidthPic -> {
//            itemView.itemPicLarge.visibility = View.VISIBLE
//            itemView.itemPicLarge.text = "宽图"
//          }
//          it.isLongPic -> {
//            itemView.itemPicLarge.visibility = View.VISIBLE
//            itemView.itemPicLarge.text = "长图"
//          }
//          else -> itemView.itemPicLarge.visibility = View.GONE
//        }
//      }
//      //选择模式
//      if (!onlySelOne && !it.isVideo) {//单图和视频不显示
//        //选中的数量
//        itemView.itemPicCheckLayout.visible()
//        itemView.itemPicCheckIv.isSelected = it.isChecked
//        itemView.itemPicCheckTv.text =
//          if (it.isChecked && !it.isVideo) (checkedIndex ?: 1).toString() else ""
//      } else {
//        itemView.itemPicCheckLayout.visibility = View.GONE
//      }
//      //混合模式，都显示
//      if (mixing) {
//        itemView.itemPicCheckLayout.visible()
//        itemView.itemPicCheckIv.isSelected = it.isChecked
//        itemView.itemPicCheckTv.text = if (it.isChecked) (checkedIndex ?: 1).toString() else ""
//      }
//    }
//  }
//
//  private fun setListener(view: View) {
//    view.setClickNotNull(localMedia, onItemClick)
//    if (onClickCheck == null) {
//      view.itemPicCheckLayout.setOnClickListener(null)
//    } else {
//      view.itemPicCheckLayout.click { onClickCheck?.invoke(localMedia, view) }
//    }
//  }
//}