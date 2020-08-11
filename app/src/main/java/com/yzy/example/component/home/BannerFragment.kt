package com.yzy.example.component.home


import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.extention.loadUrl
import com.yzy.example.repository.bean.BannerBean
import kotlinx.android.synthetic.main.item_banner_child.*

class BannerFragment(  var data: BannerBean) : BaseFragment<NoViewModel,ViewDataBinding>() {
    companion object {
        fun newInstance(bannerBean: BannerBean): BannerFragment {
            return BannerFragment(bannerBean)
        }
    }

    override fun getLayoutId(): Int= R.layout.item_banner_child

    override fun initView(savedSate: Bundle?) {
        itemBannerIV.loadUrl(data.imagePath)
        itemBannerIV.click {
        }
    }
}
