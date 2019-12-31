package com.yzy.example.imModel.audio

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Handler
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.yzy.example.R
import java.io.File

class AudioRecorderPanel(val context: Context) : IAudioRecorderPanel, View.OnTouchListener {

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
    private var currentAudioFile: String? = null

    private var rootView: View? = null
    private var button: Button? = null
    private var recorder: IMAudioRecordHelper? = null
    private var handler: Handler? = null

    var countDownTextView: TextView? = null
    var stateTextView: TextView? = null
    var recordBg: RelativeLayout? = null
    var stateImageView: ImageView? = null
    var recordingWindow: PopupWindow? = null

    /**
     * 取消的图
     */
    var cancelIcon = R.mipmap.imui_ic_volume_cancel
    /**
     * 警告的icon
     */
    var warningIcon = R.mipmap.imui_ic_volume_wraning
    /**
     * 音量大小的指示图
     */
    var volumeIcons = arrayOf(
        R.mipmap.imui_ic_volume_1,
        R.mipmap.imui_ic_volume_2,
        R.mipmap.imui_ic_volume_3,
        R.mipmap.imui_ic_volume_4,
        R.mipmap.imui_ic_volume_5,
        R.mipmap.imui_ic_volume_6,
        R.mipmap.imui_ic_volume_7,
        R.mipmap.imui_ic_volume_8
    )


    private var audioRecordListener = object : AudioRecordListener {
        override fun onStart() {
            recordListener?.onRecordStateChanged(RecordState.START)
            showRecording()
        }

        override fun onError(e: Exception) {
            recordListener?.onRecordFail(e)
        }

        override fun onRecodeData(duration: Long, maxAmplitude: Int) {
            if (duration > maxDuration) {
                timeout()
            } else if (duration > maxDuration - countDown) {
                var tmp = ((maxDuration - (duration)) / 1000).toInt()
                tmp = if (tmp > 1) tmp else 1
                showCountDown(tmp)
                recordListener?.onRecordStateChanged(RecordState.TO_TIMEOUT)
            }
            updateVolume(maxAmplitude)
        }

        override fun onComplete(filePath: String?, duration: Long) {
            if (recordListener != null) {
                if (duration > minDuration) {
                    currentAudioFile?.let {
                        recordListener?.onRecordSuccess(it, duration)
                    }
                    hideRecording()
                } else {
                    recordListener?.onRecordFail(Exception("too short"))
                    showTooShortTip()
                    handler?.postDelayed({ this@AudioRecorderPanel.hideRecording() }, 1000)
                }
            } else {
                hideRecording()
            }
            isToCancel = false
            isRecording = false
        }
    }

