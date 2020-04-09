package com.yzy.example.component.message

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.*
import com.yzy.example.R
import com.yzy.example.component.album.AlbumFragment
import com.yzy.example.component.comm.CommTitleFragment
import com.yzy.example.component.main.MainActivity
import com.yzy.example.extention.options
import kotlinx.android.synthetic.main.activity_comm_title.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class ChatFragment : CommTitleFragment<NoViewModel,ViewDataBinding>() {

    companion object {
        fun startChatFragment(controller: NavController, @IdRes id: Int) {
            controller.navigate(id, Bundle().apply { },options)
        }
    }

    override fun layoutResContentId(): Int = R.layout.fragment_chat

    override fun initContentView() {
//        flTitleBarView.layoutParams.height=BarUtils.getStatusBarHeight()+SizeUtils.dp2px(49f)
        mContext.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun initData() {
//        initInputPanel()
    }






//   private var myTouchListener = object : MainActivity.MyTouchListener {
//        override fun onTouchEvent(event: MotionEvent?) {
//            event?.let {
//                if (inputPanelkeOpen && (it.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
//                    //点击哪些View需要关闭键盘同时响应点击
//                    if (!isTouchViewOut(commTitleBack, it)) {//多个View通过||连接
//                        KeyboardUtils.hideSoftInput(mContext)
//                    }
//                    //没有点击到对于View则关闭键盘
//                    else if (isTouchViewOut(inputPanel, it)) {//多个View通过&&连接
//                        KeyboardUtils.hideSoftInput(mContext)
//                        return
//                    }
//                }
//            }
//        }
//
//    }

    override fun onDestroyView() {
//        (mContext as MainActivity).unRegisterMyTouchListener(myTouchListener)
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
//    private fun initInputPanel() {
//        inputPanel.icEmotion = R.drawable.ic_big_expression
//        inputPanel.icKeyboard = R.drawable.ic_big_keyboard
//        inputPanel.icVoice = R.drawable.ic_big_voice
//        inputPanel.audioRecorderPanel = MMAudioRecorderPanel(mContext as MainActivity)
//        inputPanel.attach(mContext as MainActivity, mContext.mContentView,rootLinearLayout,selectAlbum = {
//            AlbumFragment.startAlbumFragment(mNavController,R.id.action_chatFragment_to_albumFragment)
//        })
//        inputPanel.setSendButtonStyle {
//            backgroundResource = R.drawable.shape_solid_30d18b_13h
//            layoutParams.height = SizeUtils.dp2px(27f)
//            layoutParams.width = SizeUtils.dp2px(44f)
//        }
//        setVoiceNormalStyle()
//        inputPanel.audioRecorderPanel?.setRecordListener(object : OnRecordListener {
//            override fun onNoPermission() {
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
//            }
//
//            override fun onRecordFail(e: Exception) {
//                if (e is MMAudioRecorderPanel.TooShortException) {
//                    mContext.toast("说话时间太短！")
//                }
//                setVoiceNormalStyle()
//            }
//
//            override fun onRecordStateChanged(state: RecordState) {
//                when (state) {
//                    RecordState.START -> {
//                        //开始录音
//                        setVoiceRecodingStyle()
//                    }
//                    RecordState.TO_CANCEL -> {
//                        //手指上滑了将要取消的状态
//                    }
//                    RecordState.TO_TIMEOUT -> {
//                        //剩余时间小于10s
//                    }
//                    RecordState.STOP -> {
//                        //录音结束
//                        setVoiceNormalStyle()
//                    }
//                    else -> {
//                    }
//                }
//            }
//
//            override fun onRecordSuccess(audioFile: String, duration: Long) {
//                setVoiceNormalStyle()
//                if (duration >= 1000) {
////                    if (viewModel?.isTargetUserBanned() == true) {
////                        toast("该账号封禁中，无法进行互动")
////                    } else {
////                        viewModel?.sendVoiceMessage(audioFile, duration)
////                    }
//                }
//            }
//        })
//    }

//        private fun setVoiceRecodingStyle() {
//        inputPanel.setAudioButtonStyle {
//            text = "松开发送"
//            textColor = mContext.getResColor(R.color.c_ff999999)
//            backgroundResource = R.drawable.shape_solid_f3f3f3_8
//        }
//    }
//
//    private fun setVoiceNormalStyle() {
//        inputPanel.setAudioButtonStyle {
//            text = "按住说话"
//            textColor = mContext.getResColor(R.color.white)
//            backgroundResource = R.drawable.shape_solid_54d25d_8
//        }
//    }


}
