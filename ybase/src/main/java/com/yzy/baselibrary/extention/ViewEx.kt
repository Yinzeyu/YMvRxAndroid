package com.yzy.baselibrary.extention

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewManager
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.yzy.baselibrary.R
import com.yzy.baselibrary.utils.PressEffectHelper

/**
 *description: View的扩展.
 *@date 2019/7/15
 *@author: yzy.
 */

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/9/24 10:39
 */
//点击事件
@SuppressLint("CheckResult")
inline fun View.click(crossinline function: (view: View) -> Unit) {
    this.setOnClickListener {
        val tag = this.getTag(R.id.id_tag_click)
        if (tag == null || System.currentTimeMillis() - tag.toString().toLong() > 600) {
            this.setTag(R.id.id_tag_click, System.currentTimeMillis())
            function.invoke(it)
        }
    }
}

//设置按下效果为改变背景色
fun View.pressEffectBgColor(
        bgColor: Int = Color.parseColor("#f7f7f7"),
        topLeftRadiusDp: Float = 0f,
        topRightRadiusDp: Float = 0f,
        bottomRightRadiusDp: Float = 0f,
        bottomLeftRadiusDp: Float = 0f
) {
    PressEffectHelper.bgColorEffect(
            this,
            bgColor,
            topLeftRadiusDp,
            topRightRadiusDp,
            bottomRightRadiusDp,
            bottomLeftRadiusDp
    )
}


//设置按下效果为改变透明度
fun View.pressEffectAlpha(pressAlpha: Float = 0.7f) {
    PressEffectHelper.alphaEffect(this, pressAlpha)
}
//关闭按下效果
fun View.pressEffectDisable() {
    this.setOnTouchListener(null)
}


// 黑暗 0.0F ~ 1.0F 透明
fun Context.setBackgroundAlpha(alpha: Float) {
    val act = this as? Activity ?: return
    val attributes = act.window.attributes
    attributes.alpha = alpha
    act.window.attributes = attributes
}

// 快速双击
private var lastClickTime: Long = 0
private const val SPACE_TIME = 500
fun isDoubleClick(): Boolean {
    val currentTime = System.currentTimeMillis()
    val isDoubleClick: Boolean // in range
    isDoubleClick = currentTime - lastClickTime <= SPACE_TIME
    if (!isDoubleClick) {
        lastClickTime = currentTime
    }
    return isDoubleClick
}


fun View.gone() {
    isGone =true
}

fun View.invisible() {
   isInvisible=true
}

fun View.visible() {
    isVisible=true
}


fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

/**
 * 如果回调不为空则设置点击事件,回调函数中需要返回数据
 */
fun <T> View.setClickNotNull(
    t: T,
    onClick: ((t: T) -> Unit)?
) {
    if (onClick == null) {
        setOnClickListener(null)
    } else {
        click {
            onClick.invoke(t)
        }
    }
}

/**
 * 如果回调不为空则设置点击事件
 */
fun View.setClickNotNull(onClick: (() -> Unit)?) {
    if (onClick == null) {
        setOnClickListener(null)
    } else {
        click {
            onClick.invoke()
        }
    }
}


//从父控件移除
fun View.removeParent() {
    val parentTemp = parent
    if (parentTemp is ViewManager) parentTemp.removeView(this)
}

var View.backgroundColor: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setBackgroundColor(v)

var View.backgroundResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setBackgroundResource(v)

var android.widget.ImageView.imageResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setImageResource(v)

var android.widget.ImageView.imageURI: android.net.Uri?
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setImageURI(v)

var android.widget.ImageView.imageBitmap: android.graphics.Bitmap?
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setImageBitmap(v)

var android.widget.TextView.textColor: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setTextColor(v)

var android.widget.TextView.hintTextColor: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setHintTextColor(v)

var android.widget.TextView.linkTextColor: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setLinkTextColor(v)

var android.widget.TextView.lines: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setLines(v)

var android.widget.TextView.singleLine: Boolean
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setSingleLine(v)

var android.widget.RelativeLayout.horizontalGravity: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setHorizontalGravity(v)

var android.widget.RelativeLayout.verticalGravity: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setVerticalGravity(v)

var android.widget.LinearLayout.horizontalGravity: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setHorizontalGravity(v)

var android.widget.LinearLayout.verticalGravity: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
set(v) = setVerticalGravity(v)

var android.widget.AbsListView.selectorResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setSelector(v)

var android.widget.CheckedTextView.checkMarkDrawableResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setCheckMarkDrawable(v)

var android.widget.CompoundButton.buttonDrawableResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setButtonDrawable(v)

var android.widget.TabWidget.leftStripDrawableResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setLeftStripDrawable(v)

var android.widget.TabWidget.rightStripDrawableResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setRightStripDrawable(v)

var android.widget.TextView.hintResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setHint(v)

var android.widget.TextView.textResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR)
    get() = noGetter()
    set(v) = setText(v)

var android.widget.Toolbar.logoResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setLogo(v)

var android.widget.Toolbar.logoDescriptionResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setLogoDescription(v)

var android.widget.Toolbar.navigationContentDescriptionResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setNavigationContentDescription(v)

var android.widget.Toolbar.navigationIconResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setNavigationIcon(v)

var android.widget.Toolbar.subtitleResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setSubtitle(v)

var android.widget.Toolbar.titleResource: Int
    @Deprecated(NO_GETTER, level = DeprecationLevel.ERROR) get() = noGetter()
    set(v) = setTitle(v)


fun noGetter(): Nothing = throw RuntimeException("Property does not have a getter")

const val NO_GETTER: String = "Property does not have a getter"
