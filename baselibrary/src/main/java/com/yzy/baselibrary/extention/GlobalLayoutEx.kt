package com.yzy.baselibrary.extention

import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * description : 产生随机颜色,使用时需要创建Color对象再用，如Color().random()
 *
 *@date 2019/7/15
 *@author: yzy.
 */
private val TAG = "GlobalLayoutEx"
//需要保存键盘高度
private var saveKeyboardHeight = 0
//方便判断改变的临时变量
private var tempKeyboardHeight = 0
//方便判断改变的临时变量
private var tempNavigationBarHeight = 0
//为了解决一直重复设置高度的问题
private var lastSetSetKeyboardHeight = 0
//防止键盘高度改变太快
private var disposable: Disposable? = null

/**获取当前键盘高度*/
fun Activity.getHeightKeyboard(): Int {
    val rect = Rect()
    //使用最外层布局填充，进行测算计算
    window.decorView.getWindowVisibleDisplayFrame(rect)
    val heightDiff = window.decorView.height - (rect.bottom - rect.top)
    return max(0, heightDiff - mStatusBarHeight - getHeightNavigationBar())
}

/**添加键盘监听*/
fun Activity.addListerKeyboard(
    naHeight: ((naHeight: Int) -> Unit)? = null,
    keyboardHeight: ((keyboardHeight: Int) -> Unit)? = null
) {
    val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val realKeyboardHeight = getHeightKeyboard()
        if (realKeyboardHeight != lastSetSetKeyboardHeight && window.decorView.height > 0
            && (realKeyboardHeight > 300 || realKeyboardHeight == 0)
        ) {
            lastSetSetKeyboardHeight = realKeyboardHeight
            disposable?.dispose()
            disposable = Flowable.timer(100, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .compose(applyFlowableSchedulers())
                .subscribe {
                    val navigationHeight = getNavigationBarHeight(this)
                    if (navigationHeight != tempNavigationBarHeight) {
                        naHeight?.invoke(navigationHeight)
                        Log.i(TAG, "虚拟导航键高度=$naHeight")
                        tempNavigationBarHeight = navigationHeight
                    }
                    if (realKeyboardHeight != tempKeyboardHeight) {
                        keyboardHeight?.invoke(realKeyboardHeight)
                        Log.i(TAG, "键盘高度=$realKeyboardHeight")
                        tempKeyboardHeight = realKeyboardHeight
                        if (realKeyboardHeight > 100) {
                            saveKeyboardHeight = realKeyboardHeight
                        }
                    }
                }
        }
    }
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
}

/**获取虚拟导航高度(部分手机可能不准)*/
fun Activity.getHeightNavigationBar(): Int {
    return getNavigationBarHeight(this)
}

/**获取虚拟导航键高度*/
fun getNavigationBarHeight(activity: Activity): Int {
    return if (navigationBarIsShow(activity)) {
        activity.resources.getDimensionPixelSize(
            activity.resources.getIdentifier(
                "navigation_bar_height",
                "dimen",
                "android"
            )
        )
    } else {
        0
    }
}

/**状态栏高度*/
val Activity.mStatusBarHeight: Int
    get() {
        return Resources.getSystem()
            .getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
    }

/**判断虚拟导航键是否显示*/
fun navigationBarIsShow(activity: Activity): Boolean {
    //正常判断
    val metrics = DisplayMetrics()
    val realMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(metrics)
    activity.windowManager.defaultDisplay.getRealMetrics(realMetrics)
    return realMetrics.widthPixels - metrics.widthPixels > 0 || realMetrics.heightPixels - metrics.heightPixels > 0
}

fun <T> applyFlowableSchedulers(): FlowableTransformer<T, T> {
    return FlowableTransformer { followable ->
        followable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}