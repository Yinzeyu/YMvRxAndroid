package com.yzy.example.component.project

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yzy.baselibrary.extention.gone
import com.yzy.baselibrary.extention.parseState
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.R
import com.yzy.example.databinding.FragmentPorjectBinding
import com.yzy.example.extention.bindViewPager2
import com.yzy.example.extention.init
import com.yzy.example.repository.bean.ClassifyBean
import com.yzy.example.repository.model.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_porject.*
import kotlinx.android.synthetic.main.layout_comm_title.*

class ProjectFragment :CommFragment<ProjectViewModel,FragmentPorjectBinding>() {
    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<ClassifyBean> = arrayListOf()
    override fun initContentView() {
        commTitleText.text="项目"
        commTitleBack.gone()
        binding.vm=viewModel
        //初始化viewpager2
        viewPager.init(this, fragments)
        //初始化 magic_indicator
        magicIndicator.bindViewPager2(viewPager, mDataList)
        //初始化时设置顶部主题颜色
//        shareViewModel.appColor.value.let { viewpager_linear.setBackgroundColor(it) }
        //请求标题数据
//        viewModel.getProjectTitleData()
        viewModel.titleData.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                mDataList.clear()
                fragments.clear()
                mDataList.add(
                    0,
                    ClassifyBean(name = "最新项目")
                )
                mDataList.addAll(it?: mutableListOf())
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it?.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
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

    override fun getLayoutId(): Int  =R.layout.fragment_porject
}