package com.yzy.example.widget.ninegridview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yzy.example.R
import java.util.*


/**
 * description: NineGridView.
 *
 * @date 2018/10/22 11:08.
 * @author: yzy.
 */
class NineGridView<T> @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs) {

    private var mRowCount: Int = 0                      // 行数
    private var mColumnCount: Int = 0                   // 列数

    private var mMaxSize: Int = 0                       // 最大图片数
    private var mShowStyle: Int = 0                     // 显示风格
    private var mGap: Int = 0                           // 宫格间距
    private var mSingleImgSize: Int = 0                 // 单张图片时的尺寸
    private var mGridSize: Int = 0                      // 宫格大小,即图片大小
    private var mSpanType: Int = 0                      // 跨行跨列的类型

    private val mImageViewList = ArrayList<ImageView>()
    private var mImgDataList: MutableList<T> = mutableListOf()

    var mAdapter: NineGridViewAdapter<T>? = null
    var mItemClickListener: ItemClickListener<T>? = null
    var mItemLongClickListener: ItemLongClickListener<T>? = null

    // 处理onLayout会被重复调用的问题，只有在重新设置了数据源后才需要重写layoutChildView
    private var mFirstLoad = true

    var imageList: List<ImageView> = mImageViewList

    init {
        context.obtainStyledAttributes(attrs, R.styleable.NineGridView).apply {
            mGap = getDimension(R.styleable.NineGridView_imgGap, 0f).toInt()
            mSingleImgSize = getDimensionPixelSize(R.styleable.NineGridView_singleImgSize, -1)
            mShowStyle = getInt(R.styleable.NineGridView_showStyle, STYLE_GRID)
            mMaxSize = getInt(R.styleable.NineGridView_maxSize, 9)
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        val totalWidth = width - paddingLeft - paddingRight
        if (mImgDataList.isNotEmpty()) {
            mGridSize = if (mImgDataList.size == 1 && mSingleImgSize != -1) {
                if (mSingleImgSize > totalWidth) totalWidth else mSingleImgSize
            } else {
                (totalWidth - mGap * (mColumnCount - 1)) / mColumnCount
            }
            height = mGridSize * mRowCount + mGap * (mRowCount - 1) + paddingTop + paddingBottom
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // glide在加载完图片后会回调changed为false 的onlayout方法，所以这里做判断
        if (changed || mFirstLoad) {
            mFirstLoad = false
            layoutChildrenView()
        }
    }

    /**
     * 根据照片数量和span类型来对子视图进行动态排版布局
     */
    private fun layoutChildrenView() {
        val showChildrenCount = getNeedShowCount(mImgDataList.size)
        //对不跨行不跨列的进行排版布局,单张或者2张默认进行普通排版
        if (mSpanType == NOSPAN || showChildrenCount <= 2) {
            layoutForNoSpanChildrenView(showChildrenCount)
            return
        }
        when (showChildrenCount) {
            3 -> layoutForThreeChildrenView(showChildrenCount)
            4 -> layoutForFourChildrenView(showChildrenCount)
            5 -> layoutForFiveChildrenView(showChildrenCount)
            6 -> layoutForSixChildrenView(showChildrenCount)
            else -> layoutForNoSpanChildrenView(showChildrenCount)
        }
    }

    private fun layoutForNoSpanChildrenView(childrenCount: Int) {
        if (childrenCount <= 0) return
        var row: Int
        var column: Int
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            row = i / mColumnCount
            column = i % mColumnCount
            left = (mGridSize + mGap) * column + paddingLeft
            top = (mGridSize + mGap) * row + paddingTop
            right = left + mGridSize
            bottom = top + mGridSize
            childrenView.layout(left, top, right, bottom)
            mAdapter?.onDisplayImage(context, childrenView, mImgDataList[i])
        }
    }

    private fun layoutForThreeChildrenView(childrenCount: Int) {
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            when (mSpanType) {
                TOPCOLSPAN    //2行2列,首行跨列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                BOTTOMCOLSPAN //2行2列,末行跨列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                LEFTROWSPAN   //2行2列,首列跨行
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize * 2 + mGap
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                else -> {
                }
            }
            mAdapter?.onDisplayImage(context, childrenView, mImgDataList[i])
        }
    }

