package com.yzy.example.imModel

import android.Manifest
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.getBottomStatusHeight
import com.yzy.baselibrary.extention.gone
import com.yzy.example.R
import com.yzy.example.imModel.audio.AudioRecorderPanel
import com.yzy.example.imModel.audio.IAudioRecorderPanel
import kotlinx.android.synthetic.main.imui_layout_conversation_input_panel.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max


/**
 *description: 底部输入控件包含（输入框+语音按住说话+表情按键+发送按键+菜单按键）.
 *@date 2019/4/4 10:23.
 *@author: YangYang.
 */
class ConversationInputPanel @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defAttrStyle: Int = 0
) : FrameLayout(context, attributeSet, defAttrStyle) {

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * 输入的一些回调
     */
    var inputPanelListener: OnInputPanelListener? = null
    var  mContentView: FrameLayout? = null
    /**
     * 记录打字中回调的时间
     */
    private var lastTypingTime: Long = 0
    private var activity: FragmentActivity? = null

    /**
     * 按住录音工具类
     */
    var audioRecorderPanel: IAudioRecorderPanel? = null
    /**
     * 扩展功能
     */
    var extension: ConversationExtension? = null

    //icon
    /**
     * 键盘按键的icon
     */
    var icKeyboard = R.mipmap.imui_ic_cheat_keyboard
    /**
     * 表情按键的icon
     */
    var icEmotion = R.mipmap.imui_ic_cheat_emo
    /**
     * 语音输入按键的icon
     */
    var icVoice = R.mipmap.imui_ic_cheat_voice
    /**
     * 扩展按键的icon
     */
    var icExtension = R.mipmap.imui_ic_cheat_add
    private var minKeyboardSize: Int = 0
    private var defaultCustomKeyboardSize: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.imui_layout_conversation_input_panel, this, true)
        minKeyboardSize = SizeUtils.dp2px(50F)
        defaultCustomKeyboardSize = SizeUtils.dp2px(220F)
    }

    private var keyboardOpen = false
    /**
     * 将输入控件绑定到Activity和父布局中
     */
    fun attach(activity: FragmentActivity, mContentView: FrameLayout,rootLinearLayout:LinearLayout) {
        this.mContentView=mContentView
        activity.window.setSoftInputMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        addOnGlobalLayout()
        this.activity = activity
        initAudioRecorderPanel(rootLinearLayout)
        extImageView.setImageResource(icExtension)
        emotionImageView.setImageResource(icEmotion)
        audioImageView.setImageResource(icVoice)
        extImageView.setImageResource(icExtension)
//        initEmoji(mContentView)
//        initEditText()
//        initEmotion()
//

//        activity.addListerKeyboard { keyboardHeight: Int ->
//            if (keyboardOpen) {
//
//            } else {
//                if (keyboardHeight > 0) {
//                    emotionContainerFrameLayout.show(keyboardHeight)
//                } else {
//                    emotionContainerFrameLayout.hide()
//                }
//            }
//        }

        //语音输入按键的点击事件
        audioImageView.setOnClickListener {
            if (audioButton.isShown) {
                hideAudioButton()
                editText.requestFocus()
                KeyboardUtils.showSoftInput(editText)
            } else {
                editText.clearFocus()
                showAudioButton()
                hideEmotionLayout(false)
                hideConversationExtension(false)
                KeyboardUtils.hideSoftInput(editText)
            }
        }
    }

    private fun addOnGlobalLayout(){
        mContentView?.viewTreeObserver?.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }
     private fun removeGlobalOnLayout(){
        mContentView?.viewTreeObserver?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

  private  val onGlobalLayoutListener = OnGlobalLayoutListener {
        updateKeyboardState()
    }
    //Emoji表情弹窗
//    private var emojiPopup: EmojiPopup? = null

    //初始化emoji
//    private fun initEmoji(mContentView: FrameLayout) {
//        emojiPopup = EmojiPopup.Builder.fromRootView(mContentView)
////            .setOnEmojiPopupShownListener { chatKeyEmoji.setImageResource(R.drawable.svg_keyboard) }
////            .setOnEmojiPopupDismissListener { chatKeyEmoji.setImageResource(R.drawable.svg_emoji) }
//            .setOnSoftKeyboardOpenListener { keyHeight -> changeKeyHeight(keyHeight ) }
//            .setOnSoftKeyboardCloseListener { changeKeyHeight(0) }
//            .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
//            .build(editText)
////        chatKeyEmoji.click { emojiPopup?.toggle() }
//    }

//    }
        //菜单按键的点击事件
//        extImageView.setOnClickListener {
//            if (rootLinearLayout?.getCurrentInput() == extContainerContainerLayout) {
//                hideConversationExtension(true)
//            } else {
//                emotionImageView.setImageResource(icEmotion)
//                showConversationExtension()
//            }
//        }
//        //表情按键的点击事件
//        emotionImageView.setOnClickListener {
//            if (rootLinearLayout?.getCurrentInput() == emotionContainerFrameLayout) {
//                hideEmotionLayout(true)
//            } else {
//                hideAudioButton()
//                val permission =
//                    ActivityCompat.checkSelfPermission(
//                        activity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // 没有文件读取全新
//                    ActivityCompat.requestPermissions(
//                        activity,
//                        PERMISSIONS_STORAGE,
//                        REQUEST_EXTERNAL_STORAGE
//                    )
//                } else {
//                    showEmotionLayout()
//                }
//            }
//        }
//        //语音输入按键的点击事件
//        audioImageView.setOnClickListener {
//            if (audioButton.isShown) {
//                hideAudioButton()
//                editText.requestFocus()
//                rootLinearLayout?.showSoftKeyboard(editText)
//            } else {
//                editText.clearFocus()
//                showAudioButton()
//                hideEmotionLayout(false)
//                hideConversationExtension(false)
//            }
//        }
        //发送按键的点击事件
//    sendButton.setOnClickListener
//    {
//        val content = editText.text
//        if (TextUtils.isEmpty(content)) {
//            return@setOnClickListener
//        }
//        inputPanelListener?.onSend(content)
//        editText.setText("")
//    }
//    }

    private fun initEditText() {
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (editText.text.toString().trim { it <= ' ' }.isNotEmpty()) {
//                    if (activity?.currentFocus == editText) {
//                        notifyTyping()
//                    }
//                    sendButton.visibility = View.VISIBLE
//                    extImageView.visibility = View.GONE
//                    emotionLayout.setSendActive(true)
//                } else {
//                    sendButton.visibility = View.GONE
//                    extImageView.visibility = View.VISIBLE
//                    emotionLayout.setSendActive(false)
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (activity?.currentFocus == editText) {
//                    if (count == 1 && s?.get(start) == '@') {
//                        //输入@的情况下可能需要跳转到页面选择@的人
//                    }
//                }
//            }
//        })
    }

    /**
     * 初始化表情管理
     */
    private fun initEmotion() {
//        emotionLayout.setEmotionSelectedListener(this)
//        emotionLayout.attachEditText(editText)
//        emotionLayout.setEmotionAddVisible(true)
//        emotionLayout.setEmotionSettingVisible(true)
//        emotionLayout.sendClick {
//            val content = editText.text
//            if (!TextUtils.isEmpty(content)) {
//                inputPanelListener?.onSend(content)
//                editText.setText("")
//            }
//        }
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        updateKeyboardState()
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }


    private fun updateKeyboardState() {
        val rect = Rect()
        //使用最外层布局填充，进行测算计算
        getWindowVisibleDisplayFrame(rect)
        val heightDiff = rootView.height - (rect.bottom - rect.top)
        val keyboardHeight = max(0, heightDiff - BarUtils.getStatusBarHeight() - getBottomStatusHeight(context))
        GlobalScope.launch(Dispatchers.Main) {
            delay(100)
          if (keyboardHeight != defaultCustomKeyboardSize){
              defaultCustomKeyboardSize=keyboardHeight
              Log.e("defaultboardSize",defaultCustomKeyboardSize.toString())
              changeKeyHeight(defaultCustomKeyboardSize)
          }
        }
    }
    var keOpen = false

    private fun changeKeyHeight(height: Int) {
        keOpen = height > 0
        if (keOpen){
            emotionContainerFrameLayout.show(height)
        }else{
            emotionContainerFrameLayout.hide()
        }
    }


    /**
     * 初始化按住录音控件
     */
    private fun initAudioRecorderPanel(rootLinearLayout:LinearLayout) {
        if (audioRecorderPanel == null) {
            audioRecorderPanel = AudioRecorderPanel(context)
        }
            audioRecorderPanel?.attach(rootLinearLayout, audioButton)
    }

    /**
     * 初始化菜单管理，要在attach和所有扩展初始化之后调用
     */
    fun initExtension() {
//        activity?.let { act ->
//            extension = ConversationExtension(act, this, conversationExtViewPager)
//        }
//        llConversationExtPageNumber.removeAllViews()
//        val page = ConversationExtManager.getInstance().getExtensionPage()
//        if (page == 1) {
//            //只有一页
//            llConversationExtPageNumber.visibility = View.GONE
//        } else {
//            llConversationExtPageNumber.visibility = View.VISIBLE
//            for (i in 0 until page) {
//                val ivCur = ImageView(context)
//                ivCur.setBackgroundResource(R.drawable.imui_selector_view_pager_indicator)
//                val params = LinearLayout.LayoutParams(SizeUtils.dp2px(8F), SizeUtils.dp2px(8F))
//                ivCur.layoutParams = params
//                params.leftMargin = SizeUtils.dp2px(3F)
//                params.rightMargin = SizeUtils.dp2px(3F)
//                llConversationExtPageNumber.addView(ivCur)
//            }
//            conversationExtViewPager.addOnPageChangeListener(object :
//                ViewPager.OnPageChangeListener {
//                override fun onPageScrollStateChanged(p0: Int) {
//                }
//
//                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
//                }
//
//                override fun onPageSelected(p0: Int) {
//                    val count = llConversationExtPageNumber.childCount
//                    for (i in 0 until count) {
//                        llConversationExtPageNumber.getChildAt(p0).isSelected = i == p0
//                    }
//                }
//            })
//        }
//        extension?.init()
    }

    /**
     * 设置发送按钮的样式
     */
    fun setSendButtonStyle(style: Button.() -> Unit) {
        sendButton.apply(style)
    }

    /**
     * 设置按住说话的样式
     */
    fun setAudioButtonStyle(style: Button.() -> Unit) {
        audioButton.apply(style)
    }

    /**
     * 设置输入框的样式
     */
    fun setEditTextStyle(style: EditText.() -> Unit) {
        editText.apply(style)
    }

    /**
     * 设置扩展菜单的样式
     */
    fun setExtensionButtonStyle(style: ImageView.() -> Unit) {
        extImageView.apply(style)
    }

    /**
     * 设置语音输入按键的样式
     */
    fun setVoiceButtonStyle(style: ImageView.() -> Unit) {
        audioImageView.apply(style)
    }

    /**
     * 设置表情按键的样式
     */
    fun setEmotionButtonStyle(style: ImageView.() -> Unit) {
        emotionImageView.apply(style)
    }

    /**
     * 设置表情输入控件的样式
     */
//    fun setEmotionLayoutStyle(style: EmotionLayout.() -> Unit) {
//        emotionLayout.apply(style)
//    }

    /**
     * 设置输入控件容器的样式
     */
    fun setInputLinearLayoutStyle(style: LinearLayout.() -> Unit) {
        inputLinearLayout.apply(style)
    }

    /**
     * 设置分隔线的样式
     */
    fun setLineStyle(style: View.() -> Unit) {
        line.apply(style)
    }

    /**
     * 如果使用默认的按住录音工具类，则可以进行一些配置
     */
    fun setDefaultAudioRecorderPanel(style: AudioRecorderPanel.() -> Unit) {
        if (audioRecorderPanel is AudioRecorderPanel) {
            (audioRecorderPanel as AudioRecorderPanel).apply(style)
        }
    }

    /**
     * 获取表情的控件，可以自定进行相关的设置
     */
//    fun getEmotionLayoutStyle(style: EmotionLayout.() -> Unit) {
//        emotionLayout.apply(style)
//    }


    fun onKeyboardShown() {
        hideEmotionLayout(false)
        hideConversationExtension(false)
    }

    fun onKeyboardHidden() {
        // do nothing
    }

    /**
     * 设置输入框中的内容
     */
    fun setInputText(text: CharSequence?) {
        editText.setText(text)
    }

    /**
     * 获取输入框中的内容
     */
    fun getInputText(): CharSequence? {
        return editText.text
    }

    /**
     * 显示语音输入
     */
    private fun showAudioButton() {
        audioButton.visibility = View.VISIBLE
        editText.visibility = View.GONE
        sendButton.visibility = View.GONE
        audioImageView.setImageResource(icKeyboard)
        editText.gone()

//        rootLinearLayout?.hideCurrentInput(editText)
//        rootLinearLayout?.hideAttachedInput(true)
    }


    /**
     * 收起语音输入
     */
    private fun hideAudioButton() {
        audioButton.visibility = View.GONE
        editText.visibility = View.VISIBLE
        if (TextUtils.isEmpty(editText.text)) {
            sendButton.visibility = View.GONE
        } else {
            sendButton.visibility = View.VISIBLE
        }
        audioImageView.setImageResource(icVoice)
    }


    /**
     * 展开表情输入框
     */
    private fun showEmotionLayout() {
//        rootLinearLayout?.show(editText, emotionContainerFrameLayout)
        audioButton.visibility = View.GONE
        emotionImageView.setImageResource(icKeyboard)
        inputPanelListener?.onInputPanelStateChange(true)
    }

    /**
     * 收起表情输入框
     */
    private fun hideEmotionLayout(isNeedShowKeyBoard: Boolean) {
        emotionImageView.setImageResource(icEmotion)
        keyboardOpen = false
//        if (isNeedShowKeyBoard) {
//            rootLinearLayout?.showSoftKeyboard(editText)
//        }
//        inputPanelListener?.onInputPanelStateChange(false)
    }


    /**
     * 展开扩展菜单
     */
    private fun showConversationExtension() {
//        rootLinearLayout?.show(editText, extContainerContainerLayout)
        if (audioButton.isShown) {
            hideAudioButton()
        }
        inputPanelListener?.onInputPanelStateChange(true)
    }

    /**
     * 收起扩展菜单
     */
    private fun hideConversationExtension(isNeedShowKeyBoard: Boolean) {
        keyboardOpen = false
        if (isNeedShowKeyBoard) {
//            rootLinearLayout?.showSoftKeyboard(editText)
        }
        inputPanelListener?.onInputPanelStateChange(false)
    }

    /**
     * 收起输入
     */
    fun collapse() {
        extension?.reset()
        emotionImageView.setImageResource(icEmotion)
//        rootLinearLayout?.hideAttachedInput(true)
//        rootLinearLayout?.hideCurrentInput(editText)
    }

    private fun notifyTyping() {
        val now = System.currentTimeMillis()
        if (now - lastTypingTime > TYPING_INTERVAL_IN_SECOND * 1000) {
            lastTypingTime = now
            inputPanelListener?.onTyping()
        }
    }

//    override fun onEmojiSelected(key: String) {
//        inputPanelListener?.onEmojiInput(key)
//    }
//
//    override fun onStickerSelected(
//        categoryName: String,
//        stickerName: String,
//        stickerBitmapPath: String?
//    ) {
//        inputPanelListener?.onStickerInput(categoryName, stickerName, stickerBitmapPath)
//    }


    companion object {
        /**
         * 打字状态间隔
         */
        private val TYPING_INTERVAL_IN_SECOND = 10
    }

    /**
     * 输入面板的回调
     */
    interface OnInputPanelListener {
        /**
         * 输入面板状态变化
         * @param isExpanded 是否展开
         */
        fun onInputPanelStateChange(isExpanded: Boolean)

        /**
         * 自定义表情输入
         */
        fun onStickerInput(
            categoryName: String,
            stickerName: String,
            stickerBitmapPath: String?
        )

        /**
         * emoji表情输入
         */
        fun onEmojiInput(key: String)

        /**
         * 当前状态为输入中
         */
        fun onTyping()

        /**
         * 发送
         */
        fun onSend(editable: Editable)
    }
}