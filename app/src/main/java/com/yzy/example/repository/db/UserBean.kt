package com.yzy.example.repository.db

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * description :
 *
 *@date 2019/7/15
 *@author: yzy.
 */
@SuppressLint("ParcelCreator")
@Parcelize
//@Entity
data class UserBean(
//        @Id(assignable = true)
        var userId: Long, //用户id
        var avatarUrl: String?, //用户头像
        var userName: String?, //用户名
        var birthday: String?, //生日20181031
        var gender: Int = 0, //性别 1男、2女
        var bio: String?, //签名
        var zodiacName: String?, //星座
        var perfectInfo: Int = 0, //信息是否完善  0-否 、1-是
        var imToken: String?,
        var phoneNumber: String?,
        var genderUpdateNum: Int = 0, //性别修改次数
        var cityCode: String?, //城市码
        var userLat: Double?,
        var userLong: Double?,
        var userLevel: Int = 0, //用户等级
        var relationship: Int, //关注关系   0-关注  1-已关注 2-关注我 3-互相关注
        var feedNumber: Int = 0, //动态数量
        var popularity: Int = 0, //人气值
        var wealth: Int = 0,//豪气值
        var online: Int = 0,  // 是否在线 否-0 是-1
        var loginTime: Long?, // 当前登录时间
        var logoutTime: Long?,// 退出登录时间
        var popularityLabel: String?,//人气值标签
        var wealthLabel: String?,//豪气值标签
        var bigdataUserType: Int = 0  // 用户类型 0-普通用户，1-运营用户，3-机器人
) : Parcelable