    private fun layoutForFourChildrenView(childrenCount: Int) {
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            when (mSpanType) {
                TOPCOLSPAN    //3行3列,首行跨2行3列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize * 3 + mGap * 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                        1 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                BOTTOMCOLSPAN //3行3列,末行跨2行3列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize * 3 + mGap * 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                LEFTROWSPAN   //3行3列,首列跨3行2列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize * 3 + mGap * 2
                        }
                        1 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                else -> {
                }
            }
            mAdapter?.onDisplayImage(context, childrenView, mImgDataList[i])
        }
    }

    private fun layoutForFiveChildrenView(childrenCount: Int) {
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            when (mSpanType) {
                TOPCOLSPAN    //3行3列,首行跨2行,2列跨3列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + (mGridSize * 3 + mGap) / 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                        1 -> {
                            left = paddingLeft + (mGridSize * 3 + mGap) / 2 + mGap
                            top = paddingTop
                            right = left + (mGridSize * 3 + mGap) / 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                        2 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                BOTTOMCOLSPAN //3行3列,末行跨2行,2列跨3列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + (mGridSize * 3 + mGap) / 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                        else -> {
                            left = paddingLeft + (mGridSize * 3 + mGap) / 2 + mGap
                            top = paddingTop + mGridSize + mGap
                            right = left + (mGridSize * 3 + mGap) / 2
                            bottom = top + mGridSize * 2 + mGap
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                LEFTROWSPAN   //3行3列,2行跨3行，1列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize * 2 + mGap
                            bottom = top + (mGridSize * 3 + mGap) / 2
                        }
                        1 -> {
                            left = paddingLeft
                            top = paddingTop + (mGridSize * 3 + mGap) / 2 + mGap
                            right = left + mGridSize * 2 + mGap
                            bottom = top + (mGridSize * 3 + mGap) / 2
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                else -> {
                }
            }
            mAdapter?.onDisplayImage(context, childrenView, mImgDataList[i])
        }
    }

    private fun layoutForSixChildrenView(childrenCount: Int) {
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int
        for (i in 0 until childrenCount) {
            val childrenView = getChildAt(i) as ImageView
            when (mSpanType) {
                TOPCOLSPAN    //3行3列,第一张跨2行2列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize * 2 + mGap
                        }
                        1 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        4 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                BOTTOMCOLSPAN //3行3列,第4张跨2行2列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        2 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize * 2 + mGap
                        }
                        4 -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                LEFTROWSPAN   //3行3列,第2张跨2行2列
                -> {
                    when (i) {
                        0 -> {
                            left = paddingLeft
                            top = paddingTop
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        1 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop
                            right = left + mGridSize * 2 + mGap
                            bottom = top + mGridSize * 2 + mGap
                        }
                        2 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize + mGap
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        3 -> {
                            left = paddingLeft
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        4 -> {
                            left = paddingLeft + mGridSize + mGap
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                        else -> {
                            left = paddingLeft + mGridSize * 2 + mGap * 2
                            top = paddingTop + mGridSize * 2 + mGap * 2
                            right = left + mGridSize
                            bottom = top + mGridSize
                        }
                    }
                    childrenView.layout(left, top, right, bottom)
                }
                else -> {
                }
            }
            mAdapter?.onDisplayImage(context, childrenView, mImgDataList[i])
        }
    }

    /**
     * 根据跨行跨列的类型，以及图片数量，来确定单元格的行数和列数
     *
     * @param imagesSize 图片数量
     * @param gridParam 单元格的行数和列数
     */
    private fun generatUnitRowAndColumnForSpanType(imagesSize: Int, gridParam: IntArray) {
        when {
            imagesSize <= 2 -> {
                gridParam[0] = 1
                gridParam[1] = imagesSize
            }
            imagesSize == 3 -> when (mSpanType) {
                TOPCOLSPAN    //2行2列,首行跨列
                    , BOTTOMCOLSPAN //2行2列,末行跨列
                    , LEFTROWSPAN   //2行2列,首列跨行
                -> {
                    gridParam[0] = 2
                    gridParam[1] = 2
                }
                NOSPAN    //1行3列
                -> {
                    gridParam[0] = 1
                    gridParam[1] = 3
                }
                else -> {
                    gridParam[0] = 1
                    gridParam[1] = 3
                }
            }
            imagesSize <= 6 -> when (mSpanType) {
                TOPCOLSPAN    //3行3列,首行跨列
                    , BOTTOMCOLSPAN //3行3列,末行跨列
                    , LEFTROWSPAN   //3行3列,首列跨行
                -> {
                    gridParam[0] = 3
                    gridParam[1] = 3
                }
                NOSPAN    //2行
                -> {
                    gridParam[0] = 2
                    gridParam[1] = imagesSize / 2 + imagesSize % 2
                }
                else -> {
                    gridParam[0] = 2
                    gridParam[1] = imagesSize / 2 + imagesSize % 2
                }
            }
            else -> {
                gridParam[0] = imagesSize / 3 + if (imagesSize % 3 == 0) 0 else 1
                gridParam[1] = 3
            }
        }
    }

    /**
     * 设置图片数据
     *
     * @param lists 图片数据集合
     * @param spanType 跨行跨列排版类型
     */
    fun setImagesData(lists: MutableList<T>?, spanType: Int = NOSPAN) {
        if (lists == null || lists.isEmpty()) {
            this.visibility = View.GONE
            return
        } else {
            this.setVisibility(View.VISIBLE)
        }
        this.mSpanType = spanType
        val newShowCount = getNeedShowCount(lists.size)

        val gridParam = calculateGridParam(newShowCount, mShowStyle)
        mRowCount = gridParam[0]
        mColumnCount = gridParam[1]
        if (mImgDataList.isEmpty()) {
            var i = 0
            while (i < newShowCount) {
                val iv = getImageView(i) ?: return
                addView(iv, generateDefaultLayoutParams())
                i++
            }
        } else {
            val oldShowCount = getNeedShowCount(mImgDataList.size)
            if (oldShowCount > newShowCount) {
                removeViews(newShowCount, oldShowCount - newShowCount)
            } else if (oldShowCount < newShowCount) {
                for (i in oldShowCount until newShowCount) {
                    val iv = getImageView(i) ?: return
                    addView(iv, generateDefaultLayoutParams())
                }
            }
        }
        mImgDataList.clear()
        mImgDataList.addAll(lists)
        mFirstLoad = mImgDataList.size > 0
        requestLayout()
    }

    private fun getNeedShowCount(size: Int): Int {
        return if (mMaxSize in 1 until size) {
            mMaxSize
        } else {
            size
        }
    }

    /**
     * 获得 ImageView
     * 保证了 ImageView 的重用
     *
     * @param position 位置
     */
    private fun getImageView(position: Int): ImageView? {
        if (position < mImageViewList.size) {
            return mImageViewList[position]
        } else {
            if (mAdapter != null) {
                val imageView = mAdapter!!.generateImageView(context)
                mImageViewList.add(imageView)
                imageView.setOnClickListener { v ->
                    mAdapter?.onItemImageClick(context, v as ImageView, position, mImgDataList)
                    mItemClickListener?.onItemClick(context, v as ImageView, position, mImgDataList)
                }
                imageView.setOnLongClickListener { v ->
                    var consumedEvent = mAdapter?.onItemImageLongClick(
                        context,
                        v as ImageView,
                        position,
                        mImgDataList
                    ) ?: false
                    mItemLongClickListener?.let {
                        consumedEvent = it.onItemLongClick(
                            context,
                            v as ImageView,
                            position,
                            mImgDataList
                        ) || consumedEvent
                    }
                    consumedEvent
                }
                return imageView
            } else {
                Log.e(
                    "NineGirdImageView",
                    "Your must set a NineGridViewAdapter for NineGirdImageView"
                )
                return null
            }
        }
    }

    /**
     * 设置 宫格参数
     *
     * @param imagesSize 图片数量
     * @param showStyle 显示风格
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    private fun calculateGridParam(imagesSize: Int, showStyle: Int): IntArray {
        val gridParam = IntArray(2)
        when (showStyle) {
            STYLE_FILL -> generatUnitRowAndColumnForSpanType(imagesSize, gridParam)
            STYLE_GRID -> {
                gridParam[0] = imagesSize / 3 + if (imagesSize % 3 == 0) 0 else 1
                gridParam[1] = 3
            }
            else -> {
                gridParam[0] = imagesSize / 3 + if (imagesSize % 3 == 0) 0 else 1
                gridParam[1] = 3
            }
        }
        return gridParam
    }


    companion object {
        const val STYLE_GRID = 0     // 宫格布局
        const val STYLE_FILL = 1     // 全填充布局

        ///////////////////////////////////////////////////////////////////////////
        // 跨行跨列的类型
        ///////////////////////////////////////////////////////////////////////////
        const val NOSPAN: Int = 0         // 不跨行也不跨列
        const val TOPCOLSPAN = 2     // 首行跨列
        const val BOTTOMCOLSPAN = 3  // 末行跨列
        const val LEFTROWSPAN = 4    // 首列跨行
    }
}

interface ItemLongClickListener<T> {
    fun onItemLongClick(context: Context, imageView: ImageView, index: Int, list: List<T>): Boolean
}

interface ItemClickListener<T> {
    fun onItemClick(context: Context, imageView: ImageView, index: Int, list: List<T>)
}
