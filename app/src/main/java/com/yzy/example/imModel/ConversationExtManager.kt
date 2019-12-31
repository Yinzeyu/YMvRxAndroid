package com.yzy.example.imModel

class ConversationExtManager {

    /**
     * 扩展的列表
     */
    private val conversationExts = mutableListOf<ConversationExt>()

    /**
     * 添加扩展
     */
    fun addConversationExt(conversationExt: ConversationExt) {
        conversationExts.add(conversationExt)
    }

    /**
     * 清除扩展
     */
    fun clearConversationExt() {
        conversationExts.clear()
    }

    /**
     * 获取扩展列表
     */
    fun getConversationExtList(): List<ConversationExt> {
        return conversationExts
    }

    /**
     * 获取扩展的数量
     */
    fun getExtensionSize(): Int = conversationExts.size

    /**
     * 获取Page
     */
    fun getExtensionPage(): Int = conversationExts.size / EXTENSION_PER_PAGE + 1

    companion object {

        /**
         * 每一页显示的扩展数量
         */
        const val EXTENSION_PER_PAGE = 8

        fun getInstance(): ConversationExtManager {
            return SingleHolder.instance
        }
    }

    private class SingleHolder {
        companion object {
            val instance = ConversationExtManager()
        }
    }
}