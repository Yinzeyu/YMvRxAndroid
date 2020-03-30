package com.yzy.baselibrary.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.yzy.baselibrary.R
import com.yzy.baselibrary.extention.StatusBarHelper
import com.yzy.baselibrary.extention.backgroundColor
import com.yzy.baselibrary.extention.inflate
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
    private var noteView: View? = null
    lateinit var mNavController: NavController
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as Activity
    }

    /**
     * 内容布局的ResId
     */
    protected abstract val contentLayout: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true
        Log.e("fragment", this.javaClass.name)
        mNavController = NavHostFragment.findNavController(this)
        val contentView = inflater.inflate(R.layout.base_fragment, null)
        noteView = mContext.inflate(contentLayout)
        rootView = contentView.findViewById(R.id.contentView)
        if (contentLayout > 0) {
            rootView?.addView(noteView)
        } else {
            rootView?.removeParent()
        }
       val baseStatusView = contentView.findViewById<View>(R.id.baseStatusView)
        baseStatusView?.let {
            it.layoutParams.height =  if (fillStatus()) StatusBarHelper.getStatusbarHeight(mContext) else 0
            it.backgroundColor =statusColor()
        }
        if (isBack()) {
            StatusBarHelper.setStatusBarLightMode(mContext)
        } else {
            StatusBarHelper.setStatusBarDarkMode(mContext)
        }
        return contentView
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

    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    //是否需要默认填充状态栏,默认填充为白色view
    protected open fun fillStatus(): Boolean {
        return true
    }
    protected open fun statusColor(): Int {
        return Color.TRANSPARENT
    }
    protected open fun isBack(): Boolean {
        return true
    }

    fun <T : ViewModel> getViewModel(clazz: Class<T>): T =
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(clazz)

    override fun onDestroyView() {
        cancel()
        super.onDestroyView()
    }

    open fun onBackPressed() {
        (mContext as BaseActivity).onBackPressed()
    }
}