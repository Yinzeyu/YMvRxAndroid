package com.yzy.baselibrary.extention

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.view.ViewPropertyAnimator

/**
 *description: 动画的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

inline fun ViewPropertyAnimator.onEnd(crossinline func: () -> Unit): ViewPropertyAnimator {
  setListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      super.onAnimationEnd(animation)
      func()
    }
  })
  return this
}

inline fun ViewPropertyAnimator.onStart(crossinline func: () -> Unit): ViewPropertyAnimator {
  setListener(object : AnimatorListenerAdapter() {
    override fun onAnimationStart(animation: Animator?) {
      super.onAnimationStart(animation)
      func()
    }
  })
  return this
}

fun ViewPropertyAnimator.delay(delay: Long): ViewPropertyAnimator {
  startDelay = delay
  return this
}


fun ViewPropertyAnimator.interpolate(interp: TimeInterpolator): ViewPropertyAnimator {
  interpolator = interp
  return this
}