package com.yzy.baselibrary.extention

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator

/**
 *description: 动画的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */
inline fun ValueAnimator.onEnd(crossinline func: () -> Unit): ValueAnimator {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            func()
        }
    })
    return this
}

inline fun ValueAnimator.onStart(crossinline func: () -> Unit): ValueAnimator {
    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)
            func()
        }
    })
    return this
}

inline fun ValueAnimator.onUpdate(crossinline func: (value: Any) -> Unit): ValueAnimator {
    addUpdateListener { animation ->
        func(animation.animatedValue)
    }
    return this
}

fun ValueAnimator.delay(delay: Long): ValueAnimator {
    startDelay = delay
    return this
}

fun ValueAnimator.start(runningAnims: ArrayList<ValueAnimator>) {
    runningAnims.add(this)
    start()
}

fun ValueAnimator.interpolate(interp: TimeInterpolator): ValueAnimator {
    interpolator = interp
    return this
}