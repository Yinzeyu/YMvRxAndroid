package com.yzy.pj.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.blankj.utilcode.util.LogUtils


/**
 * description: 权限弹窗、权限管理（目前用于首页活动）.
 *
 * @author Sorgs.
 * @date 2018/7/14.
 */
object PermissionUtils {


  /**
   * 是否有录音权限
   */
  fun hasRecordPermission(): Boolean {
    LogUtils.d("Permission hasRecordPermission 判断是否有语音权限")
    val minBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT)
    var audioRecord: AudioRecord? = AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
        AudioFormat.CHANNEL_IN_STEREO,
        AudioFormat.ENCODING_PCM_16BIT, minBufferSize)
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
      } catch (e: Exception) {
        LogUtils.e("hasRecordPermission", e)
      }
      return false
    }
    val bufferSizeInBytes = 1024
    val audioData = ByteArray(bufferSizeInBytes)
    var readSize = 0
    // 正在录音
    readSize = audioRecord.read(audioData, 0, bufferSizeInBytes)
    if (readSize == AudioRecord.ERROR_INVALID_OPERATION || readSize <= 0) {
      LogUtils.e("hasRecordPermission readSize : $readSize")
      return false
    }

    try {
      audioRecord.stop()
      audioRecord.release()
    } catch (e: Exception) {
      LogUtils.e("hasRecordPermission", e)
    }
    return true
  }

//  /**
//   * 没有录音权限时的弹框提示
//   */
//  fun showAudioRecordPermissionDialog(activity: Activity) {
//    CommDialog.builder(activity)
//        .setTitleStr("无法访问麦克风")
//        .setContentStr("请检查是否有其他应用占用麦克风或前往系统设置里为咪哒星球开放麦克风权限")
//        .setCancelStr("我再看看")
//        .setConfirmStr("前往授权")
//        .setConfirmClick {
//          PermissionUtils.gotoPermissionSetting(activity)
//        }
//        .build()
//        .show()
//  }
}
