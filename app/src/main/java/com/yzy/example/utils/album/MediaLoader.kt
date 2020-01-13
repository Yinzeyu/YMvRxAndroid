package com.yzy.example.utils.album

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.yzy.example.utils.album.entity.LocalMedia
import com.yzy.example.utils.album.entity.LocalMediaFolder
import java.io.File
import java.util.*

class MediaLoader private constructor() {
    private val QUERY_URI = MediaStore.Files.getContentUri("external")
    private val ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC"
    private val DURATION = "duration"
    private val NOT_GIF = "!='image/gif'"
    //媒体文件数据库字段
    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.WIDTH,
        MediaStore.MediaColumns.HEIGHT,
        DURATION
    )
    //只读取图片
    private val SELECTION_IMG = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0")
    //只读取非GIF图片
    private val SELECTION_IMG_NOT_GIF = (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF);

    private object SingletonHolder {
        val holder = MediaLoader()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    //加载图片
    fun loadImage(
        config: LoadConfig = LoadConfig.instance.reset(),
        activity: FragmentActivity,
        listener: LocalMediaLoadListener? = null
    ) {
        LoaderManager.getInstance(activity)
            .initLoader(
                config.resourceType.ordinal,
                null,
                object : LoaderManager.LoaderCallbacks<Cursor> {
                    override fun onCreateLoader(
                        type: Int,
                        bundle: Bundle?
                    ): Loader<Cursor> {
                        val args = arrayListOf<String>()
                        val selection: String
                        when (type) {
                            MediaType.ALL.ordinal -> {
                                selection = getSelectionArgsForAll(
                                    getDurationArgs(config.durationMin, config.durationMax),
                                    config.loadGif
                                )
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
                            }
                            MediaType.IMAGE_VIDEO.ordinal -> {
                                selection = getSelectionArgsForAll(
                                    getDurationArgs(config.durationMin, config.durationMax),
                                    config.loadGif
                                )
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                            }
                            MediaType.IMAGE.ordinal -> {
                                selection = if (config.loadGif) SELECTION_IMG else SELECTION_IMG_NOT_GIF
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                            }
                            MediaType.VIDEO.ordinal -> {
                                selection = getSelectionArgsForAudioAndVideo(
                                    getDurationArgs(
                                        config.durationMin,
                                        config.durationMax
                                    )
                                )
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                            }
                            MediaType.AUDIO.ordinal -> {
                                selection = getSelectionArgsForAudioAndVideo(
                                    getDurationArgs(
                                        config.durationMin,
                                        config.durationMax
                                    )
                                )
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
                            }
                            else -> {
                                selection = if (config.loadGif) SELECTION_IMG else SELECTION_IMG_NOT_GIF
                                args.add(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                            }
                        }
                        return CursorLoader(
                            activity,
                            QUERY_URI,
                            PROJECTION,
                            selection,
                            args.toTypedArray(),
                            ORDER_BY
                        )
                    }

                    override fun onLoadFinished(
                        loader: Loader<Cursor>,
                        cursor: Cursor?
                    ) {
                        listener?.let { listener ->
                            //所有media文件夹列表
                            val imageFolders = ArrayList<LocalMediaFolder>()

                            //所有图片文件夹(第一个文件夹)
                            val allImageFolder = LocalMediaFolder()
                            allImageFolder.path="com/aimy/media/loader/first"
                            //所有图片文件((第一个文件夹里面的文件内容)
                            val latelyImages = ArrayList<LocalMedia>()

                            //所有视频文件夹(第二个文件夹)
                            val allVideoFolder = LocalMediaFolder()
                            allVideoFolder.path="com/aimy/media/loader/second"
                            //所有视频文件(第二个文件夹里面的文件内容)
                            val latelyVideos = ArrayList<LocalMedia>()
                            cursor?.let {
                                try {
                                    if (it.count > 0) {
                                        it.moveToFirst()
                                        do {
                                            val media = LocalMedia()
                                            media.mimeType = config.resourceType.ordinal
                                            media.path = it.getString(it.getColumnIndexOrThrow(PROJECTION[1]))
                                            media.pictureType = it.getString(it.getColumnIndexOrThrow(PROJECTION[2]))
                                            media.width = it.getInt(it.getColumnIndexOrThrow(PROJECTION[3]))
                                            media.height = it.getInt(it.getColumnIndexOrThrow(PROJECTION[4]))
                                            media.duration = it.getInt(it.getColumnIndexOrThrow(PROJECTION[5]))
                                                .toLong()
                                            //过滤长图
                                            if (!config.loadBigImg && MediaUtils.instance.isLongImg(media)) {
                                                continue
                                            }
                                            val isVideo = MediaUtils.instance.isVideo(media.pictureType)
                                            if (isVideo) {
                                                media.isVideo = true
                                                media.durationTime = videoTime(media.duration)
                                            } else {
                                                media.isLongPic = media.height > media.width * 5
                                                media.isWidthPic = media.width > media.height * 3
                                            }
                                            media.path?.let { path ->
                                                //视频加到第二个文件夹
                                                if (config.videoInSecondDir && media.isVideo) {
                                                    latelyVideos.add(media)
                                                    allVideoFolder.imageNum = allVideoFolder.imageNum + 1
                                                } else {
                                                    //正常添加
                                                    val folder = getImageFolder(path, imageFolders)
                                                    folder.images?.add(media)
                                                    folder.imageNum = folder.imageNum + 1
                                                }
                                                //添加到所有文件夹中
                                                if (!media.isVideo || config.videoInFirstDir) {
                                                    latelyImages.add(media)
                                                    allImageFolder.imageNum = allImageFolder.imageNum + 1
                                                }
                                            }
                                        } while (it.moveToNext())
                                        //添加全部
                                        if (latelyImages.size > 0) {
                                            imageFolders.add(0, allImageFolder)
                                            allImageFolder.firstImagePath = latelyImages[0].path
                                            allImageFolder.name = config.allName
                                            allImageFolder.images = latelyImages
                                        }
                                        //添加视频
                                        if (config.videoInSecondDir && latelyVideos.size > 0) {
                                            imageFolders.add(if (imageFolders.size > 0) 1 else 0, allVideoFolder)
                                            allVideoFolder.firstImagePath = latelyVideos[0].path
                                            allVideoFolder.name = config.secondDirName
                                            allVideoFolder.images = latelyVideos
                                        }
                                    }
                                    listener.loadComplete(imageFolders)
                                    return
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            listener.loadComplete(null)
                        }
                    }

                    override fun onLoaderReset(loader: Loader<Cursor>) {
                    }

                })
    }

    private var formatterBuilder: StringBuilder = StringBuilder()
    private var formatterHHMM: Formatter = Formatter(formatterBuilder, Locale.getDefault())
    /**时长格式化为HH:MM*/
    private fun videoTime(time: Long): String {
        val totalSeconds: Int = (time / 1000).toInt()
        val seconds: Int = totalSeconds % 60
        val minutes: Int = totalSeconds / 60 % 60
        val hours: Int = totalSeconds / 3600
        formatterBuilder.setLength(0)
        return if (hours > 0) {
            formatterHHMM.format("%d:%02d:%02d", hours, minutes, seconds)
                .toString()
        } else {
            formatterHHMM.format("%02d:%02d", minutes, seconds)
                .toString()
        }
    }

    //全部筛选
    private fun getSelectionArgsForAll(
        durationArgs: String,
        loadGif: Boolean
    ): String {
        return ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + (if (loadGif) "" else " AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF)
                + " OR " + (MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + durationArgs) + ")"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0")
    }

    //筛选音视频
    private fun getSelectionArgsForAudioAndVideo(durationArgs: String): String {
        return (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + durationArgs)
    }

    //筛选时长
    private fun getDurationArgs(
        min: Long,
        max: Long
    ): String {
        return String.format(Locale.CHINA, "%d <= duration and duration <= %d", min, max)
    }

    //获取图片所在文件夹信息
    private fun getImageFolder(
        path: String,
        imageFolders: MutableList<LocalMediaFolder>
    ): LocalMediaFolder {
        val imageFile = File(path)
        val folderFile = imageFile.parentFile
        for (folder in imageFolders) {
            // 同一个文件夹下，返回自己，否则创建新文件夹
            if (folder.name.equals(folderFile?.name)) {
                return folder
            }
        }
        val newFolder = LocalMediaFolder()
        newFolder.name = folderFile?.name
        newFolder.path = folderFile?.absolutePath
        newFolder.firstImagePath = path
        imageFolders.add(newFolder)
        return newFolder
    }
}