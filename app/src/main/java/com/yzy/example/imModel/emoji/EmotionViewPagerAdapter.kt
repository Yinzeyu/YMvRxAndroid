package com.yzy.example.imModel.emoji

import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils
import com.yzy.example.imModel.IEmotionSelectedListener
import com.yzy.example.imModel.emoji.EmotionConstants.Companion.STICKER_PER_PAGE
import com.yzy.example.imModel.emoji.sticker.StickerAdapter
import com.yzy.example.imModel.emoji.sticker.StickerManager

class EmotionViewPagerAdapter(
    private val emotionLayoutWidth: Int,
    private val emotionLayoutHeight: Int,
    private val listener: IEmotionSelectedListener?
) : PagerAdapter() {

    private var mPageCount = 0

    private var mMessageEditText: EditText? = null

    private var emojiListener: AdapterView.OnItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val index = position + parent.tag as Int * EmotionConstants.EMOJI_PER_PAGE
            val count = EmojiManager.getDisplayCount()
            if (position == EmotionConstants.EMOJI_PER_PAGE || index >= count) {
                listener?.onEmojiSelected("/DEL")
                onEmojiSelected("/DEL")
            } else {
                val text = EmojiManager.getDisplayText(id.toInt())
                text?.let {
                    if (!TextUtils.isEmpty(it)) {
                        listener?.onEmojiSelected(it)
                        onEmojiSelected(it)
                    }
                }
            }
        }

    private var stickerListener: AdapterView.OnItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val tag = parent.tag as Int
            val pagePosition = tag and 0xFFF
            val categoryIndex = tag shr 12 and 0xFFF
            var index = position
            if (pagePosition > 0) {
                val categoryStartPagePosition = categoryTabIndexToPagePosition(categoryIndex)
                index += (pagePosition - categoryStartPagePosition) * EmotionConstants.STICKER_PER_PAGE
            }
            val category = StickerManager.getInstance().getStickerCategories()[categoryIndex - 1]
            val stickers = category.stickers

            if (index >= stickers?.size ?: 0) {
                return@OnItemClickListener
            }
            listener?.let { listener ->
                val sticker = stickers?.get(index)
                sticker?.let { sticker1 ->
                    StickerManager.getInstance().getCategory(sticker1.category)
                        ?: return@OnItemClickListener
                    listener.onStickerSelected(
                        sticker1.category,
                        sticker1.name,
                        StickerManager.getInstance().getStickerBitmapPath(
                            sticker1.category,
                            sticker1.name
                        )
                    )
                }
            }
        }

    init {
        mPageCount =
            Math.ceil((EmojiManager.getDisplayCount() / EmotionConstants.EMOJI_PER_PAGE).toDouble())
                .toInt()
        StickerManager.getInstance().getStickerCategories().forEach { category ->
            mPageCount += (category.stickers?.size
                ?: 0) / STICKER_PER_PAGE
        }
    }

    fun attachEditText(messageEditText: EditText?) {
        mMessageEditText = messageEditText
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return if (mPageCount == 0) 1 else mPageCount
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val relativeLayout = RelativeLayout(context)
        relativeLayout.setPadding(SizeUtils.dp2px(4F), 0, SizeUtils.dp2px(4F), 0)
        relativeLayout.gravity = Gravity.CENTER
        val gridView = GridView(context)
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        gridView.layoutParams = params
        gridView.gravity = Gravity.CENTER
        val tabPosition = positionToCategoryTabIndex(position)
        gridView.tag =
            tabPosition and 0xFFF shl 12 or (position and 0xFFF) // category index | pageIndex

        if (tabPosition == 0) {
            gridView.onItemClickListener = emojiListener
            gridView.adapter =
                EmojiAdapter(
                    context,
                    emotionLayoutWidth,
                    emotionLayoutHeight,
                    position * EmotionConstants.EMOJI_PER_PAGE
                )
            gridView.numColumns = EmotionConstants.EMOJI_COLUMNS
        } else {
            val categoryStickerPageIndex = positionToCategoryPageIndex(position)
            val category = StickerManager.getInstance()
                .getCategory(StickerManager.getInstance().getStickerCategories()[tabPosition - 1].name)
            gridView.onItemClickListener = stickerListener
            gridView.adapter = StickerAdapter(
                context,
                category,
                emotionLayoutWidth,
                emotionLayoutHeight,
                categoryStickerPageIndex * STICKER_PER_PAGE
            )
            gridView.numColumns = EmotionConstants.STICKER_COLUMNS
        }
        relativeLayout.addView(gridView)
        container.addView(relativeLayout)
        return relativeLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    /**
     * 根据categoryTabIndex计算page position
     *
     * @param categoryTabIndex
     * @return
     */
    fun categoryTabIndexToPagePosition(categoryTabIndex: Int): Int {
        var position =
            Math.ceil((EmojiManager.getDisplayCount() / EmotionConstants.EMOJI_PER_PAGE).toDouble())
                .toInt()
        if (categoryTabIndex == 0) {
            return 0
        } else {
            for (i in 0 until categoryTabIndex - 1) {
                position += Math.ceil(
                    (StickerManager.getInstance().getStickerCategories()[i].stickers?.size
                        ?: 0).toDouble() / EmotionConstants.STICKER_PER_PAGE
                ).toInt()
            }
        }
        return position
    }

    /**
     * 根据page position，计算tab index
     *
     * @param position
     * @return
     */
    fun positionToCategoryTabIndex(position: Int): Int {
        var categoryTabIndex = 0
        val emojiPageCount =
            Math.ceil((EmojiManager.getDisplayCount() / EmotionConstants.EMOJI_PER_PAGE).toDouble())
                .toInt()
        if (position >= emojiPageCount) {
            var stickerPageCount = 0
            for (i in 0 until StickerManager.getInstance().getStickerCategories().size) {
                stickerPageCount += Math.ceil(
                    (StickerManager.getInstance().getStickerCategories()[i].stickers?.size
                        ?: 0).toDouble() / EmotionConstants.STICKER_PER_PAGE
                )
                    .toInt()
                if (position < emojiPageCount + stickerPageCount) {
                    categoryTabIndex = 1 + i
                    break
                }
            }
        }
        return categoryTabIndex
    }

    /**
     * 根据page position，计算Category内部的pageIndex
     *
     * @param position
     * @return
     */
    fun positionToCategoryPageIndex(position: Int): Int {
        val emojiPageCount =
            Math.ceil((EmojiManager.getDisplayCount() / EmotionConstants.EMOJI_PER_PAGE).toDouble())
                .toInt()
        var categoryPageIndex = -1
        if (position < emojiPageCount) {
            categoryPageIndex = position
        } else {
            var stickerPageCount = 0
            for (i in 0 until StickerManager.getInstance().getStickerCategories().size) {
                categoryPageIndex = position - emojiPageCount - stickerPageCount
                stickerPageCount += Math.ceil(
                    (StickerManager.getInstance().getStickerCategories()[i].stickers?.size
                        ?: 0).toDouble() / EmotionConstants.STICKER_PER_PAGE
                )
                    .toInt()
                if (position < emojiPageCount + stickerPageCount) {
                    break
                }
            }
        }
        return categoryPageIndex
    }

    private fun onEmojiSelected(key: String) {
        if (mMessageEditText == null)
            return
        val editable = mMessageEditText?.text
        if (key == "/DEL") {
            mMessageEditText?.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        } else {
            val code = Integer.decode(key)
            val chars = Character.toChars(code)
            var value = Character.toString(chars[0])
            for (i in 1 until chars.size) {
                value += Character.toString(chars[i])
            }

            var start = mMessageEditText?.selectionStart ?: 0
            var end = mMessageEditText?.selectionEnd ?: 0
            start = if (start < 0) 0 else start
            end = if (start < 0) 0 else end
            editable?.replace(start, end, value)
            val editEnd = mMessageEditText?.selectionEnd ?: 0
            MoonUtils.replaceEmoticons(
                Utils.getApp(),
                editable,
                0,
                editable.toString().length
            )
            mMessageEditText?.setSelection(editEnd)
        }
    }
}