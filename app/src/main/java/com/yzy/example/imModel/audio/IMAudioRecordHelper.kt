package com.yzy.example.imModel.audio

import android.content.Context
import android.media.MediaRecorder
import kotlinx.coroutines.*
import java.io.File

class IMAudioRecordHelper {

    /**
     * 音频焦点工具类
     */
    private var audioFocusManager: AudioFocusManager? = null
    /**
     * 录音工具类
     */
    private var mediaRecorder: MediaRecorder? = null
    /**
     * 录音的回调
     */
    var recordListener: AudioRecordListener? = null
    /**
     * 录音的文件路径
     */
    private var filePath: String? = null
    /**
     * 录音的最大时长,默认一分钟
     */
    private var maxDuration: Long = 6000L
    /**
     * 已经录音的时长
     */
    private var duration: Long = 0L
    /**
     * 开始时间
     */
    private var startTime: Long = 0L
    /**
     * 录音中的Disposable
     */
    private var mainScope: CoroutineScope? = null

    /**
     * 开始录音
     */
    fun startRecord(
        context: Context,
        filePath: String,
        maxDuration: Long = 60000,
        sampleRate: IMAudioSampleRate = IMAudioSampleRate.HZ_8000,
        listener: AudioRecordListener? = null
    ) {
        if (mainScope != null) {
            listener?.onError(Exception("已经在录音中"))
            return
        }
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        this.filePath = filePath
        if (audioFocusManager == null) {
            audioFocusManager = AudioFocusManager(context)
        } else {
            //如果已经使用过录音工具类，先释放一下音频焦点
            audioFocusManager?.releaseAudioFocus()
        }
        //先获取音频焦点
        audioFocusManager?.requestAudioFocus()

        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }
        mediaRecorder?.let { recorder ->
            recordListener = listener
            this.maxDuration = maxDuration
            //设置采样率
            recorder.setAudioSamplingRate(sampleRate.sampleRate)
            //设置码率
            recorder.setAudioEncodingBitRate(sampleRate.bps)

            recorder.setAudioChannels(1)
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)

            //根据采样率设置对应的格式
            if (sampleRate == IMAudioSampleRate.HZ_16000) {
                recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            } else {
                recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            }
            recorder.setOutputFile(file.absolutePath)
            try {
                recorder.prepare()
                recorder.start()
                recoding()
            } catch (e: Exception) {
                recordListener?.onError(e)
            }
        }
    }

    private fun recoding() {
        startTime = System.currentTimeMillis()
        recordListener?.onStart()
        mainScope = MainScope()
        mainScope?.launch {
            repeat(100_000) {
                duration = System.currentTimeMillis() - startTime
                recordListener?.onRecodeData(duration, mediaRecorder?.maxAmplitude ?: 0)
                if (duration >= maxDuration) {
                    //录音时长已经到了最大时长了
                    stopRecord()
                }
                println("stopRecord$duration")
                delay(100L)
            }
        }
    }

    /**
     * 停止录音
     */
    fun stopRecord() {
        mainScope?.cancel()
        mainScope=null
        //释放音频焦点
        audioFocusManager?.requestAudioFocus()
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            recordListener?.onComplete(filePath, duration)
        } catch (e: Exception) {
            recordListener?.onError(e)
        } finally {
            mediaRecorder = null
            recordListener = null
            audioFocusManager = null
            filePath = null
            duration = 0
            startTime = 0
        }
    }
}

/**
 * 录音回调的接口
 */
interface AudioRecordListener {
    /**
     * 录音开始
     */
    fun onStart()

    /**
     * 录音异常
     */
    fun onError(e: Exception)

    /**
     * 录音中的回调
     * @param duration 当前时长单位毫秒
     * @param maxAmplitude 声音的最大振幅
     */
    fun onRecodeData(duration: Long, maxAmplitude: Int)

    /**
     * 录音结束
     * @param filePath 录音文件的地址
     * @param duration 录音的时长
     */
    fun onComplete(filePath: String?, duration: Long)
}

/**
 * 录音的采样率，融云只支持8K和16K
 */
enum class IMAudioSampleRate(val sampleRate: Int, val bps: Int) {
    HZ_16000(16000, 12650),
    HZ_8000(8000, 7950);
}