package com.yzy.example.component.main


import android.view.View
import androidx.fragment.app.Fragment
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.component.web.WebsiteDetailFragment
import com.yzy.example.extention.load
import com.yzy.example.extention.startNavigate
import com.yzy.example.repository.bean.BannerBean
import kotlinx.android.synthetic.main.item_banner_child.*


/**
 * Created by lwj on 2018/2/6.
 * A simple [Fragment] subclass.
 */
class BannerFragment(  var data: BannerBean,override val contentLayout: Int = R.layout.item_banner_child) : BaseFragment() {

    override fun initView(root: View?) {
        itemBannerIV.load(data.imagePath)
        itemBannerIV.click {
            startNavigate(view, MainFragmentDirections.actionMainFragmentToWebsiteDetailFragment(data.url ?: ""))
//            WebActivity.startActivity(mContext, data.url ?: "")
        }
    }

    override fun initData() {
    }

    companion object {
        fun newInstance(bannerBean: BannerBean): BannerFragment {
            return BannerFragment(bannerBean)
        }
    }
}
