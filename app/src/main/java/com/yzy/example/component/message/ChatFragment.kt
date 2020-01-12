package com.yzy.example.component.message

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.*
import com.yzy.example.R
import com.yzy.example.component.comm.CommTitleFragment
import com.yzy.example.component.main.MainActivity
import com.yzy.example.imModel.audio.MMAudioRecorderPanel
import com.yzy.example.imModel.audio.OnRecordListener
import com.yzy.example.imModel.audio.RecordState
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class ChatFragment : CommTitleFragment() {

    companion object {
        fun startChatFragment(controller: NavController, @IdRes id: Int) {
            controller.navigate(id, Bundle().apply { })
        }
    }

    override fun layoutResContentId(): Int = R.layout.fragment_chat

    override fun initContentView() {
        mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
        (mContext as MainActivity).registerMyTouchListener(myTouchListener)
    }


    override fun initData() {
        initInputPanel()
//        initEmoji()
    }






   private var myTouchListener = object : MainActivity.MyTouchListener {
        override fun onTouchEvent(event: MotionEvent?) {
            event?.let {
                if (inputPanel.keOpen && (it.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    //点击哪些View需要关闭键盘同时响应点击
                    if (!isTouchViewOut(commTitleBack, it)) {//多个View通过||连接
                        KeyboardUtils.hideSoftInput(mContext)
                    }
                    //没有点击到对于View则关闭键盘
                    else if (isTouchViewOut(inputPanel, it)) {//多个View通过&&连接
                        KeyboardUtils.hideSoftInput(mContext)
                        return
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        (mContext as MainActivity).unRegisterMyTouchListener(myTouchListener)
        super.onDestroyView()
    }

    // Return whether touch the view.
    private fun isTouchViewOut(
        v: View,
        event: MotionEvent
    ): Boolean {
        val l = intArrayOf(0, 0)
        v.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom = top + v.height
        val right = left + v.width
        return !(event.x > left && event.x < right
                && event.y > top && event.y < bottom)
    }
    private fun initInputPanel() {
        inputPanel.icEmotion = R.drawable.ic_big_expression
        inputPanel.icExtension = R.drawable.ic_big_dynamic_add_pic
        inputPanel.icKeyboard = R.drawable.ic_big_keyboard
        inputPanel.icVoice = R.drawable.ic_big_voice
        inputPanel.audioRecorderPanel = MMAudioRecorderPanel(mContext as MainActivity)
        inputPanel.attach(mContext as MainActivity, mContext.mContentView,rootLinearLayout)
        inputPanel.setSendButtonStyle {
            backgroundResource = R.drawable.shape_solid_30d18b_13h
            layoutParams.height = SizeUtils.dp2px(27f)
            layoutParams.width = SizeUtils.dp2px(44f)
        }
        setVoiceNormalStyle()
        inputPanel.audioRecorderPanel?.setRecordListener(object : OnRecordListener {
            override fun onNoPermission() {
//                val disposable = RxPermissions(this@ChatActivity)
//                    .request(
//                        Manifest.permission.RECORD_AUDIO
//                    )
//                    .subscribe({
//                        if (it) {
//                            //录音权限
//                            if (!AudioPermissionHelper.hasRecordPermission()) {
//                                showAudioRecordPermissionDialog()
//                            }
//                        } else {
//                            //没有录音权限
//                            showAudioRecordPermissionDialog()
//                        }
//                    }, {
//                        toast("录音限获取异常")
//                    })
            }

            override fun onRecordFail(e: Exception) {
                if (e is MMAudioRecorderPanel.TooShortException) {
                    mContext.toast("说话时间太短！")
                }
                setVoiceNormalStyle()
            }

            override fun onRecordStateChanged(state: RecordState) {
                when (state) {
                    RecordState.START -> {
                        //开始录音
                        setVoiceRecodingStyle()
                    }
                    RecordState.TO_CANCEL -> {
                        //手指上滑了将要取消的状态
                    }
                    RecordState.TO_TIMEOUT -> {
                        //剩余时间小于10s
                    }
                    RecordState.STOP -> {
                        //录音结束
                        setVoiceNormalStyle()
                    }
                    else -> {
                    }
                }
            }

            override fun onRecordSuccess(audioFile: String, duration: Long) {
                setVoiceNormalStyle()
                if (duration >= 1000) {
//                    if (viewModel?.isTargetUserBanned() == true) {
//                        toast("该账号封禁中，无法进行互动")
//                    } else {
//                        viewModel?.sendVoiceMessage(audioFile, duration)
//                    }
                }
            }
        })
    }

        private fun setVoiceRecodingStyle() {
        inputPanel.setAudioButtonStyle {
            text = "松开发送"
            textColor = mContext.getResColor(R.color.c_ff999999)
            backgroundResource = R.drawable.shape_solid_f3f3f3_8
        }
    }

    private fun setVoiceNormalStyle() {
        inputPanel.setAudioButtonStyle {
            text = "按住说话"
            textColor = mContext.getResColor(R.color.white)
            backgroundResource = R.drawable.shape_solid_54d25d_8
        }
    }


}


//        inputPanel.inputPanelListener = object : ConversationInputPanel.OnInputPanelListener {
//            override fun onInputPanelStateChange(isExpanded: Boolean) {
//                if (isExpanded) {
//                    //输入面板展开，需要将列表滚动到底部
////          listMove2Bottom()
//                } else {
//                    //面板关闭
//                }
//            }
//
//            override fun onStickerInput(
//                categoryName: String,
//                stickerName: String,
//                stickerBitmapPath: String?
//            ) {
//            }
//
//            override fun onEmojiInput(key: String) {
////        Log.e("ConversationActivity", "输入了Emoji表情$key")
//            }
//
//            override fun onTyping() {
////        tvTitle.text = "输入中"
//            }
//
//            override fun onSend(editable: Editable) {
////                if (viewModel?.isTargetUserBanned() == true) {
////                    toast("该账号封禁中，无法进行互动")
////                } else {
////                    viewModel?.sendTextMessage(editable.toString())
////                }
//            }
//        }
//        inputPanel.setEditTextStyle {
//            setPadding(
//                SizeUtils.dp2px(12f),
//                SizeUtils.dp2px(8f),
//                SizeUtils.dp2px(12f),
//                SizeUtils.dp2px(8f)
//            )
//            backgroundResource = R.drawable.shape_solid_f3f3f3_8
//            hint = "说点什么..."
//            hintTextColor = mContext.getResColor(R.color.c_ff999999)
//            textColor = mContext.getResColor(R.color.c_ff333333)
//            textSize = 14f
//            maxLines = 5
//            minHeight = SizeUtils.dp2px(33f)
//        }
//
//        inputPanel.setEmotionLayoutStyle {
//            icSend = R.drawable.ic_big_send_expression
//            icSendActive = R.drawable.ic_big_send_expression_active
//            icDelete = R.drawable.ic_big_delete_expression
//            indicatorRes = R.drawable.im_selector_view_pager_indicator
//        }
////        inputPanel.setInputLinearLayoutStyle {
////            layoutParams.apply {
////                height = SizeUtils.dp2px(49f)
////            }
////        }

//}


/**
 * 初始化表情
 */
//private fun initEmotion() {
//        inputPanel.getEmotionLayoutStyle {
//            //      icSetting = //表情管理的icon
////      icAdd =//添加表情的icon
////      icEmoji =//emoji的icon
//            setEmotionExtClickListener(object : IEmotionExtClickListener {
//                override fun onEmotionAddClick(view: View) {
//                    Log.e("ConversationActivity", "点击了表情添加")
//                }
//
//                override fun onEmotionSettingClick(view: View) {
//                    Log.e("ConversationActivity", "点击了表情管理")
//                }
//            })
//            inputPanel.setExtensionButtonStyle {
//                //               onSendClick
//            }
//            inputPanel.setExtensionButtonStyle {
//                onClick {
//                    RxPermissions(this@ChatActivity)
//                        .request(
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        )
//                        .subscribe({
//                            if (it) {
//                                //文件读取权限
////                                PicComponent.startPicSelect(this@ChatActivity, mixing = true)
//                            } else {
//                                //没有录音或者文件读取权限
//                                toast("没有文件读取权限")
//                            }
//                        }, {
//                            toast("文件读取权限获取异常")
//                        })
//                }
//            }
//            setEmotionAddVisible(false)//添加显示
//            setEmotionSettingVisible(false)//设置显示
//        }
//}

/**
 * 初始化扩展
 */
//    private fun initExtension() {
//        ConversationExtManager.getInstance().run {
//            clearConversationExt()
////            viewModel?.let {
////                addConversationExt(AlbumExtension(it))
////            }
//        }
//        inputPanel.initExtension()
////    inputPanel.extension?.setImageViewStyle {
////     //设置所有扩展image的样式
////    }
//        inputPanel.extension?.setTextViewStyle {
//            //设置所有扩展的文字样式
//            setTextColor(Color.RED)
//        }
//    }

//    fun showAudioRecordPermissionDialog() {
//        (mCo as MainActivity).supportFragmentManager.let {
//            commAlertDialog(it) {
//                type = AlertDialogType.DOUBLE_BUTTON
//                title = "无法访问麦克风"
//                content = "请前往系统设置里为开放麦克风权限"
//                confirmText = "前往授权"
//                cancelText = "我再看看"
//                confirmCallback = {
//                    PermissionUtils.launchAppDetailsSettings()
//                }
//            }
//        }
//    }
//}