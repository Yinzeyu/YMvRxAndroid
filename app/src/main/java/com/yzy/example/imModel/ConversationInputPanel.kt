package com.yzy.example.imModel

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
import com.yzy.baselibrary.extention.click
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
    private var minKeyboardSize: Int = 0
    private var defaultCustomKeyboardSize: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.imui_layout_conversation_input_panel, this, true)
        minKeyboardSize = SizeUtils.dp2px(50F)
        defaultCustomKeyboardSize = SizeUtils.dp2px(220F)
    }

    /**
     * 将输入控件绑定到Activity和父布局中
     */
    fun attach(activity: FragmentActivity, mContentView: FrameLayout,rootLinearLayout:LinearLayout,selectAlbum:()->Unit) {
        this.mContentView=mContentView
        activity.window.setSoftInputMode(PopupWindow.INPUT_METHOD_NOT_NEEDED)
        addOnGlobalLayout()
        this.activity = activity
        initAudioRecorderPanel(rootLinearLayout)
        emotionImageView.setImageResource(icEmotion)
        audioImageView.setImageResource(icVoice)
        extAlbumImageView.click {
            selectAlbum.invoke()
        }
        //语音输入按键的点击事件
        audioImageView.setOnClickListener {
            if (audioButton.isShown) {
                hideAudioButton()
                editText.requestFocus()
                KeyboardUtils.showSoftInput(editText)
            } else {
                editText.clearFocus()
                showAudioButton()
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


    private fun notifyTyping() {
        val now = System.currentTimeMillis()
        if (now - lastTypingTime > TYPING_INTERVAL_IN_SECOND * 1000) {
            lastTypingTime = now
            inputPanelListener?.onTyping()
        }
    }

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
         * 当前状态为输入中
         */
        fun onTyping()

        /**
         * 发送
         */
        fun onSend(editable: Editable)
    }
}