package com.yzy.baselibrary.base.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.BarUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.yzy.baselibrary.base.dialog.ActionLoadingDialog
import com.yzy.baselibrary.base.dialog.LoadingDialog
import com.yzy.baselibrary.extention.screenHeight
import com.yzy.baselibrary.extention.screenWidth
import com.yzy.baselibrary.extention.setStatusColor
import com.yzy.baselibrary.extention.uiMode1Normal
import org.kodein.di.*
import org.kodein.di.android.*
import org.kodein.di.android.retainedSubKodein
import org.kodein.di.generic.kcontext

abstract class BaseActivity : RxAppCompatActivity(), KodeinAware {
    private var loadingDialog: LoadingDialog? = null
    private var actionLoadingDialog: ActionLoadingDialog? = null
    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> = kcontext(this)
    override val kodein by retainedSubKodein(kodein(), copy = Copy.All) {
        initKodein(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.onCreateBefore()
        this.initStatus()
        initBeforeCreateView(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        kodeinTrigger.trigger()
        initView()
        initDate()
    }


    /**
     * 页面内容布局resId
     */
    protected abstract fun layoutResId(): Int


    abstract fun initView();
    abstract fun initDate();
    /**
     * 需要在onCreateView中调用的方法
     */
    protected open fun initBeforeCreateView(savedInstanceState: Bundle?) {

    }

    /** 适配状态栏  */
    protected open fun initStatus() {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(true)
        }
    }

    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {}

    protected open fun initKodein(builder: Kodein.MainBuilder) {
    }

    open fun showLoading(style: (LoadingDialog.() -> Unit)? = null) {
        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_LOADING) != null
            || supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_LOADING_ACTION) != null
        ) {
            //loading已经在显示中
            return
        }
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance()
        }
        style?.let {
            loadingDialog?.apply(it)
        }
        loadingDialog?.show(supportFragmentManager, TAG_FRAGMENT_LOADING)
    }

    open fun showActionLoading(
        tips: String? = null,
        style: (ActionLoadingDialog.() -> Unit)? = null
    ) {
        if (supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_LOADING_ACTION) != null ||
            supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_LOADING) != null
        ) {
            //loading已经在显示中
            return
        }
        if (actionLoadingDialog == null) {
            actionLoadingDialog = ActionLoadingDialog.newInstance()
        }
        actionLoadingDialog?.setTips(tips)
        style?.let {
            actionLoadingDialog?.apply(it)
        }
        actionLoadingDialog?.show(supportFragmentManager, TAG_FRAGMENT_LOADING_ACTION)
    }

    open fun dismissLoading() {
        loadingDialog?.dismiss()
        actionLoadingDialog?.dismiss()
    }

    companion object {
        private const val TAG_FRAGMENT_LOADING = "LoadingDialog"
        private const val TAG_FRAGMENT_LOADING_ACTION = "ActionLoadingDialog"
    }


}
