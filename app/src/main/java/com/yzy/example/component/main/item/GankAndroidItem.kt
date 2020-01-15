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
import com.yzy.example.widget.imagewatcher.style.index.NumberIndexIndicator
import com.yzy.example.widget.imagewatcher.style.progress.ProgressBarIndicator
import com.yzy.example.widget.imagewatcher.transfer.TransferConfig
import com.yzy.example.widget.imagewatcher.transfer.Transferee
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
            itemView.itemGankAndroidNine.isVisible = !it.images.isNullOrEmpty()
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
        nineGridView.mAdapter = NineGridAdapter()
        nineGridView.setImagesData(picBeans)
        nineGridView.mItemClickListener = object : ItemClickListener<PicBean> {
            override fun onItemClick(context: Context, imageView: ImageView, index: Int, list: List<PicBean>) {
                val arrList=ArrayList<String>()
                picStr.forEach {
                    arrList.add(it?:"")
                }
              val config = TransferConfig.build()
                    .setProgressIndicator(ProgressBarIndicator())
                    .setIndexIndicator(NumberIndexIndicator())
                    .setJustLoadHitImage(true)
                  .setNowThumbnailIndex(index)
                    .bindImageView(imageView,arrList)
                Transferee.getDefault(imageView.context).apply(config,nineGridView.imageList).show()
            }

        }
    }


}
