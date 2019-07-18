package com.yzy.baselibrary.base.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.BarUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.extention.screenHeight
import com.yzy.baselibrary.extention.screenWidth
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
abstract class BaseFragment : RxFragment(), KodeinAware, LifecycleOwner {

    /** 屏幕宽度  */
    var mScreenWidth: Int = 0
    /** 屏幕高度(包含状态栏高度但不包含底部虚拟按键高度)  */
    var mScreenHeight: Int = 0
    /** 屏幕状态栏高度  */
    var mStatusBarHeight: Int = 0
    /** 上下文  */
    protected lateinit var mContext: Context
    protected lateinit var mActivity: Activity
    private var isFragmentVisible = true
    private var isPrepared = false
    private var isFirst = true
    private var isInViewPager = false
    protected var rootView: View? = null
    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> get() = kcontext(activity)


    override val kodein: Kodein = Kodein.lazy {
        extend(BaseApplication.INSTANCE.kodein)
        initKodein(this)
    }


    /**
     * 内容布局的ResId
     */
    protected abstract val contentLayout: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context!!
        this.mActivity = activity!!
        mScreenWidth = mContext.screenWidth()
        mScreenHeight = mContext.screenHeight()
        mStatusBarHeight = BarUtils.getStatusBarHeight()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBeforeCreateView(savedInstanceState)
        if (contentLayout != 0) {
            rootView = inflater.inflate(contentLayout, null)
            rootView!!.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
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

    /**
     * 懒加载
     */
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
    /**
     * 需要在onCreateView中调用的方法
     */
    protected open fun initBeforeCreateView(savedInstanceState: Bundle?) {

    }

    /**
     * 初始化kodein
     */
    protected open fun initKodein(builder: Kodein.MainBuilder) {
    }
    /**
     * 初始化View
     */
    protected abstract fun initView(root: View?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()
}