package com.yzy.example.imModel.emoji

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

class EmotionManager {

    private var stickerPath: String = ""

    fun init(context: Context, stickerPath: String = "") {
        Utils.init(context)
        this.stickerPath = stickerPath
        copyStickerToStickerPath(STICKER_NAME_IN_ASSETS)
        if (this.stickerPath.isBlank()) {
            this.stickerPath = File(
                context.filesDir,
                STICKER_NAME_IN_ASSETS
            ).absolutePath
        }
    }

    fun getStickerPath(): String {
        return stickerPath
    }

    private fun copyStickerToStickerPath(assetsFolderPath: String) {
        val assetManager = Utils.getApp().resources.assets
        val srcFile = ArrayList<String>()
        try {
            val stickers = assetManager.list(assetsFolderPath)
            for (fileName in stickers!!) {
                if (!File(stickerPath, fileName).exists()) {
                    srcFile.add(fileName)
                }
            }
            if (srcFile.size > 0) {
                copyToStickerPath(assetsFolderPath, srcFile)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyToStickerPath(assetsFolderPath: String, srcFile: List<String>) {
        Thread(Runnable {
            val assetManager = Utils.getApp().resources.assets
            val dir = File(getStickerPath())
            if (!dir.exists()) {
                dir.mkdirs()
            }
            for (fileName in srcFile) {
                if (fileName.contains(".")) {//文件
                    var inputStream: InputStream? = null
                    var fos: FileOutputStream? = null
                    try {
                        inputStream = assetManager.open(assetsFolderPath + File.separator + fileName)
                        val destinationFile: File =
                            if (assetsFolderPath.startsWith(STICKER_NAME_IN_ASSETS + File.separator)) {//递归回来的时候assetsFolderPath可能变为"sticker/tsj"
                                File(
                                    getStickerPath(),
                                    assetsFolderPath.substring(assetsFolderPath.indexOf(File.separator) + 1) + File.separator + fileName
                                )
                            } else {
                                File(getStickerPath(), fileName)
                            }
                        LogUtils.e("copyToStickerPath path = ${destinationFile.absolutePath}")
                        fos = FileOutputStream(destinationFile)
                        var read = -1
                        inputStream?.use { input ->
                            fos?.use { output ->
                                while (input.read().also { read = it } != -1) {
                                    output.write(read)
                                }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close()
                                inputStream = null
                            } catch (e: IOException) {
                                e.printStackTrace()
                                inputStream = null
                            }

                        }
                        if (fos != null) {
                            try {
                                fos.close()
                                fos = null
                            } catch (e: IOException) {
                                e.printStackTrace()
                                fos = null
                            }
                        }
                    }
                } else {//文件夹
                    val dir = File(getStickerPath(), fileName)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    copyStickerToStickerPath(assetsFolderPath + File.separator + fileName)
                }
            }
        }).start()
    }


    companion object {
        /**
         * 默认的自定义表情在assets中的文件夹
         */
        private const val STICKER_NAME_IN_ASSETS = "sticker"

        fun getInstance(): EmotionManager {
            return SingletonHolder.instance
        }
    }

    private class SingletonHolder {
        companion object {
            val instance = EmotionManager()
        }
    }

}