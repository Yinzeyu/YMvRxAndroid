package com.yzy.example.component.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yzy.baselibrary.base.BaseFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.example.R
import com.yzy.example.databinding.FragmentCollectBinding
import com.yzy.example.extention.bindViewPager2
import com.yzy.example.extention.init
import kotlinx.android.synthetic.main.fragment_collect.*

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏
 */
class CollectFragment: BaseFragment<NoViewModel, FragmentCollectBinding>() {

    var titleData = arrayListOf("文章","网址")

    private var fragments : ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(CollectAriticleFragment())
        fragments.add(CollectUrlFragment())
    }

    override fun initView(savedSate: Bundle?)  {
        //初始化时设置顶部主题颜色
//        shareViewModel.appColor.value.let { collect_viewpager_linear.setBackgroundColor(it) }
        //初始化viewpager2
        collectViewPager.init(this,fragments)
        //初始化 magic_indicator
        collect_magic_indicator.bindViewPager2(collectViewPager,mStringList = titleData)
//        toolbar.initClose(){
//            nav().navigateUp()
//        }
        commTitleBack.click {
            onBackPressed()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_collect
}