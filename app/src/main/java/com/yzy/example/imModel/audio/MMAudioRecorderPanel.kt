package com.yzy.example.imModel.audio

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import com.yzy.example.utils.AudioPermissionHelper
import com.yzy.example.widget.file.AppFileDirManager
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class MMAudioRecorderPanel(val context: Context) : IAudioRecorderPanel, View.OnTouchListener {

    /**
     * 最长录音时间，单位：毫秒
     */
    var maxDuration = 60 * 1000L
    /**
     * 最短录音时间，单位：毫秒
     */
    var minDuration = 1 * 1000L
    /**
     * 录音剩余多少秒时开始倒计时，单位：毫秒
     */
    var countDown = 10 * 1000L
    /**
     * 录音的采样率，默认为8K
     */
    var sampleRate: IMAudioSampleRate = IMAudioSampleRate.HZ_8000

    private var recordListener: OnRecordListener? = null

    private var isRecording: Boolean = false
    private var isToCancel: Boolean = false
    private var isCanceled: Boolean = false
    private var currentAudioFile: String? = null

    private var rootView: View? = null
    private var button: Button? = null
    private var recorder: IMAudioRecordHelper? = null
    private var handler: Handler? = null

    var durationTextView: TextView? = null
    var stateTextView: TextView? = null
    var recordBg: LinearLayout? = null
    var stateImageView: ImageView? = null
    var voiceAmplitudeView: VoiceAmplitudeView? = null
    var recordingWindow: PopupWindow? = null

    private var isLongClick = false
    private var isStart = false
    private var startX = 0f
    private var startY = 0f
    private var mainScope: CoroutineScope? = null


    var durationFormat = SimpleDateFormat("m:ss", Locale.getDefault())
    /**
     * 取消的图
     */
    var cancelIcon = R.mipmap.ic_chat_voice_cancel
    /**
     * 警告的icon
     */
    var warningIcon = R.mipmap.ic_chat_voice_warning
//  /**
//   * 音量大小的指示图
//   */
//  var volumeIcons = arrayOf(
//    R.mipmap.imui_ic_volume_1,
//    R.mipmap.imui_ic_volume_2,
//    R.mipmap.imui_ic_volume_3,
//    R.mipmap.imui_ic_volume_4,
//    R.mipmap.imui_ic_volume_5,
//    R.mipmap.imui_ic_volume_6,
//    R.mipmap.imui_ic_volume_7,
//    R.mipmap.imui_ic_volume_8
//  )


    private var audioRecordListener = object : AudioRecordListener {
        override fun onStart() {
            if (isStart) {
                showRecording()
            } else {
                hideRecording()
            }
        }

        override fun onError(e: Exception) {
            LogUtils.e("MMAudioRecorderPanel AudioRecordListener onError", e)
            recordListener?.onRecordFail(e)
            hideRecording()
        }

        override fun onRecodeData(duration: Long, maxAmplitude: Int) {
            if (duration > maxDuration) {
                timeout()
            } else if (duration > maxDuration - countDown) {
//        var tmp = ((maxDuration - (duration)) / 1000).toInt()
//        tmp = if (tmp > 1) tmp else 1
//        showCountDown(tmp)
                recordListener?.onRecordStateChanged(RecordState.TO_TIMEOUT)
            }
            showDuration(duration)
            updateVolume(maxAmplitude)
        }

        override fun onComplete(filePath: String?, duration: Long) {
            if (recordListener != null) {
                if (duration > minDuration) {
                    currentAudioFile?.let {
                        if (!isCanceled) {
                            recordListener?.onRecordSuccess(it, duration)
                        }
                    }
                    hideRecording()
                } else {
                    recordListener?.onRecordFail(TooShortException(minDuration))
                    showTooShortTip()
                    handler?.postDelayed({ this@MMAudioRecorderPanel.hideRecording() }, 1000)
                }
            } else {
                hideRecording()
            }
            isToCancel = false
            isRecording = false
        }
    }

    /**
     * 将[MMAudioRecorderPanel]附加到button上面
     *
     * @param rootView 录音界面显示的rootView
     * @param button   长按触发录音的按钮
     */
    override fun attach(rootView: View, button: Button) {
        this.rootView = rootView
        this.button = button
        this.button?.setOnTouchListener(this)
    }

    override fun setRecordListener(recordListener: OnRecordListener) {
        this.recordListener = recordListener
    }

    private fun startRecord() {
        if (isStart) {
            //有录音权限
            isRecording = true
            if (recorder == null) {
                recorder = IMAudioRecordHelper()
                handler = Handler()
            }
            currentAudioFile = genAudioFile()
            currentAudioFile?.let {
                LogUtils.e("MMAudioRecorderPanel startRecord file=$it")
                isCanceled = false
                if (isStart) {
                    recorder?.startRecord(context, it, maxDuration, sampleRate, audioRecordListener)
                }
            }
        }
    }


    private fun stopRecord() {
        if (!isRecording) {
            return
        }
        recorder?.stopRecord()
    }

    private fun cancelRecord() {
        isCanceled = true
        recorder?.stopRecord()
        recordListener?.onRecordFail(Exception("user canceled"))
        hideRecording()
        isToCancel = false
        isRecording = false
    }

    private fun showRecording() {
        if (recordingWindow == null) {
            val view =
                View.inflate(context, R.layout.layout_message_audio_popup, null)
            recordBg = view.findViewById(R.id.rc_audio_bg)
            stateImageView = view.findViewById(R.id.rc_audio_state_image)
            voiceAmplitudeView = view.findViewById(R.id.rc_audio_voice_amplitude)
            stateTextView = view.findViewById(R.id.rc_audio_state_text)
            durationTextView = view.findViewById(R.id.rc_audio_timer)
            recordingWindow =
                PopupWindow(
                    view,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            recordingWindow?.isFocusable = true
            recordingWindow?.isOutsideTouchable = false
            recordingWindow?.isTouchable = false
        }
        recordingWindow?.showAtLocation(rootView, Gravity.CENTER, 0, 0)
        stateImageView?.visible()
//    if (volumeIcons.isNotEmpty()) {
//      stateImageView?.setImageResource(volumeIcons[0])
//    }
        voiceAmplitudeView?.setProgress(0)
        stateTextView?.visibility = View.VISIBLE
        stateTextView?.setText(R.string.imui_voice_rec)
        stateTextView?.setTextColor(Color.WHITE)
    }

    private fun hideRecording() {
        recordingWindow?.dismiss()
        recordingWindow = null
    }

    private fun showTooShortTip() {
        stateImageView?.setImageResource(warningIcon)
        voiceAmplitudeView?.setProgress(0)
        stateTextView?.text = "说话时间太短"
        stateTextView?.setTextColor(Color.parseColor("#F85859"))
    }

    private fun showCancelTip() {
        stateImageView?.visibility = View.VISIBLE
        stateImageView?.setImageResource(cancelIcon)
        voiceAmplitudeView?.setProgress(0)
        stateTextView?.visibility = View.VISIBLE
        stateTextView?.setText(R.string.imui_voice_cancel)
        stateTextView?.setTextColor(Color.parseColor("#F85859"))
    }

    private fun hideCancelTip() {
        showRecording()
    }

    /**
     * 显示时长进度
     */
    private fun showDuration(duration: Long) {
        durationTextView?.text = durationFormat.format(duration)
    }

//  /**
//   * @param seconds
//   */
//  private fun showCountDown(seconds: Int) {
//    stateImageView?.visibility = View.INVISIBLE
//    stateTextView?.visibility = View.VISIBLE
//    stateTextView?.setText(R.string.imui_voice_rec)
//    stateTextView?.setBackgroundResource(R.drawable.imui_bg_voice_popup)
//    durationTextView?.text = String.format("%s", seconds)
//    durationTextView?.visibility = View.VISIBLE
//  }

    private fun timeout() {
        stopRecord()
    }

    private fun updateVolume(maxAmplitude: Int) {
        if (isToCancel) {
            return
        }
        this.stateImageView?.setImageResource(R.drawable.bitmap_microphone)
        val db = 100 * maxAmplitude / 32768
        if (db <= 100) {
//        this.stateImageView?.setImageResource(volumeIcons[db])
            voiceAmplitudeView?.setProgress(db)
        } else {
            voiceAmplitudeView?.setProgress(100)
//        this.stateImageView?.setImageResource(volumeIcons[volumeIcons.size - 1])
        }
    }

    private fun genAudioFile(): String {
        val dir = File(AppFileDirManager.getImVoiceDir(context))
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, "im_voice_${System.currentTimeMillis()}")
        return file.absolutePath
    }

    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return (event.rawX < location[0] || event.rawX > location[0] + view.width
                || event.rawY < location[1] - 40)
    }

    private fun longPressDisposable() {
        mainScope = MainScope()
        mainScope?.launch {
            delay(80L)
            isLongClick = true
            cancel()
            mainScope=null
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isLongClick = false
                isStart = true
                startX = event.x
                startY = event.y
                longPressDisposable()
                if (AudioPermissionHelper.hasRecordPermission()) {
                    startRecord()
                    recordListener?.onRecordStateChanged(RecordState.START)
                } else {
                    recordListener?.onNoPermission()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX =
                    sqrt(((event.x - startX) * (event.x - startX) + (event.y - startY) * (event.y - startY)).toDouble())
                //移动超过20像素
                if (deltaX > 20 && mainScope != null) {
                    mainScope?.cancel()
                    mainScope=null
                }
        if (isLongClick) {
                isToCancel = isCancelled(v, event)
                if (isToCancel) {
                    if (recordListener != null) {
                        recordListener?.onRecordStateChanged(RecordState.TO_CANCEL)
                    }
                    showCancelTip()
                } else {
                    hideCancelTip()
                }
        }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                when {
                    isToCancel -> {
                        cancelRecord()
                    }
                    isRecording -> {
                        stopRecord()
                    }
                    else -> {
                        hideRecording()
                    }
                }
                isLongClick = false
                if (mainScope != null) {
                    mainScope?.cancel()
                    mainScope=null
                    hideRecording()
                }
                recordListener?.onRecordStateChanged(RecordState.STOP)
                isStart = false
            }
            else -> {
                isLongClick = false
                mainScope?.cancel()
                mainScope=null
            }
        }
        return true
    }

    class TooShortException(val minDuration: Long) : Exception()
}