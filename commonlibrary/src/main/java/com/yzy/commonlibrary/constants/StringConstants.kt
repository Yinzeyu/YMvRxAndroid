package com.yzy.commonlibrary.constants

interface StringConstants {
    //MMKV 保存的key
    interface Mmkv {
        companion object {
            const val KEY_TOKEN = "AIMYFUN_TOKEN"
            const val KEY_NEED_GUIDE = "KEY_NEED_GUIDE"
            //键盘高度缓存的key
            const val KEY_HEIGHT_KEYBOARD = "KEY_HEIGHT_KEYBOARD"
        }
    }

    //push key and secret
    interface Push {
        companion object {
            const val UM_DEBUG_KEY = ""//友盟 测试key
            const val UM_DEBUG_MESSAGE_SECRET =
                ""//友盟 测试MessageSecret

            const val UM_RELEASE_KEY = ""//友盟 正式 key
            const val UM_RELEASE_MESSAGE_SECRET =
                ""//友盟 正式 MessageSecret


            const val XM_RELEASE_KEY = ""//小米 正式 key
            const val XM_RELEASE_SECRET = ""//小米 正式MessageSecret


            const val HW_RELEASE_KEY = ""//华为 正式 key
            const val HW_RELEASE_SECRET = ""//华为 正式 Secret


            const val MZ_RELEASE_KEY = ""//魅族 正式 key 1002572
            const val MZ_RELEASE_SECRET = ""//魅族 正式 Secret

            const val OPPO_RELEASE_KEY = ""//OPPO正式 key 1002572
            const val OPPO_RELEASE_SECRET = ""//OPPO 正式 Secret

            const val VIVO_RELEASE_KEY = ""//VIVO正式 key 1002572
            const val VIVO_RELEASE_SECRET = ""//VIVO 正式 Secret
        }
    }
}
