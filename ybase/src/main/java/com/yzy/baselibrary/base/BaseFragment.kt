package com.yzy.baselibrary.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yzy.baselibrary.extention.removeParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 *description: BaseFragment.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseFragment : Fragment(), CoroutineScope by MainScope() {
    //页面基础信息
    lateinit var mContext: Activity
    protected var rootView: FrameLayout? = null
    lateinit var mNavController: NavController
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }
    /**
     * 内容布局的ResId
     */
    protected abstract val contentLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true
        Log.e("fragment",this.javaClass.name)
        mNavController = NavHostFragment.findNavController(this)

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
            Log.e("fragment_removeParent",this.javaClass.name)
            //防止重新create时还存在
            rootView?.removeParent()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initData()
    }
    /**
     * 初始化View
     */
    protected abstract fun initView(root: View?)

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(clazz)

    override fun onDestroyView() {
        cancel()
        super.onDestroyView()
    }
}