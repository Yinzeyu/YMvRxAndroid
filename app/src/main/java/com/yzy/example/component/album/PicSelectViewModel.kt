package com.yzy.example.component.album

import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.example.repository.bean.AlbumBean
import com.yzy.example.utils.album.LoadConfig
import com.yzy.example.utils.album.LocalMediaLoadListener
import com.yzy.example.utils.album.MediaLoader
import com.yzy.example.utils.album.MediaType
import com.yzy.example.utils.album.entity.LocalMedia
import com.yzy.example.utils.album.entity.LocalMediaFolder
import java.io.File
import kotlin.math.max

/**
 * Description:
 * @author: yzy
 * @date: 19-5-14 下午7:55
 */
class PicSelectViewModel : ViewModel() {
    private val _bannerAndArticleResult: BaseLiveData<AlbumBean> = BaseLiveData()
    val uiState: BaseLiveData<AlbumBean> get() = _bannerAndArticleResult
    //用于确定选择后的数据同步
    private var selMediaList: MutableList<LocalMedia> = mutableListOf()
    //当前选中的文件夹位置
    private var currentCheckDirIndex = 0

    /**判断是否是第一次load*/
    private var isFirstLoad = true
    private var isMixing = false

    /**开始加载图片★★★注意onResume会自动回调★★★*/
    fun loadAllDirs(
        activity: FragmentActivity,
        onlyImg: Boolean = true,
        mindur: Long = 3 * 1000,
        maxdur: Long = 100 * 1000,
        mixing: Boolean = false
    ) {
        isMixing = mixing
        MediaLoader.instance.loadImage(
            config = LoadConfig.instance.reset()
                .buildDuration(mindur, maxdur)
                .buildAllName("所有照片")
                .buildGif(false)
                .buildVideoInSecondDir(!onlyImg)
                .buildSecondName("videos")
                .buildType(if (onlyImg) MediaType.IMAGE else MediaType.IMAGE_VIDEO),
            activity = activity,
            listener = loadListener
        )
    }

    private var loadListener = object : LocalMediaLoadListener {
        override fun loadComplete(folders: List<LocalMediaFolder>?) {
            val tempDir = folders?.toMutableList() ?: mutableListOf()
            if (tempDir.isNullOrEmpty()) {
                return
            }
            if (tempDir[0].images == null) {
                tempDir[0].images = mutableListOf()
            }
            cameraMedia?.let { media ->
                if (tempDir[0].images?.count {
                        TextUtils.equals(it.path, media.path)
                    } == 0) {
                    checkAndAddCameraMedia(tempDir, media)
                }
            }
            val result: MutableList<LocalMediaFolder> = mutableListOf()
            setSelectDefault(isMixing)
            result.addAll(tempDir)
            //打开相册时默认勾选已经选择了的照片
            if (result.size > 0 && selMediaList.size > 0) {
                setAllDirCheck(result)
            }
            loadPicsInDir(
                dirPath = when {
                    folders.isNullOrEmpty() -> ""
                    //防止文件夹被删除后重新回来
                    folders.size > currentCheckDirIndex -> folders[currentCheckDirIndex].path ?: ""
                    else -> {
                        currentCheckDirIndex = 0
                        folders[0].path ?: ""
                    }
                },
                result = result,
                scrollTop = isFirstLoad
            )
            isFirstLoad = false
        }
    }

    /**设置新的选中文件夹*/
    fun loadPicsInDir(
        dirPath: String,
        result: MutableList<LocalMediaFolder>? = null,
        scrollTop: Boolean = true
    ) {
        var index = 0
        if (!TextUtils.isEmpty(dirPath)) {
            _bannerAndArticleResult.value?.let {
                for (i in 0 until it.data.size) {
                    if (TextUtils.equals(it.data[i].path, dirPath)) {
                        index = i
                        break
                    }
                }
            }
        }
        currentCheckDirIndex = index
        val value:MutableList<LocalMediaFolder> =   result?:  _bannerAndArticleResult.value?.data?: mutableListOf()
        value.let {
            _bannerAndArticleResult.update(AlbumBean(it , currentCheckDirIndex, scrollTop))
        }
    }

    /**设置图文选中状态的改变*/
    fun changeMediaChecked(
        index: Int,
        checkAfterMedia: LocalMedia
    ) {
        if (checkAfterMedia.isChecked) {
            if (selMediaList.count { TextUtils.equals(it.path, checkAfterMedia.path) } == 0) {
                selMediaList.add(checkAfterMedia)
                addSelMedia(checkAfterMedia)
            }
        } else {
            selMediaList =
                selMediaList.filterNot { TextUtils.equals(it.path, checkAfterMedia.path) }
                    .toMutableList()
            removeSelMedia(checkAfterMedia)
        }
        syncOtherDir(index, checkAfterMedia)
        changeCheckNum(checkAfterMedia)
    }

    //解决刚拍照的图片没有的问题
    private var cameraMedia: LocalMedia? = null

    fun andAddCameraMedia(media: LocalMedia) {
        cameraMedia = media
        if (selMediaList.count { TextUtils.equals(it.path, media.path) } == 0) {
            selMediaList.add(media)
        }
        Log.i("picSel", "拍摄的图片地址：" + media.path)
    }

