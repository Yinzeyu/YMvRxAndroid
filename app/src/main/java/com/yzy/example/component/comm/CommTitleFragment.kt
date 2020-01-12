package com.yzy.example.component.comm

import android.view.View
import android.view.ViewGroup
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.example.R
import kotlinx.android.synthetic.main.activity_comm_title.*
import kotlinx.android.synthetic.main.layout_comm_title.*

/**
 * Description:
 * @author: caiyoufei
 * @date: 2019/10/8 10:48
 */
abstract class CommTitleFragment : CommFragment() {

    override val contentLayout: Int= R.layout.activity_comm_title
    override fun initView(root: View?) {
        commTitleBack.pressEffectAlpha()
        commTitleBack.click {  mContext.onBackPressed() }
        //添加子view
        if (commonRootView.childCount == 1) {
            commonRootView.addView(
                View.inflate(mContext, layoutResContentId(), null),
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        //调用初始化
        initContentView()
    }

    //设置标题
    fun setTitleText(title: CharSequence) {
        commTitleText.text = title
    }

    //子xml
    abstract fun layoutResContentId(): Int

    //子控件初始化
    abstract fun initContentView()





    override fun initData() {
    }
}