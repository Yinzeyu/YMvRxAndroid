package com.yzy.example.imModel.emoji.sticker

import com.yzy.example.imModel.emoji.EmotionManager
import java.io.File
import java.util.ArrayList
import java.util.HashMap

class StickerManager {

    //数据源
    private val stickerCategories = ArrayList<StickerCategory>()
    private val stickerCategoryMap = HashMap<String, StickerCategory>()

    init {
        loadStickerCategory()
    }

    private fun loadStickerCategory() {
        val stickerDir = File(EmotionManager.getInstance().getStickerPath())
        if (stickerDir.exists()) {
            val files = stickerDir.listFiles()
            for (i in files.indices) {
                val file = files[i]
                //当前的目录下同名的有文件和文件夹，只需要其中的一个取其名
                if (file.isDirectory) {
                    val name = file.name
                    val category = StickerCategory(name, name, true, i)
                    stickerCategories.add(category)
                    stickerCategoryMap[name] = category
                }
            }
            //排序
            stickerCategories.sortWith(Comparator { o1, o2 -> o1.order - o1.order })
        }
    }

    @Synchronized
    fun getStickerCategories(): List<StickerCategory> {
        return stickerCategories
    }

    @Synchronized
    fun getCategory(name: String): StickerCategory? {
        return stickerCategoryMap[name]
    }

    fun getStickerBitmapUri(categoryName: String, stickerName: String): String {
        val path = getStickerBitmapPath(categoryName, stickerName)
        return "file://$path"
    }

    fun getStickerBitmapPath(categoryName: String, stickerName: String): String? {
        val manager = StickerManager.getInstance()
        val category = manager.getCategory(categoryName) ?: return null
        return EmotionManager.getInstance().getStickerPath() + File.separator + category.name + File.separator + stickerName
    }

    companion object {
        fun getInstance(): StickerManager {
            return SingletonHolder.instance
        }
    }


    private class SingletonHolder {
        companion object {
            val instance = StickerManager()
        }
    }
}