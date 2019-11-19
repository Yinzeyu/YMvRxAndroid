package com.yzy.example.ui.video

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.mActivity
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.startActivity
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.widget.RecorderView
import kotlinx.android.synthetic.main.activity_taste_vide.*

class TasteVideoActivity : BaseActivity() {
    companion object {
        fun starTasteVideoActivity(context: Context) {
            context.startActivity<TasteVideoActivity>()
        }
    }

    override fun layoutResId(): Int = R.layout.activity_taste_vide
    override fun initView() {
        message_chat_recorder_view.listener = onRecorderListener
//        plVideoVIew.setVideoPath("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4")
//        plVideoVIew.start()
    }

    override fun initData() {
    }


    var onRecorderListener = object : RecorderView.OnRecorderListener {
        override fun cancelRecorder() {
        }

        override fun startRecorder() {

        }

        override fun completeRecorder(path: Uri, duration: Int) {
            if (duration <= 1) {
                mActivity.toast("录音时间过短，无法发送")
                return
            }
            MediaPlayer().apply {
                try {
                    setDataSource(path.path)
                    setAudioStreamType(AudioManager.STREAM_MUSIC)
                    prepareAsync()
                    setOnPreparedListener {
                        it.start()
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }
    }


}
