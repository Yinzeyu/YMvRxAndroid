package com.yzy.example.imModel.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

class AudioFocusManager(
    val context: Context,
    private val audioFocusListener: AudioFocusListener? = null
) {

    private var mAudioManager: AudioManager? = null
    private var mAudioFocusChangeListener: AudioManager.OnAudioFocusChangeListener
    private var mAudioFocusRequest: AudioFocusRequest? = null

    init {
        mAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            //监听器
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    //获得了Audio Focus
                    audioFocusListener?.onGain()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    //失去了Audio Focus，并将会持续很长的时间。
                    audioFocusListener?.onLoss()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    //暂时失去Audio Focus，并会很快再次获得。
                    audioFocusListener?.onLoss()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    //暂时失去AudioFocus，但是可以继续播放，不过要在降低音量。
                    audioFocusListener?.onLoss()
                }
            }
        }
    }

    /**
     * 请求音频焦点 设置监听
     */
    fun requestAudioFocus() {
        if (mAudioManager == null) {
            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        if (mAudioManager != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mAudioManager?.requestAudioFocus(
                    mAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                )
            } else {
                if (mAudioFocusRequest == null) {
                    mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(mAudioFocusChangeListener)
                        .build()
                }
                mAudioFocusRequest?.let {
                    mAudioManager?.requestAudioFocus(it)
                }
            }
        }
    }


    /**
     * 释放音频焦点
     */
    fun releaseAudioFocus() {
        if (mAudioManager == null) {
            mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        if (mAudioManager != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mAudioManager?.abandonAudioFocus(mAudioFocusChangeListener)
            } else {
                if (mAudioFocusRequest == null) {
                    mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(mAudioFocusChangeListener)
                        .build()
                }
                mAudioFocusRequest?.let {
                    mAudioManager?.abandonAudioFocusRequest(it)
                }
            }
        }
    }

    /**
     * 音频焦点变化的回调接口
     */
    interface AudioFocusListener {
        /**
         * 获得焦点
         */
        fun onGain()

        /**
         * 失去焦点
         */
        fun onLoss()
    }
}