package com.yzy.baselibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.mvrx.BaseMvRxFragment
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.extention.removeParent
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.KodeinTrigger
import org.kodein.di.generic.kcontext

/**
 *description: BaseFragment.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseFragment : BaseMvRxFragment(), KodeinAware {
    //页面基础信息
    lateinit var mContext: Activity
    private var isFragmentVisible = true
    private var isPrepared = false
    private var isFirst = true
    private var isInViewPager = false
    protected var rootView: FrameLayout? = null
    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> get() = kcontext(activity)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    override val kodein: Kodein = Kodein.lazy {
        extend(BaseApplication.getApp().kodein)
        initKd(this)
    }
    /**
     * 内容布局的ResId
     */
    protected abstract val contentLayout: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBeforeCreateView(savedInstanceState)
        //第一次的时候加载xml
        if (contentLayout > 0 && rootView == null) {
            val contentView = inflater.inflate(contentLayout, null)
            if (contentView is FrameLayout) {
                contentView.layoutParams = ViewGroup.LayoutParams(-1, -1)
                rootView = contentView
            } else {
                rootView = FrameLayout(mContext)
                rootView?.layoutParams = ViewGroup.LayoutParams(-1, -1)
                rootView?.addView(contentView, ViewGroup.LayoutParams(-1, -1))
            }
        } else {
            //防止重新create时还存在
            rootView?.removeParent()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kodeinTrigger.trigger()
        initView(view)
        isPrepared = true
        lazyLoad()
    }

    /**
     * 视图真正可见的时候才调用
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFragmentVisible = isVisibleToUser
        isInViewPager = true
        lazyLoad()
    }

    override fun invalidate() {
    }

    //懒加载
    private fun lazyLoad() {
        if (!isInViewPager) {
            isFirst = false
            initData()
            return
        }
        if (!isPrepared || !isFragmentVisible || !isFirst) {
            return
        }
        isFirst = false
        initData()
    }


    //初始化前的处理
    protected open fun initBeforeCreateView(savedInstanceState: Bundle?) {}

    /**
     * 初始化kodein
     */
    protected open fun initKd(builder: Kodein.MainBuilder) {}

    /**
     * 初始化View
     */
    protected abstract fun initView(root: View?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()
}