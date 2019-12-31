package com.yzy.example.widget.file

/**
 *description: 文件存储目录的常量类.
 *@date 2019/4/1 15:10.
 *@author: YangYang.
 */
interface FilePathConstants {

  companion object {
    /**
     * 公司文件夹名称
     */
    const val COMPANY_FOLDER = "aimyunion"
    /**
     * APP文件夹名称
     */
    const val APP_FOLDER = "midamusic"
    /**
     * 图片
     */
    const val IMAGES = "images"
    /**
     * 文件
     */
    const val FILES = ".files"
    /**
     * 录制相关文件夹
     */
    const val RECORDER = ".recorder"
    /**
     * crash
     */
    const val CRASH = "crash"
    /**
     * 临时文件夹(用完之后会删除)
     */
    const val TEMP = "temp"
    /**
     * 裁剪图片文件名
     */
    const val CROP_IMAGE = "crop/.image"
    /**
     * 缓存的音视频文件(为了不让APP看到缓存视频,所以改名)
     */
    const val MEDIA_CACHE = ".media/.cache"
    /**
     * 视频的裁剪目录
     */
    const val MEDIA_CLIP = ".media/.clip"
    /**
     * 上传
     */
    const val UPLOAD = ".upload"
    /**
     * 阿里分片上传文件的目录
     */
    const val ALI_UPLOAD_FILE = "oss_record"
    /**
     * 聊天的表情包
     */
    const val IM_STICKER = "im/.sticker"
    /**
     * 聊天的缩略图缓存
     */
    const val IM_THUM = "im/.thum"
    /**
     * 聊天的音频文件缓存目录
     */
    const val IM_VOICE = "im/.voice"
    /**
     * 聊天的视频文件缓存目录
     */
    const val IM_VIDEO = "im/.video"
    /**
     * 图片下载保存的路径
     */
    const val IMAGE_SAVE = "咪哒"
  }

}