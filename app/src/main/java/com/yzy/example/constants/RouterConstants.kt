package com.yzy.example.constants

/**
 *description: ARouter相关的常量.
 *@date 2019/3/27 15:50.
 *@author: YangYang.
 */
interface RouterConstants {
    companion object {
        /**
         * 全局降级策略的path
         */
        const val PATH_DEGRADE_GLOBAL = "midamusic/degrade/global"

        /**
         * 通过gson实现ARouter的对象传递
         */
        const val PATH_SERIALIZATION_GSON = "midamusic/serialization/gson"
    }

    interface User {
        companion object {
            /**
             * 打开地图页面
             */
            const val PAGE_MAP_ACTIVITY = "/user/home"
            /**
             * 经纬度
             */
            const val KEY_MAP_LAT = "mapLat"
            const val KEY_MAP_LNG = "mapLng"
            /**
             * 地点名称
             */
            const val KEY_MAP_TITLE = "mapTitle"
            /**
             * 详细地址
             */
            const val KEY_MAP_ADDRESS = "mapAddress"
        }
    }
}