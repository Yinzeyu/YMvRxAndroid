package com.yzy.baselibrary.extention

import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yzy.baselibrary.BuildConfig
import com.yzy.baselibrary.toast.YToast

/**
 *description: Context相关的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

fun Context.getClipboardManager() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.getInputMethodManager() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.getVersionCode(): Int = com.yzy.baselibrary.BuildConfig.VERSION_CODE

fun Context.getVersionName(): String = com.yzy.baselibrary.BuildConfig.VERSION_NAME

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

fun Context.screenWidth(): Int {
  val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val dm = DisplayMetrics()
  val display = windowManager.defaultDisplay
  display.getMetrics(dm)
  return dm.widthPixels
}

fun Context.screenHeight(): Int {
  val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val dm = DisplayMetrics()
  val display = windowManager.defaultDisplay
  display.getMetrics(dm)
  return dm.heightPixels
}

fun Context.getTextFromClipboard(): CharSequence {
  val clipData = getClipboardManager().primaryClip
  if (clipData != null && clipData.itemCount > 0) {
    return clipData.getItemAt(0).coerceToText(this)
  }
  return ""
}

fun Context.getUriFromClipboard(): Uri? {
  val clipData = getClipboardManager().primaryClip
  if (clipData != null && clipData.itemCount > 0) {
    return clipData.getItemAt(0).uri
  }

  return null
}

