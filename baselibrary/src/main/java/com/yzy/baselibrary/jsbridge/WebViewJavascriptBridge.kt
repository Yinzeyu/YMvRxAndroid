package com.yzy.baselibrary.jsbridge


interface WebViewJavascriptBridge {

    fun send(data: String?)
    fun send(data: String?, responseCallback: CallBackFunction?)


}
