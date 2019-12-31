package com.yzy.example.widget.file

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SDCardUtils
import java.io.File

/**
 *description: app的文件存储目录相关的管理工具类.
 *@date 2019/4/1 15:12.
 *@author: YangYang.
 */
class AppFileDirManager {
  companion object {
    /**
     * 获取自定义的app的主目录
     */
    fun getAppFileDir(context: Context): String {
      val rootDir = if (SDCardUtils.getSDCardPathByEnvironment().isNotBlank())
        SDCardUtils.getSDCardPathByEnvironment()
      else
        context.cacheDir.path
      return (rootDir
          + File.separator
          + FilePathConstants.COMPANY_FOLDER
          + File.separator
          + FilePathConstants.APP_FOLDER
          + File.separator)
    }

    /**
     * 初始化项目的各种文件夹
     */
    fun initAppFile(context: Context) {
      val appPath = getAppFileDir(context)
      //图片
      FileUtils.createOrExistsDir(appPath + FilePathConstants.IMAGES)
      //文件
      FileUtils.createOrExistsDir(appPath + FilePathConstants.FILES)
      //闪退
      FileUtils.createOrExistsDir(appPath + FilePathConstants.CRASH)
      //临时文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.TEMP)
      //录制目录
      FileUtils.createOrExistsDir(appPath + FilePathConstants.RECORDER)
      //视频或音频缓存文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.MEDIA_CACHE)
      //视频或音频裁剪文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.MEDIA_CLIP)
      //图片裁剪的文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.CROP_IMAGE)
      //上传
      FileUtils.createOrExistsDir(appPath + FilePathConstants.UPLOAD)
      //阿里文件上传记录文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.ALI_UPLOAD_FILE)
      //聊天的表情包文件夹
      FileUtils.createOrExistsDir(appPath + FilePathConstants.IM_STICKER)
      //聊天的图片缩略图缓存目录
      FileUtils.createOrExistsDir(getImImageThumbCacheDir(context))
      //聊天的录音相关缓存目录
      FileUtils.createOrExistsDir(getImVoiceDir(context))
      //聊天的视频相关缓存目录
      FileUtils.createOrExistsDir(getImVideoDir(context))
    }


    /**
     * 获取视频或者音频文件缓存的目录
     */
    fun getMediaCacheDir(context: Context): File {
      return File(getAppFileDir(context) + FilePathConstants.MEDIA_CACHE)
    }

    /**
     * 获取视频或者音频文件裁剪的目录
     */
    fun getMediaClipCacheDir(context: Context): File {
      return File(getAppFileDir(context) + FilePathConstants.MEDIA_CLIP)
    }

    /**
     * 获取文件目录，主要用于下载文件
     */
    fun getDownloadFileDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.FILES
    }

    /**
     * 获取文件目录，主要用于上传文件
     */
    fun getUploadFileDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.UPLOAD
    }

    /**
     * 获取文件目录，主要用于录制的文件
     */
    fun getRecorderFileDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.RECORDER
    }

    /**
     * 获取临时文件目录
     */
    fun getTempFileDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.TEMP
    }

    /**
     * 获取聊天的表情包
     */
    fun getImStickerDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.IM_STICKER
    }

    /**
     * 聊天图片的缩略图缓存目录
     */
    fun getImImageThumbCacheDir(context: Context): String {
      return context.cacheDir.path + File.separator + FilePathConstants.IM_THUM
    }

    /**
     * 聊天音频相关的缓存目录
     */
    fun getImVoiceDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.IM_VOICE
    }

    /**
     * 聊天视频相关的缓存目录
     */
    fun getImVideoDir(context: Context): String {
      return getAppFileDir(context) + FilePathConstants.IM_VIDEO
    }

    /**
     * 图片下载的目录
     */
    fun getImageSaveDir(activity: Activity): String {
      val permission =
        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      if (permission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
          activity,
          arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
          ),
          1
        )
      } else {
        val path =
          Environment.getExternalStorageDirectory().toString() + File.separator + FilePathConstants.COMPANY_FOLDER + File.separator + FilePathConstants.IMAGE_SAVE
        FileUtils.createOrExistsDir(path)
        return path
      }
      return ""
    }
  }
}