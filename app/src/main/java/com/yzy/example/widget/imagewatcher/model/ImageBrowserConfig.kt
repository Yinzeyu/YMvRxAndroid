package com.yzy.example.widget.imagewatcher.model

import android.view.View
import androidx.annotation.AnimRes
import com.yzy.example.R
import com.yzy.example.widget.imagewatcher.listeners.OnClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnLongClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnPageChangeListener
import java.util.*

/**
 * author : maning
 * time   : 2018/04/10
 * desc   : 相关配置信息
 * version: 1.0
 */
class ImageBrowserConfig {
    private object SingletonHolder {
        val holder = ImageBrowserConfig()
    }

    companion object {
        val instance = SingletonHolder.holder
    }
    //枚举类型：切换动画类型
    enum class TransformType {
        Transform_Default, Transform_DepthPage, Transform_RotateDown, Transform_RotateUp, Transform_ZoomIn, Transform_ZoomOutSlide, Transform_ZoomOut
    }

    //枚举类型：指示器类型
    enum class IndicatorType {
        Indicator_Circle, Indicator_Number
    }

    //枚举类型：屏幕方向
    enum class ScreenOrientationType {
        //默认：横竖屏全部支持
        Screenorientation_Default,  //竖屏
        ScreenOrientation_Portrait,  //横屏
        Screenorientation_Landscape
    }

    //当前位置
    var position = 0
    //切换效果
    var transformType = TransformType.Transform_Default
    //指示器类型
    var indicatorType = IndicatorType.Indicator_Number
    //图片源
    var imageList: ArrayList<String>? = null
    //单击监听
    var onClickListener: OnClickListener? =
        null
    //长按监听
    var onLongClickListener: OnLongClickListener? =
        null
    //页面切换监听
    var onPageChangeListener: OnPageChangeListener? =
        null
    //设置屏幕的方向
    var screenOrientationType =
        ScreenOrientationType.Screenorientation_Default
    //是否隐藏指示器
    var isIndicatorHide = false
    //自定义View
    var customShadeView: View? = null
    //自定义ProgressView
    var customProgressViewLayoutID = 0
    //全部模式：默认false
    var isFullScreenMode = false
    //下拉缩小效果：默认开启true
    var isOpenPullDownGestureEffect = true
    //打开动画
    @AnimRes
    var activityOpenAnime = R.anim.mn_browser_enter_anim
    //关闭动画
    @AnimRes
    var activityExitAnime = R.anim.mn_browser_exit_anim

}