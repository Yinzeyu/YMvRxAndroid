package com.yzy.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.ref.WeakReference

class CycleViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
     var mViewPager2: ViewPager2? = null
    private var mAutoTurningRunnable: AutoTurningRunnable? = null
    private var isTurning = false
    private var canAutoTurning = false
     var autoTurningTime: Long = 0
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
            val itemCount = (mViewPager2?.adapter?.itemCount ?: 0)
            if (itemCount <= 1) {
                if (isTurning) {
                    stopAutoTurning()
                }
            } else {
                if (!isTurning) {
                    startAutoTurning()
                }
            }
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
            val lastPosition = (mViewPager2?.adapter?.itemCount ?: 0) - 1
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
    }

    internal class AutoTurningRunnable(cycleViewPager: CycleViewPager) :
        Runnable {
        private val reference: WeakReference<CycleViewPager> = WeakReference(cycleViewPager)
        override fun run() {
            val cycleViewPager = reference.get()
            if (cycleViewPager != null && cycleViewPager.canAutoTurning && cycleViewPager.isTurning) {
                val itemCount = (cycleViewPager.mViewPager2?.adapter?.itemCount ?: 0)
                if (itemCount == 0) return
                val nextItem = (cycleViewPager.currentItem + 1) % itemCount
                cycleViewPager.setCurrentItem(nextItem, true)
                cycleViewPager.postDelayed(cycleViewPager.mAutoTurningRunnable, cycleViewPager.autoTurningTime)
            }
        }

    }
    fun  setAdapter(adapter: RecyclerView.Adapter<*>?){
        mViewPager2?.adapter = adapter
        setCurrentItem(1, false)
        mViewPager2?.offscreenPageLimit = listSize
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

}


