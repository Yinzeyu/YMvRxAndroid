package com.yzy.example.widget.ninegridview

import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.SizeUtils
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.PicBean

/**
 *description: 九宫格布局的Adapter.
 *@date 2018/10/24 14:20.
 *@author: yzy.
 */
class NineGridAdapter(private val imageSize: Int = SizeUtils.dp2px(100f)) :
    NineGridViewAdapter<PicBean>() {
    override fun generateImageView(context: Context): ImageView {
        return ImageView(context)
    }

    override fun onDisplayImage(context: Context, imageView: ImageView, t: PicBean) {
        t.let {bean->
            context.let { c ->
                imageView.load(bean.url)
            }
        }
    }

//    private val colorSt/roke = ColorUtils.getColor(R.color.white_F5F5F5)
//    override fun onDisplayImage(
//        context: Context?,
//        imageView: ImageView?,
//        pic: PicBean?
//    ) {
//        pic?.let {
//            context?.let { c ->
                //        if (imageView is SketchImageView) {
//          val displayOptions = ActionBar.DisplayOptions()
//          val holder = RandomPlaceholder.instance.getPlaceHolder(it.url)
//          displayOptions.setLoadingImage(holder)
//          displayOptions.setErrorImage(R.drawable.svg_placeholder_fail)
//          //圆角
//          val shaper = RoundRectImageShaper(SizeUtils.dp2px(5f).toFloat())
////          shaper.setStroke(colorStroke, 1)
//          displayOptions.shaper = shaper
//          //图片尺寸
//          val shapeSize = ShapeSize(imageSize, imageSize, ScaleType.CENTER_CROP)
//          displayOptions.shapeSize = shapeSize
//          imageView.setOptions(displayOptions)
//          imageView.options.displayer = FadeInImageDisplayer()
//          // DisplayHelper
//          Sketch.with(c)
//              .display(it.url, imageView)
//              .commit()
//        }
//            }
//        }
//    }commit
}