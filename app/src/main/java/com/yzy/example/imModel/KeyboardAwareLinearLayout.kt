package com.yzy.example.imModel

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.view.Surface
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.LinearLayoutCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import java.util.HashSet

//open class KeyboardAwareLinearLayout @JvmOverloads constructor(
//    context: Context,
//    attributeSet: AttributeSet? = null,
//    defAttrStyle: Int = 0
//) : LinearLayoutCompat(context, attributeSet, defAttrStyle) {


//    private val rect = Rect()
//    private val hiddenListeners = HashSet<OnKeyboardHiddenListener>()
//    private val shownListeners = HashSet<OnKeyboardShownListener>()
//    private var minKeyboardSize: Int = 0
//    private var minCustomKeyboardSize: Int = 0
//    private var defaultCustomKeyboardSize: Int = 0
//    private var minCustomKeyboardTopMargin: Int = 0
//    private var statusBarHeight: Int =BarUtils.getStatusBarHeight()

//    private var viewInset: Int = 0
//
//    private var keyboardOpen = false
//    private var rotation = -1

//    init {
//        minKeyboardSize = SizeUtils.dp2px(50F)
//        minCustomKeyboardSize = SizeUtils.dp2px(110F)
//        defaultCustomKeyboardSize = SizeUtils.dp2px(220F)
//        minCustomKeyboardTopMargin = SizeUtils.dp2px(170F)
//        viewInset = getViewInset()
//    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        updateRotation()
//        updateKeyboardState()
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
//
//    private fun updateRotation() {
////        val oldRotation = rotation
////        rotation = getDeviceRotation()
////        if (oldRotation != rotation) {
////            onKeyboardClose()
////        }
//    }
//
//    private fun updateKeyboardState() {
////        if (isLandscape()) {
////            if (keyboardOpen) onKeyboardClose()
////            return
////        }
//
////        if (viewInset == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
////            viewInset = getViewInset()
////        val availableHeight = this.rootView.height - statusBarHeight - viewInset
////        getWindowVisibleDisplayFrame(rect)
//
////        val keyboardHeight = availableHeight - (rect.bottom - rect.top)
//
////        if (keyboardHeight > minKeyboardSize) {
////            if (getKeyboardHeight() != keyboardHeight) setKeyboardPortraitHeight(keyboardHeight)
////            if (!keyboardOpen) onKeyboardOpen(keyboardHeight)
////        } else if (keyboardOpen) {
////            onKeyboardClose()
////        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun getViewInset(): Int {
//        try {
//            val attachInfoField = View::class.java.getDeclaredField("mAttachInfo")
//            attachInfoField.isAccessible = true
//            val attachInfo = attachInfoField.get(this)
//            if (attachInfo != null) {
//                val stableInsetsField = attachInfo.javaClass.getDeclaredField("mStableInsets")
//                stableInsetsField.isAccessible = true
//                val insets = stableInsetsField.get(attachInfo) as Rect
//                return insets.bottom
//            }
//        } catch (nsfe: NoSuchFieldException) {
//            nsfe.printStackTrace()
//        } catch (iae: IllegalAccessException) {
//            iae.printStackTrace()
//        }
//        return 0
//    }
//
//    protected fun onKeyboardOpen(keyboardHeight: Int) {
//        keyboardOpen = true
//        notifyShownListeners()
//    }
//
//    protected fun onKeyboardClose() {
//        keyboardOpen = false
//        notifyHiddenListeners()
//    }
//
//    fun isKeyboardOpen(): Boolean {
//        return keyboardOpen
//    }
//
//    fun getKeyboardHeight(): Int {
//        return if (isLandscape()) getKeyboardLandscapeHeight() else getKeyboardPortraitHeight()
//    }
//
//    fun isLandscape(): Boolean {
//        val rotation = getDeviceRotation()
//        return rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270
//    }
//
//    private fun getDeviceRotation(): Int {
//        return (context.getSystemService(Activity.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
//    }
//
//    private fun getKeyboardLandscapeHeight(): Int {
//        return height.coerceAtLeast(rootView.height) / 2
//    }
//
//    private fun getKeyboardPortraitHeight(): Int {
//        val keyboardHeight = PreferenceManager.getDefaultSharedPreferences(context).getInt("keyboard_height_portrait", defaultCustomKeyboardSize)
//        return keyboardHeight.coerceAtLeast(minCustomKeyboardSize).coerceAtMost(rootView.height - minCustomKeyboardTopMargin)
//    }
//
//    private fun setKeyboardPortraitHeight(height: Int) {
//        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("keyboard_height_portrait", height).apply()
//    }
//
//    fun postOnKeyboardClose(runnable: Runnable) {
//        if (keyboardOpen) {
//            addOnKeyboardHiddenListener(object : OnKeyboardHiddenListener {
//                override fun onKeyboardHidden() {
//                    removeOnKeyboardHiddenListener(this)
//                    runnable.run()
//                }
//            })
//        } else {
//            runnable.run()
//        }
//    }
//
//    fun postOnKeyboardOpen(runnable: Runnable) {
//        if (!keyboardOpen) {
//            addOnKeyboardShownListener(object : OnKeyboardShownListener {
//                override fun onKeyboardShown() {
//                    removeOnKeyboardShownListener(this)
//                    runnable.run()
//                }
//            })
//        } else {
//            runnable.run()
//        }
//    }
//
//    fun addOnKeyboardHiddenListener(listener: OnKeyboardHiddenListener) {
//        hiddenListeners.add(listener)
//    }
//
//    fun removeOnKeyboardHiddenListener(listener: OnKeyboardHiddenListener) {
//        hiddenListeners.remove(listener)
//    }
//
//    fun addOnKeyboardShownListener(listener: OnKeyboardShownListener) {
//        shownListeners.add(listener)
//    }
//
//    fun removeOnKeyboardShownListener(listener: OnKeyboardShownListener) {
//        shownListeners.remove(listener)
//    }
//
//    private fun notifyHiddenListeners() {
//        val listeners = HashSet(hiddenListeners)
//        for (listener in listeners) {
//            listener.onKeyboardHidden()
//        }
//    }
//
//    private fun notifyShownListeners() {
//        val listeners = HashSet(shownListeners)
//        for (listener in listeners) {
//            listener.onKeyboardShown()
//        }
//    }
//
//    interface OnKeyboardHiddenListener {
//        fun onKeyboardHidden()
//    }
//
//    interface OnKeyboardShownListener {
//        fun onKeyboardShown()
//    }
//}