    /**设置全部文件夹的文件选中的情况*/
    fun setAllDirCheck(
        allFileList: MutableList<LocalMediaFolder>
    ) {
        //每个media分别在2个文件夹中,所以需要判断2次
        var tempList: MutableList<LocalMedia> = mutableListOf()
        tempList.addAll(selMediaList)
        //用于判断是否已经查找过了
        val fistAddList: MutableList<LocalMedia> = mutableListOf()
        //遍历文件夹
        for (folder in allFileList) {
            folder.images?.let { images ->
                //遍历文件
                for (media in images) {
                    val path = media.path
                    //找到文件
                    if (path != null && tempList.count { TextUtils.equals(it.path, path) } > 0) {
                        media.isChecked = true
                        folder.checkedNum = folder.checkedNum + 1
                        if (fistAddList.count { TextUtils.equals(it.path, path) } > 0) {
                            tempList = tempList.filterNot { TextUtils.equals(it.path, path) }
                                .toMutableList()
                        } else {
                            fistAddList.add(media)
                        }
                        //全部找到，退出循环1
                        if (tempList.isEmpty()) {
                            break
                        }
                    }
                }
            }
            //全部找到，退出循环2
            if (tempList.isEmpty()) {
                break
            }
        }
    }

    /**同步选中的数据到liveData*/
    fun sync2PicSelLiveData() {
        PicSelLiveData.setSelectPicList(selMediaList)
    }

    /**修改选中的数量*/
    private fun changeCheckNum(checkAfterMedia: LocalMedia) {
        _bannerAndArticleResult.value?.let {
            var count = 0
            for (folder in it.data) {
                val images = folder.images
                if (images != null) {
                    for (media in images) {
                        if (TextUtils.equals(checkAfterMedia.path, media.path)) {
                            folder.checkedNum = max(0, folder.checkedNum + if (checkAfterMedia.isChecked) 1 else -1)
                            count++
                            break
                        }
                    }
                }
                if (count == 2) {
                    break
                }
            }
        }
    }

    /**图片选中状态同步到其他文件夹*/
    private fun syncOtherDir(index: Int, media: LocalMedia) {
        //当前从全部操作的则遍历后面的文件夹，如果是后面的文件夹，则从开始遍历即可
        val start = if (index == 0) 1 else 0
        _bannerAndArticleResult.value?.let {image->
            //遍历文件夹
            for (i in start until image.data.size) {
                val images = image.data[i].images
                images?.let { list ->
                    //遍历文件
                    for (it in list) {
                        //找到文件(只找一次)
                        if (TextUtils.equals(it.path, media.path)) {
                            it.isChecked = media.isChecked
                            break
                        }
                    }
                    val tempList: MutableList<LocalMediaFolder> = mutableListOf()
                    tempList.addAll(image.data)
                    _bannerAndArticleResult.update(AlbumBean(tempList, currentCheckDirIndex, false))
                }
            }
        }
    }

    /**检查并添加*/
    private fun checkAndAddCameraMedia(
        dirs: MutableList<LocalMediaFolder>,
        media: LocalMedia
    ) {
        //全部相册中没有，则添加
        if (dirs[0].images?.count { TextUtils.equals(it.path, media.path) } == 0) {
            media.path?.let { path ->
                dirs[0].images?.add(0, media.copy())
                dirs[0].checkedNum += 1
                val fileParent = File(path).parentFile
                //获取父文件夹
                val parentMediaDir = dirs.filter { TextUtils.equals(it.path, fileParent?.path) }
                if (parentMediaDir.isNullOrEmpty()) {//如果不存在，则添加
                    val newFolder = LocalMediaFolder()
                    newFolder.name = fileParent?.name
                    newFolder.path = fileParent?.absolutePath
                    newFolder.firstImagePath = path
                    newFolder.images?.add(media)
                    newFolder.checkedNum = 1
                    dirs.add(newFolder)
                } else {//如果已经存在，则添加文件夹
                    if (parentMediaDir[0].images == null) {
                        parentMediaDir[0].images = mutableListOf()
                    }
                    parentMediaDir[0].images?.add(0, media)
                    parentMediaDir[0].checkedNum += 1
                }
            }
        } else {//如果有,则直接更新
            changeMediaChecked(0, media)
            cameraMedia = null
        }
    }

    /**添加选中的图片*/
    private fun addSelMedia(media: LocalMedia) {
        for (i in 0 until selMediaList.size) {
            if (TextUtils.equals(media.path, selMediaList[i].path)) {
                return
            }
        }
        selMediaList.add(media)
    }

    /**移出选中的图片*/
    private fun removeSelMedia(media: LocalMedia) {
        for (i in 0 until selMediaList.size) {
            if (TextUtils.equals(media.path, selMediaList[i].path)) {
                selMediaList.removeAt(i)
                return
            }
        }
    }

    /**设置默认选中的数据*/
    private fun setSelectDefault(mixing: Boolean) {
        val list: MutableList<LocalMedia> = mutableListOf()
        val tempList = PicTempLiveData.getSelectPicList()
        if (tempList.size > 0) {//当打开过pager列表后，临时选中的图片就已经包含了发布页的图片，所以直接判断临时选中的图片(这样才能取消掉发布页选中的图片状态)
            list.addAll(tempList)
        } else {//没有跳转pager列表页时大小为0，所以重新load的时候只需要判断发布页的选中状态+本页面临时选中的即可
            list.addAll(PicSelLiveData.getSelectPicList())
            list.addAll(selMediaList)
        }
        val listTempMedia: MutableList<LocalMedia> = mutableListOf()
        for (media in list) {
            if (((!media.isVideo && !mixing) || (mixing)) &&
                listTempMedia.count { TextUtils.equals(it.path, media.path) } == 0
            ) {
                listTempMedia.add(media)
            }
        }
        selMediaList = mutableListOf()
        selMediaList.addAll(listTempMedia)
    }

    fun getSelectMedias(): MutableList<LocalMedia> {
        return selMediaList
    }
}