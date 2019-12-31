package com.yzy.example.imModel.emoji.sticker

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.blankj.utilcode.util.SizeUtils
import com.yzy.example.R
import com.yzy.example.imModel.emoji.EmotionConstants
import com.yzy.example.imModel.emoji.ImUiManager

class StickerAdapter(
    private val context: Context,
    private val category: StickerCategory?,
    emotionLayoutWidth: Int,
    private var emotionLayoutHeight: Int,
    private val startIndex: Int
) : BaseAdapter() {
    private var mPerWidth: Float
    private val mPerHeight: Float
    private var mIvSize: Float = 0.toFloat()

    init {
        //减去底部的tab高度、小圆点的高度才是viewpager的高度，再减少35dp是让表情整体的顶部和底部有个外间距
        emotionLayoutHeight -= SizeUtils.dp2px(35F + 26F + 50F)
        mPerWidth = emotionLayoutWidth * 1f / EmotionConstants.STICKER_COLUMNS
        mPerHeight = emotionLayoutHeight * 1f / EmotionConstants.STICKER_ROWS
        val ivWidth = mPerWidth * .8f
        val ivHeight = mPerHeight * .8f
        mIvSize = Math.min(ivWidth, ivHeight)
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: StickerViewHolder
        val view = if (convertView == null) {
            val relativeLayout = RelativeLayout(context)
            relativeLayout.layoutParams =
                AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mPerHeight.toInt())
            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.imui_ic_tab_emoji)
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.width = mIvSize.toInt()
            params.height = mIvSize.toInt()
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            imageView.layoutParams = params

            relativeLayout.addView(imageView)

            viewHolder = StickerViewHolder()
            viewHolder.mImageView = imageView
            relativeLayout.tag = viewHolder
            relativeLayout
        } else {
            viewHolder = convertView.tag as StickerViewHolder
            convertView
        }
        val index = startIndex + position
        if (index >= category?.stickers?.size ?: 0) {
            return view
        }
        val sticker = category?.stickers?.get(index) ?: return view
        val stickerBitmapUri =
            StickerManager.getInstance().getStickerBitmapUri(sticker.category, sticker.name)
        viewHolder.mImageView?.let {
            ImUiManager.getImageLoader()?.displayImage(context, stickerBitmapUri, it)
        }
        return view
    }

    override fun getItem(position: Int): Any? {
        return category?.stickers?.get(startIndex + position)
    }

    override fun getItemId(position: Int): Long {
        return (startIndex + position).toLong()
    }

    override fun getCount(): Int {
        var count = category?.stickers?.size ?: 0 - startIndex
        count = if (count < 0) {
            EmotionConstants.STICKER_PER_PAGE
        } else {
            Math.min(count, EmotionConstants.STICKER_PER_PAGE)
        }
        return count
    }

    internal inner class StickerViewHolder {
        var mImageView: ImageView? = null
    }
}