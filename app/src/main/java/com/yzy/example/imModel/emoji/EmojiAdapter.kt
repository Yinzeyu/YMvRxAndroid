package com.yzy.example.imModel.emoji

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import com.blankj.utilcode.util.SizeUtils

class EmojiAdapter(
    private val context: Context, emotionLayoutWidth: Int, private var emotionLayoutHeight: Int,
    private val startIndex: Int
) : BaseAdapter() {

    private var mPerWidth: Float
    private val mPerHeight: Float
    private var mIvSize: Float = 0.toFloat()

    init {
        //减去底部的tab高度、小圆点的高度才是viewpager的高度，再减少35dp是让表情整体的顶部和底部有个外间距
        emotionLayoutHeight -= SizeUtils.dp2px(35F + 26F + 50F)

        mPerWidth = emotionLayoutWidth * 1f / EmotionConstants.EMOJI_COLUMNS
        mPerHeight = emotionLayoutHeight * 1f / EmotionConstants.EMOJI_ROWS

        //emoji图标的大小
        val ivWidth = mPerWidth * .6f
        val ivHeight = mPerHeight * .6f
        mIvSize = Math.min(ivWidth, ivHeight)
        mIvSize =
            Math.min(
                mIvSize,
                SizeUtils.dp2px(EmotionConstants.EMOJI_IMAGE_SIZE_MAX).toFloat()
            )//emoji图标的最大大小
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val relativeLayout = RelativeLayout(context)
        relativeLayout.layoutParams =
            AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mPerHeight.toInt())
        val emojiThumb = ImageView(context)
//    val count = EmojiManager.getDisplayCount()
        val index = startIndex + position
//    if (position == EmotionConstants.EMOJI_PER_PAGE || index == count) {
//      emojiThumb.setBackgroundResource(R.drawable.imui_ic_emoji_del)
//    } else if (index < count) {
//    }
        emojiThumb.background = EmojiManager.getDisplayDrawable(context, index)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        layoutParams.width = mIvSize.toInt()
        layoutParams.height = mIvSize.toInt()
        emojiThumb.layoutParams = layoutParams
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(emojiThumb)
        return relativeLayout
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return (startIndex + position).toLong()
    }

    override fun getCount(): Int {
        var count = EmojiManager.getDisplayCount() - startIndex
        count = Math.min(count, EmotionConstants.EMOJI_PER_PAGE)
        return count
    }
}