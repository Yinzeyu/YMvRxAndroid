package com.yzy.example.imModel.emoji

import android.content.Context
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.yzy.example.R
import com.yzy.example.imModel.IEmotionSelectedListener
import com.yzy.example.imModel.emoji.sticker.StickerManager
import kotlinx.android.synthetic.main.imui_layout_emotion_layout.view.*

class EmotionLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : LinearLayout(context, attributeSet, defAttrStyle), View.OnClickListener {

    private var mMeasuredWidth: Int = 0
    private var mMeasuredHeight: Int = 0

    private var mTabPosi = 0
    private var mVpEmotion: ViewPager? = null
    private var mLlPageNumber: LinearLayout? = null
    private var mLlTabContainer: LinearLayout? = null
    private var mRlEmotionAdd: RelativeLayout? = null

    private var mFlDelete: FrameLayout? = null
    private var mIvDelIcon: ImageView? = null
    private var mFlSend: FrameLayout? = null
    private var mIvSend: ImageView? = null

    private var mTabCount: Int = 0
    private val mTabViewArray = SparseArray<View>()
    private var mSettingTab: EmotionTab? = null
    private var mEmotionSelectedListener: IEmotionSelectedListener? = null
    private var mEmotionExtClickListener: IEmotionExtClickListener? = null
    private var mEmotionAddVisible = false
    private var mEmotionSettingVisible = false

    private var adapter: EmotionViewPagerAdapter? = null

    private var mMessageEditText: EditText? = null
    /**
     * 发送按键的icon
     */
    var icSend = R.mipmap.imui_ic_cheat_add
    /**
     * 发送按键可点击的icon
     */
    var icSendActive = R.mipmap.imui_ic_cheat_add
    /**
     * 删除按键的icon
     */
    var icDelete = R.mipmap.imui_ic_cheat_add
    /**
     * vp的选中资源
     */
    var indicatorRes = R.drawable.imui_selector_view_pager_indicator

    var onSendClick: (() -> Unit)? = null

    /**
     * 设置表情选中的回调
     */
    fun setEmotionSelectedListener(emotionSelectedListener: IEmotionSelectedListener?) {
        this.mEmotionSelectedListener = emotionSelectedListener
    }


    /**
     * 设置表情管理的点击回调
     */
    fun setEmotionExtClickListener(emotionExtClickListener: IEmotionExtClickListener?) {
        this.mEmotionExtClickListener = emotionExtClickListener
    }


    /**
     * 设置表情添加按钮的显隐
     *
     * @param visible
     */
    fun setEmotionAddVisible(visible: Boolean) {
        mEmotionAddVisible = visible
        mRlEmotionAdd?.visibility = if (mEmotionAddVisible) View.VISIBLE else View.GONE
    }

    /**
     * 设置表情设置按钮的显隐
     *
     * @param visible
     */
    fun setEmotionSettingVisible(visible: Boolean) {
        mEmotionSettingVisible = visible
        mSettingTab?.visibility = if (mEmotionSettingVisible) View.VISIBLE else View.GONE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mMeasuredWidth = measureWidth(widthMeasureSpec)
        mMeasuredHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = SizeUtils.dp2px(200F)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    private fun measureHeight(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = SizeUtils.dp2px(200F)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.imui_layout_emotion_layout, this)

        mVpEmotion = findViewById(R.id.vpEmotioin)
        mLlPageNumber = findViewById(R.id.llPageNumber)
        mLlTabContainer = findViewById(R.id.llTabContainer)
        mRlEmotionAdd = findViewById(R.id.rlEmotionAdd)

        mFlDelete = findViewById(R.id.flDelete)
        mIvDelIcon = findViewById(R.id.ivDelIcon)
        mIvDelIcon?.setImageResource(icDelete)
        mFlSend = findViewById(R.id.flSend)
        mIvSend = findViewById(R.id.ivSendIcon)
        mIvSend?.setImageResource(icSend)
        ivAddIcon.setImageResource(R.drawable.imui_ic_tab_add)
        setEmotionAddVisible(mEmotionAddVisible)
        initTabs()
    }

    private fun initTabs() {
        //默认添加一个表情tab
        val emojiTab = EmotionTab(context, R.drawable.imui_ic_tab_emoji)
        mLlTabContainer?.addView(emojiTab)
        mTabViewArray.put(0, emojiTab)

        //添加所有的贴图tab
        val stickerCategories = StickerManager.getInstance().getStickerCategories()
        Log.e("EmotionLayout", "贴图的stickerCategories size = ${stickerCategories.size}")
        for (i in stickerCategories.indices) {
            val category = stickerCategories[i]

            Log.e(
                "EmotionLayout",
                "贴图 ${category.name} 的cover: ${category.getCoverImgPath()} count:${category.getCount()}"
            )
            val tab = EmotionTab(context, category.getCoverImgPath() ?: "")

            mLlTabContainer?.addView(tab)
            mTabViewArray.put(i + 1, tab)
        }
        //最后添加一个表情设置Tab
        mSettingTab = EmotionTab(context, R.drawable.imui_ic_emotion_setting)
        val drawable = StateListDrawable()
        val unSelected = context.resources.getDrawable(R.color.imui_white, null)
        drawable.addState(intArrayOf(-android.R.attr.state_pressed), unSelected)
        val selected = context.resources.getDrawable(R.color.imui_gray, null)
        drawable.addState(intArrayOf(android.R.attr.state_pressed), selected)
        mSettingTab?.background = drawable
        mLlTabContainer?.addView(mSettingTab)
        mTabViewArray.put(mTabViewArray.size(), mSettingTab)
        setEmotionSettingVisible(mEmotionSettingVisible)
        selectTab(0)
        fillVpEmotioin(0)
    }

