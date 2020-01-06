package com.yzy.example.component.message

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.*
import com.yzy.example.R
import com.yzy.example.component.comm.CommTitleActivity
import com.yzy.example.component.dialog.AlertDialogType
import com.yzy.example.component.dialog.commAlertDialog
import com.yzy.example.imModel.ConversationExtManager
import com.yzy.example.imModel.ConversationInputPanel
import com.yzy.example.imModel.audio.MMAudioRecorderPanel
import com.yzy.example.imModel.audio.OnRecordListener
import com.yzy.example.imModel.audio.RecordState
import com.yzy.example.imModel.emoji.IEmotionExtClickListener
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : CommTitleActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun layoutResContentId(): Int = R.layout.activity_chat

    override fun initContentView() {
        initRootView()
        initInputPanel()
        initEmotion()
        initExtension()
    }

    override fun initData() {
    }

    private fun initRootView() {
//        rootLinearLayout.addOnKeyboardShownListener(object :
//            KeyboardAwareLinearLayout.OnKeyboardShownListener {
//            override fun onKeyboardShown() {
//                inputPanel.onKeyboardShown()
//                //滚动到最下边
////        listMove2Bottom()
//            }
//        })
//        rootLinearLayout.addOnKeyboardHiddenListener(object :
//            KeyboardAwareLinearLayout.OnKeyboardHiddenListener {
//            override fun onKeyboardHidden() {
//                inputPanel.onKeyboardHidden()
//            }
//        })
    }

    private fun initInputPanel() {
        inputPanel.icEmotion = R.drawable.ic_big_expression
        inputPanel.icExtension = R.drawable.ic_big_dynamic_add_pic
        inputPanel.icKeyboard = R.drawable.ic_big_keyboard
        inputPanel.icVoice = R.drawable.ic_big_voice
        inputPanel.audioRecorderPanel = MMAudioRecorderPanel(this)
        inputPanel.attach(this,rootLinearLayout)
        inputPanel.inputPanelListener = object : ConversationInputPanel.OnInputPanelListener {
            override fun onInputPanelStateChange(isExpanded: Boolean) {
                if (isExpanded) {
                    //输入面板展开，需要将列表滚动到底部
//          listMove2Bottom()
                } else {
                    //面板关闭
                }
            }

            override fun onStickerInput(
                categoryName: String,
                stickerName: String,
                stickerBitmapPath: String?
            ) {
            }

            override fun onEmojiInput(key: String) {
//        Log.e("ConversationActivity", "输入了Emoji表情$key")
            }

            override fun onTyping() {
//        tvTitle.text = "输入中"
            }

            override fun onSend(editable: Editable) {
//                if (viewModel?.isTargetUserBanned() == true) {
//                    toast("该账号封禁中，无法进行互动")
//                } else {
//                    viewModel?.sendTextMessage(editable.toString())
//                }
            }
        }
        inputPanel.setEditTextStyle {
            setPadding(
                SizeUtils.dp2px(12f),
                SizeUtils.dp2px(8f),
                SizeUtils.dp2px(12f),
                SizeUtils.dp2px(8f)
            )
            backgroundResource = R.drawable.shape_solid_f3f3f3_8
            hint = "说点什么..."
            hintTextColor = getResColor(R.color.c_ff999999)
            textColor = getResColor(R.color.c_ff333333)
            textSize = 14f
            maxLines = 5
            minHeight = SizeUtils.dp2px(33f)
        }

        inputPanel.setEmotionLayoutStyle {
            icSend = R.drawable.ic_big_send_expression
            icSendActive = R.drawable.ic_big_send_expression_active
            icDelete = R.drawable.ic_big_delete_expression
            indicatorRes = R.drawable.im_selector_view_pager_indicator
        }
//        inputPanel.setInputLinearLayoutStyle {
//            layoutParams.apply {
//                height = SizeUtils.dp2px(49f)
//            }
//        }
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
                    toast("说话时间太短！")
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
            textColor = getResColor(R.color.c_ff999999)
            backgroundResource = R.drawable.shape_solid_f3f3f3_8
        }
    }

    private fun setVoiceNormalStyle() {
        inputPanel.setAudioButtonStyle {
            text = "按住说话"
            textColor = getResColor(R.color.white)
            backgroundResource = R.drawable.shape_solid_54d25d_8
        }
    }

    /**
     * 初始化表情
     */
    private fun initEmotion() {
        inputPanel.getEmotionLayoutStyle {
            //      icSetting = //表情管理的icon
//      icAdd =//添加表情的icon
//      icEmoji =//emoji的icon
            setEmotionExtClickListener(object : IEmotionExtClickListener {
                override fun onEmotionAddClick(view: View) {
                    Log.e("ConversationActivity", "点击了表情添加")
                }

                override fun onEmotionSettingClick(view: View) {
                    Log.e("ConversationActivity", "点击了表情管理")
                }
            })
            inputPanel.setExtensionButtonStyle {
                //               onSendClick
            }
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
            setEmotionAddVisible(false)//添加显示
            setEmotionSettingVisible(false)//设置显示
        }
    }

    /**
     * 初始化扩展
     */
    private fun initExtension() {
        ConversationExtManager.getInstance().run {
            clearConversationExt()
//            viewModel?.let {
//                addConversationExt(AlbumExtension(it))
//            }
        }
        inputPanel.initExtension()
//    inputPanel.extension?.setImageViewStyle {
//     //设置所有扩展image的样式
//    }
        inputPanel.extension?.setTextViewStyle {
            //设置所有扩展的文字样式
            setTextColor(Color.RED)
        }
    }

    fun showAudioRecordPermissionDialog() {
        supportFragmentManager.let {
            commAlertDialog(it) {
                type = AlertDialogType.DOUBLE_BUTTON
                title = "无法访问麦克风"
                content = "请前往系统设置里为开放麦克风权限"
                confirmText = "前往授权"
                cancelText = "我再看看"
                confirmCallback = {
                    PermissionUtils.launchAppDetailsSettings()
                }
            }
        }
    }
}