package com.yzy.example.component.comm

import android.view.View
import androidx.core.view.isVisible
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.pressEffectAlpha
import com.yzy.baselibrary.extention.visible
import kotlinx.android.synthetic.main.layout_comm_title.*

/**
 * Description:
 * @author: yzy
 * @date: 2019/10/8 10:48
 */
abstract class CommTitleFragment<VM : BaseViewModel<*>> :
    CommFragment<VM>() {
//    abstract fun layoutTitleContentId(): Int

//    override fun layoutTitleContentId(): Int = R.layout.layout_comm_title
    override fun initView(root: View?) {
        commTitleBack.pressEffectAlpha()
        commonTitleRightTv.pressEffectAlpha()
        commTitleBack.click { mActivity.onBackPressed() }
//        //添加子view
//        if (layoutTitleContentId() > 0) {
//            titleView.addView(
//                View.inflate(mContext, layoutResContentId(), null),
//                ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//            )
//        }
    }

    //设置标题
    fun setTitleText(title: CharSequence) {
        commTitleText.text = title
    }

    //设置右边文字
    fun setTitleRightTv(txt: String) {
        commonTitleRightTv.text = txt
        commonTitleRightTv.visible()
    }

    //设置右边文字显示状态
    fun setTitleRightTv(visible: Boolean) {
        commonTitleRightTv.isVisible = visible
        commonTitleRightTv.textSize
    }

    //右边点击事件
    fun setTitleRightTv(onClick: (() -> Unit)?) {
        commonTitleRightTv.click { onClick?.invoke() }
    }
    //子xml
//    abstract fun layoutResContentId(): Int

    //子控件初始化
//    abstract fun initContentView()


    override fun initData() {
    }
}