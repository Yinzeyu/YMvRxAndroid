package com.yzy.example.widget.imagewatcher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.yzy.baselibrary.extention.isDoubleClick
import com.yzy.example.R
import com.yzy.example.widget.imagewatcher.listeners.OnClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnLongClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnPageChangeListener
import com.yzy.example.widget.imagewatcher.model.ImageBrowserConfig
import com.yzy.example.widget.imagewatcher.model.ImageBrowserConfig.*
import java.util.*

/**
 * @author : maning
 * @desc : 启动管理
 */
class MNImageBrowser private constructor(private val context: Context) {
    private var imageBrowserConfig: ImageBrowserConfig=ImageBrowserConfig.instance
    fun setImageUrl(imageUrl: String): MNImageBrowser {
        val imageList = ArrayList<String>()
        imageList.add(imageUrl)
        imageBrowserConfig.imageList = imageList
        return this
    }

    fun setImageList(imageList: ArrayList<String>): MNImageBrowser {
        imageBrowserConfig.imageList = imageList
        return this
    }

    fun setCurrentPosition(position: Int): MNImageBrowser {
        imageBrowserConfig.position = position
        return this
    }

    fun setTransformType(transformType: TransformType?): MNImageBrowser {
        imageBrowserConfig.transformType = transformType!!
        return this
    }

    fun setOnClickListener(onClickListener: OnClickListener?): MNImageBrowser {
        imageBrowserConfig.onClickListener = onClickListener
        return this
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener?): MNImageBrowser {
        imageBrowserConfig.onLongClickListener = onLongClickListener
        return this
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener?): MNImageBrowser {
        imageBrowserConfig.onPageChangeListener = onPageChangeListener
        return this
    }

    fun setIndicatorType(indicatorType: IndicatorType): MNImageBrowser {
        imageBrowserConfig.indicatorType = indicatorType
        return this
    }

    fun setIndicatorHide(indicatorHide: Boolean): MNImageBrowser {
        imageBrowserConfig.isIndicatorHide = indicatorHide
        return this
    }

    fun setCustomShadeView(customView: View?): MNImageBrowser {
        imageBrowserConfig.customShadeView = customView
        return this
    }

    fun setCustomProgressViewLayoutID(@LayoutRes customViewID: Int): MNImageBrowser {
        imageBrowserConfig.customProgressViewLayoutID = customViewID
        return this
    }

    fun setScreenOrientationType(screenOrientationType: ScreenOrientationType?): MNImageBrowser {
        imageBrowserConfig.screenOrientationType = screenOrientationType!!
        return this
    }

    fun setFullScreenMode(fullScreenMode: Boolean): MNImageBrowser {
        imageBrowserConfig.isFullScreenMode = fullScreenMode
        return this
    }

    fun setOpenPullDownGestureEffect(openPullDownGestureEffect: Boolean): MNImageBrowser {
        imageBrowserConfig.isOpenPullDownGestureEffect = openPullDownGestureEffect
        return this
    }

    fun setActivityOpenAnime(@AnimRes activityOpenAnime: Int): MNImageBrowser {
        imageBrowserConfig.activityOpenAnime = activityOpenAnime
        return this
    }

    fun setActivityExitAnime(@AnimRes activityExitAnime: Int): MNImageBrowser {
        imageBrowserConfig.activityExitAnime = activityExitAnime
        return this
    }

    fun show(view: View) {
        if (isDoubleClick()) {
            return
        }
        //判断是不是空
        if (imageBrowserConfig.imageList == null || (imageBrowserConfig.imageList?.size?:0) <= 0) {
            return
        }
        MNImageBrowserActivity.instance.imageBrowserConfig = imageBrowserConfig
        startBrowserAvtivity(context, view, Intent(context, MNImageBrowserActivity::class.java))
    }

    private fun startBrowserAvtivity(
        context: Context,
        view: View,
        intent: Intent
    ) {
        if (imageBrowserConfig.activityOpenAnime != R.anim.mn_browser_enter_anim) {
            context.startActivity(intent)
            (context as Activity).overridePendingTransition(imageBrowserConfig.activityOpenAnime, 0)
        } else { //android V4包的类,用于两个activity转场时的缩放效果实现
            val optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view,
                view.width / 2,
                view.height / 2,
                0,
                0
            )
            try {
                ActivityCompat.startActivity(context, intent, optionsCompat.toBundle())
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                context.startActivity(intent)
                (context as Activity).overridePendingTransition(R.anim.mn_browser_enter_anim, 0)
            }
        }
    }

    companion object {
        fun with(context: Context): MNImageBrowser {
            return MNImageBrowser(context)
        }

        /**
         * 手动关闭图片浏览器
         */
        fun finishImageBrowser() {
            MNImageBrowserActivity.instance.finishActivity()
        }



        /**
         * 获取当前位置
         */
        val currentPosition: Int=MNImageBrowserActivity.instance.getCurrentPosition()

        /**
         * 获取ViewPager
         */
        val viewPager: ViewPager?=MNImageBrowserActivity.instance.viewPager

        /**
         * 删除图片
         *
         * @param position
         */
        fun removeImage(position: Int) {
            MNImageBrowserActivity.instance.removeImage(position)
        }

        /**
         * 删除图片
         */
        fun removeCurrentImage() {
            MNImageBrowserActivity.instance.removeCurrentImage()
        }

        /**
         * 获取图片集合
         */
        val imageList: ArrayList<String>?=MNImageBrowserActivity.instance.imageList
    }
}