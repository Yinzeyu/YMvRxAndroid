package com.yzy.baselibrary.extention

import android.app.Activity
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

/**
 * Description:
 * @author: yzy
 * @date: 2019/9/20 16:36
 */

//全屏
fun Activity.extFullScreen() {
  window?.let { win ->
    win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    win.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
  }
}

//常亮
fun Activity.extKeepScreenOn() {
  window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

//ContentView
val Activity.mContentView: FrameLayout
  get() {
    return this.findViewById(android.R.id.content)
  }

//监听键盘高度
fun Fragment.extKeyBoard(keyCall: (statusHeight: Int, navigationHeight: Int, keyBoardHeight: Int) -> Unit) {
  requireActivity().mContentView.post { requireActivity().mContentView.layoutParams.height = requireActivity().mContentView.height }//防止键盘弹出导致整个布局高度变小
  requireActivity().window.decorView.setOnApplyWindowInsetsListener(object : View.OnApplyWindowInsetsListener {
    var preKeyOffset: Int = 0//键盘高度改变才回调
    override fun onApplyWindowInsets(
      v: View?,
      insets: WindowInsets?
    ): WindowInsets {
      insets?.let { ins ->
        val navHeight = ins.systemWindowInsetBottom//下面弹窗到屏幕底部的高度，比如键盘弹出后的键盘+虚拟导航键高度
        val offset = if (navHeight < ins.stableInsetBottom) navHeight
        else navHeight - ins.stableInsetBottom
        if (offset != preKeyOffset || offset == 0) {//高度变化
          val decorHeight = requireActivity().window.decorView.height//整个布局高度，包含虚拟导航键
          if (decorHeight > 0) {//为了防止手机去设置页修改虚拟导航键高度，导致整个内容显示有问题，所以需要重新设置高度(与上面设置固定高度对应)
            requireActivity().mContentView.layoutParams.height =
              decorHeight - navHeight.coerceAtMost(ins.stableInsetBottom)//取小值
          }
          preKeyOffset = offset
          keyCall.invoke(ins.stableInsetTop, ins.stableInsetBottom, offset)
        }
      }
      return requireActivity().window.decorView.onApplyWindowInsets(insets)
    }
  })
}