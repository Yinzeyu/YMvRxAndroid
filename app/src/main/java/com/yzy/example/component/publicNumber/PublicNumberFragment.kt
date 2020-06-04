package com.yzy.example.component.publicNumber

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.parseState
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R
import com.yzy.example.extention.bindViewPager2
import com.yzy.example.extention.init
import com.yzy.example.repository.bean.ClassifyBean
import com.yzy.example.repository.model.PublicNumberViewModelViewModel
import kotlinx.android.synthetic.main.fragment_public_number.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class PublicNumberFragment :CommFragment<PublicNumberViewModelViewModel,ViewDataBinding>() {
    //fragment集合
    private var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    private var mDataList: ArrayList<ClassifyBean> = arrayListOf()
    override fun initContentView() {
        commTitleText.text="公众号"
        commTitleBack.gone()
        //初始化viewpager2
        viewPager.init(this,fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(viewPager,mDataList)
        viewModel.getPublicTitleData()
        viewModel.titleData.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                mDataList.addAll(it?: mutableListOf())
                it?.forEach { classify ->
                    fragments.add(PublicChildFragment.newInstance(classify.id))
                }
                magicIndicator.navigator.notifyDataSetChanged()
                viewPager.adapter?.notifyDataSetChanged()
                viewPager.offscreenPageLimit = fragments.size
            }, {
                //请求项目标题失败
//                loadsir.showCallback(ErrorCallback::class.java)
//                loadsir.setErrorText(it.errorMsg)
            })
        })
    }

    override fun getLayoutId(): Int  =R.layout.fragment_public_number
}