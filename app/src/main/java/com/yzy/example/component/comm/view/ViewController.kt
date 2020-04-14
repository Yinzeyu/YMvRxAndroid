package com.yzy.example.component.comm.view

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.inflate
import com.yzy.example.R
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.loading.view.tv_msg
import kotlinx.android.synthetic.main.network_error.view.*
import kotlinx.android.synthetic.main.no_data.view.*

class ViewController(var replaceView: FrameLayout) {
    //是否已经调用过restore方法
    var hasRestore: Boolean = false

    //当前显示的view
    private var mCurrentView: View? = null

    fun showLoading(msg: String? = null) {
        hasRestore = false
        val layout = replaceView.context.inflate(R.layout.loading)
        msg?.let {
            layout.tv_msg.text = msg
        }
        showView(layout)
    }


    fun showEmpty(emptyMsg: String?= null,showBtn:Boolean=false, listener:()->Unit) {
        hasRestore = false
        val layout = replaceView.context.inflate(R.layout.no_data)
        val againBtn = layout.btnNoDataError
        val textView = layout.tvNoData
        if (!TextUtils.isEmpty(emptyMsg)) {
            textView.text = emptyMsg
        }
        againBtn.click {
            listener.invoke()
        }
        if (showBtn)
            againBtn.isVisible =showBtn
        showView(layout)
    }

    private fun showNetworkError(msg: String?= "网络状态异常，请刷新重试", listener:()->Unit) {
        hasRestore = false
        val layout = replaceView.context.inflate(R.layout.network_error)
        val againBtn = layout.btnNetWorkError
         layout.tvMsg.text = msg
        againBtn.click {
            listener.invoke()
        }
        showView(layout)
    }


    fun restore() {
        hasRestore = true
        restoreView()
    }

    /**
     * 设置索要展示的布局 空布局 loading 等
     * @param view
     */
    private fun showView(view: View) {
        mCurrentView = view
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        replaceView.addView(view, params)
    }

    /**
     * 移除当前布局
     */
    private fun restoreView() {
        mCurrentView?.let {
            replaceView.removeView(it)
        }
    }
}