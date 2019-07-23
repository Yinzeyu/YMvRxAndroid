package com.yzy.baselibrary.jsbridge

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * 如果要自定义WebViewClient必须要集成此类
 * Created by bruce on 10/28/15.
 */
class BridgeWebViewClient(private val webView: BridgeWebView) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        var url = url
        try {
            url = URLDecoder.decode(url, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url)
            return true
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue()
            return true
        } else {
            return if (this.onCustomShouldOverrideUrlLoading(url)) true else super.shouldOverrideUrlLoading(
                view,
                url
            )
        }
    }

    // 增加shouldOverrideUrlLoading在api》=24时
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var url = request.url.toString()
            try {
                url = URLDecoder.decode(url, "UTF-8")
            } catch (ex: UnsupportedEncodingException) {
                ex.printStackTrace()
            }

            if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
                webView.handlerReturnData(url)
                return true
            } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
                webView.flushMessageQueue()
                return true
            } else {
                return if (this.onCustomShouldOverrideUrlLoading(url)) true else super.shouldOverrideUrlLoading(
                    view,
                    request
                )
            }
        } else {
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs)

        //
        for (m in webView.startupMessage) {
            webView.dispatchMessage(m)
        }
        onCustomPageFinishd(view, url)

    }


    protected fun onCustomShouldOverrideUrlLoading(url: String): Boolean {
        return false
    }


    protected fun onCustomPageFinishd(view: WebView, url: String) {

    }


}