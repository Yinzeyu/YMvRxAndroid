package com.yzy.example.imModel

interface IEmotionSelectedListener {
    /**
     * 选中了Emoji表情
     */
    fun onEmojiSelected(key: String)

    /**
     * 选中了自定义表情
     */
    fun onStickerSelected(
        categoryName: String,
        stickerName: String,
        stickerBitmapPath: String?
    )
}