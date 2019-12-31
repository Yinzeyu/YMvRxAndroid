package com.yzy.example.imModel.emoji.sticker

import com.yzy.example.imModel.emoji.EmotionConstants
import com.yzy.example.imModel.emoji.EmotionManager
import java.io.File
import java.util.ArrayList

class StickerCategory(
    val name: String,
    val title: String,
    val system: Boolean,
    val order: Int
) {
    init {
        loadStickerData()
    }

    var stickers: List<StickerItem>? = null

    fun getCount(): Int {
        return stickers?.size ?: 0
    }

    fun getCoverImgPath(): String? {
        for (file in File(EmotionManager.getInstance().getStickerPath()).listFiles()) {
            if (file.isFile && file.name.startsWith(name)) {
                return "file://" + file.absolutePath
            }
        }
        return null
    }

    fun loadStickerData(): List<StickerItem> {
        val stickers = ArrayList<StickerItem>()
        val stickerDir = File(EmotionManager.getInstance().getStickerPath(), name)
        if (stickerDir.exists()) {
            val files = stickerDir.listFiles()
            for (file in files) {
                //比如：tsj ---> tsj/tsj_001.gif
                stickers.add(StickerItem(name, file.name))
            }
        }
        //补充最后一页缺少的贴图
        val tmp = stickers.size % EmotionConstants.STICKER_PER_PAGE
        if (tmp != 0) {
            val tmp2 =
                EmotionConstants.STICKER_PER_PAGE - (stickers.size - stickers.size / EmotionConstants.STICKER_PER_PAGE * EmotionConstants.STICKER_PER_PAGE)
            for (i in 0 until tmp2) {
                stickers.add(StickerItem("", ""))
            }
        }
        this.stickers = stickers
        return stickers
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is StickerCategory) {
            return false
        }
        if (other === this) {
            return true
        }
        val otherS = other as StickerCategory
        return otherS.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}