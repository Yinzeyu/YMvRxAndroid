package com.yzy.example.im.message

import android.net.Uri
import io.rong.message.MediaMessageContent
import java.io.File

/**
 *description: 视频消息.
 *@date 2019/5/27 11:36.
 *@author: yzy.
 */
abstract class VideoMessage : MediaMessageContent() {
    var videoLocalUri: Uri? = null
        set(value) {
            value?.let {
                val file = File(it.path)
                if (file.exists()) {
                    localPath = Uri.fromFile(file)
                    field = value
                }
            }
        }
    var videoUrl: String? = null
    var coverLocalUri: Uri? = null
    var coverUrl: String? = null
}