package com.yzy.example.ui.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.yzy.baselibrary.extention.getResColor
import com.yzy.baselibrary.extention.mContext
import com.yzy.example.R
import com.yzy.example.comm.CommTitleActivity
import com.yzy.example.widget.LollipopFixedWebView
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Description: 如果需要js对接，参考添加BridgeWebView https://github.com/lzyzsd/JsBridge
 * @author: caiyoufei
 * @date: 2019/10/3 15:25
 */
class WebActivity : CommTitleActivity() {

    //需要加载的web地址
    private var webUrl: String? = null
    //AgentWeb相关
    private var agentWeb: AgentWeb? = null
    private var agentBuilder: AgentWeb.CommonBuilder? = null

    //外部跳转
    companion object {
        private const val WEB_URL = "INTENT_KEY_WEB_URL"
        fun startActivity(context: Context, url: String) {
            val intent = Intent(context, WebActivity::class.java)
            if (url.isNotBlank()) intent.putExtra(WEB_URL, url)
            context.startActivity(intent)
        }
    }

    //xml
    override fun layoutResContentId() = R.layout.activity_web

    //初始化view
    override fun initContentView() {
        webRootView.removeAllViews()
        initAgentBuilder()
    }

    //加载数据
    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        webUrl = intent.getStringExtra(WEB_URL) ?: "https://www.baidu.com"//获取加载地址
        agentWeb = agentBuilder?.createAgentWeb()?.ready()?.go(webUrl)//创建web并打开
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
        val webView = LollipopFixedWebView(this)
        webView.overScrollMode = View.OVER_SCROLL_NEVER
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
        agentBuilder = AgentWeb.with(this)
                .setAgentWebParent(webRootView, ViewGroup.LayoutParams(-1, -1))//添加到父容器
                .useDefaultIndicator(mContext.getResColor(R.color.colorPrimary))//设置进度条颜色
//            .setWebViewClient(getWebViewClient())//监听结束，适配宽度
                .setWebChromeClient(webChromeClient)//监听标题
                .setWebView(webView)//真正的webview
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)//失败的布局
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
        //给WebView添加Header
//        val headers = HeaderManger.instance.getStaticHeaders()
//        agentBuilder?.additionalHttpHeader(webUrl, headers)
    }

    //获取标题
    private val webChromeClient = object : com.just.agentweb.WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            title?.let { setTitleText(it) }
        }
    }

}