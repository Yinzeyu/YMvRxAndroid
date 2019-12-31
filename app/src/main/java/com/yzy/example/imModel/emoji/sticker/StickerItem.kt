package com.yzy.example.imModel.emoji.sticker

class StickerItem(val category: String, val name: String) {

    fun getIdentifier(): String {
        return "$category/$name"
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is StickerItem) {
            val item = other as StickerItem
            return item.category == category && item.name == name
        }
        return false
    }

}