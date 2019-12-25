//package com.yzy.example.widget.cycleviewpager2
//
//import android.content.Context
//import android.os.Build
//import android.os.Parcel
//import android.os.Parcelable
//import android.os.Parcelable.ClassLoaderCreator
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.RecyclerView
//import androidx.viewpager2.widget.ViewPager2
//import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
//import com.yzy.example.widget.cycleviewpager2.adapter.CyclePagerAdapter
//import com.yzy.example.widget.cycleviewpager2.adapter.CyclePagerFragmentAdapter
//import com.yzy.example.widget.cycleviewpager2.indicator.Indicator
//import com.yzy.example.widget.cycleviewpager2.util.Logger
//import java.lang.ref.WeakReference
//import java.util.*
//
///**
// * Created by wangpeiyuan on 2019-12-02.
// */
//class CycleViewPager2(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
//    private var mViewPager2: ViewPager2? = null
//    private var canAutoTurning = false
//    private var autoTurningTime: Long = 0
//    private var isTurning = false
//    private var mAutoTurningRunnable: AutoTurningRunnable? = null
//    private var mPendingCurrentItem = RecyclerView.NO_POSITION
//    private val mAdapterDataObserver: RecyclerView.AdapterDataObserver =
//        object : RecyclerView.AdapterDataObserver() {
//            override fun onChanged() {
//                val itemCount = Objects.requireNonNull(adapter).itemCount
//                if (itemCount <= 1) {
//                    if (isTurning) {
//                        stopAutoTurning()
//                    }
//                } else {
//                    if (!isTurning) {
//                        startAutoTurning()
//                    }
//                }
//                if (mIndicator != null) {
//                    mIndicator!!.onChanged(pagerRealCount, realCurrentItem)
//                }
//            }
//        }
//    private var mIndicator: Indicator? = null
//    init {
//        mViewPager2 = ViewPager2(context)
//        mViewPager2!!.layoutParams = LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        mViewPager2!!.offscreenPageLimit = 1
//        val mCycleOnPageChangeCallback = CycleOnPageChangeCallback()
//        mViewPager2!!.registerOnPageChangeCallback(mCycleOnPageChangeCallback)
//        mAutoTurningRunnable = AutoTurningRunnable(this)
//        addView(mViewPager2)
//    }
//
//
//    fun setAutoTurning(autoTurningTime: Long) {
//        setAutoTurning(true, autoTurningTime)
//    }
//
//    fun setAutoTurning(canAutoTurning: Boolean, autoTurningTime: Long) {
//        this.canAutoTurning = canAutoTurning
//        this.autoTurningTime = autoTurningTime
//        stopAutoTurning()
//        startAutoTurning()
//    }
//
//    fun startAutoTurning() {
//        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return
//        isTurning = true
//        postDelayed(mAutoTurningRunnable, autoTurningTime)
//    }
//
//    fun stopAutoTurning() {
//        isTurning = false
//        removeCallbacks(mAutoTurningRunnable)
//    }
//
//    var adapter: RecyclerView.Adapter<*>?
//        get() = mViewPager2!!.adapter
//        set(adapter) {
//            if (adapter is CyclePagerAdapter<*> || adapter is CyclePagerFragmentAdapter) {
//                if (mViewPager2!!.adapter === adapter) return
//                adapter.registerAdapterDataObserver(mAdapterDataObserver)
//                mViewPager2!!.adapter = adapter
//                setCurrentItem(1, false)
//                initIndicator()
//                return
//            }
//            throw IllegalArgumentException("adapter must be an instance of CyclePagerAdapter or CyclePagerFragmentAdapter")
//        }
//
//    private val pagerRealCount: Int
//        private get() {
//            val adapter = adapter
//            if (adapter is CyclePagerAdapter<*>) {
//                return adapter.realItemCount
//            }
//            return if (adapter is CyclePagerFragmentAdapter) {
//                adapter.realItemCount
//            } else 0
//        }
//
//    var orientation: Int? =mViewPager2?.orientation
//
////    fun setPageTransformer(transformer: ViewPager2.PageTransformer?) {
////        mViewPager2!!.setPageTransformer(transformer)
////    }
//
////    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
////        mViewPager2!!.addItemDecoration(decor)
////    }
//
////    fun addItemDecoration(
////        decor: RecyclerView.ItemDecoration,
////        index: Int
////    ) {
////        mViewPager2!!.addItemDecoration(decor, index)
////    }
//
//    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
//        Logger.d("setCurrentItem $item")
//        mViewPager2?.setCurrentItem(item, smoothScroll)
//        if (!smoothScroll && mIndicator != null) {
//            mIndicator?.onPageSelected(realCurrentItem)
//        }
//    }
//
//    var currentItem: Int = mViewPager2?.currentItem?:0
//
//    val realCurrentItem: Int = if (currentItem >= 1) currentItem - 1 else currentItem
//
//    var offscreenPageLimit: Int ?=mViewPager2?.offscreenPageLimit
//
//    fun registerOnPageChangeCallback(callback: OnPageChangeCallback) {
//        mViewPager2!!.registerOnPageChangeCallback(callback)
//    }
//
//    fun unregisterOnPageChangeCallback(callback: OnPageChangeCallback) {
//        mViewPager2!!.unregisterOnPageChangeCallback(callback)
//    }
//
//    val viewPager2: ViewPager2
//        get() = mViewPager2!!
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        val action = ev.actionMasked
//        if (action == MotionEvent.ACTION_DOWN) {
//            if (canAutoTurning && isTurning) {
//                stopAutoTurning()
//            }
//        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE
//        ) {
//            if (canAutoTurning) startAutoTurning()
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        startAutoTurning()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        stopAutoTurning()
//    }
//
//    override fun onSaveInstanceState(): Parcelable? {
//        val superState = super.onSaveInstanceState()
//        val ss =
//            SavedState(superState)
//        ss.mCurrentItem = currentItem
//        Logger.d("onSaveInstanceState: " + ss.mCurrentItem)
//        return ss
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable) {
//        if (state !is SavedState) {
//            super.onRestoreInstanceState(state)
//            return
//        }
//        val ss =
//            state
//        super.onRestoreInstanceState(ss.superState)
//        mPendingCurrentItem = ss.mCurrentItem
//        Logger.d("onRestoreInstanceState: $mPendingCurrentItem")
//        restorePendingState()
//    }
//
//    private fun restorePendingState() {
//        if (mPendingCurrentItem == RecyclerView.NO_POSITION) { // No state to restore, or state is already restored
//            return
//        }
//        val currentItem = Math.max(
//            0,
//            Math.min(
//                mPendingCurrentItem,
//                Objects.requireNonNull(adapter).itemCount - 1
//            )
//        )
//        Logger.d("restorePendingState: $currentItem")
//        mPendingCurrentItem = RecyclerView.NO_POSITION
//        setCurrentItem(currentItem, false)
//    }
//
//    fun setIndicator(indicator: Indicator?) {
//        if (mIndicator === indicator) return
//        removeIndicatorView()
//        mIndicator = indicator
//        initIndicator()
//    }
//
//    private fun initIndicator() {
//        if (mIndicator == null || adapter == null) return
//        addView(mIndicator!!.indicatorView)
//        mIndicator!!.onChanged(pagerRealCount, realCurrentItem)
//    }
//
//    private fun removeIndicatorView() {
//        if (mIndicator == null) return
//        removeView(mIndicator!!.indicatorView)
//    }
//
//    //1.normal:
////onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
//// -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
////2.setCurrentItem(,true):
////onPageScrollStateChanged(state=2) -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
////3.other: no call onPageSelected()
////onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
//// -> onPageScrolled... -> onPageScrollStateChanged(state=0)
//    private inner class CycleOnPageChangeCallback : OnPageChangeCallback() {
//        private var isBeginPagerChange = false
//        private var mTempPosition = Companion.INVALID_ITEM_POSITION
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            Logger.d(
//                "onPageScrolled: " + position + " positionOffset: " + positionOffset
//                        + " positionOffsetPixels: " + positionOffsetPixels
//            )
//            if (mIndicator != null) {
//                mIndicator!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//        }
//
//        override fun onPageSelected(position: Int) {
//            Logger.d("onPageSelected: $position")
//            if (isBeginPagerChange) {
//                mTempPosition = position
//            }
//            if (mIndicator != null) {
//                mIndicator!!.onPageSelected(realCurrentItem)
//            }
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            Logger.d("onPageScrollStateChanged: state $state")
//            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
//                isTurning && state == ViewPager2.SCROLL_STATE_SETTLING
//            ) {
//                isBeginPagerChange = true
//            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
//                isBeginPagerChange = false
//                val fixCurrentItem = getFixCurrentItem(mTempPosition)
//                if (fixCurrentItem != Companion.INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
//                    mTempPosition = Companion.INVALID_ITEM_POSITION
//                    Logger.d("onPageScrollStateChanged: fixCurrentItem $fixCurrentItem")
//                    setCurrentItem(fixCurrentItem, false)
//                }
//            }
//            if (mIndicator != null) {
//                mIndicator!!.onPageScrollStateChanged(state)
//            }
//        }
//
//        private fun getFixCurrentItem(position: Int): Int {
//            if (position == Companion.INVALID_ITEM_POSITION) return Companion.INVALID_ITEM_POSITION
//            val lastPosition =
//                Objects.requireNonNull(adapter).itemCount - 1
//            var fixPosition = Companion.INVALID_ITEM_POSITION
//            if (position == 0) {
//                fixPosition = if (lastPosition == 0) 0 else lastPosition - 1
//            } else if (position == lastPosition) {
//                fixPosition = 1
//            }
//            return fixPosition
//        }
//
//        companion object {
//            private const val INVALID_ITEM_POSITION = -1
//        }
//    }
//
//    internal class AutoTurningRunnable(cycleViewPager2: CycleViewPager2) :
//        Runnable {
//        private val reference: WeakReference<CycleViewPager2>
//        override fun run() {
//            val cycleViewPager2 = reference.get()
//            if (cycleViewPager2 != null && cycleViewPager2.canAutoTurning && cycleViewPager2.isTurning) {
//                val itemCount =
//                    Objects.requireNonNull(cycleViewPager2.adapter).itemCount
//                if (itemCount == 0) return
//                val nextItem = (cycleViewPager2.currentItem + 1) % itemCount
//                cycleViewPager2.setCurrentItem(nextItem, true)
//                cycleViewPager2.postDelayed(
//                    cycleViewPager2.mAutoTurningRunnable,
//                    cycleViewPager2.autoTurningTime
//                )
//            }
//        }
//
//        init {
//            reference = WeakReference(cycleViewPager2)
//        }
//    }
//
//    internal class SavedState : BaseSavedState {
//        var mCurrentItem = 0
//
//        constructor(source: Parcel) : super(source) {
//            readValues(source, null)
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
//            readValues(source, loader)
//        }
//
//        constructor(superState: Parcelable?) : super(superState) {}
//
//        private fun readValues(source: Parcel, loader: ClassLoader?) {
//            mCurrentItem = source.readInt()
//        }
//
//        override fun writeToParcel(out: Parcel, flags: Int) {
//            super.writeToParcel(out, flags)
//            out.writeInt(mCurrentItem)
//        }
//
//        companion object {
//            val CREATOR: Parcelable.Creator<SavedState> =
//                object :
//                    ClassLoaderCreator<SavedState> {
//                    override fun createFromParcel(
//                        source: Parcel,
//                        loader: ClassLoader
//                    ): SavedState {
//                        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) SavedState(
//                            source,
//                            loader
//                        ) else SavedState(
//                            source
//                        )
//                    }
//
//                    override fun createFromParcel(source: Parcel): SavedState {
//                        return createFromParcel(source, null)
//                    }
//
//                    override fun newArray(size: Int): Array<SavedState> {
//                        return arrayOfNulls(
//                            size
//                        )
//                    }
//                }
//        }
//    }
//}