    private fun initListener() {
        if (mLlTabContainer != null) {
            mTabCount = mLlTabContainer?.childCount ?: 1 - 1//不包含最后的设置按钮
            for (position in 0 until mTabCount) {
                val tab = mLlTabContainer?.getChildAt(position)
                tab?.tag = position
                tab?.setOnClickListener(this)
            }
        }

        mVpEmotion?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                setCurPageCommon(position)
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        mRlEmotionAdd?.setOnClickListener { v ->
            mEmotionExtClickListener?.onEmotionAddClick(v)
        }

        mSettingTab?.setOnClickListener { v ->
            mEmotionExtClickListener?.onEmotionSettingClick(v)
        }
        mFlDelete?.setOnClickListener {
            mMessageEditText?.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
        }
        mFlSend?.setOnClickListener {
            onSendClick?.invoke()
        }
    }

    private fun setCurPageCommon(position: Int) {
        mTabPosi = adapter?.positionToCategoryTabIndex(position) ?: 0
        if (mTabPosi == 0)
            setCategoryPageIndexIndicator(
                position,
                Math.ceil((EmojiManager.getDisplayCount() / EmotionConstants.EMOJI_PER_PAGE.toFloat()).toDouble()).toInt()
            )
        else {
            val category = StickerManager.getInstance().getStickerCategories()[mTabPosi - 1]
            setCategoryPageIndexIndicator(
                position,
                Math.ceil(
                    (category.stickers?.size ?: 0).toDouble() / EmotionConstants.STICKER_PER_PAGE.toFloat()
                ).toInt()
            )
        }
        selectTab(mTabPosi)
    }

    private fun selectTab(tabPosi: Int) {
        if (tabPosi == mTabViewArray.size() - 1)
            return
        for (i in 0 until mTabCount) {
            val tab = mTabViewArray.get(i)
            tab.setBackgroundResource(R.drawable.imui_shape_tab_normal)
        }
        mTabViewArray.get(tabPosi).setBackgroundResource(R.drawable.imui_shape_tab_press)
    }

    private fun fillVpEmotioin(tabPosi: Int) {
        if (adapter == null) {
            adapter = EmotionViewPagerAdapter(mMeasuredWidth, mMeasuredHeight, mEmotionSelectedListener)
            mVpEmotion?.adapter = adapter
            mVpEmotion?.offscreenPageLimit = 1
        }
        mLlPageNumber?.removeAllViews()
        setCurPageCommon(0)
        if (tabPosi == 0) {
            adapter?.attachEditText(mMessageEditText)
        }
    }

    private fun setCategoryPageIndexIndicator(position: Int, pageCount: Int) {
        val hasCount = mLlPageNumber?.childCount ?: 0
        val categoryPageIndex = adapter?.positionToCategoryPageIndex(position)
        var ivCur: ImageView? = null
        if (hasCount > pageCount) {
            for (i in pageCount until hasCount) {
                // 循环删除index是pageCount的view
                mLlPageNumber?.removeViewAt(pageCount)
            }
        }
        for (i in 0 until pageCount) {
            if (pageCount <= hasCount) {
                ivCur = mLlPageNumber?.getChildAt(i) as ImageView
            } else {
                if (i < hasCount) {
                    ivCur = mLlPageNumber?.getChildAt(i) as ImageView
                } else {
                    ivCur = ImageView(context)
                    ivCur.setBackgroundResource(indicatorRes)
                    val params = LinearLayout.LayoutParams(SizeUtils.dp2px(16F), SizeUtils.dp2px(16F))
                    ivCur.layoutParams = params
                    params.leftMargin = SizeUtils.dp2px(1F)
                    params.rightMargin = SizeUtils.dp2px(1F)
                    mLlPageNumber?.addView(ivCur)
                }
            }
            ivCur.id = i
            ivCur.isSelected = i == categoryPageIndex
            ivCur.visibility = View.VISIBLE
        }
    }

    fun attachEditText(messageEditText: EditText) {
        mMessageEditText = messageEditText
    }

    fun setSendActive(isActive: Boolean) {
        if (isActive) {
            mIvSend?.setImageResource(icSendActive)
        } else {
            mIvSend?.setImageResource(icSend)
        }
    }

    fun sendClick(onSendClick: () -> Unit) {
        this.onSendClick = onSendClick
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
        initListener()
    }


    override fun onClick(v: View?) {
        v?.let { view ->
            if (view.tag is Int) {
                val tabPosi = view.tag as Int
                if (mTabPosi != tabPosi) {
                    //当前选中的不是该tab
                    mTabPosi = tabPosi
                    selectTab(mTabPosi)
                    val position = adapter?.categoryTabIndexToPagePosition(mTabPosi) ?: 0
                    mVpEmotion?.currentItem = position
                    mLlPageNumber?.removeAllViews()
                }
            }
        }
    }
}