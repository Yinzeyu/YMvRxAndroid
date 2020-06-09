package com.yzy.baselibrary.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.yzy.baselibrary.R
import com.yzy.baselibrary.extention.dp2px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.lang.reflect.ParameterizedType

/**
 *description: Dialog的基类.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class BaseDialogFragment<VM : BaseViewModel<*>, DB : ViewDataBinding> : DialogFragment(),
    CoroutineScope by MainScope() {
    lateinit var viewModel: VM
    var mBinding: DB? = null

    var mWidth = WRAP_CONTENT
    var mHeight = WRAP_CONTENT
    var mGravity = Gravity.CENTER
    var mOffsetX = 0
    var mOffsetY = 0
    var mAnimation: Int? = null
    var touchOutside: Boolean = true
    var mSoftInputMode: Int = SOFT_INPUT_STATE_ALWAYS_HIDDEN
    private var disListener: (() -> Unit)? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            return mBinding?.root
        }
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createViewModel()
        lifecycle.addObserver(viewModel)
        initView(savedInstanceState)
    }

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, SavedStateViewModelFactory(requireActivity().application, this)).get(tClass) as VM
        }
    }

    //防止快速弹出多个
    private var showTime = 0L

    /**
     * 防止同时弹出两个dialog
     */
    override fun show(manager: FragmentManager, tag: String?) {
        if (System.currentTimeMillis() - showTime < 500 || activity?.isFinishing == true) {
            return
        }
        showTime = System.currentTimeMillis()
        setBooleanField("mDismissed", false)
        setBooleanField("mShownByMe", true)
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    private fun setBooleanField(fieldName: String, value: Boolean) {
        try {
            val field = DialogFragment::class.java.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(this, value)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }


    /**
     * 设置统一样式
     */
    private fun setStyle() {
        //获取Window
        val window = dialog?.window
        //无标题
        dialog?.requestWindowFeature(STYLE_NO_TITLE)
        // 透明背景
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(touchOutside)
        //设置宽高
        window!!.decorView.setPadding(0, 0, 0, 0)
        val wlp = window.attributes
        wlp.dimAmount = 0.5f
        wlp.width = mWidth
        wlp.height = mHeight
        //设置对齐方式
        wlp.gravity = mGravity
        //设置偏移量
        wlp.x = dialog?.context?.dp2px(mOffsetX) ?: 0
        wlp.y = dialog?.context?.dp2px(mOffsetY) ?: 0
        wlp.softInputMode = mSoftInputMode
        //设置动画
        if (mAnimation ==null){
            window.setWindowAnimations(R.style.CommonDialog)
        }else{
            mAnimation?.also { window.setWindowAnimations(it) }
        }
        window.attributes = wlp
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化
     */
    abstract fun initView(savedState: Bundle?)

    fun onDismiss(listener: () -> Unit) {
        disListener = listener
    }
    override fun onDismiss(dialog: DialogInterface) {
        disListener?.invoke()

        super.onDismiss(dialog)
    }


}