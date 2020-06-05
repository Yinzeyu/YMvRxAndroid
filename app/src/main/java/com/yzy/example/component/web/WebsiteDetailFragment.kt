package com.yzy.example.component.web

import android.annotation.SuppressLint
import androidx.navigation.fragment.navArgs
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.databinding.FragmentWesiteDetailBinding
import com.yzy.example.repository.TokenStateManager

class WebsiteDetailFragment : CommFragment<NoViewModel,FragmentWesiteDetailBinding>() {

    private val url: WebsiteDetailFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun initContentView() {
        //设置适配
        binding.webRootView.loadUrl(url.url)
        binding.webRootView.settings?.let { ws ->
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
        TokenStateManager.instance.mNetworkStateCallback.postValue(true)
    }


    override fun getLayoutId(): Int= R.layout.fragment_wesite_detail


}