package com.yzy.baselibrary.toast

import androidx.annotation.IntDef

/**
 *description: 自定义Toast的显示时长.
 *@date 2019/7/15
 *@author: yzy.
 */
//自定义toast显示时长：短
const val DURATION_SHORT = 2000
//自定义toast显示时长：长
const val DURATION_LONG = 3500

@Retention(AnnotationRetention.SOURCE)
@IntDef(DURATION_SHORT, DURATION_LONG)
annotation class ToastDuration