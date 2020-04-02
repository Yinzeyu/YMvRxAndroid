package com.yzy.baselibrary.extention

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yzy.baselibrary.toast.YToast

/**
 *description: Context相关的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */


fun Context.getResColor(resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.getResDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) = YToast.showCenterToast(this, text, duration)

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) = YToast.showCenterToast(this, getString(resId), duration)

fun Context.inflate(layoutResource: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this).inflate(layoutResource, parent, attachToRoot)
}

fun Context.dp2px(dip: Int): Int {
    val scale = resources.displayMetrics.density
    return (dip * scale + 0.5f).toInt()
}

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
//常亮
fun Activity.extKeepScreenOn() {
    window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}
