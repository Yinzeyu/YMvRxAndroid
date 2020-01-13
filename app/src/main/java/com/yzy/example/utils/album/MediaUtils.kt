package com.yzy.example.utils.album

import android.media.MediaMetadataRetriever
import com.yzy.example.utils.album.entity.LocalMedia
import java.io.File
/**
 * description : 多媒体工具
 *
 * @author : yzy
 * @date : 2020/1/13 14:02
 */
class MediaUtils private constructor() {
    private object SingletonHolder {
        val holder = MediaUtils()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    //获取多媒体文件类型
    fun getMediaType(pictureType: String?): MediaType {
        var type = MediaType.IMAGE
        pictureType?.let {
            type = when (it) {
                "image/png",
                "image/PNG",
                "image/jpeg",
                "image/JPEG",
                "image/webp",
                "image/WEBP",
                "image/gif",
                "image/bmp",
                "image/GIF",
                "imagex-ms-bmp" -> MediaType.IMAGE
                "video/3gp",
                "video/3gpp",
                "video/3gpp2",
                "video/avi",
                "video/mp4",
                "video/quicktime",
                "video/x-msvideo",
                "video/x-matroska",
                "video/mpeg",
                "video/webm",
                "video/mp2ts" -> MediaType.VIDEO
                "audio/mpeg",
                "audio/x-ms-wma",
                "audio/x-wav",
                "audio/amr",
                "audio/wav",
                "audio/aac",
                "audio/mp4",
                "audio/quicktime",
                "audio/lamr",
                "audio/3gpp" -> MediaType.AUDIO
                else -> MediaType.IMAGE
            }
        }
        return type
    }

    //判断是否是GIF图片
    fun isGif(pictureType: String?): Boolean {
        var gif = false
        pictureType?.let {
            gif = when (it) {
                "image/gif" -> true
                "image/GIF" -> true
                else -> false
            }
        }
        return gif
    }

    //判断是否是视频
    fun isVideo(pictureType: String?): Boolean {
        var video = false
        pictureType?.let {
            video = when (it) {
                "video/3gp" -> true
                "video/3gpp" -> true
                "video/3gpp2" -> true
                "video/avi" -> true
                "video/mp4" -> true
                "video/quicktime" -> true
                "video/x-msvideo" -> true
                "video/x-matroska" -> true
                "video/mpeg" -> true
                "video/webm" -> true
                "video/mp2ts" -> true
                else -> false
            }
        }
        return video
    }

    //获取文件多媒体类型
    fun getPictureType(file: File?): String {
        var result = "image/jpeg"
        file?.let {
            val name = it.name
            if (name.endsWith(".mp4") || name.endsWith(".avi") || name.endsWith(".3gpp")
                || name.endsWith(".3gp") || name.startsWith(".mov")
            ) {
                result = "video/mp4"
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")
                || name.endsWith(".bmp")
            ) {
                result = "image/jpeg"
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                || name.endsWith(".aac") || name.endsWith(".war")
                || name.endsWith(".flac") || name.endsWith(".lamr")
            ) {
                result = "audio/mpeg"
            }
        }
        return result
    }

    //获取视频长度
    fun getLocalVideoDuration(videoPath: String?): Long {
        var duration = 0L
        videoPath?.let {
            try {
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(it)
                duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return duration
    }

    //判断是否是长图
    fun isLongImg(media: LocalMedia?): Boolean {
        var longImg = false
        media?.let {
            longImg =
                if (getMediaType(it.pictureType) == MediaType.IMAGE) {
                    it.height > it.width * 5 || it.width > it.height * 3
                } else false
        }
        return longImg
    }

    //获取图片后缀名(文件存在默认png)
    fun getImageSuffix(path: String?): String {
        path?.let {
            return try {
                val index = path.lastIndexOf(".")
                if (index > 0) {
                    when (val imageType = path.substring(index, path.length)) {
                        ".png", ".PNG", ".jpg", ".jpeg", ".JPEG", ".WEBP", ".bmp", ".BMP", ".webp" -> imageType
                        else -> ".png"
                    }
                } else {
                    ".png"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ".png"
            }
        }
        return ""
    }
}