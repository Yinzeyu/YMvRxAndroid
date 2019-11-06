package com.yzy.example.constants

/**
 *description: ARouter相关的常量.
 *@date 2019/3/27 15:50.
 *@author: YangYang.
 */
interface RouterConstants {


    interface User {
      companion object {
        /**
         * 打开地图页面
         */
        const val PAGE_MAP_ACTIVITY = "user/home"
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