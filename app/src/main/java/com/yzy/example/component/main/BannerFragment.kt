package com.yzy.example.component.main


import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.extention.load
import com.yzy.example.repository.bean.BannerBean
import kotlinx.android.synthetic.main.item_banner_child.*


/**
 * Created by lwj on 2018/2/6.
 * A simple [Fragment] subclass.
 */
class BannerFragment(  var data: BannerBean) : BaseFragment<NoViewModel,ViewDataBinding>() {


    companion object {
        fun newInstance(bannerBean: BannerBean): BannerFragment {
            return BannerFragment(bannerBean)
        }
    }

    override fun getLayoutId(): Int= R.layout.item_banner_child

    override fun initView(savedSate: Bundle?) {
        itemBannerIV.load(data.imagePath)
        itemBannerIV.click {
        }
    }
}
