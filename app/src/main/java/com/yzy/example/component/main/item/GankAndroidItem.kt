package com.yzy.example.component.main.item

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.yzy.baselibrary.base.BaseEpoxyHolder
import com.yzy.baselibrary.base.BaseEpoxyModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectBgColor
import com.yzy.example.R
import com.yzy.example.repository.bean.GankAndroidBean
import com.yzy.example.repository.bean.PicBean
import com.yzy.example.widget.ninegridview.ItemClickListener
import com.yzy.example.widget.ninegridview.NineGridAdapter
import com.yzy.example.widget.ninegridview.NineGridView
import kotlinx.android.synthetic.main.item_gank_android.view.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/1 17:05
 */
@EpoxyModelClass(layout = R.layout.item_gank_android)
abstract class GankAndroidItem : BaseEpoxyModel<BaseEpoxyHolder>() {
    //数据源
    @EpoxyAttribute
    var dataBean: GankAndroidBean? = null
    //点击item
    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    var onItemClick: ((bean: GankAndroidBean?) -> Unit)? = null

    override fun onBind(itemView: View) {
        dataBean?.let {
            //发布者
            itemView.itemGankAndroidUser.text = it.who
            //发布时间
            itemView.itemGankAndroidTime.text = it.publishTime
            //内容
            itemView.itemGankAndroidDes.text = it.desc
            //图片
      itemView.itemGankAndroidNine.isVisible=!it.images.isNullOrEmpty()
            if (!it.images.isNullOrEmpty()) {
                //多张图片，九宫格
                val nieView: NineGridView<PicBean> = itemView.findViewById(R.id.itemGankAndroidNine)

                setMultiImages(it.images ?: mutableListOf(), it.urlImgs, nieView)
            }
        }
        //item点击
        itemView.click { onItemClick?.invoke(dataBean) }
        //item按压效果
        itemView.pressEffectBgColor()
    }

  private fun setMultiImages(
      picStr: MutableList<String?>,
      picBeans: MutableList<PicBean>,
      nineGridView: NineGridView<PicBean>
  ) {
    nineGridView.mAdapter=NineGridAdapter()
    nineGridView.setImagesData(picBeans)
    nineGridView.mItemClickListener =object :ItemClickListener<PicBean>{
        override fun onItemClick(
            context: Context,
            imageView: ImageView,
            index: Int,
            list: List<PicBean>
        ) {
            val count = nineGridView.childCount
            val views = mutableListOf<ImageView>()
            for (i in 0 until count) {
                views.add(nineGridView.getChildAt(i) as ImageView)
            }
        }

    }
//
//      { _, imageView, index, list ->
//      val count = nineGridView.childCount
//      val views = mutableListOf<ImageView>()
//      for (i in 0 until count) {
//        views.add(nineGridView.getChildAt(i) as ImageView)
//      }
//      //多图预览
////      PreviewImgUtils.instance.instancestartPreview(nineGridView.context as Activity, list, views, index)
//    }
//  }
}


}
