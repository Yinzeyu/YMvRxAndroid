package com.yzy.example.widget.imagewatcher

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.inflate
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.visible
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.widget.imagewatcher.listeners.OnClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnLongClickListener
import com.yzy.example.widget.imagewatcher.listeners.OnPageChangeListener
import com.yzy.example.widget.imagewatcher.model.ImageBrowserConfig
import com.yzy.example.widget.imagewatcher.model.ImageBrowserConfig.*
import com.yzy.example.widget.imagewatcher.transforms.*
import com.yzy.example.widget.imagewatcher.immersionbar.BarHide
import com.yzy.example.widget.imagewatcher.immersionbar.ImmersionBar
import com.yzy.example.widget.imagewatcher.view.MNGestureView.OnSwipeListener
import kotlinx.android.synthetic.main.activity_mnimage_browser.*
import kotlinx.android.synthetic.main.mn_image_browser_item_show_image.view.*
import java.lang.ref.WeakReference

/**
 * 图片浏览的页面
 */
class MNImageBrowserActivity : BaseActivity() {
    private object SingletonHolder {
        val holder = MNImageBrowserActivity()
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    override fun layoutResId(): Int {
        return R.layout.activity_mnimage_browser
    }

    override fun initView() {
        ImmersionBar.with(this).navigationBarColor(R.color.mn_ib_black).init()
        //判断是否全屏模式，隐藏状态栏
        if (imageBrowserConfig.isFullScreenMode) {
            ImmersionBar.with(this@MNImageBrowserActivity).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init()
        }
        circleIndicator.gone()
        numberIndicator.gone()
        ll_custom_view.gone()
        initViewPager()
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        imageUrlList = imageBrowserConfig.imageList
        currentPosition = imageBrowserConfig.position
        transformType = imageBrowserConfig.transformType
        onClickListener = imageBrowserConfig.onClickListener
        onLongClickListener = imageBrowserConfig.onLongClickListener
        indicatorType = imageBrowserConfig.indicatorType
        screenOrientationType = imageBrowserConfig.screenOrientationType
        onPageChangeListener = imageBrowserConfig.onPageChangeListener
        if (imageUrlList == null) {
            imageUrlList = ArrayList()
            //直接关闭
            finishActivity()
            return
        }
        if ((imageUrlList?.size?:0) <= 1) {
            rl_indicator.gone()
        } else {
            rl_indicator.visible()
                rl_indicator.isVisible=imageBrowserConfig.isIndicatorHide
            if (indicatorType == IndicatorType.Indicator_Number) {
                numberIndicator.visible()
                numberIndicator.text = "${(currentPosition + 1)}/${imageUrlList?.size}"
            } else {
                circleIndicator.visible()
            }
        }
        //自定义View
        val customShadeView = imageBrowserConfig.customShadeView
        if (customShadeView != null) {
            ll_custom_view.visible()
            ll_custom_view.removeAllViews()
            ll_custom_view.addView(customShadeView)
            rl_indicator.gone()
        }
        //横竖屏梳理
        requestedOrientation = when (screenOrientationType) {
                ScreenOrientationType.ScreenOrientation_Portrait -> { //设置横竖屏
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
                ScreenOrientationType.Screenorientation_Landscape -> { //设置横横屏
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
                else -> { //设置默认:由设备的物理方向传感器决定
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR
                }
            }
        //自定义ProgressView
        progressViewLayoutId = imageBrowserConfig.customProgressViewLayoutID
    }

    //图片地址
    private var imageUrlList: ArrayList<String>? = null
    //当前位置
    private var currentPosition = 0
    //当前切换的动画
    private var transformType: TransformType? = null
    //切换器
    private var indicatorType: IndicatorType? = null
    //图片加载引擎
    //监听
    var onLongClickListener: OnLongClickListener? =
        null
    var onClickListener: OnClickListener? = null
    var onPageChangeListener: OnPageChangeListener? =
        null
    private var imageBrowserAdapter: MyAdapter? = null
    private var screenOrientationType: ScreenOrientationType? = null
    //图片加载进度View的布局ID
    private var progressViewLayoutId = 0

    private fun initViewPager() {
        imageBrowserAdapter = MyAdapter()
        viewPagerBrowser.adapter = imageBrowserAdapter
        viewPagerBrowser.currentItem = currentPosition
        setViewPagerTransforms()
        circleIndicator.setViewPager(viewPagerBrowser)
        viewPagerBrowser.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                currentPosition = position
                numberIndicator.text =  "${(currentPosition + 1)}/${imageUrlList?.size}"
                onPageChangeListener?.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mnGestureView?.setOnGestureListener {
            //8.0去掉下拉缩小效果,8.0背景透明的Activity不能设置方向
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O)   return@setOnGestureListener false
            //是否开启手势下拉效果
            if (!imageBrowserConfig.isOpenPullDownGestureEffect)  return@setOnGestureListener false

            if (( imageBrowserAdapter?.primaryItem?.imageView?.scale?.toDouble()?:0.0) != 1.0) {
                return@setOnGestureListener false
            }
            true
        }
        mnGestureView.setOnSwipeListener(object : OnSwipeListener {
            override fun downSwipe() {
                finishBrowser()
            }

            override fun onSwiping(deltaY: Float) {
                rl_indicator.gone()
                ll_custom_view.gone()
                var mAlpha = 1 - deltaY / 500
                if (mAlpha < 0.3) {
                    mAlpha = 0.3f
                }
                if (mAlpha > 1) {
                    mAlpha = 1f
                }
                rl_black_bg.alpha = mAlpha
            }

            override fun overSwipe() {
                if ((imageUrlList?.size?:0) <= 1) {
                    rl_indicator.gone()
                } else {
                    rl_indicator.visible()
                    rl_indicator.isVisible =!imageBrowserConfig.isIndicatorHide
                }
                //自定义View
                val customShadeView = imageBrowserConfig.customShadeView
                if (customShadeView != null) {
                    ll_custom_view.visible()
                    rl_indicator.gone()
                } else {
                    ll_custom_view.gone()
                }
                rl_black_bg.alpha = 1f
            }
        })
    }

    private fun setViewPagerTransforms() {
        if (transformType == TransformType.Transform_Default) {
            viewPagerBrowser.setPageTransformer(true, DefaultTransformer())
        } else if (transformType == TransformType.Transform_DepthPage) {
            viewPagerBrowser.setPageTransformer(
                true,
                DepthPageTransformer()
            )
        } else if (transformType == TransformType.Transform_RotateDown) {
            viewPagerBrowser.setPageTransformer(true, RotateDownTransformer())
        } else if (transformType == TransformType.Transform_RotateUp) {
            viewPagerBrowser.setPageTransformer(true, RotateUpTransformer())
        } else if (transformType == TransformType.Transform_ZoomIn) {
            viewPagerBrowser.setPageTransformer(true, ZoomInTransformer())
        } else if (transformType == TransformType.Transform_ZoomOutSlide) {
            viewPagerBrowser.setPageTransformer(true, ZoomOutSlideTransformer())
        } else if (transformType == TransformType.Transform_ZoomOut) {
            viewPagerBrowser.setPageTransformer(true, ZoomOutTransformer())
        } else {
            viewPagerBrowser.setPageTransformer(true, DefaultTransformer())
        }
    }

    private fun finishBrowser() {
        try {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            ll_custom_view?.gone()
            rl_indicator?.gone()
            finish()
            overridePendingTransition(0, imageBrowserConfig.activityExitAnime)
            //销毁相关数据
            sActivityRef = null
        } catch (e: Exception) {
            finish()
        }
    }

    override fun onBackPressed() {
        finishBrowser()
    }

    private inner class MyAdapter : PagerAdapter() {
        var primaryItem: View? = null
        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            primaryItem = `object` as View
        }

        override fun getCount(): Int {
            return imageUrlList!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any
        ): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any
        ) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflate = mContext.inflate(R.layout.mn_image_browser_item_show_image, container, false)
            val url = imageUrlList!![position]
            inflate.rl_browser_root.setOnClickListener { finishBrowser() }
            inflate.imageView.setOnClickListener {
                //单击事件
            onClickListener?.onClick(this@MNImageBrowserActivity, inflate.imageView, position, url)
                finishBrowser()
            }
            inflate.imageView.setOnLongClickListener {
                    onLongClickListener?.onLongClick(this@MNImageBrowserActivity, inflate.imageView, position, url)
                false
            }
            //ProgressView
            if (progressViewLayoutId != 0) {
                val customProgressView = layoutInflater.inflate(progressViewLayoutId, null)
                if (customProgressView != null) {
                    inflate.progress_view.removeAllViews()
                    inflate.progress_view.addView(customProgressView)
                    inflate.progress_view.visible()
                } else {
                    inflate.progress_view.gone()
                }
            } else {
                inflate.progress_view.gone()
            }
            inflate.imageView.load(url,R.mipmap.ic_launcher,success = {
                inflate.progress_view.gone()
            },failed = {
                inflate.progress_view.gone()
            })
            //图片加载
            container.addView(inflate)
            return inflate
        }

    }

    //用来保存当前Activity
    private var sActivityRef: WeakReference<MNImageBrowserActivity?>? = null
    //相关配置信息
    var imageBrowserConfig: ImageBrowserConfig=ImageBrowserConfig.instance
    /**
     * 关闭当前Activity
     */
    fun finishActivity() = sActivityRef?.get()?.finishBrowser()

    /**
     * 获取ViewPager
     *
     * @return
     */
    val viewPager: ViewPager? =  sActivityRef?.get()?.viewPagerBrowser

    /**
     * 获取当前位置
     *
     * @return
     */
    fun getCurrentPosition(): Int = sActivityRef?.get()?.currentPosition?:-1

    /**
     * 获取当前ImageView
     *
     * @return
     */
    val currentImageView: ImageView?=sActivityRef?.get()?.imageBrowserAdapter?.primaryItem?.imageView

    /**
     * 删除一张图片
     *
     * @param position
     * @return
     */
    fun removeImage(position: Int) {
        val get = sActivityRef?.get()

        val imageUrlList1 = get?.imageUrlList?: ArrayList()
        if (imageUrlList1.size > 1){
            imageUrlList1.removeAt(position)
            get?.let {
                //更新当前位置
                if (it.currentPosition >= (it.imageList?.size?:0) && it.currentPosition >= 1) {
                    it.currentPosition--
                }
                if (it.currentPosition >=(it.imageList?.size?:0)) {
                    it.currentPosition = (it.imageList?.size?:0)- 1
                }
                it.initViewPager()
                it.imageBrowserAdapter?.notifyDataSetChanged()
            }

        }
    }

    /**
     * 删除一张图片
     *
     * @return
     */
    fun removeCurrentImage() {
        removeImage(getCurrentPosition())
    }

    /**
     * 图片资源列表
     *
     * @return
     */
    val imageList: ArrayList<String>?= sActivityRef?.get()?.imageUrlList
}