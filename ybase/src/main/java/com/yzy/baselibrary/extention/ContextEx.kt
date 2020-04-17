package com.yzy.baselibrary.extention

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yzy.baselibrary.toast.YToast
import java.lang.reflect.Field

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
fun getScreenSize(context: Context): IntArray {
    val size = IntArray(2)
    val w =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val d = w.defaultDisplay
    val metrics = DisplayMetrics()
    d.getMetrics(metrics)
    size[0] = metrics.widthPixels
    size[1] = metrics.heightPixels
    return size
}


//修复输入法导致的内存泄漏
fun fixInputMethodManagerLeak(destContext: Context?) {
    if (destContext == null) return
    val manager =
        destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
    val viewArray = arrayOf("mCurRootView", "mServedView", "mNextServedView")
    var filed: Field
    var filedObject: Any?
    for (view in viewArray) {
        try {
            filed = manager.javaClass.getDeclaredField(view)
            if (!filed.isAccessible) filed.isAccessible = true
            filedObject = filed.get(manager)
            if (filedObject != null && filedObject is View) {
                if (filedObject.context === destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                    filed.set(manager, null) // 置空，破坏掉path to gc节点
                } else {
                    break// 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}