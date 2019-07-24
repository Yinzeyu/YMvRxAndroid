package com.yzy.baselibrary.extention

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.ToastUtils

/**
 * description :Activity相关扩展：如屏幕宽高、状态栏高度、虚拟导航键高度获取、键盘高度获取、键盘监听
 * 额外增加：设置状态栏颜色、设置是否填充布局到状态栏、设置虚拟导航键是否隐藏、设置是否全屏显示等
 *
 *@date 2019/7/15
 *@author: yzy.
 */
//打印的Tag
private val TAG = "ActivityEx"


/** 上下文(方便使用)*/
val Activity.mContext: Context
    get() {
        return this
    }

/**Activity本身(弹窗,权限请求等需要用到Activity)*/
val Activity.mActivity: Activity
    get() {
        return this
    }

/**屏幕宽度*/
val Activity.mScreenWidth: Int
    get() {
        return resources.displayMetrics.widthPixels
    }

/**屏幕高度(包含状态栏高度但不包含底部虚拟按键高度)*/
val Activity.mScreenHeight: Int
    get() {
        return resources.displayMetrics.heightPixels
    }


/**ContentView*/
val Activity.mContentView: FrameLayout
    get() {
        return this.findViewById(android.R.id.content)
    }
fun Context.toast(text: String) = ToastUtils.showLong(text)

fun Context.toast(resId: Int) = ToastUtils.showLong(getString(resId))
