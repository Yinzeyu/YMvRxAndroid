package com.yzy.example.component.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.yzy.baselibrary.base.BaseDialogFragment
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.databinding.DialogThemeColorBinding
import com.yzy.example.repository.bean.ColorBean
import com.yzy.example.utils.ColorUtil
import kotlinx.android.synthetic.main.dialog_action.*
import kotlinx.android.synthetic.main.dialog_theme_color.*

/**
 * Description:
 * @author: yzy
 * @date: 2019/9/24 13:03
 */
class ThemeColorDialog : BaseDialogFragment<NoViewModel,DialogThemeColorBinding>() {
    override fun getLayoutId(): Int =R.layout.dialog_theme_color

    override fun initView(savedState: Bundle?) {

//        val adapter = ColorGridAdapter()
//        val mutableList :MutableList<ColorBean<Int>> = mutableListOf()
//        ColorUtil.ACCENT_COLORS.forEach {
//            mutableList.add(ColorBean(bean =it,itemBeanType = ColorBean.COLOR_LIST))
//        }
//        adapter.setNewInstance(mutableList)
//        rvThemeColorView.adapter = adapter
    }

    companion object {
        fun newInstance(): ThemeColorDialog = ThemeColorDialog()
    }
}
//  DSL style
inline fun initThemeColorDialog(
    fragmentManager: FragmentManager,
    dsl: ThemeColorDialog.() -> Unit
) {
    val dialog = ThemeColorDialog.newInstance().apply(dsl)
    dialog.mGravity = Gravity.CENTER
    dialog.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
    dialog.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog.touchOutside = false
    dialog.show(fragmentManager, "ThemeColorDialog")
}
