package com.yzy.example.imModel.emoji

interface EmotionConstants {
    companion object {

        /**
         * Emoji表情图片的最大size，单位dp
         */
        const val EMOJI_IMAGE_SIZE_MAX = 28F

        /**
         * emoji表情的列数
         */
        const val EMOJI_COLUMNS = 8
        /**
         * emoji表情的行数
         */
        const val EMOJI_ROWS = 3
        /**
         * 每一页可以显示的emoji数量
         */
        const val EMOJI_PER_PAGE = EMOJI_COLUMNS * EMOJI_ROWS

        /**
         * 表情图的列数
         */
        const val STICKER_COLUMNS = 4
        /**
         * 表情图的行数
         */
        const val STICKER_ROWS = 2
        /**
         * 每一页可以显示的表情图数量
         */
        const val STICKER_PER_PAGE = STICKER_COLUMNS * STICKER_ROWS
    }
}