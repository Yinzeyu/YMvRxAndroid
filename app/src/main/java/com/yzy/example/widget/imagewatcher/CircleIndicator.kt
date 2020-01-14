package com.yzy.example.widget.imagewatcher

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import com.yzy.example.R
import kotlin.math.abs

class CircleIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val DEFAULT_INDICATOR_WIDTH: Float = 5f
    private var mViewpager: ViewPager? = null
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1
    private var mAnimatorResId = R.animator.browser_scale_with_alpha
    private var mAnimatorReverseResId = 0
    private var mIndicatorBackgroundResId = R.drawable.mn_browser_white_radius
    private var mIndicatorUnselectedBackgroundResId = R.drawable.mn_browser_white_radius
    private var mAnimatorOut: Animator? = null
    private var mAnimatorIn: Animator? = null
    private var mImmediateAnimatorOut: Animator? = null
    private var mImmediateAnimatorIn: Animator? = null

    private var mLastPosition = -1

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator)
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1)
        mIndicatorHeight =
            typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1)
        mIndicatorMargin =
            typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1)
        mAnimatorResId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_animator,
            R.animator.browser_scale_with_alpha
        )
        mAnimatorReverseResId =
            typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator_reverse, 0)
        mIndicatorBackgroundResId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_drawable,
            R.drawable.mn_browser_white_radius
        )
        mIndicatorUnselectedBackgroundResId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_drawable_unselected,
            mIndicatorBackgroundResId
        )
        val orientation = typedArray.getInt(R.styleable.CircleIndicator_ci_orientation, -1)
        setOrientation(if (orientation == VERTICAL) VERTICAL else HORIZONTAL)
        val gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, -1)
        setGravity(if (gravity >= 0) gravity else Gravity.CENTER)
        typedArray.recycle()
        mIndicatorWidth =
            if (mIndicatorWidth < 0) dip2px() else mIndicatorWidth
        mIndicatorHeight =
            if (mIndicatorHeight < 0) dip2px() else mIndicatorHeight
        mIndicatorMargin =
            if (mIndicatorMargin < 0) dip2px() else mIndicatorMargin

        mAnimatorResId =
            if (mAnimatorResId == 0) R.animator.browser_scale_with_alpha else mAnimatorResId
        mAnimatorOut = createAnimatorOut(context)
        mImmediateAnimatorOut = createAnimatorOut(context)
        mImmediateAnimatorOut?.duration = 0
        mAnimatorIn = createAnimatorIn(context)
        mImmediateAnimatorIn = createAnimatorIn(context)
        mImmediateAnimatorIn?.duration = 0
        mIndicatorBackgroundResId =
            if (mIndicatorBackgroundResId == 0) R.drawable.mn_browser_white_radius else mIndicatorBackgroundResId
        mIndicatorUnselectedBackgroundResId =
            if (mIndicatorUnselectedBackgroundResId == 0) mIndicatorBackgroundResId else mIndicatorUnselectedBackgroundResId
    }

    private fun createAnimatorOut(context: Context): Animator? {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId)
    }

    private fun createAnimatorIn(context: Context): Animator? {
        val animatorIn: Animator
        if (mAnimatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId)
        }
        return animatorIn
    }

    private class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return abs(1.0f - value)
        }
    }

    private fun dip2px(dpValue: Float=DEFAULT_INDICATOR_WIDTH): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    fun setViewPager(viewPager: ViewPager) {
        mViewpager = viewPager
        mViewpager?.let {
            if (it.adapter != null){
                mLastPosition = -1
                createIndicators()
                it.removeOnPageChangeListener(mInternalPageChangeListener)
                it.addOnPageChangeListener(mInternalPageChangeListener)
                mInternalPageChangeListener.onPageSelected(it.currentItem)
            }
        }
    }
    private fun createIndicators() {
        removeAllViews()
        mViewpager?.let {viewPager ->
            viewPager.adapter?.let {pagerAdapter ->
                val count = pagerAdapter.count
                if (count <= 0) {
                    return
                }
                val currentItem = viewPager.currentItem
                val orientation = orientation
                for (i in 0 until count) {
                    if (currentItem == i) {
                        addIndicator(orientation, mIndicatorBackgroundResId, mImmediateAnimatorOut)
                    } else {
                        addIndicator(orientation, mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn)
                    }
                }
            }

        }

    }

    private fun addIndicator(orientation: Int, @DrawableRes backgroundDrawableId: Int, animator: Animator?) {
        animator?.let {
            if (it.isRunning){
                it.end()
                it.cancel()
            }
            val view = View(context)
            view.setBackgroundResource(backgroundDrawableId)
            addView(view, mIndicatorWidth, mIndicatorHeight)
            val lp = view.layoutParams as LayoutParams
            when(orientation){
                HORIZONTAL->{
                    lp.leftMargin = mIndicatorMargin
                    lp.rightMargin = mIndicatorMargin
                }
                else ->{
                    lp.topMargin = mIndicatorMargin
                    lp.bottomMargin = mIndicatorMargin
                }
            }
            view.layoutParams = lp
            it.setTarget(view)
            it.start()
        }
    }

    fun getDataSetObserver(): DataSetObserver? {
        return mInternalDataSetObserver
    }

    private val mInternalDataSetObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mViewpager?.let {
                val newCount =it.adapter?.count?:0
                val currentCount = childCount
                mLastPosition = when {
                    newCount == currentCount -> { // No change
                        return
                    }
                    mLastPosition < newCount -> {
                        it.currentItem
                    }
                    else -> {
                        -1
                    }
                }
                createIndicators()
            }

        }
    }


    private val mInternalPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                mViewpager?.let {viewpager->
                    viewpager.adapter?.let {pagerAdapter ->
                        if (pagerAdapter.count <= 0){
                            return
                        }
                        mAnimatorIn?.let {
                            if (it.isRunning) {
                                it.end()
                                it.cancel()
                            }
                        }
                        mAnimatorOut?.let {
                            if (it.isRunning) {
                                it.end()
                                it.cancel()
                            }
                        }
                        var currentIndicator: View?=null
                        if (mLastPosition >= 0 && getChildAt(mLastPosition).also { currentIndicator = it } != null) {
                            currentIndicator?.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
                            mAnimatorIn?.setTarget(currentIndicator)
                            mAnimatorIn?.start()
                        }
                       getChildAt(position)?.let {
                            setBackgroundResource(mIndicatorBackgroundResId)
                           mAnimatorOut?.setTarget(it)
                           mAnimatorOut?.start()
                        }
                        mLastPosition = position
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        }
}