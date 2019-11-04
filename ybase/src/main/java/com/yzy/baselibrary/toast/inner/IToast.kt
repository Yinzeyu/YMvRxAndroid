package com.yzy.baselibrary.toast.inner

import android.view.View
import com.yzy.baselibrary.toast.ToastDuration


/**
 *description: 自定义toast的接口.
 *@date 2019/7/15
 *@author: yzy.
 */
interface IToast {

    fun show()

    fun cancel()

    fun setView(mView: View): IToast

    fun getView(): View

    fun setDuration(@ToastDuration duration: Int): IToast

    fun setGravity(gravity: Int): IToast

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int): IToast

    fun setAnimation(animation: Int): IToast

    fun setPriority(mPriority: Int): IToast
}