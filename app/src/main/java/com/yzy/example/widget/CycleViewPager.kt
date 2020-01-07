package com.yzy.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.yzy.example.component.main.BannerPagerAdapter
import com.yzy.example.widget.cycleviewpager2.indicator.Indicator
import java.lang.ref.WeakReference

class CycleViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mViewPager2: ViewPager2? = null
    private var mAutoTurningRunnable: AutoTurningRunnable? = null
    private var mIndicator: Indicator? = null
    private var isTurning = false
    private var canAutoTurning = false
    private var autoTurningTime: Long = 0
     var listSize: Int = 0
    private val INVALID_ITEM_POSITION = -1

    init {
        mViewPager2 = ViewPager2(context)
        mViewPager2?.apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            offscreenPageLimit = 1
            val mCycleOnPageChangeCallback = CycleOnPageChangeCallback()
            registerOnPageChangeCallback(mCycleOnPageChangeCallback)
        }
        addView(mViewPager2)
        mAutoTurningRunnable = AutoTurningRunnable(this)
    }

    private inner class CycleOnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        private var isBeginPagerChange = false
        private var mTempPosition = INVALID_ITEM_POSITION
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            if (isBeginPagerChange) {
                mTempPosition = position
            }
            val itemCount = (adapter?.itemCount ?: 0)
            if (itemCount <= 1) {
                if (isTurning) {
                    stopAutoTurning()
                }
            } else {
                if (!isTurning) {
                    startAutoTurning()
                }
            }
            mIndicator?.onChanged(listSize, itemCount % listSize)
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
                isTurning && state == ViewPager2.SCROLL_STATE_SETTLING
            ) {
                isBeginPagerChange = true
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isBeginPagerChange = false
                val fixCurrentItem = getFixCurrentItem(mTempPosition)
                if (fixCurrentItem != INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
                    mTempPosition = INVALID_ITEM_POSITION
                    setCurrentItem(fixCurrentItem, false)
                }
            }
        }

        private fun getFixCurrentItem(position: Int): Int {
            if (position == INVALID_ITEM_POSITION) return INVALID_ITEM_POSITION
            val lastPosition = (adapter?.itemCount ?: 0) - 1
            var fixPosition = INVALID_ITEM_POSITION
            if (position == 0) {
                fixPosition = if (lastPosition == 0) 0 else lastPosition - 1
            } else if (position == lastPosition) {
                fixPosition = 1
            }
            return fixPosition
        }

    }


    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        mViewPager2?.setCurrentItem(item, smoothScroll)
        if (smoothScroll) {
            mIndicator?.onPageSelected((adapter?.itemCount ?: 0) % listSize)
        }
    }

    internal class AutoTurningRunnable(cycleViewPager: CycleViewPager) :
        Runnable {
        private val reference: WeakReference<CycleViewPager> = WeakReference(cycleViewPager)
        override fun run() {
            val cycleViewPager = reference.get()
            if (cycleViewPager != null && cycleViewPager.canAutoTurning && cycleViewPager.isTurning) {
                val itemCount = (cycleViewPager.adapter?.itemCount ?: 0)
                if (itemCount == 0) return
                val nextItem = (cycleViewPager.currentItem + 1) % itemCount
                cycleViewPager.setCurrentItem(nextItem, true)
                cycleViewPager.postDelayed(cycleViewPager.mAutoTurningRunnable, cycleViewPager.autoTurningTime)
            }
        }

    }

    var adapter: RecyclerView.Adapter<*>?
        get() = mViewPager2?.adapter
        set(adapter) {
            if (adapter is BannerPagerAdapter) {
                if (mViewPager2?.adapter === adapter) return
                adapter.registerAdapterDataObserver(mAdapterDataObserver)
                mViewPager2?.adapter = adapter
                setCurrentItem(1, false)
                initIndicator()
                return
            }

        }

    private val mAdapterDataObserver: RecyclerView.AdapterDataObserver =
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {

            }
        }

    private fun initIndicator() {
        mIndicator?.let {
            addView(it.indicatorView)
            it.onChanged(listSize,0)
        }
    }

    private fun removeIndicatorView() {
        mIndicator?.let {
            removeView(it.indicatorView)
        }

    }


    fun setAutoTurning(autoTurningTime: Long) {
        setAutoTurning(true, autoTurningTime)
    }

    private fun setAutoTurning(canAutoTurning: Boolean, autoTurningTime: Long) {
        this.canAutoTurning = canAutoTurning
        this.autoTurningTime = autoTurningTime
        stopAutoTurning()
        startAutoTurning()
    }

    fun startAutoTurning() {
        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return
        isTurning = true
        postDelayed(mAutoTurningRunnable, autoTurningTime)
    }

    fun stopAutoTurning() {
        isTurning = false
        removeCallbacks(mAutoTurningRunnable)
    }

    var currentItem: Int
        get() = (mViewPager2?.currentItem ?: 0)
        set(value) {
            mViewPager2?.currentItem = value
        }


    var offscreenPageLimit: Int
        get() = (mViewPager2?.offscreenPageLimit ?: 0)
        set(value) {
            mViewPager2?.offscreenPageLimit = value
        }

    var orientation: Int
        get() = (mViewPager2?.orientation ?: 0)
        set(value) {
            mViewPager2?.orientation = value
        }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer?) {
        mViewPager2?.setPageTransformer(transformer)
    }


    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mViewPager2?.addItemDecoration(decor)
    }

    fun setIndicator(indicator: Indicator?) {
        if (mIndicator === indicator) return
        removeIndicatorView()
        mIndicator = indicator
        initIndicator()
    }
    fun registerOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        mViewPager2?.registerOnPageChangeCallback(callback)
    }

    fun unregisterOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        mViewPager2?.unregisterOnPageChangeCallback(callback)
    }

}


