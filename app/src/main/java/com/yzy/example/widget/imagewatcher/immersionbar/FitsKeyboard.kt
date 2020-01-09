package com.yzy.example.widget.imagewatcher.immersionbar

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.widget.FrameLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.blankj.utilcode.util.BarUtils
import com.yzy.example.R

/**
 * 适配软键盘弹出问题
 *
 * @author geyifeng
 * @date 2018/11/9 10:24 PM
 */
internal class FitsKeyboard(
    private val mImmersionBar: ImmersionBar,
    var mWindow: Window
) : OnGlobalLayoutListener {
    private var mStatusBarHeight: Int=BarUtils.getStatusBarHeight()
    private var mActionBarHeight: Int=BarUtils.getActionBarHeight()
    private val mDecorView: View = mWindow.decorView
    private val mContentView: View
    private var mChildView: View?
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0
    private var mTempKeyboardHeight = 0
    private var mIsAddListener = false
    fun enable(mode: Int) {
        mWindow.setSoftInputMode(mode)
            if (!mIsAddListener) {
                mDecorView.viewTreeObserver.addOnGlobalLayoutListener(this)
                mIsAddListener = true
            }
    }

    /**
     * 为啥要更新？因为横竖屏切换状态栏高度有可能不一样
     * Update bar config.
     *
     * @param barConfig the bar config
     */
    fun updateBarConfig(barConfig: BarConfig) {
        mStatusBarHeight = barConfig.statusBarHeight
        if (mImmersionBar.isActionBarBelowLOLLIPOP) {
            mActionBarHeight = barConfig.actionBarHeight
        }
    }

    fun disable() {
        if (mIsAddListener) {
            if (mChildView != null) {
                mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            } else {
                mImmersionBar.let {
                    mContentView.setPadding(it.paddingLeft, it.paddingTop, it.paddingRight, it.paddingBottom)
                }
            }
        }
    }

    fun cancel() {
        if (mIsAddListener) {
            mDecorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            mIsAddListener = false
        }
    }

    override fun onGlobalLayout() {
        mImmersionBar.let {
            it.barParams?.let {barParams->
                if (barParams.keyboardEnable ) {
                    var bottom = 0
                    var keyboardHeight: Int
                    val navigationBarHeight: Int = BarUtils.getNavBarHeight()
                    var isPopup = false
                    val rect = Rect()
                    //获取当前窗口可视区域大小
                    mDecorView.getWindowVisibleDisplayFrame(rect)
                    keyboardHeight = mContentView.height - rect.bottom
                    if (keyboardHeight != mTempKeyboardHeight) {
                        mTempKeyboardHeight = keyboardHeight
                        if (!ImmersionBar.checkFitsSystemWindows(
                                mWindow.decorView.findViewById(
                                    R.id.content
                                )
                            )
                        ) {
                            if (mChildView != null) {
                                if (barParams.isSupportActionBar) {
                                    keyboardHeight += mActionBarHeight + mStatusBarHeight
                                }
                                if (barParams.fits) {
                                    keyboardHeight += mStatusBarHeight
                                }
                                if (keyboardHeight > navigationBarHeight) {
                                    bottom = keyboardHeight + mPaddingBottom
                                    isPopup = true
                                }
                                mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, bottom)
                            } else {
                                bottom = it.paddingBottom
                                keyboardHeight -= navigationBarHeight
                                if (keyboardHeight > navigationBarHeight) {
                                    bottom = keyboardHeight + navigationBarHeight
                                    isPopup = true
                                }
                                mContentView.setPadding(
                                    it.paddingLeft,
                                    it.paddingTop,
                                    it.paddingRight,
                                    bottom
                                )
                            }
                        } else {
                            keyboardHeight -= navigationBarHeight
                            if (keyboardHeight > navigationBarHeight) {
                                isPopup = true
                            }
                        }
                        if (keyboardHeight < 0) {
                            keyboardHeight = 0
                        }
                        barParams.onKeyboardListener?.onKeyboardChange(
                            isPopup,
                            keyboardHeight
                        )
                        if (!isPopup && barParams.barHide != BarHide.FLAG_SHOW_BAR) {
                            mImmersionBar.setBar()
                        }
                    }
                }
            }

        }

    }

    init {
        val frameLayout = mDecorView.findViewById<FrameLayout>(R.id.content)
        mChildView = frameLayout.getChildAt(0)
        if (mChildView != null) {
            if (mChildView is DrawerLayout) {
                mChildView = (mChildView as DrawerLayout).getChildAt(0)
            }
            if (mChildView != null) {
                mPaddingLeft = mChildView?.paddingLeft?:0
                mPaddingTop = mChildView?.paddingTop?:0
                mPaddingRight = mChildView?.paddingRight?:0
                mPaddingBottom = mChildView?.paddingBottom?:0
            }
        }
        mContentView = if (mChildView != null) mChildView!! else frameLayout
    }
}