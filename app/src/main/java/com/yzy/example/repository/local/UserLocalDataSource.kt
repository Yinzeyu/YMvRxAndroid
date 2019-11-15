package com.yzy.example.repository.local

import com.yzy.example.repository.db.UserBean

class UserLocalDataSource : LocalDataSource() {
    //获取个人信息
    fun getUserBean(): UserBean? {
//        try {
//            val all = box(UserBean::class.java).all
//            return if (all.isEmpty()) {
//                null
//            } else {
//                all[0]
//            }
//        } catch (e: Exception) {
//            LogUtils.e("读取个人信息出错:${e.message}")
//            return null
//        }
        return null
    }

    //保存个人信息
    fun saveUserBean() {
//        clearUserBean()
//        box(UserBean::class.java).put(userBean)
    }

    //清除个人信息
    fun clearUserBean() {
//        box(UserBean::class.java).removeAll()
    }
}
