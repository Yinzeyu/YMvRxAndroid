package com.yzy.baselibrary.extention

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.*
import com.yzy.baselibrary.toast.YToast

/**
 *description: Context相关的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */
//屏幕宽度
val Context.mScreenWidth: Int
  get() {
    return ScreenUtils.getScreenWidth()
  }

//屏幕高度
val Context.mScreenHeight: Int
  get() {
    return ScreenUtils.getScreenHeight()
  }

//状态栏高度
val Context.mStatusBarHeight: Int
  get() {
    return BarUtils.getStatusBarHeight()
  }

//dp转px
fun Context.dp2px(dp: Float): Int {
  return SizeUtils.dp2px(dp)
}

//dp转px
fun Context.dp2px(dp: Int): Int {
  return SizeUtils.dp2px(dp.toFloat())
}

//Toast 文字
fun Context.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) =
  if (!msg.isNullOrBlank()) {
    YToast.showCenterToast(this, msg, duration)
  } else {
    LogUtils.e("toast内容为空")
  }


fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) = YToast.showCenterToast(this, getString(resId), duration)

//获取颜色
fun Context.getColorRes(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

//获取Drawable
fun Context.getDrawableRes(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

//XML的layout转换为View
fun Context.inflate(
  @LayoutRes layoutResource: Int,
  parent: ViewGroup? = null,
  attachToRoot: Boolean = false
): View {
  return LayoutInflater.from(this).inflate(layoutResource, parent, attachToRoot)
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
