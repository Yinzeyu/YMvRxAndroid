package com.yzy.example.component.web

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.navArgs
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.getResColor
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.widget.LollipopFixedWebView
import kotlinx.android.synthetic.main.fragment_wesite_detail.*

class WebsiteDetailFragment : CommFragment<NoViewModel>() {

    private val url: WebsiteDetailFragmentArgs by navArgs()
    //AgentWeb相关
    private var agentWeb: AgentWeb? = null
    private var agentBuilder: AgentWeb.CommonBuilder? = null

    override fun statusColor(): Int = Color.parseColor("#008577")

    override fun layoutResContentId(): Int =R.layout.fragment_wesite_detail
    override fun layoutTitleContentId(): Int =R.layout.web_fragment_title

    override fun initContentView() {
        webRootView.removeAllViews()
        initAgentBuilder()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        agentWeb = agentBuilder?.createAgentWeb()?.ready()?.go(url.url)//创建web并打开
        //设置适配
        val web = agentWeb?.webCreator?.webView
        web?.settings?.let { ws ->
            //支持javascript
            ws.javaScriptEnabled = true
            //设置可以支持缩放
            ws.setSupportZoom(true)
            //设置内置的缩放控件
            ws.builtInZoomControls = true
            //隐藏原生的缩放控件
            ws.displayZoomControls = false
            //扩大比例的缩放
            ws.useWideViewPort = true
            ws.loadWithOverviewMode = true
        }
    }

    //初始化web
    private fun initAgentBuilder() {
        //为了解决安卓5.x的bug
        val webView = LollipopFixedWebView(mContext)
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
        agentBuilder = AgentWeb.with(this)
            .setAgentWebParent(webRootView, ViewGroup.LayoutParams(-1, -1))//添加到父容器
            .useDefaultIndicator(mContext.getResColor(R.color.colorPrimary))//设置进度条颜色
            .setWebView(webView)//真正的webview
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)//失败的布局
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
    }

}