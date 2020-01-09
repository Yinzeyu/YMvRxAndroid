package com.yzy.example.widget.imagewatcher

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.yzy.example.R
import com.yzy.example.widget.imagewatcher.model.ImageBrowserConfig

/**
 * Description:
 * @author: caiyoufei
 * @date: 19-5-9 下午2:45
 */
class BrowseUtils private constructor() {
    private object SingletonHolder {
        val holder = BrowseUtils()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    fun showNineImage(
      context: Context,
      transformType: ImageBrowserConfig.TransformType? = ImageBrowserConfig.TransformType.Transform_Default,
      indicatorType: ImageBrowserConfig.IndicatorType? = ImageBrowserConfig.IndicatorType.Indicator_Circle,
      indicatorHide: Boolean? = false,
      customView: View? = null,
      showCustomProgressView: Int? = 0,
      position:Int=0,
      sourceImageList:ArrayList<String> = ArrayList(),
      screenOrientationType:ImageBrowserConfig.ScreenOrientationType?=ImageBrowserConfig.ScreenOrientationType.Screenorientation_Default,
      isFulScreenMode:Boolean=false,
      openAnim:Int= R.anim.mn_browser_enter_anim,
      exitAnim:Int=R.anim.mn_browser_exit_anim,
      isOpenPullDownGestureEffect:Boolean=true,
      imageView:ImageView,
      pageChange: ((pageSize: Int) -> Unit)? =null
    ) {
        MNImageBrowser.with(context)
            //页面切换效果
            .setTransformType(transformType)
            //指示器效果
            .setIndicatorType(indicatorType)
            //设置隐藏指示器
            .setIndicatorHide(indicatorHide ?: false)
            //设置自定义遮盖层，定制自己想要的效果，当设置遮盖层后，原本的指示器会被隐藏
            .setCustomShadeView(customView)
            //自定义ProgressView，不设置默认默认没有
            .setCustomProgressViewLayoutID(showCustomProgressView?:0)
            //当前位置
            .setCurrentPosition(position)
            //图片引擎
            .setImageEngine(GlideImageEngine())
            //图片集合
            .setImageList(sourceImageList)
            //方向设置
            .setScreenOrientationType(screenOrientationType)
          //点击监听
          .setOnClickListener { activity, view, pos, url ->

          }
          //长按监听
          .setOnLongClickListener { activity, view, pos, url ->

          }
          .setOnPageChangeListener {
            pageChange?.invoke(it)
          }
            //全屏模式
            .setFullScreenMode(isFulScreenMode)
            //打开动画
            .setActivityOpenAnime(openAnim)
            //关闭动画
            .setActivityExitAnime(exitAnim)
            //手势下拉缩小效果
            .setOpenPullDownGestureEffect(isOpenPullDownGestureEffect)
            //显示：传入当前View
            .show(imageView);
    }

}