    /**
     * 将[AudioRecorderPanel]附加到button上面
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
        if (hasRecordPermission()) {
            //有录音权限
            isRecording = true
            if (recorder == null) {
                recorder = IMAudioRecordHelper()
                handler = Handler()
            }
            currentAudioFile = genAudioFile()
            currentAudioFile?.let {
                recorder?.startRecord(context, it, maxDuration, sampleRate, audioRecordListener)
            }
        } else {
            recordListener?.onNoPermission()
        }
    }

    /**
     * 是否有录音权限
     */
    private fun hasRecordPermission(): Boolean {
        val minBufferSize = AudioRecord.getMinBufferSize(
            44100,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        var audioRecord: AudioRecord? = AudioRecord(
            MediaRecorder.AudioSource.MIC, 44100,
            AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT, minBufferSize
        )
        try {
            // 开始录音
            audioRecord?.startRecording()
        } catch (e: Exception) {
            //可能情况一
            audioRecord?.release()
            LogUtils.e("hasRecordPermission", e)
        }

        // 检测是否在录音中,6.0以下会返回此状态
        if (audioRecord?.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
            //可能情况二
            try {
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null
            } catch (e: Exception) {
                LogUtils.e("hasRecordPermission", e)
            }
            return false
        }
        val bufferSizeInBytes = 1024
        val audioData = ByteArray(bufferSizeInBytes)
        var readSize = 0
        // 正在录音
        readSize = audioRecord?.read(audioData, 0, bufferSizeInBytes)
        if (readSize == AudioRecord.ERROR_INVALID_OPERATION || readSize <= 0) {
            LogUtils.e("hasRecordPermission readSize : $readSize")
            return false
        }
        try {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        } catch (e: Exception) {
            LogUtils.e("hasRecordPermission", e)
        }
        return true
    }

    private fun stopRecord() {
        if (!isRecording) {
            return
        }
        recorder?.stopRecord()

    }

    private fun cancelRecord() {
        recorder?.stopRecord()
        recordListener?.onRecordFail(Exception("user canceled"))
        hideRecording()
        isToCancel = false
        isRecording = false
    }

    private fun showRecording() {
        if (recordingWindow == null) {
            val view = View.inflate(context, R.layout.imui_layout_audio_popup_wi_vo, null)
            recordBg = view.findViewById(R.id.rc_audio_bg)
            stateImageView = view.findViewById(R.id.rc_audio_state_image)
            stateTextView = view.findViewById(R.id.rc_audio_state_text)
            countDownTextView = view.findViewById(R.id.rc_audio_timer)
            recordingWindow =
                PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            recordingWindow?.isFocusable = true
            recordingWindow?.isOutsideTouchable = false
            recordingWindow?.isTouchable = false
        }
        recordingWindow?.showAtLocation(rootView, Gravity.CENTER, 0, 0)
        stateImageView?.visibility = View.VISIBLE
        if (volumeIcons.isNotEmpty()) {
            stateImageView?.setImageResource(volumeIcons[0])
        }
        stateTextView?.visibility = View.VISIBLE
        stateTextView?.setText(R.string.imui_voice_rec)
        stateTextView?.setBackgroundResource(R.drawable.imui_bg_voice_popup)
        countDownTextView?.visibility = View.GONE
    }

    private fun hideRecording() {
        recordingWindow?.dismiss()
        recordingWindow = null
    }

    private fun showTooShortTip() {
        stateImageView?.setImageResource(warningIcon)
        stateTextView?.setText(R.string.imui_voice_short)
    }

    private fun showCancelTip() {
        countDownTextView?.visibility = View.GONE
        stateImageView?.visibility = View.VISIBLE
        stateImageView?.setImageResource(cancelIcon)
        stateTextView?.visibility = View.VISIBLE
        stateTextView?.setText(R.string.imui_voice_cancel)
        stateTextView?.setBackgroundResource(R.drawable.imui_corner_voice_style)
    }

    private fun hideCancelTip() {
        showRecording()
    }

    /**
     * @param seconds
     */
    private fun showCountDown(seconds: Int) {
        stateImageView?.visibility = View.GONE
        stateTextView?.visibility = View.VISIBLE
        stateTextView?.setText(R.string.imui_voice_rec)
        stateTextView?.setBackgroundResource(R.drawable.imui_bg_voice_popup)
        countDownTextView?.text = String.format("%s", seconds)
        countDownTextView?.visibility = View.VISIBLE
    }

    private fun timeout() {
        stopRecord()
    }

    private fun updateVolume(maxAmplitude: Int) {
        if (isToCancel) {
            return
        }
        if (volumeIcons.isNotEmpty()) {
            val db = volumeIcons.size * maxAmplitude / 32768
            if (db < volumeIcons.size) {
                this.stateImageView?.setImageResource(volumeIcons[db])
            } else {
                this.stateImageView?.setImageResource(volumeIcons[volumeIcons.size - 1])
            }
        }
    }

    private fun genAudioFile(): String {
        val dir = File(context.filesDir, "audio")
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

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> startRecord()
            MotionEvent.ACTION_MOVE -> {
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
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (isToCancel) {
                cancelRecord()
            } else if (isRecording) {
                stopRecord()
            }
            else -> {
            }
        }
        return true
    }
}